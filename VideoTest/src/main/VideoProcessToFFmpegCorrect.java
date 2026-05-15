package main;

import org.opencv.core.Core;

import core.ConsoleMediaTaskListener;
import core.ImageTask;
import core.MediaTaskManager;
import core.VideoTask;

public class VideoProcessToFFmpegCorrect
{

    static
    {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception
    {

	MediaTaskManager mediaTaskManager = MediaTaskManager.getInstance();
	ConsoleMediaTaskListener cpl = new ConsoleMediaTaskListener();
	mediaTaskManager.addMediaTaskListener(cpl);
	
	
	
	VideoTask vp1 = new VideoTask("C:\\Users\\Guille\\git\\ColorDiveCorrector\\VideoTest\\pezLeon.mp4","C:\\Users\\Guille\\git\\ColorDiveCorrector\\VideoTest\\pezLeon.mp4.corrected.mp4");
	

	
	VideoTask vp2 = new VideoTask("input.mp4","input_corrected.mp4");
	ImageTask it1=new ImageTask("input.jpg","input_corrected.jpg");
	mediaTaskManager.addTask(vp1);
	mediaTaskManager.addTask(vp2);
	mediaTaskManager.addTask(it1);
	
	mediaTaskManager.startTask();
	
    }
}