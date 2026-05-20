package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Mat;

import core.AbstractMediaTask;
import core.Environment;
import core.MediaImportResult;
import core.MediaTaskListener;
import core.MediaTaskManager;
import core.TaskFactory;
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
		case IVista.RENAME_DUPLICATED_FILES:
		case IVista.OVERWRITE_DUPLICATED_FILES:
		case IVista.IGNORE_DUPLICATED_FILES:
			this.duplicatedFilePolicyChange();
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
	public void mediaCorrectionFinished(AbstractMediaTask abstractMediaTask, double elapsedMs)
	{
		this.vista.updateTaskStatus(abstractMediaTask);
		String text = null;
		if (abstractMediaTask.isCanceled())
			text = abstractMediaTask.getInputFileName() + " correccion de color Cancelada en " + elapsedMs + " ms";
		else
			text = abstractMediaTask.getInputFileName() + " correccion de color Completa en " + elapsedMs + " ms";

		this.vista.updateLogText(text);

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
		MediaImportResult result = this.factory.getMediaImportResult(selectedFiles);
		ArrayList<AbstractMediaTask> mediaTasks = result.getMediaTasks();
		for (AbstractMediaTask task : mediaTasks)
			MediaTaskManager.getInstance().addTask(task);

		this.vista.addTasks(mediaTasks);

		if (!result.getIgnoredFiles().isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Arvhivos ignorados:\n");
			for (String fileName : result.getIgnoredFiles())
			{
				sb.append(fileName);sb.append("\n");
			}
			this.vista.updateLogText(sb.toString());
		}
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

	public void duplicatedFilePolicyChange()
	{
		Environment.getInstance().setDuplicateFilePolicy(this.vista.getDuplicateFilePolicy());
	}

}
