package controlador;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import core.AbstractMediaTask;

public interface IVista
{
	public static final String ADD_FILES = "ADD_FILES";
	public static final String DELETE_TASK = "DELETE_TASK";
	public static final String START_TASK = "START_TASK";
	public static final String STOP_ALL = "STOP_ALL";
	public static final String CANCEL_TASK = "CANCEL_TASK";
	public static final String CHANGE_OUTPUT = "CHANGE_OUTPUT";

	void setControlador(Controlador controlador);

	void updateTaskStatus(AbstractMediaTask abstractMediaTask);

	void addTasks(ArrayList<AbstractMediaTask> abstractMediaTasks);

	void removeTasks(AbstractMediaTask abstractMediaTask);

	ArrayList<AbstractMediaTask> getSelectedMediaTask();

	void updatePercentageCompleted(AbstractMediaTask abstractMediaTask);

	void allTaskFinished(double elapsedMs);
	public void updateLogText(String text);

}
