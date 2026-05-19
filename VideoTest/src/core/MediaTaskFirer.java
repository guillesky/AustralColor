package core;

public class MediaTaskFirer extends Thread
{

	@Override
	public void run()
	{
		long start = System.nanoTime();
		while (MediaTaskManager.getInstance().isWorking())
		{
			MediaTaskManager.getInstance().fireTask();
		}
		long end = System.nanoTime();
		double elapsedMs = (end - start) / 1_000_000.0;
		MediaTaskManager.getInstance().allTaskFinished(elapsedMs);

	}

}
