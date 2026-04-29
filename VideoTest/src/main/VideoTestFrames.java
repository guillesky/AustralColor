package main;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import core.ImageProcessor;

public class VideoTestFrames
{

	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args)
	{

		String input = "input.mp4";
		String outputDir = "frames";

		// Crear carpeta si no existe
		File dir = new File(outputDir);
		if (!dir.exists())
		{
			boolean created = dir.mkdirs();
			if (!created)
			{
				System.out.println("No se pudo crear la carpeta de salida");
				return;
			}
		}

		VideoCapture cap = new VideoCapture(input);

		if (!cap.isOpened())
		{
			System.out.println("No se pudo abrir el video de entrada");
			return;
		}

		System.out.println("Backend captura: " + cap.getBackendName());

		double fps = cap.get(Videoio.CAP_PROP_FPS);
		int width = (int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH);
		int height = (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT);

		System.out.println("FPS: " + fps);
		System.out.println("Resolución: " + width + "x" + height);

		Mat frame = new Mat();
		int frameCount = 0;

		while (cap.read(frame))
		{
			processAndSave(frame, outputDir, frameCount);
			
			if (frameCount % 30 == 0)
			{
				System.out.println("Procesados: " + frameCount + " frames");
			}
			frameCount++;
		}

		cap.release();

		System.out.println("Proceso terminado");
		System.out.println("Total frames: " + frameCount);
	}

	private static void processAndSave(Mat frame, String outputDir, int frameCount)
	{
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
		frame = ImageProcessor.correct(frame);

		String filename = String.format("%s/frame_%05d.jpg", outputDir, frameCount);
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2BGR);

		Imgcodecs.imwrite(filename, frame);
		frame.release();
	}
}