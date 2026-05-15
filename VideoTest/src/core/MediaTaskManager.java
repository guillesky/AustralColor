package core;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class MediaTaskManager implements MediaTaskListener
{
	private static MediaTaskManager instance = null;
	private boolean allCanceled = false;
	private ArrayList<AbstractMediaTask> mediaTasksCanceled = new ArrayList<AbstractMediaTask>();
	private ArrayList<AbstractMediaTask> mediaTasksQueued = new ArrayList<AbstractMediaTask>();
	private ArrayList<AbstractMediaTask> mediaTasksProcessing = new ArrayList<AbstractMediaTask>();
	private ArrayList<AbstractMediaTask> mediaTasksTerminated = new ArrayList<AbstractMediaTask>();
	private int maxSimultaneousProcessing = 2;
	private int taskWorking = 0;

	private ArrayList<MediaTaskListener> mediaTaskListeners = new ArrayList<MediaTaskListener>();

	private MediaTaskManager()
	{
	}

	public static MediaTaskManager getInstance()
	{
		if (instance == null)
			instance = new MediaTaskManager();
		return instance;
	}

	public synchronized void removeMediaTasksQueued(AbstractMediaTask mediaTask)
	{
		this.mediaTasksQueued.remove(mediaTask);
		if(this.isWorking())
			this.cancelTask(mediaTask);
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

	public synchronized void addMediaTaskListener(MediaTaskListener mediaTaskListener)
	{
		this.mediaTaskListeners.add(mediaTaskListener);

	}

	public synchronized void getResource(AbstractMediaTask abstractMediaTask)
	{
		while (this.mediaTasksProcessing.size() >= this.maxSimultaneousProcessing && !this.isMyTurn(abstractMediaTask))
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{

			}
		}
		this.mediaTasksQueued.remove(abstractMediaTask);
		this.mediaTasksProcessing.add(abstractMediaTask);
	}

	public synchronized void releaseResource(AbstractMediaTask abstractMediaTask)
	{
		this.mediaTasksProcessing.remove(abstractMediaTask);
		this.mediaTasksTerminated.add(abstractMediaTask);
		this.taskWorking--;
		notifyAll();
	}

	public boolean isWorking()
	{
		return this.taskWorking != 0;
	}

	@Override
	public void exceptionThrowed(Exception e)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.exceptionThrowed(e);

	}

	public synchronized void startTask()
	{
		for (AbstractMediaTask mediaTask : this.mediaTasksQueued)
		{
			Thread h = new Thread(mediaTask);
			h.start();
			this.taskWorking++;
		}

	}

	public synchronized void cancelTask(AbstractMediaTask mediaTask)
	{
		this.mediaTasksCanceled.add(mediaTask);
		this.mediaTasksProcessing.remove(mediaTask);

		notifyAll();
	}

	protected synchronized boolean isCanceled(AbstractMediaTask mediaTask)
	{
		return this.mediaTasksCanceled.contains(mediaTask);
	}

	public synchronized void addTask(AbstractMediaTask abstractMediaTask)
	{
		this.mediaTasksQueued.add(abstractMediaTask);
	}

	public synchronized void addFirstTask(AbstractMediaTask abstractMediaTask)
	{
		this.mediaTasksQueued.addFirst(abstractMediaTask);
	}

	private synchronized boolean isMyTurn(AbstractMediaTask abstractMediaTask)
	{
		int index = this.mediaTasksQueued.indexOf(abstractMediaTask);
		int available = this.maxSimultaneousProcessing - this.mediaTasksProcessing.size();
		return index > -1 && index < available;
	}

	@Override
	public void updatePercentageCompleted(AbstractMediaTask abstractMediaTask)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.updatePercentageCompleted(abstractMediaTask);
	}

}
