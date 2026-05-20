package core;

import java.util.ArrayList;
import java.util.HashMap;

public class MediaImportResult
{
	private ArrayList<AbstractMediaTask> mediaTasks;
	private ArrayList<String> ignoredFiles;
	private HashMap<String, String> renamedFiles;
	private ArrayList<String> unknowFiles;
	
	
	public MediaImportResult(ArrayList<AbstractMediaTask> mediaTasks, ArrayList<String> ignoredFiles,
			HashMap<String, String> renamedFiles,ArrayList<String> unknowFiles)
	{
		super();
		this.mediaTasks = mediaTasks;
		this.ignoredFiles = ignoredFiles;
		this.renamedFiles = renamedFiles;
		this.unknowFiles=unknowFiles;
	}
	public ArrayList<AbstractMediaTask> getMediaTasks()
	{
		return mediaTasks;
	}
	public ArrayList<String> getIgnoredFiles()
	{
		return ignoredFiles;
	}
	public HashMap<String, String> getRenamedFiles()
	{
		return renamedFiles;
	}
	public ArrayList<String> getUnknowFiles()
	{
		return unknowFiles;
	}
	
	
}
