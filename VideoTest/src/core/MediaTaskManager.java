package core;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class MediaTaskManager implements MediaTaskListener
{
	private boolean allCanceled = false;
	private ArrayList<AbstractMediaTask> mediaTasksCanceled = new ArrayList<AbstractMediaTask>();
	private ArrayList<AbstractMediaTask> mediaTasksQueued = new ArrayList<AbstractMediaTask>();
	private ArrayList<AbstractMediaTask> mediaTasksProcessing = new ArrayList<AbstractMediaTask>();
	private ArrayList<AbstractMediaTask> mediaTasksTerminated = new ArrayList<AbstractMediaTask>();
	private int maxSimultaneousProcessing = 2;

	private ArrayList<MediaTaskListener> mediaTaskListeners = new ArrayList<MediaTaskListener>();

	public void removeMediaProcessorQueued(AbstractMediaTask mediaTask)
	{
		this.mediaTasksQueued.remove(mediaTask);
	}

	public synchronized int getMaxSimultaneousProcessing()
	{
		return maxSimultaneousProcessing;
	}

	public synchronized void setMaxSimultaneousProcessing(int maxSimultaneousProcessing)
	{
		if (maxSimultaneousProcessing > this.maxSimultaneousProcessing)
			notifyAll();
		this.maxSimultaneousProcessing = maxSimultaneousProcessing;
	}

	public synchronized boolean isAllCanceled()
	{
		return allCanceled;
	}

	public synchronized void cancellAll()
	{
		this.allCanceled = true;
		for (AbstractMediaTask mediaTak : this.mediaTasksProcessing)
		{
			this.cancelTask(mediaTak);
		}
		notifyAll();
	}

	@Override
	public void videoAnalized(VideoTask videoTask, VideoAnalysisResult videoAnalysisResult)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.videoAnalized(videoTask, videoAnalysisResult);
	}

	@Override
	public void frameProcessed(VideoTask videoTask, Mat frame, int frameIndex)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.frameProcessed(videoTask, frame, frameIndex);

	}

	@Override
	public void mediaCorrectCompleted(AbstractMediaTask videoTask, double elapsedMs)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.mediaCorrectCompleted(videoTask, elapsedMs);
	}

	@Override
	public void mediaCorrectInitiated(AbstractMediaTask abstractMediaTask)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.mediaCorrectInitiated(abstractMediaTask);

	}

	public void addMediaTaskListener(MediaTaskListener mediaTaskListener)
	{
		this.mediaTaskListeners.add(mediaTaskListener);

	}

	public synchronized void getResource(AbstractMediaTask abstractMediaTask)
	{
		while (this.mediaTasksProcessing.size() >= this.maxSimultaneousProcessing)
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.mediaTasksQueued.remove(abstractMediaTask);
		this.mediaTasksProcessing.add(abstractMediaTask);
	}

	public synchronized void releaseResource(AbstractMediaTask abstractMediaTask)
	{
		this.mediaTasksProcessing.remove(abstractMediaTask);
		this.mediaTasksTerminated.add(abstractMediaTask);
		notifyAll();
	}

	@Override
	public void exceptionThrowed(Exception e)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.exceptionThrowed(e);

	}

	public void startTask()
	{
		for (AbstractMediaTask mediaTask : this.mediaTasksQueued)
		{
			Thread h = new Thread(mediaTask);
			h.start();
		}

	}

	public synchronized void cancelTask(AbstractMediaTask mediaTask)
	{
		this.mediaTasksCanceled.add(mediaTask);
		this.mediaTasksProcessing.remove(mediaTask);

		notifyAll();
	}

	public synchronized void addTask(AbstractMediaTask abstractMediaTask)
	{

		this.mediaTasksQueued.add(abstractMediaTask);

	}

}
