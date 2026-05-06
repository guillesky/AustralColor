package core;

import org.opencv.core.Mat;

public interface MediaProcessorListener
{
	void videoAnalized(VideoProcessorTask videoProcessor, VideoAnalysisResult videoAnalysisResult);

	void frameProcessed(VideoProcessorTask videoProcessor, Mat frame, int frameIndex);

	void mediaCorrectCompleted(AbstractMediaProcessor abstractMediaProcessor, double elapsedMs);

	void mediaCorrectInitiated(AbstractMediaProcessor abstractMediaProcessor);

	void exceptionThrowed(Exception e);
}
