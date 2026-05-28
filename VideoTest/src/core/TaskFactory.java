package core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import i18n.Messages;

public class TaskFactory
{

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
		    AbstractMediaTask abstractMediaTask = this.getAbstractMediaTask(f, type, ignoredFiles,
			    renamedFiles);
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

    private AbstractMediaTask getAbstractMediaTask(File file, int type, ArrayList<String> existingFiles,
	    HashMap<String, String> renamedFiles)
    {
	AbstractMediaTask abstractMediaTask = null;
	boolean outputFileExist = false;
	String extension = null;
	switch (type)
	{
	case Util.TYPE_IMAGE:
	    extension = Util.outputImageExtension;
	    break;
	case Util.TYPE_VIDEO:
	    extension = Util.outputVideoExtension;
	    break;
	}
	String onlyName = Util.getNameAndExtension(file)[0];
	String outputFileName = Environment.getInstance().getOutputPath() + File.separator + onlyName + "_"
		+ Messages.CORRECTED.getValue() + extension;
	String canceledFile = Environment.getInstance().getOutputPath() + File.separator + onlyName + "_"
		+ Messages.CANCELED.getValue();

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
		existingFiles.add(outputFileName);
		break;

	    case Environment.RENAME_DUPLICATED_FILES:
		String renamedOutputFileName = this.getRenamedOutputFileName(file, type);
		abstractMediaTask = this.getAbstractMediaTaskByType(type, file, renamedOutputFileName, canceledFile);

		renamedFiles.put(file.getName(), renamedOutputFileName);
	    }
	}
	return abstractMediaTask;
    }

    private String getRenamedOutputFileName(File file, int type)
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
	int count = 1;
	String onlyName = Util.getNameAndExtension(file)[0];
	String outputFileName;
	File outputFile;
	do
	{
	    outputFileName = Environment.getInstance().getOutputPath() + File.separator + onlyName + "_"
		    + Messages.CORRECTED.getValue() + "_(" + count+")"  + extension;
	    outputFile = new File(outputFileName);
	    count++;

	} while (outputFile.exists());

	return outputFileName;
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
	    abstractMediaTask = new VideoTask(file, outputFileName, canceledFileName);
	    break;
	}
	return abstractMediaTask;
    }
}
