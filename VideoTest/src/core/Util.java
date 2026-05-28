package core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import i18n.AllLanguages;
import i18n.Language;

public class Util
{

	public static final int TYPE_UNKNOW = 0;
	public static final int TYPE_IMAGE = 1;

	public static final int TYPE_VIDEO = 2;

	private static final Set<String> VIDEO_EXTENSIONS = Set.of("mp4", "mov", "avi", "mkv", "3gp", "webm");

	private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "bmp", "gif", "webp", "dng", "gpr",
			"arw");

	public static final String outputVideoExtension = ".mp4";
	public static final String outputImageExtension = ".jpg";

	private static String sufix = "corrected";

	public static String getFFmpegPath()
	{
		String os = System.getProperty("os.name").toLowerCase();

		String exeName = os.contains("win") ? "ffmpeg.exe" : "ffmpeg";

		File ffmpegFile = new File("ffmpeg", exeName);

		if (!ffmpegFile.exists())
		{
			throw new RuntimeException("No se encontró ffmpeg en: " + ffmpegFile.getAbsolutePath());
		}

		return ffmpegFile.getAbsolutePath();
	}

	public static String getSufix()
	{

		return Util.sufix;
	}

	public static void setSufix(String sufix)
	{
		Util.sufix = sufix;
	}

	public static String[] getNameAndExtension(File file)
	{

		String fileName = file.getName();

		int dotIndex = fileName.lastIndexOf('.');

		String nombreSinExtension;
		String extension;

		if (dotIndex > 0)
		{
			nombreSinExtension = fileName.substring(0, dotIndex);
			extension = fileName.substring(dotIndex + 1).toLowerCase();
		} else
		{
			nombreSinExtension = fileName;
			extension = "";
		}
		String result[] =
		{ nombreSinExtension, extension };
		return result;
	}

	public static String getExtension(File file)
	{
		return Util.getNameAndExtension(file)[1];
	}

	public static int getTypeExtension(File file)
	{
		int result = Util.TYPE_UNKNOW;
		String extension = Util.getExtension(file);
		if (file.exists() && file.isFile())
		{
			if (Util.IMAGE_EXTENSIONS.contains(extension))
				result = Util.TYPE_IMAGE;
			else if (Util.VIDEO_EXTENSIONS.contains(extension))
				result = Util.TYPE_VIDEO;
		}

		return result;
	}

	public static Language readLanguage(String fileCode)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Language l = null;

		try
		{
			FileReader reader;
			reader = new FileReader(fileCode);
			l = gson.fromJson(reader, Language.class);
			reader.close();
			
		} catch (IOException e)
		{
			
			e.printStackTrace();
		}
		return l;
	}

	public static AllLanguages readAllLanguage()
	{
		AllLanguages result = null;
		File carpeta = new File("i18n");

		File[] archivosJson = carpeta.listFiles(new FilenameFilter()
		{

			@Override
			public boolean accept(File dir, String nombre)
			{
				return nombre.toLowerCase().endsWith(".json");
			}
		});

		if (archivosJson != null)
		{
			result = new AllLanguages();
			for (File archivo : archivosJson)
			{
				Language language = Util.readLanguage(archivo.getAbsolutePath());
				result.addLanguaje(language);
			}
		}
		return result;
	}

	public static void saveConfig(Config config)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		FileWriter writer;
		try
		{
			writer = new FileWriter("config.json");
			gson.toJson(config, writer);
			writer.close();

		} catch (IOException e)
		{
			
			e.printStackTrace();
		}

	}
}
