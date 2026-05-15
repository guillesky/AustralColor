package core;

import java.io.File;
import java.util.ArrayList;

public class TaskFactory
{
	private String outputDirectory;
	private String sufix;

	public TaskFactory()
	{
		File currentDir = new File(System.getProperty("user.dir"));
		String sufix = "_corrected";
		this(currentDir.getAbsolutePath() + File.separator + "output", sufix);
	}

	public TaskFactory(String outputDirectory, String sufix)
	{
		this.outputDirectory = outputDirectory;
		this.sufix = sufix;
	}

	public AbstractMediaTask getAbstractMediaTask(File f)
	{
		AbstractMediaTask result = null;
		int type = Util.getTypeExtension(f);
		String outputFileName = outputDirectory + File.separator + Util.getNameAndExtension(f)[0] + this.sufix;
		switch (type)
		{
		case Util.TYPE_IMAGE:
			result = new ImageTask(f.getAbsolutePath(), outputFileName + ".jpg");

		case Util.TYPE_VIDEO:
			result = new VideoTask(f.getAbsolutePath(), outputFileName + ".mp4");

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

			int type = Util.getTypeExtension(f);
			String outputFileName = outputDirectory + File.separator + Util.getNameAndExtension(f)[0] + this.sufix;
			switch (type)
			{
			case Util.TYPE_IMAGE:
				imageTasks.add(new ImageTask(f.getAbsolutePath(), outputFileName + ".jpg"));
				break;
			case Util.TYPE_VIDEO:
				videoTasks.add(new VideoTask(f.getAbsolutePath(), outputFileName + ".mp4"));
				break;
			}
		}
		result.addAll(imageTasks);
		result.addAll(videoTasks);
		return result;
	}

}
