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

	MediaTaskManager mediaTaskManager = new MediaTaskManager();
	ConsoleMediaTaskListener cpl = new ConsoleMediaTaskListener();
	mediaTaskManager.addMediaTaskListener(cpl);
	
	
	
	VideoTask vp1 = new VideoTask("pezLeon.mp4","pezLeon_corrected.mp4", mediaTaskManager);
	

	
	VideoTask vp2 = new VideoTask("input.mp4","input_corrected.mp4", mediaTaskManager);
	ImageTask it1=new ImageTask("input.jpg","input_corrected.jpg", mediaTaskManager);
	mediaTaskManager.addTask(vp1);
	mediaTaskManager.addTask(vp2);
	mediaTaskManager.addTask(it1);
	
	mediaTaskManager.startTask();
	
    }
}