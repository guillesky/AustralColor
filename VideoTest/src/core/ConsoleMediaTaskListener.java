package core;

import org.opencv.core.Mat;

public class ConsoleMediaTaskListener implements MediaTaskListener
{

	@Override
	public void videoAnalized(VideoTask videoTask, VideoAnalysisResult videoAnalysisResult)
	{
		System.out.println(videoTask);
		System.out.println("Analisis Completo");
		System.out.println(videoAnalysisResult.detalle());
	}

	@Override
	public void frameProcessed(VideoTask videoTask, Mat frame, int frameIndex)
	{
		System.out.println(videoTask.getInputPath() + " Frame procesada:" + frameIndex);

	}

	@Override
	public void mediaCorrectCompleted(AbstractMediaTask abstractMediaTask, double elapsedMs)
	{
		System.out.println(abstractMediaTask.getInputPath());
		System.out.println("Correccion completa");
		System.out.println("Tiempo total: " + elapsedMs);

	}

	@Override
	public void mediaCorrectInitiated(AbstractMediaTask abstractMediaTask)
	{
		System.out.println(abstractMediaTask);
		System.out.println("Correccion iniciada");

	}

	@Override
	public void exceptionThrowed(Exception e)
	{
		System.out.println(e.getMessage());
		
	}
	
	

}
