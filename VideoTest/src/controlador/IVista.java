package controlador;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import core.AbstractMediaTask;

public interface IVista
{
	public static final String  ADD_FILES = "ADD_FILES";
	public static final int DELETE_TASK = 1;
	public static final String START_TASK = "START_TASK";
	public static final int STOP_ALL = 3;
	public static final int CANCEL_TASK = 4;
	
	
	
	

	void setControlador(Controlador controlador);
	void updateTaskVisualization();
	void addTasks(ArrayList<AbstractMediaTask> abstractMediaTasks);
	void removeTasks(AbstractMediaTask abstractMediaTask);
	

}
