package core;

import java.util.Arrays;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageTask extends AbstractMediaTask
{

	public ImageTask(String inputPath, String outputPath, MediaTaskManager mediaTaskManager)
	{
		super(inputPath, outputPath, mediaTaskManager);

	}

	@Override
	protected double processMedia()
	{
		long start = System.nanoTime();
		double elapsedMs = 0;
		Mat mat = Imgcodecs.imread(this.getInputPath());

		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);
		Mat correctedMat = ImageProcessor.correct(mat);

		Imgproc.cvtColor(correctedMat, correctedMat, Imgproc.COLOR_RGB2BGR);
		Imgcodecs.imwrite(this.getOutputPath(), correctedMat);
		mat.release();
		correctedMat.release();
		long end = System.nanoTime();
		elapsedMs = (end - start) / 1_000_000.0;
		return elapsedMs;
	}

	@Override
	public String toString()
	{
		return "ImageProcessor: inputPath=" + this.getInputPath() + ", outputPath=" + this.getOutputPath();
	}
}
