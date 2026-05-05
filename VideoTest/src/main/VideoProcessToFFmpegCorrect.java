package main;

import org.opencv.core.Core;

import core.ConsoleVideoProcessorListener;
import core.VideoProcessor;
import core.VideoProcessorManager;

public class VideoProcessToFFmpegCorrect
{

    static
    {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception
    {

	VideoProcessorManager videoProcessorManager = new VideoProcessorManager();
	ConsoleVideoProcessorListener cpl = new ConsoleVideoProcessorListener();
	videoProcessorManager.addVideoProcessorListener(cpl);
	
	
	
	VideoProcessor vp1 = new VideoProcessor("pezLeon.mp4", videoProcessorManager);
	vp1.start();

	
	VideoProcessor vp2 = new VideoProcessor("input.mp4", videoProcessorManager);
	vp2.start();
    }
}