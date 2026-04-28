package videotest;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class VideoProcessor
{
	private static final int SAMPLE_SECONDS = 2; // Extracts color correction from every N seconds

	public static VideoAnalysisResult analyzeVideo(String inputPath, String outputPath)
	{
		System.out.println(inputPath);
		VideoCapture cap = new VideoCapture(inputPath);

		int fps =(int) cap.get(Videoio.CAP_PROP_FPS);
		int frameCount =(int) cap.get(Videoio.CAP_PROP_FRAME_COUNT);

		int count = 0;
		Mat frame = new Mat();

		System.out.println("Analyzing...");
		VideoAnalysisResult result = new VideoAnalysisResult(inputPath, outputPath, fps, frameCount);

		while (cap.read(frame))
		{

			System.out.print(count + " frames\r");

			// cada N segundos
			if (count % ((int)fps * SAMPLE_SECONDS) == 0 || count==frameCount-1)
			{

				Mat rgb = new Mat();
				Imgproc.cvtColor(frame, rgb, Imgproc.COLOR_BGR2RGB);
				result.addFilter(count, ImageProcessor.getFilterMatrix(rgb));

			}
			
			count++;
		}

		cap.release();

		return result;
	}

}
