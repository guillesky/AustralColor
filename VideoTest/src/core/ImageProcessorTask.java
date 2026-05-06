package core;

import java.util.Arrays;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageProcessorTask extends AbstractMediaProcessor
{

	public ImageProcessorTask(String inputPath, String outputPath, MediaProcessorManager mediaProcessorManager)
	{
		super(inputPath, outputPath, mediaProcessorManager);

	}

	@Override
	protected double processMedia()
	{
		long start = System.nanoTime();
		double elapsedMs = 0;
		Mat mat = Imgcodecs.imread(this.getInputPath());

		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);
		double[] filtros = ImageProcessor.getFilterMatrix(mat);
		Mat correctedMat = ImageProcessor.correct(mat);

		Imgproc.cvtColor(correctedMat, correctedMat, Imgproc.COLOR_RGB2BGR);
		Imgcodecs.imwrite("salida.jpg", correctedMat);
		mat.release();
		correctedMat.release();
		long end = System.nanoTime();
		elapsedMs = (end - start) / 1_000_000.0;
		return elapsedMs;
	}

}
