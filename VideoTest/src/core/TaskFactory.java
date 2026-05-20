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

	public AbstractMediaTask getAbstractMediaTask(File f)
	{
		return this.getMediaFileResult(f).abstractMediaTask;
	}

	public MediaImportResult getMediaImportResult(File[] files)
	{
		ArrayList<AbstractMediaTask> allMediaTasks = new ArrayList<AbstractMediaTask>();
		ArrayList<AbstractMediaTask> imageTasks = new ArrayList<AbstractMediaTask>();
		ArrayList<AbstractMediaTask> videoTasks = new ArrayList<AbstractMediaTask>();
		ArrayList<String> IgnoredFiles = new ArrayList<String>();
		ArrayList<String> unknowFiles = new ArrayList<String>();
		HashMap<String, String> renamedFiles = new HashMap<String, String>();
		for (File f : files)
		{
			if (MediaTaskManager.getInstance().isInQueded(f.getAbsolutePath()))
			{
				IgnoredFiles.add(f.getAbsolutePath());
			} else
			{
				MediaFileResult mediaFileResult = this.getMediaFileResult(f);
				if (mediaFileResult.abstractMediaTask != null)
					switch (mediaFileResult.fileType)
					{
					case Util.TYPE_IMAGE:
						imageTasks.add(mediaFileResult.abstractMediaTask);
						break;
					case Util.TYPE_VIDEO:
						videoTasks.add(mediaFileResult.abstractMediaTask);
						break;
					case Util.TYPE_UNKNOW:
						unknowFiles.add(f.getAbsolutePath());
						break;

					}
			}
		}
		allMediaTasks.addAll(imageTasks);
		allMediaTasks.addAll(videoTasks);
		MediaImportResult result = new MediaImportResult(allMediaTasks, IgnoredFiles, renamedFiles, unknowFiles);
		return result;
	}

	private MediaFileResult getMediaFileResult(File file)
	{
		AbstractMediaTask abstractMediaTask = null;
		boolean outputFileExist = false;
		int type = Util.getTypeExtension(file);
		if (type != Util.TYPE_UNKNOW)
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
					+ Messages.CORRECTED.getValue() + extension;
			String canceledFile = Environment.getInstance().getOutputPath() + File.separator + onlyName + "_"
					+ Messages.CANCELED.getValue() + extension;

			File outputFile = new File(outputFileName);
			outputFileExist = (outputFile.exists() && outputFile.isFile());
			if (!outputFileExist
					|| Environment.getInstance().getDuplicateFilePolicy() == Environment.OVERWRITE_DUPLICATED_FILES)
			{
				abstractMediaTask = this.getAbstractMediaTaskByType(type, file, outputFileName, canceledFile);
			} else
			{
				if (Environment.getInstance().getDuplicateFilePolicy() == Environment.RENAME_DUPLICATED_FILES)
				{
					outputFileName += "LALALALALALA";
					abstractMediaTask = this.getAbstractMediaTaskByType(type, file, outputFileName, canceledFile);
				}

			}

		}
		return new MediaFileResult(type, abstractMediaTask, outputFileExist);
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
