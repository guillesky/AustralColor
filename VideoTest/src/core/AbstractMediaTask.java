package core;

public abstract class AbstractMediaTask implements Runnable
{
	private String inputPath;
	private String outputPath;
	
	protected int percentageCompleted=0;
	private String status;
	
	
	
	

	public void setOutputPath(String outputPath)
	{
		this.outputPath = outputPath;
	}






	public String getStatus()
	{
	    return status;
	}






	public AbstractMediaTask(String inputPath,String outputPath)
	{
		super();
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	
	}

	



	
	@Override
	public void run()
	{
		MediaTaskManager.getInstance().getResource(this);
		MediaTaskManager.getInstance().mediaCorrectInitiated(this);
	   double elpasedTime=this.processMedia();
	   
	   this.percentageCompleted=100;
	   MediaTaskManager.getInstance().updatePercentageCompleted(this);
	   MediaTaskManager.getInstance().mediaCorrectCompleted(this, elpasedTime);
		  
	   MediaTaskManager.getInstance().releaseResource(this);
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
