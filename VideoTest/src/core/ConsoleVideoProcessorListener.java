package core;

import org.opencv.core.Mat;

public class ConsoleVideoProcessorListener implements VideoProcessorListener
{

	@Override
	public void videoAnalized(VideoProcessor videoProcessor, VideoAnalysisResult videoAnalysisResult)
	{
		System.out.println(videoProcessor);
		System.out.println("Analisis Completo");
		System.out.println(videoAnalysisResult.detalle());
	}

	@Override
	public void frameProcessed(VideoProcessor videoProcessor,Mat frame, int frameIndex)
	{
		System.out.println(videoProcessor.getInputPath()+" Frame procesada:"+frameIndex);

	}

	@Override
	public void videoCorrectCompleted(VideoProcessor videoProcessor, double elapsedMs)
	{
		System.out.println(videoProcessor.getInputPath());
		System.out.println("Correccion completa");
		System.out.println("Tiempo total: "+elapsedMs);

	}

	@Override
	public void videoCorrectInitiated(VideoProcessor videoProcessor)
	{
		System.out.println(videoProcessor);
		System.out.println("Correccion iniciada");
		
	}

}
