package main;

import org.opencv.core.Core;

import core.ConsoleVideoProcessorListener;
import core.VideoProcessor;

public class VideoProcessToFFmpegCorrect
{

	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) throws Exception
	{

		

		
		
		
		String input = "input.mp4";
		String output = "output_interpolated.mp4";
		VideoProcessor vp = new VideoProcessor(input, output);
		ConsoleVideoProcessorListener cpl=new ConsoleVideoProcessorListener();
		vp.addVideoProcessorListener(cpl);
		vp.processVideo();
	
	}
}