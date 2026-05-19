package core;

import java.io.File;
import java.util.ArrayList;

import i18n.Messages;

public class TaskFactory
{

    public TaskFactory()
    {

    }

    public AbstractMediaTask getAbstractMediaTask(File f)
    {
	AbstractMediaTask result = null;
	String onlyName = Util.getNameAndExtension(f)[0];
	String outputFile = Environment.getInstance().getOutputPath() + File.separator + onlyName + "_"
		+ Messages.CORRECTED.getValue();
	int type = Util.getTypeExtension(f);
	switch (type)
	{
	case Util.TYPE_IMAGE:
	{

	    result = new ImageTask(f, outputFile + ".jpg");
	    break;
	}
	case Util.TYPE_VIDEO:

	    result = new VideoTask(f, outputFile + ".mp4");
	    break;

	}

	return result;
    }

    public ArrayList<AbstractMediaTask> getTasks(File[] files)
    {
	ArrayList<AbstractMediaTask> result = new ArrayList<AbstractMediaTask>();
	ArrayList<AbstractMediaTask> imageTasks = new ArrayList<AbstractMediaTask>();
	ArrayList<AbstractMediaTask> videoTasks = new ArrayList<AbstractMediaTask>();

	for (File f : files)
	{
	    String onlyName = Util.getNameAndExtension(f)[0];
	    String outputFile = Environment.getInstance().getOutputPath() + File.separator + onlyName + "_"
		    + Messages.CORRECTED.getValue();

	    int type = Util.getTypeExtension(f);
	    switch (type)
	    {
	    case Util.TYPE_IMAGE:
		imageTasks.add(new ImageTask(f, outputFile + ".jpg"));
		break;
	    case Util.TYPE_VIDEO:
		videoTasks.add(new VideoTask(f, outputFile + ".mp4"));
		break;
	    }
	}
	result.addAll(imageTasks);
	result.addAll(videoTasks);
	return result;
    }

}
