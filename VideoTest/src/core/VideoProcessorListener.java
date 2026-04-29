package core;

import org.opencv.core.Mat;

public interface VideoProcessorListener
{
	void videoAnalized(VideoProcessor videoProcessor,VideoAnalysisResult videoAnalysisResult);

	void frameProcessed(VideoProcessor videoProcessor, Mat frame, int frameIndex);

	void videoCorrectCompleted(VideoProcessor videoProcessor, double elapsedMs);
	void videoCorrectInitiated(VideoProcessor videoProcessor);
}
