package core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class VideoProcessor
{
	private static final int SAMPLE_SECONDS = 2; // Extracts color correction from every N seconds
	private String inputPath;
	private String outputPath;
	private double fps;
	private int frameCount;
	private VideoAnalysisResult videoAnalysisResult = null;
	private ArrayList<VideoProcessorListener> videoProcessorListeners = new ArrayList<VideoProcessorListener>();

	public String getInputPath()
	{
		return inputPath;
	}

	public String getOutputPath()
	{
		return outputPath;
	}

	public VideoProcessor(String inputPath, String outputPath)
	{
		super();
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}

	private void analyzeVideo()
	{
		VideoCapture cap = new VideoCapture(inputPath);

		int fps = (int) cap.get(Videoio.CAP_PROP_FPS);
		int frameCount = (int) cap.get(Videoio.CAP_PROP_FRAME_COUNT);
		this.fps = fps;
		this.frameCount = frameCount;
		int count = 0;
		Mat frame = new Mat();

		// System.out.println("Analyzing...");
		this.videoAnalysisResult = new VideoAnalysisResult();

		while (cap.read(frame))
		{

			// System.out.print(count + " frames\r");

			// cada N segundos
			if (count % ((int) fps * SAMPLE_SECONDS) == 0 || count == frameCount - 1)
			{

				Mat rgb = new Mat();
				Imgproc.cvtColor(frame, rgb, Imgproc.COLOR_BGR2RGB);
				this.videoAnalysisResult.addFilter(count, ImageProcessor.getFilterMatrix(rgb));

			}

			count++;
		}

		cap.release();
		for (VideoProcessorListener videoProcessorListener : this.videoProcessorListeners)
			videoProcessorListener.videoAnalized(this, this.videoAnalysisResult);

	}

	public void processVideo() throws IOException, InterruptedException
	{

		this.analyzeVideo();
		VideoCapture cap = new VideoCapture(this.inputPath);

		int width = (int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH);
		int height = (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT);
		double fps = cap.get(Videoio.CAP_PROP_FPS);

		String ffmpegPath=Util.getFFmpegPath();
		// ===== CONFIGURACIÓN FFMPEG =====

		ProcessBuilder pb = new ProcessBuilder(ffmpegPath, "-y",

				// ===== VIDEO DESDE JAVA =====
				"-f", "rawvideo", "-pixel_format", "bgr24", "-video_size", width + "x" + height, "-framerate",
				String.valueOf(fps), "-i", "-", // stdin (frames procesados)

				// ===== AUDIO DESDE ARCHIVO ORIGINAL =====
				"-i", this.inputPath,

				// ===== MAPEO =====
				"-map", "0:v:0", // video del pipe
				"-map", "1:a:0", // audio del archivo

				// ===== CODECS =====
				"-c:v", "libx264", "-pix_fmt", "yuv420p", "-c:a", "copy",

				// ===== OUTPUT =====
				this.outputPath);
		File logFile = new File("proceso.log");
		pb.redirectOutput(ProcessBuilder.Redirect.to(logFile));
		pb.redirectError(ProcessBuilder.Redirect.to(logFile));
		long start = System.nanoTime();
		Process process = pb.start();
		for (VideoProcessorListener videoProcessorListener : this.videoProcessorListeners)
			videoProcessorListener.videoCorrectInitiated(this);

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
			for (VideoProcessorListener videoProcessorListener : this.videoProcessorListeners)
				videoProcessorListener.frameProcessed(this,frame, frameCount);

			frame.release();


			frameCount++;
		}

		ffmpegInput.close();
		cap.release();

		int exitCode = process.waitFor();
		
		
		long end = System.nanoTime();

		double elapsedMs = (end - start) / 1_000_000.0;

		
		
		for (VideoProcessorListener videoProcessorListener : this.videoProcessorListeners)
			videoProcessorListener.videoCorrectCompleted(this, elapsedMs);
		
//		System.out.println("Proceso terminado");
//		System.out.println("Frames totales: " + frameCount);
//		System.out.println("FFmpeg exit code: " + exitCode);
	}

	@Override
	public String toString()
	{
		return "VideoProcessor [inputPath=" + inputPath + ", outputPath=" + outputPath + ", fps=" + fps
				+ ", frameCount=" + frameCount + "]";
	}

	public void addVideoProcessorListener(ConsoleVideoProcessorListener videoProcessorListener)
	{
		this.videoProcessorListeners.add(videoProcessorListener);

	}

}
