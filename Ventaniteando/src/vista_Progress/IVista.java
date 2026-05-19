package vista_Progress;

import java.util.ArrayList;

public interface IVista
{

    public static final String ADD_FILES = null;
    public static final String START_TASK = null;
    public static final String STOP_ALL = null;
    public static final String CANCEL_TASK = null;
    public static final String DELETE_TASK = null;
	public static final String CHANGE_OUTPUT = null;
    
    
    
    
   
    void setControlador(Controlador controlador);
    void updateTaskVisualization();
    void addTasks(ArrayList<AbstractMediaTask> abstractMediaTasks);
    void removeTasks(AbstractMediaTask abstractMediaTask);
}
