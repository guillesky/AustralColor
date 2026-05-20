package core;

import java.util.ArrayList;
import java.util.HashMap;

public class MediaImportResult
{
	private ArrayList<AbstractMediaTask> mediaTasks;
	private ArrayList<String> unknowFiles;
	private ArrayList<String> alreadyQueuedFiles;
	private ArrayList<String> existingFiles;
	private HashMap<String, String> renamedFiles;
	
	

	
	
	
	public MediaImportResult(ArrayList<AbstractMediaTask> mediaTasks, ArrayList<String> unknowFiles,
		ArrayList<String> alreadyQueuedFiles, ArrayList<String> existingFiles,
		HashMap<String, String> renamedFiles)
	{
	    super();
	    this.mediaTasks = mediaTasks;
	    this.unknowFiles = unknowFiles;
	    this.alreadyQueuedFiles = alreadyQueuedFiles;
	    this.existingFiles = existingFiles;
	    this.renamedFiles = renamedFiles;
	}
	public ArrayList<AbstractMediaTask> getMediaTasks()
	{
		return mediaTasks;
	}
	public ArrayList<String> getExistingFiles()
	{
		return existingFiles;
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
