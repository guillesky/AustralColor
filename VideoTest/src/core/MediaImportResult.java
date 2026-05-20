package core;

import java.util.ArrayList;
import java.util.HashMap;

public class MediaImportResult
{
	private ArrayList<AbstractMediaTask> mediaTasks;
	private ArrayList<String> unknowFiles;
	private ArrayList<String> alreadyQueuedFiles;
	private ArrayList<String> ignoredFiles;
	private HashMap<String, String> renamedFiles;
	
	

	
	
	
	public MediaImportResult(ArrayList<AbstractMediaTask> mediaTasks, ArrayList<String> unknowFiles,
		ArrayList<String> alreadyQueuedFiles, ArrayList<String> ignoredFiles,
		HashMap<String, String> renamedFiles)
	{
	    super();
	    this.mediaTasks = mediaTasks;
	    this.unknowFiles = unknowFiles;
	    this.alreadyQueuedFiles = alreadyQueuedFiles;
	    this.ignoredFiles = ignoredFiles;
	    this.renamedFiles = renamedFiles;
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
	public ArrayList<String> getAlreadyQueuedFiles()
	{
	    return alreadyQueuedFiles;
	}
	
	
}
