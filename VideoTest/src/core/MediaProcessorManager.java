package core;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class MediaProcessorManager implements MediaProcessorListener
{
	private boolean allCanceled = false;
	private ArrayList<AbstractMediaProcessor> mediaProcessorsCanceled = new ArrayList<AbstractMediaProcessor>();
	private ArrayList<AbstractMediaProcessor> mediaProcessorsQueued = new ArrayList<AbstractMediaProcessor>();
	private ArrayList<AbstractMediaProcessor> mediaProcessorsProcessing = new ArrayList<AbstractMediaProcessor>();
	private ArrayList<AbstractMediaProcessor> mediaProcessorsTerminated = new ArrayList<AbstractMediaProcessor>();
	private int maxSimultaneousProcessing = 2;

	private ArrayList<MediaProcessorListener> mediaProcessorListeners = new ArrayList<MediaProcessorListener>();

	public void removeMediaProcessorQueued(AbstractMediaProcessor mediaProcessor)
	{
		this.mediaProcessorsQueued.remove(mediaProcessor);
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
		for (AbstractMediaProcessor mediaProcessor : this.mediaProcessorsProcessing)
		{
			this.cancelTask(mediaProcessor);
		}
		notifyAll();
	}

	@Override
	public void videoAnalized(VideoProcessorTask videoProcessor, VideoAnalysisResult videoAnalysisResult)
	{
		for (MediaProcessorListener mediaProcessorListener : this.mediaProcessorListeners)
			mediaProcessorListener.videoAnalized(videoProcessor, videoAnalysisResult);
	}

	@Override
	public void frameProcessed(VideoProcessorTask videoProcessor, Mat frame, int frameIndex)
	{
		for (MediaProcessorListener mediaProcessorListener : this.mediaProcessorListeners)
			mediaProcessorListener.frameProcessed(videoProcessor, frame, frameIndex);

	}

	@Override
	public void mediaCorrectCompleted(AbstractMediaProcessor videoProcessor, double elapsedMs)
	{
		for (MediaProcessorListener mediaProcessorListener : this.mediaProcessorListeners)
			mediaProcessorListener.mediaCorrectCompleted(videoProcessor, elapsedMs);
	}

	@Override
	public void mediaCorrectInitiated(AbstractMediaProcessor abstractMediaProcessor)
	{
		for (MediaProcessorListener mediaProcessorListener : this.mediaProcessorListeners)
			mediaProcessorListener.mediaCorrectInitiated(abstractMediaProcessor);

	}

	public void addVideoProcessorListener(ConsoleVideoProcessorListener videoProcessorListener)
	{
		this.mediaProcessorListeners.add(videoProcessorListener);

	}

	public synchronized void getResource(AbstractMediaProcessor abstractMediaProcessor)
	{
		while (this.mediaProcessorsProcessing.size() >= this.maxSimultaneousProcessing)
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
		this.mediaProcessorsQueued.remove(abstractMediaProcessor);
		this.mediaProcessorsProcessing.add(abstractMediaProcessor);
	}

	public synchronized void releaseResource(AbstractMediaProcessor abstractMediaProcessor)
	{
		this.mediaProcessorsProcessing.remove(abstractMediaProcessor);
		this.mediaProcessorsTerminated.add(abstractMediaProcessor);
		notifyAll();
	}

	@Override
	public void exceptionThrowed(Exception e)
	{
		for (MediaProcessorListener mediaProcessorListener : this.mediaProcessorListeners)
			mediaProcessorListener.exceptionThrowed(e);

	}

	public void startTask()
	{
		for (AbstractMediaProcessor mediaProcessor : this.mediaProcessorsQueued)
		{
			Thread h = new Thread(mediaProcessor);
			h.start();
		}

	}

	public synchronized void cancelTask(AbstractMediaProcessor mediaProcessor)
	{
		this.mediaProcessorsCanceled.add(mediaProcessor);
		this.mediaProcessorsProcessing.remove(mediaProcessor);

		notifyAll();
	}

	public synchronized void addTask(AbstractMediaProcessor abstractMediaProcessor)
	{

		this.mediaProcessorsQueued.add(abstractMediaProcessor);

	}

}
