package core;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class VideoProcessorManager implements VideoProcessorListener
{
    private boolean allCanceled = false;
    private ArrayList<VideoProcessor> videoProcessorsCanceled = new ArrayList<VideoProcessor>();
    private ArrayList<VideoProcessor> videoProcessorsQueued = new ArrayList<VideoProcessor>();
    private ArrayList<VideoProcessor> videoProcessorsProcessing = new ArrayList<VideoProcessor>();
    private ArrayList<VideoProcessor> videoProcessorsTerminated = new ArrayList<VideoProcessor>();
    private int maxSimultaneousProcessing = 1;
    private ArrayList<VideoProcessorListener> videoProcessorListeners = new ArrayList<VideoProcessorListener>();

    public synchronized void addVideoProcessorCanceled(VideoProcessor videoProcessor)
    {
	this.videoProcessorsCanceled.add(videoProcessor);
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

    public synchronized void setAllCanceled(boolean allCanceled)
    {
	this.allCanceled = allCanceled;
	notifyAll();
    }

    @Override
    public void videoAnalized(VideoProcessor videoProcessor, VideoAnalysisResult videoAnalysisResult)
    {
	for (VideoProcessorListener videoProcessorListener : this.videoProcessorListeners)
	    videoProcessorListener.videoAnalized(videoProcessor, videoAnalysisResult);
    }

    @Override
    public void frameProcessed(VideoProcessor videoProcessor, Mat frame, int frameIndex)
    {
	for (VideoProcessorListener videoProcessorListener : this.videoProcessorListeners)
	    videoProcessorListener.frameProcessed(videoProcessor, frame, frameIndex);

    }

    @Override
    public void videoCorrectCompleted(VideoProcessor videoProcessor, double elapsedMs)
    {
	for (VideoProcessorListener videoProcessorListener : this.videoProcessorListeners)
	    videoProcessorListener.videoCorrectCompleted(videoProcessor, elapsedMs);
    }

    @Override
    public void videoCorrectInitiated(VideoProcessor videoProcessor)
    {
	for (VideoProcessorListener videoProcessorListener : this.videoProcessorListeners)
	    videoProcessorListener.videoCorrectInitiated(videoProcessor);

    }

    public void addVideoProcessorListener(ConsoleVideoProcessorListener videoProcessorListener)
    {
	this.videoProcessorListeners.add(videoProcessorListener);

    }

}
