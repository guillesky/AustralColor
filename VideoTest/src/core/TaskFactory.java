package core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import i18n.Messages;

public class TaskFactory
{
    private class MediaFileResult
    {
	private int fileType;
	private AbstractMediaTask abstractMediaTask;
	private boolean outputFileExist;

	public MediaFileResult(int fileType, AbstractMediaTask abstractMediaTask, boolean outputFileExist)
	{
	    super();
	    this.fileType = fileType;
	    this.abstractMediaTask = abstractMediaTask;
	    this.outputFileExist = outputFileExist;
	}

    }

    public TaskFactory()
    {

    }

    public MediaImportResult getMediaImportResult(File[] files)
    {
	ArrayList<AbstractMediaTask> allMediaTasks = new ArrayList<AbstractMediaTask>();
	ArrayList<AbstractMediaTask> imageTasks = new ArrayList<AbstractMediaTask>();
	ArrayList<AbstractMediaTask> videoTasks = new ArrayList<AbstractMediaTask>();
	ArrayList<String> ignoredFiles = new ArrayList<String>();
	ArrayList<String> unknowFiles = new ArrayList<String>();
	ArrayList<String> alreadyQueuedFiles = new ArrayList<String>();
	HashMap<String, String> renamedFiles = new HashMap<String, String>();
	for (File f : files)
	{
	    if (MediaTaskManager.getInstance().isInQueded(f.getAbsolutePath()))
	    {
		alreadyQueuedFiles.add(f.getAbsolutePath());
	    } else
	    {

		int type = Util.getTypeExtension(f);

		if (type == Util.TYPE_UNKNOW)
		    unknowFiles.add(f.getAbsolutePath());
		else
		{
		    AbstractMediaTask abstractMediaTask = this.getMediaFileResult(f, type, ignoredFiles, renamedFiles);
		    if (abstractMediaTask != null)
			switch (type)
			{
			case Util.TYPE_IMAGE:
			    imageTasks.add(abstractMediaTask);
			    break;
			case Util.TYPE_VIDEO:
			    videoTasks.add(abstractMediaTask);
			    break;
			}
		}
	    }
	}
	allMediaTasks.addAll(imageTasks);
	allMediaTasks.addAll(videoTasks);
	MediaImportResult result = new MediaImportResult(allMediaTasks, unknowFiles, alreadyQueuedFiles, ignoredFiles,
		renamedFiles);
	return result;
    }

    private AbstractMediaTask getMediaFileResult(File file, int type, ArrayList<String> existingFiles,
	    HashMap<String, String> renamedFiles)
    {
	AbstractMediaTask abstractMediaTask = null;
	boolean outputFileExist = false;
	String extension = null;
	switch (type)
	{
	case Util.TYPE_IMAGE:
	    extension = ".jpg";
	    break;
	case Util.TYPE_VIDEO:
	    extension = ".mp4";
	    break;
	}
	String onlyName = Util.getNameAndExtension(file)[0];
	String outputFileName = Environment.getInstance().getOutputPath() + File.separator + onlyName + "_"
		+ Messages.CORRECTED.getValue() + extension;
	String canceledFile = Environment.getInstance().getOutputPath() + File.separator + onlyName + "_"
		+ Messages.CANCELED.getValue() + extension;

	File outputFile = new File(outputFileName);
	outputFileExist = (outputFile.exists() && outputFile.isFile());

	if (!outputFileExist)
	    abstractMediaTask = this.getAbstractMediaTaskByType(type, file, outputFileName, canceledFile);
	else
	{
	    switch (Environment.getInstance().getDuplicateFilePolicy())
	    {
	    case Environment.OVERWRITE_DUPLICATED_FILES:
		abstractMediaTask = this.getAbstractMediaTaskByType(type, file, outputFileName, canceledFile);
	    case Environment.IGNORE_DUPLICATED_FILES:
		existingFiles.add(file.getName());
		break;

	    case Environment.RENAME_DUPLICATED_FILES:
		String[] renamedOutputFileName = this.getRenamedOutputFileName(file, type);
		abstractMediaTask = this.getAbstractMediaTaskByType(type, file, renamedOutputFileName[0],
			renamedOutputFileName[1]);

		renamedFiles.put(file.getName(), renamedOutputFileName[0]);
	    }
	}
	return abstractMediaTask;
    }

    private String[] getRenamedOutputFileName(File file, int type)
    {
	String extension = null;
	switch (type)
	{
	case Util.TYPE_IMAGE:
	    extension = ".jpg";
	    break;
	case Util.TYPE_VIDEO:
	    extension = ".mp4";
	    break;
	}
	String onlyName = Util.getNameAndExtension(file)[0];
	String outputFileName = Environment.getInstance().getOutputPath() + File.separator + onlyName + "_"
		+ Messages.CORRECTED.getValue() + "LALALALALA" + extension;
	String canceledFile = Environment.getInstance().getOutputPath() + File.separator + onlyName + "_"
		+ Messages.CANCELED.getValue() + "LALALALALA" + extension;

	String[] result =
	{ outputFileName, canceledFile };

	return result;
    }

    private AbstractMediaTask getAbstractMediaTaskByType(int type, File file, String outputFileName,
	    String canceledFileName)
    {
	AbstractMediaTask abstractMediaTask = null;
	switch (type)
	{
	case Util.TYPE_IMAGE:

	    abstractMediaTask = new ImageTask(file, outputFileName);
	    break;
	case Util.TYPE_VIDEO:
	    outputFileName += ".mp4";
	    abstractMediaTask = new VideoTask(file, outputFileName, canceledFileName);
	    break;
	}
	return abstractMediaTask;
    }
}
