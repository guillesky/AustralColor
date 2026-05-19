package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Mat;

import core.AbstractMediaTask;
import core.ConsoleMediaTaskListener;
import core.MediaTaskListener;
import core.MediaTaskManager;
import core.TaskFactory;
import core.Util;
import core.VideoAnalysisResult;
import core.VideoTask;
import i18n.Messages;

public class Controlador implements ActionListener, MediaTaskListener
{

	private IVista vista;
	private TaskFactory factory = new TaskFactory();

	public Controlador(IVista vista)
	{
		super();

		this.vista = vista;
		vista.setControlador(this);
		MediaTaskManager.getInstance().addMediaTaskListener(this);
		// ConsoleMediaTaskListener cpl = new ConsoleMediaTaskListener();
		// MediaTaskManager.getInstance().addMediaTaskListener(cpl);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
		case IVista.START_TASK:
			MediaTaskManager.getInstance().startTask();
			break;
		case IVista.CANCEL_TASK:
			this.cancelTask();
			break;
		case IVista.DELETE_TASK:
			this.deleteTask();
			break;
		}

	}

	private void deleteTask()
	{
		ArrayList<AbstractMediaTask> selected = this.vista.getSelectedMediaTask();
		for (AbstractMediaTask mediaTask : selected)
		{
			if (mediaTask.getStatus() == Messages.QUEUED.getValue())
			{
				MediaTaskManager.getInstance().removeMediaTasksQueued(mediaTask);
				this.vista.removeTasks(mediaTask);
			} else if (mediaTask.getStatus() == Messages.CANCEL.getValue()
					|| mediaTask.getStatus() == Messages.COMPLETED.getValue())
				this.vista.removeTasks(mediaTask);
		}
	}

	private void cancelTask()
	{
		ArrayList<AbstractMediaTask> selected = this.vista.getSelectedMediaTask();
		for (AbstractMediaTask mediaTask : selected)
		{
			if (mediaTask.getStatus() == Messages.PROCESSING.getValue())
				MediaTaskManager.getInstance().cancelTask(mediaTask);
		}

	}

	@Override
	public void videoAnalized(VideoTask videoTask, VideoAnalysisResult videoAnalysisResult)
	{
		this.vista.updateTaskStatus(videoTask);

	}

	@Override
	public void frameProcessed(VideoTask videoTask, Mat frame, int frameIndex)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePercentageCompleted(AbstractMediaTask abstractMediaTask)
	{
		this.vista.updatePercentageCompleted(abstractMediaTask);

	}

	@Override
	public void mediaCorrectCompleted(AbstractMediaTask abstractMediaTask, double elapsedMs)
	{
		this.vista.updateTaskStatus(abstractMediaTask);
		this.vista.updateLogText(
				abstractMediaTask.getInputFileName() + " correccion de color completa en " + elapsedMs + " ms");

	}

	@Override
	public void mediaCorrectInitiated(AbstractMediaTask abstractMediaTask)
	{
		this.vista.updateTaskStatus(abstractMediaTask);

	}

	@Override
	public void exceptionThrowed(Exception e)
	{
		this.vista.updateLogText(e.getMessage());

	}

	public void addFiles(File[] selectedFiles)
	{
		ArrayList<AbstractMediaTask> mediaTasks = this.factory.getTasks(selectedFiles);
		for (AbstractMediaTask task : mediaTasks)
			MediaTaskManager.getInstance().addTask(task);
		this.vista.addTasks(mediaTasks);
	}

	@Override
	public void allTaskFinished(double elapsedMs)
	{
		this.vista.allTaskFinished(elapsedMs);

	}

	@Override
	public void videoTaskCanceled(AbstractMediaTask abstractMediaTask)
	{
		this.vista.updateTaskStatus(abstractMediaTask);
		this.vista.updateLogText("Cancelada la correccion de: " + abstractMediaTask.getInputFileName());

	}

}
