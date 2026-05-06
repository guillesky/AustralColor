package main;

import org.opencv.core.Core;

import core.ConsoleMediaTaskListener;
import core.VideoTask;
import core.MediaTaskManager;

public class VideoProcessToFFmpegCorrect
{

    static
    {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception
    {

	MediaTaskManager videoProcessorManager = new MediaTaskManager();
	ConsoleMediaTaskListener cpl = new ConsoleMediaTaskListener();
	videoProcessorManager.addMediaTaskListener(cpl);
	
	
	
	VideoTask vp1 = new VideoTask("pezLeon.mp4","pezLeon_corrected.mp4", videoProcessorManager);
	

	
	VideoTask vp2 = new VideoTask("input.mp4","input_corrected.mp4", videoProcessorManager);
	Thread h1=new Thread(vp1);
	Thread h2=new Thread(vp2);
	h1.start();
	h2.start();
	
    }
}