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
		ConsoleMediaTaskListener cpl = new ConsoleMediaTaskListener();
		MediaTaskManager.getInstance().addMediaTaskListener(cpl);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
		case IVista.START_TASK:
			MediaTaskManager.getInstance().startTask();
			break;

		}

	}

	@Override
	public void videoAnalized(VideoTask videoTask, VideoAnalysisResult videoAnalysisResult)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void frameProcessed(VideoTask videoTask, Mat frame, int frameIndex)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePercentageCompleted(AbstractMediaTask abstractMediaTask)
	{
		this.vista.updateTaskVisualization();

	}

	@Override
	public void mediaCorrectCompleted(AbstractMediaTask abstractMediaTask, double elapsedMs)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaCorrectInitiated(AbstractMediaTask abstractMediaTask)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void exceptionThrowed(Exception e)
	{
		// TODO Auto-generated method stub

	}

	public void addFiles(File[] selectedFiles)
	{
		ArrayList<AbstractMediaTask> mediaTasks = this.factory.getTasks(selectedFiles);
		for (AbstractMediaTask task : mediaTasks)
			MediaTaskManager.getInstance().addTask(task);
		this.vista.addTasks(mediaTasks);
	}

}
