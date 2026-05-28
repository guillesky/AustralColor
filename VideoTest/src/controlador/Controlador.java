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
import i18n.Language;
import i18n.Messages;
import util.Config;
import util.Util;

public class Controlador implements ActionListener, MediaTaskListener
{

	private IVista vista;
	private TaskFactory factory = new TaskFactory();
	private int processedFrame = 0;
	private long start;

	private long end;
	private double maxDeltaFrame = 20;

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
			this.startTasks();

			break;
		case IVista.CANCEL_TASK:
			this.cancelTask();
			break;
		case IVista.DELETE_TASK:
			this.deleteTask();
			break;
		case IVista.STOP_ALL:
			this.stopAll();
			break;

		case IVista.RENAME_DUPLICATED_FILES:
		case IVista.OVERWRITE_DUPLICATED_FILES:
		case IVista.IGNORE_DUPLICATED_FILES:
			this.duplicatedFilePolicyChange();
			break;
		case IVista.CHANGE_LANGUAGE:
			this.changeLanguage();
			break;
		case IVista.CREDITS:
			this.showCredits();
			break;
			
		}

	}

	private void showCredits()
	{
		this.vista.showCredits();
	}

	private void changeLanguage()
	{
		Language language = this.vista.getSelectedLanguage();
		language.setMessages();
		this.vista.updateLanguage(language);
		Environment.getInstance().setSelectedLanguage(language);
	}

	private void stopAll()
	{
		MediaTaskManager.getInstance().emitStopSignal();
	}

	private void startTasks()
	{
		MediaTaskManager.getInstance().startTask();
		this.start = System.nanoTime();
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
		this.processedFrame++;
		if (this.processedFrame >= this.maxDeltaFrame)
		{
			this.end = System.nanoTime();
			double elapsedMs = (end - start) / 1_000_000_000.0;
			this.vista.updateFPS(this.maxDeltaFrame / elapsedMs);

			this.processedFrame = 0;
			this.start = System.nanoTime();
		}
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
			text = abstractMediaTask.getInputFileName() + Messages.TASK_CANCELED.getValue() + elapsedMs
					+ Messages.MILLISECONDS.getValue();
		else
			text = abstractMediaTask.getInputFileName() + Messages.TASK_COMPLETED.getValue() + elapsedMs
					+ Messages.MILLISECONDS.getValue();

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
		StringBuilder sb = new StringBuilder();
		if (!result.getUnknowFiles().isEmpty())
		{
			sb.append(Messages.UNKNOWN_FILES.getValue());
			for (String fileName : result.getUnknowFiles())
			{
				sb.append(fileName);
				sb.append("\n");
			}

		}

		if (!result.getAlreadyQueuedFiles().isEmpty())
		{
			sb.append(Messages.ALREADY_QUEUED_FILES.getValue());
			for (String fileName : result.getAlreadyQueuedFiles())
			{
				sb.append(fileName);
				sb.append("\n");
			}

		}
		if (!result.getExistingFiles().isEmpty())
		{
			if (Environment.getInstance().getDuplicateFilePolicy() == Environment.OVERWRITE_DUPLICATED_FILES)
				sb.append(Messages.ALREADY_EXISTING_FILES_OVERWRITTEN.getValue());
			else if (Environment.getInstance().getDuplicateFilePolicy() == Environment.IGNORE_DUPLICATED_FILES)
				sb.append(Messages.ALREADY_EXISTING_FILES_IGNORED.getValue());
			for (String fileName : result.getExistingFiles())
			{
				sb.append(fileName);
				sb.append("\n");
			}

		}
		if (!result.getRenamedFiles().isEmpty())
		{
			sb.append(Messages.ALREADY_EXISTING_FILES_RENAMED.getValue());
			for (String fileName : result.getRenamedFiles().keySet())
				sb.append(fileName + " ---> " + result.getRenamedFiles().get(fileName) + "\n");

		}
		sb.append(Messages.ADDED.getValue() + result.getMediaTasks().size() + Messages.NEW_FILES_TO_PROCESS.getValue());
		this.vista.updateLogText(sb.toString());
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
		this.vista.updateLogText(Messages.CANCELED_TASK_OF.getValue() + abstractMediaTask.getInputFileName());

	}

	public void duplicatedFilePolicyChange()
	{
		Environment.getInstance().setDuplicateFilePolicy(this.vista.getDuplicateFilePolicy());
	}

	@Override
	public void videoTaskCompleted(VideoTask videoTask)
	{
		this.vista.updateTaskStatus(videoTask);

	}

	public void changeSimultaneosProcess(int value)
	{
		MediaTaskManager.getInstance().setMaxSimultaneousProcessing(value);
		Environment.getInstance().configHasChanged();
	}

	public void shutdown()
	{
		if (Environment.getInstance().isConfigHasChanged())
		{
			Config config = new Config(Environment.getInstance().getSelectedLanguage().getFileCode(),MediaTaskManager.getInstance().getMaxSimultaneousProcessing());
			Util.saveConfig(config);
		}
		System.exit(0);
	}

}
