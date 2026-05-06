package main;

import org.opencv.core.Core;

import core.ConsoleVideoProcessorListener;
import core.VideoProcessorTask;
import core.MediaProcessorManager;

public class VideoProcessToFFmpegCorrect
{

    static
    {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception
    {

	MediaProcessorManager videoProcessorManager = new MediaProcessorManager();
	ConsoleVideoProcessorListener cpl = new ConsoleVideoProcessorListener();
	videoProcessorManager.addVideoProcessorListener(cpl);
	
	
	
	VideoProcessorTask vp1 = new VideoProcessorTask("pezLeon.mp4","pezLeon_corrected.mp4", videoProcessorManager);
	

	
	VideoProcessorTask vp2 = new VideoProcessorTask("input.mp4","input_corrected.mp4", videoProcessorManager);
	Thread h1=new Thread(vp1);
	Thread h2=new Thread(vp2);
	h1.start();
	h2.start();
	
    }
}