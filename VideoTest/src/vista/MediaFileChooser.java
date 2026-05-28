package vista;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

import i18n.Messages;
import util.Util;

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
				return Messages.IMAGE_FILES.getValue();
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
				return Messages.VIDEO_FILES.getValue();
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
				return Messages.MEDIA_FILES.getValue();
			}
		};
		this.setMultiSelectionEnabled(true);

		this.addChoosableFileFilter(imageFilter);
		this.addChoosableFileFilter(videoFilter);
		this.addChoosableFileFilter(mediaFilter);
		this.setFileFilter(mediaFilter);
	}
}
