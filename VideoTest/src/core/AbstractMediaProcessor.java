package core;

public abstract class AbstractMediaProcessor implements Runnable
{
	private String inputPath;
	private String outputPath;
	protected MediaProcessorManager mediaProcessorManager;
	
	

	public AbstractMediaProcessor(String inputPath,String outputPath,MediaProcessorManager mediaProcessorManager)
	{
		super();
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.mediaProcessorManager=mediaProcessorManager;
	}

	



	
	@Override
	public void run()
	{
	   this.mediaProcessorManager.getResource(this);
	   this.mediaProcessorManager.mediaCorrectInitiated(this);
		
	   double elpasedTime=this.processMedia();
	   this.mediaProcessorManager.mediaCorrectCompleted(this, elpasedTime);
	   
	   this.mediaProcessorManager.releaseResource(this);
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
}
