package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import i18n.Language;
import i18n.Messages;

public class Environment
{
	public static final int IGNORE_DUPLICATED_FILES = 0;
	public static final int RENAME_DUPLICATED_FILES = 1;
	public static final int OVERWRITE_DUPLICATED_FILES = 2;
	public static final String APP_NAME = "AustralColor";
	public static final String VERSION = "v 1.0.0 R 20260522";
	

	private static Environment instance = null;
	private String outputPath;
	private int duplicateFilePolicy = 0;

	public static Environment getInstance()
	{
		if (instance == null)
			instance = new Environment();
		return instance;
	}

	private Environment()
	{
		this.readConfig();
		File currentDir = new File(System.getProperty("user.dir"));
		this.outputPath = currentDir.getAbsolutePath();

	}

	private void readLanguage(String fileCode)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Language l;

		try
		{
			FileReader reader;
			reader = new FileReader(fileCode);
			l = gson.fromJson(reader, Language.class);
			reader.close();
			Language.i18n(l);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getOutputPath()
	{
		return outputPath;
	}

	public void setOutputPath(String outputPath)
	{
		this.outputPath = outputPath;
	}

	public int getDuplicateFilePolicy()
	{
		return duplicateFilePolicy;
	}

	public void setDuplicateFilePolicy(int duplicateFilePolicy)
	{
		this.duplicateFilePolicy = duplicateFilePolicy;
	}

	private void readConfig()
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		FileReader reader;
		Config config;

		try
		{
			reader = new FileReader("config.json");
			config = gson.fromJson(reader, Config.class);
			reader.close();
			MediaTaskManager.getInstance().setMaxSimultaneousProcessing(config.getMaxSimultaneousProcessing());
			this.readLanguage(config.getLanguageFile());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
