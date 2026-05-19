package vista_Progress;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class FolderFileChooser extends JFileChooser
{

    public FolderFileChooser()
    {
	super();
	this.initFilters();
    }

    public FolderFileChooser(File currentDirectory, FileSystemView fsv)
    {
	super(currentDirectory, fsv);
	this.initFilters();
    }

    public FolderFileChooser(File currentDirectory)
    {
	super(currentDirectory);
	this.initFilters();
    }

    public FolderFileChooser(FileSystemView fsv)
    {
	super(fsv);
	this.initFilters();
    }

    public FolderFileChooser(String currentDirectoryPath, FileSystemView fsv)
    {
	super(currentDirectoryPath, fsv);
	this.initFilters();
    }

    public FolderFileChooser(String currentDirectoryPath)
    {
	super(currentDirectoryPath);
	this.initFilters();
    }

    private void initFilters()
    {
    	this.setFileSelectionMode(
		        JFileChooser.DIRECTORIES_ONLY);

    	this.setAcceptAllFileFilterUsed(false);
    }
}
