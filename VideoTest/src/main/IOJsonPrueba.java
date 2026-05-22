package main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core.Config;
import i18n.Language;
import i18n.Messages;

public class IOJsonPrueba
{

	public static void main(String[] args)
	{
		try
		{
			escribo();
			// leo("en.json");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void leo(String fileCode) throws IOException
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		FileReader reader;
		Language l;

		reader = new FileReader(fileCode);
		l = gson.fromJson(reader, Language.class);
		reader.close();
		Language.i18n(l);

		for (Messages key : Messages.values())
		{
			System.out.println("Key: " + key.name() + " Value: " + key.getValue());

		}

	}

	private static void escribo() throws IOException
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		{

			FileWriter writer;
			Language l = Language.getLanguage();

			writer = new FileWriter("es.json");
			gson.toJson(l, writer);
			writer.close();
			System.out.println(l);
			
			/*
			Config c=new Config();
				
			writer = new FileWriter("config.json");
			gson.toJson(c, writer);
			writer.close();
*/
		}
	}
}
