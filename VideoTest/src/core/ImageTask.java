package core;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import i18n.Messages;

public class ImageTask extends AbstractMediaTask
{

	public ImageTask(File file,String outputPath)
	{
		super(file,outputPath);

	}

	@Override
	protected double processMedia()
	{
		
		long start = System.nanoTime();
		double elapsedMs = 0;
		this.status = Messages.PROCESSING.getValue();
		Mat mat = Imgcodecs.imread(this.getInputPath());

		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);
		Mat correctedMat = ImageProcessor.correct(mat);

		Imgproc.cvtColor(correctedMat, correctedMat, Imgproc.COLOR_RGB2BGR);
		Imgcodecs.imwrite(this.getOutputPath(), correctedMat);
		mat.release();
		correctedMat.release();
		long end = System.nanoTime();
		elapsedMs = (end - start) / 1_000_000.0;
		this.percentageCompleted = 100;
		MediaTaskManager.getInstance().updatePercentageCompleted(this);
		return elapsedMs;
	}

	@Override
	public String toString()
	{
		return "ImageProcessor: inputPath=" + this.getInputPath();
	}
}
