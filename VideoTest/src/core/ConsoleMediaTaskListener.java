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
		// System.out.println(videoTask.getInputPath() + " Frame procesada:" +
		// frameIndex);

	}

	@Override
	public void mediaCorrectionFinished(AbstractMediaTask abstractMediaTask, double elapsedMs)
	{
		System.out.println(abstractMediaTask.getInputPath() + " Correccion completa - Tiempo total: " + elapsedMs
				+ "\n**************************************************");

	}

	@Override
	public void mediaCorrectInitiated(AbstractMediaTask abstractMediaTask)
	{
		System.out.println(abstractMediaTask
				+ "  Correccion iniciada \n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

	}

	@Override
	public void exceptionThrowed(Exception e)
	{
		System.out.println(e.getMessage());

	}

	@Override
	public void updatePercentageCompleted(AbstractMediaTask abstractMediaTask)
	{
		System.out.println(
				abstractMediaTask.getInputPath() + " Porcentage: " + abstractMediaTask.getPercentageCompleted());

	}

	@Override
	public void allTaskFinished(double elapsedMs)
	{
		System.out.println("Todos los procesos terminaron " + elapsedMs);
	}

	@Override
	public void videoTaskCanceled(AbstractMediaTask abstractMediaTask)
	{
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" + abstractMediaTask.getInputPath()
				+ "\n CANCELADO\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

	@Override
	public void videoTaskCompleted(VideoTask videoTask)
	{
		System.out.println(videoTask.getInputPath() + "Completado");
	}

}
