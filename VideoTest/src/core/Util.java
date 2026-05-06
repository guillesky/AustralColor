package core;

import java.io.File;

public class Util
{

	private static String sufix = "_corrected.mp4";

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

	public static AbstractMediaProcessor getAbstractMediaProcessorByType(String inputPath, String outputPath,
			MediaProcessorManager mediaProcessorManager)
	{
		AbstractMediaProcessor result = new VideoProcessorTask(inputPath, outputPath, mediaProcessorManager);
		return result;
	}
}
