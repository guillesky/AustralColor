package vista_Progress;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

import core.Util;

public class MediaFileChooser extends JFileChooser
{

    public MediaFileChooser()
    {
	super();
	this.initFilters();
    }

    public MediaFileChooser(File currentDirectory, FileSystemView fsv)
    {
	super(currentDirectory, fsv);
	this.initFilters();
    }

    public MediaFileChooser(File currentDirectory)
    {
	super(currentDirectory);
	this.initFilters();
    }

    public MediaFileChooser(FileSystemView fsv)
    {
	super(fsv);
	this.initFilters();
    }

    public MediaFileChooser(String currentDirectoryPath, FileSystemView fsv)
    {
	super(currentDirectoryPath, fsv);
	this.initFilters();
    }

    public MediaFileChooser(String currentDirectoryPath)
    {
	super(currentDirectoryPath);
	this.initFilters();
    }

    private void initFilters()
    {
	FileFilter imageFilter = new FileFilter()
	{
	    @Override
	    public boolean accept(File f)
	    {
		return (f.isDirectory() || Util.getTypeExtension(f) == Util.TYPE_IMAGE);
	    }

	    @Override
	    public String getDescription()
	    {
		return "Image files";
	    }
	};
	FileFilter videoFilter = new FileFilter()
	{
	    @Override
	    public boolean accept(File f)
	    {
		return (f.isDirectory() || Util.getTypeExtension(f) == Util.TYPE_VIDEO);
	    }

	    @Override
	    public String getDescription()
	    {
		return "Video files";
	    }
	};

	FileFilter mediaFilter = new FileFilter()
	{
	    @Override
	    public boolean accept(File f)
	    {
		return (f.isDirectory() || Util.getTypeExtension(f) == Util.TYPE_IMAGE
			|| Util.getTypeExtension(f) == Util.TYPE_VIDEO);
	    }

	    @Override
	    public String getDescription()
	    {
		return "Image and Video files";
	    }
	};
	this.setMultiSelectionEnabled(true);

	this.addChoosableFileFilter(imageFilter);
	this.addChoosableFileFilter(videoFilter);
	this.addChoosableFileFilter(mediaFilter);
	this.setFileFilter(mediaFilter);
    }
}
