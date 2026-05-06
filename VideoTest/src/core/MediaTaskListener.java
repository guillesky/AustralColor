package core;

import org.opencv.core.Mat;

public interface MediaTaskListener
{
	void videoAnalized(VideoTask videoTask, VideoAnalysisResult videoAnalysisResult);

	void frameProcessed(VideoTask videoTask, Mat frame, int frameIndex);

	void mediaCorrectCompleted(AbstractMediaTask abstractMediaTask, double elapsedMs);

	void mediaCorrectInitiated(AbstractMediaTask abstractMediaTask);

	void exceptionThrowed(Exception e);
}
