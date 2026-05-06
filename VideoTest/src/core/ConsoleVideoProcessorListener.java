package core;

import org.opencv.core.Mat;

public class ConsoleVideoProcessorListener implements MediaProcessorListener
{

	@Override
	public void videoAnalized(VideoProcessorTask videoProcessor, VideoAnalysisResult videoAnalysisResult)
	{
		System.out.println(videoProcessor);
		System.out.println("Analisis Completo");
		System.out.println(videoAnalysisResult.detalle());
	}

	@Override
	public void frameProcessed(VideoProcessorTask videoProcessor, Mat frame, int frameIndex)
	{
		System.out.println(videoProcessor.getInputPath() + " Frame procesada:" + frameIndex);

	}

	@Override
	public void mediaCorrectCompleted(AbstractMediaProcessor abstractMediaProcessor, double elapsedMs)
	{
		System.out.println(abstractMediaProcessor.getInputPath());
		System.out.println("Correccion completa");
		System.out.println("Tiempo total: " + elapsedMs);

	}

	@Override
	public void mediaCorrectInitiated(AbstractMediaProcessor abstractMediaProcessor)
	{
		System.out.println(abstractMediaProcessor);
		System.out.println("Correccion iniciada");

	}

	@Override
	public void exceptionThrowed(Exception e)
	{
		System.out.println(e.getMessage());
		
	}
	
	

}
