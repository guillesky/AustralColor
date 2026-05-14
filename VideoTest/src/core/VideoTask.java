package core;

import java.io.IOException;
import java.io.OutputStream;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class VideoTask extends AbstractMediaTask
{
	public VideoTask(String inputPath, String outputPath, MediaTaskManager mediaTaskManager)
	{
		super(inputPath, outputPath, mediaTaskManager);

	}

	private static final int SAMPLE_SECONDS = 2; // Extracts color correction from every N seconds

	private double fps;
	private int totalFrameCount;
	private VideoAnalysisResult videoAnalysisResult = null;

	private void analyzeVideo()
	{
		VideoCapture cap = new VideoCapture(this.getInputPath());

		
		this.fps = (int) cap.get(Videoio.CAP_PROP_FPS);
		this.totalFrameCount = (int) cap.get(Videoio.CAP_PROP_FRAME_COUNT);
		int count = 0;
		Mat frame = new Mat();

		// System.out.println("Analyzing...");
		this.videoAnalysisResult = new VideoAnalysisResult();

		while (cap.read(frame))
		{

			// System.out.print(count + " frames\r");

			// cada N segundos
			if (count % ((int) fps * SAMPLE_SECONDS) == 0 || count == totalFrameCount - 1)
			{

				Mat rgb = new Mat();
				Imgproc.cvtColor(frame, rgb, Imgproc.COLOR_BGR2RGB);
				this.videoAnalysisResult.addFilter(count, ImageProcessor.getFilterMatrix(rgb));

			}

			count++;
		}

		cap.release();
		this.mediaTaskManager.videoAnalized(this, this.videoAnalysisResult);

	}

	@Override
	protected double processMedia()
	{
		double elapsedMs = 0;
		long start = System.nanoTime();
		this.analyzeVideo();
		VideoCapture cap = new VideoCapture(this.getInputPath());
		int width = (int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH);
		int height = (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT);
		double fps = cap.get(Videoio.CAP_PROP_FPS);

		String ffmpegPath = Util.getFFmpegPath();
		// ===== CONFIGURACIÓN FFMPEG =====

		ProcessBuilder pb = new ProcessBuilder(ffmpegPath, "-y",

				// ===== VIDEO DESDE JAVA =====
				"-f", "rawvideo", "-pixel_format", "bgr24", "-video_size", width + "x" + height, "-framerate",
				String.valueOf(fps), "-i", "-", // stdin (frames procesados)

				// ===== AUDIO DESDE ARCHIVO ORIGINAL =====
				"-i", this.getInputPath(),

				// ===== MAPEO =====
				"-map", "0:v:0", // video del pipe
				"-map", "1:a:0", // audio del archivo

				// ===== CODECS =====
				"-c:v", "libx264", "-pix_fmt", "yuv420p", "-c:a", "copy",

				// ===== OUTPUT =====
				this.getOutputPath());
		//File logFile = new File("proceso.log");
		pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
		pb.redirectError(ProcessBuilder.Redirect.DISCARD);

		Process process;
		try
		{
			process = pb.start();

			OutputStream ffmpegInput = process.getOutputStream();
			Mat frame = new Mat();
			int frameCount = 0;

			while (cap.read(frame))
			{

				// ===== PROCESAMIENTO: CORREGIR =====

				Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
				frame = ImageProcessor.applyFilter(frame, this.videoAnalysisResult.getFilter(frameCount));

				Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2BGR);

				// convertir Mat → bytes
				byte[] data = new byte[(int) (frame.total() * frame.channels())];
				frame.get(0, 0, data);

				// enviar a FFmpeg
				ffmpegInput.write(data);
				this.mediaTaskManager.frameProcessed(this, frame, frameCount);
				double p=(double)frameCount*100/(double)this.totalFrameCount;
				if((int)p!=this.percentageCompleted)
				{
				    this.percentageCompleted=(int) p;
				    this.mediaTaskManager.updatePercentageCompleted(this);
				}
				frame.release();

				frameCount++;
			}

			ffmpegInput.close();
			cap.release();

			int exitCode = process.waitFor();

			long end = System.nanoTime();

			elapsedMs = (end - start) / 1_000_000.0;

			System.out.println("FFmpeg exit code: " + exitCode);

		} catch (IOException | InterruptedException e)
		{
			
			e.printStackTrace();
		}
		return elapsedMs;
	}

	@Override
	public String toString()
	{
		return "VideoProcessor [inputPath=" + this.getInputPath() + ", outputPath=" + this.getOutputPath() + ", fps="
				+ fps + ", frameCount=" + totalFrameCount + "]";
	}

}
