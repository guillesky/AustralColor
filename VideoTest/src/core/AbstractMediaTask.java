package core;

import java.io.File;

import i18n.Messages;

public abstract class AbstractMediaTask implements Runnable
{
	private String inputPath;
	private String outputPath;
	private String inputFileName;
	protected boolean canceled = false;
	protected int percentageCompleted = 0;
	protected String status;

	public void setOutputPath(String outputPath)
	{
		this.outputPath = outputPath;
	}

	public String getStatus()
	{
		return status;
	}

	public AbstractMediaTask(File file, String outputPath)
	{
		super();
		this.status = Messages.QUEUED.getValue();
		this.inputPath = file.getAbsolutePath();
		this.inputFileName = file.getName();

		this.outputPath = outputPath;
	}

	@Override
	public void run()
	{

		MediaTaskManager.getInstance().mediaCorrectInitiated(this);
		double elpasedTime = this.processMedia();

		MediaTaskManager.getInstance().releaseResource(this);
		if (this.status == Messages.PROCESSING.getValue())
			this.status = Messages.COMPLETED.getValue();
		MediaTaskManager.getInstance().mediaCorrectionFinished(this, elpasedTime);

	}

	protected abstract double processMedia();

	public String getInputPath()
	{
		return inputPath;
	}

	public String getOutputPath()
	{
		return outputPath;
	}

	public int getPercentageCompleted()
	{
		return percentageCompleted;
	}

	public String getInputFileName()
	{
		return inputFileName;
	}

	public boolean isCanceled()
	{
		return canceled;
	}

}
