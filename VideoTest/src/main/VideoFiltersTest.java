package main;

import java.util.Arrays;

import org.opencv.core.Core;

import videotest.VideoAnalysisResult;
import videotest.VideoProcessor;

public class VideoFiltersTest
{
	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args)
	{
		String input = "input.mp4";
		String output = "output_interpolado.mp4";
		VideoAnalysisResult result = VideoProcessor.analyzeVideo(input, output);

		System.out.println(result.detalle());
		System.out.println(Arrays.toString(result.getFilter(0)));
		System.out.println(Arrays.toString(result.getFilter(120)));
		System.out.println(Arrays.toString(result.getFilter(253)));
		System.out.println(Arrays.toString(result.getFilter(20)));
		
		System.out.println(Arrays.toString(result.getFilter(200)));
		System.out.println(Arrays.toString(result.getFilter(250)));
		
		
		
		
	}

}
