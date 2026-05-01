package core;

import java.io.File;

public class Util
{
    public static String getFFmpegPath() {
	    String os = System.getProperty("os.name").toLowerCase();

	    String exeName = os.contains("win") ? "ffmpeg.exe" : "ffmpeg";

	    File ffmpegFile = new File("ffmpeg", exeName);

	    if (!ffmpegFile.exists()) {
	        throw new RuntimeException("No se encontró ffmpeg en: " + ffmpegFile.getAbsolutePath());
	    }

	    return ffmpegFile.getAbsolutePath();
	}
    
    
}
