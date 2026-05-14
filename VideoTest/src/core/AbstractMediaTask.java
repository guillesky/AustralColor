package core;

public abstract class AbstractMediaTask implements Runnable
{
	private String inputPath;
	private String outputPath;
	protected MediaTaskManager mediaTaskManager;
	protected int percentageCompleted=0;
	private String status;
	
	
	
	

	public String getStatus()
	{
	    return status;
	}






	public AbstractMediaTask(String inputPath,String outputPath,MediaTaskManager mediaTaskManager)
	{
		super();
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.mediaTaskManager=mediaTaskManager;
	}

	



	
	@Override
	public void run()
	{
	   this.mediaTaskManager.getResource(this);
	   this.mediaTaskManager.mediaCorrectInitiated(this);
	   double elpasedTime=this.processMedia();
	   
	   this.percentageCompleted=100;
	   this.mediaTaskManager.updatePercentageCompleted(this);
	   this.mediaTaskManager.mediaCorrectCompleted(this, elpasedTime);
		  
	   this.mediaTaskManager.releaseResource(this);
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
	
}
