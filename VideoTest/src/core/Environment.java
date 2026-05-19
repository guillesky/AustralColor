package core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import i18n.Language;

public class Environment
{
	private static Environment instance = null;
	private String outputPath;

	public static Environment getInstance()
	{
		if (instance == null)
			instance = new Environment();
		return instance;
	}

	private Environment()
	{
		this.readLanguage("es.json");
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


	
	
}
