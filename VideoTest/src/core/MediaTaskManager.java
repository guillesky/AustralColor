package core;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class MediaTaskManager 
{
	private static MediaTaskManager instance = null;
	private boolean stopSignalEmited = false;
	private ArrayList<AbstractMediaTask> mediaTasksCanceled = new ArrayList<AbstractMediaTask>();
	private ArrayList<AbstractMediaTask> mediaTasksQueued = new ArrayList<AbstractMediaTask>();
	private ArrayList<AbstractMediaTask> mediaTasksProcessing = new ArrayList<AbstractMediaTask>();
	private ArrayList<AbstractMediaTask> mediaTasksTerminated = new ArrayList<AbstractMediaTask>();
	private int maxSimultaneousProcessing = 2;
	private boolean working = false;
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
		notifyAll();
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

	public synchronized void emitStopSignal()
	{
		this.stopSignalEmited = true;
		notifyAll();
	}

	
	public void videoAnalized(VideoTask videoTask, VideoAnalysisResult videoAnalysisResult)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.videoAnalized(videoTask, videoAnalysisResult);
	}

	
	public void frameProcessed(VideoTask videoTask, Mat frame, int frameIndex)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.frameProcessed(videoTask, frame, frameIndex);

	}

	
	public void mediaCorrectCompleted(AbstractMediaTask videoTask, double elapsedMs)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.mediaCorrectCompleted(videoTask, elapsedMs);
	}

	
	public void mediaCorrectInitiated(AbstractMediaTask abstractMediaTask)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.mediaCorrectInitiated(abstractMediaTask);

	}

	public synchronized void addMediaTaskListener(MediaTaskListener mediaTaskListener)
	{
		this.mediaTaskListeners.add(mediaTaskListener);

	}

	public synchronized void releaseResource(AbstractMediaTask abstractMediaTask)
	{
		this.mediaTasksProcessing.remove(abstractMediaTask);
		this.mediaTasksTerminated.add(abstractMediaTask);

		notifyAll();
	}

	public boolean isWorking()
	{
		return this.working;
	}

	
	public void exceptionThrowed(Exception e)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.exceptionThrowed(e);

	}

	public synchronized void cancelTask(AbstractMediaTask mediaTask)
	{
		this.mediaTasksCanceled.add(mediaTask);
		this.mediaTasksProcessing.remove(mediaTask);

		notifyAll();
	}

	protected synchronized boolean wasCanceled(AbstractMediaTask mediaTask)
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

	
	public void updatePercentageCompleted(AbstractMediaTask abstractMediaTask)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.updatePercentageCompleted(abstractMediaTask);
	}

	protected synchronized void fireTask()
	{
		while (this.maxSimultaneousProcessing - this.mediaTasksProcessing.size() <= 0)
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				this.exceptionThrowed(e);

			}
		if (!this.mediaTasksQueued.isEmpty())
		{
			AbstractMediaTask abstractMediaTask = this.mediaTasksQueued.get(0);
			Thread h = new Thread(abstractMediaTask);
			h.start();
			this.mediaTasksQueued.remove(abstractMediaTask);
			this.mediaTasksProcessing.add(abstractMediaTask);

		}
		else if (this.mediaTasksProcessing.isEmpty()) 
			this.working=false;
	}

	public void allTaskFinished(double elapsedMs)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.allTaskFinished(elapsedMs);
	}

	public void startTask()
	{
		this.working = true;
		MediaTaskFirer mediaTaskFirer = new MediaTaskFirer();
		mediaTaskFirer.start();

	}

	
	public void videoTaskCanceled(AbstractMediaTask abstractMediaTask)
	{
		for (MediaTaskListener mediaTaskListener : this.mediaTaskListeners)
			mediaTaskListener.videoTaskCanceled(abstractMediaTask);
	}

	public synchronized int getProcessingTaskCount()
	{
		return this.mediaTasksProcessing.size();
	}

	public synchronized int getQueuedTaskCount()
	{
		return this.mediaTasksQueued.size();
	}

}
