package main;

import javax.swing.ImageIcon;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import controlador.Controlador;

public class Main
{
	public static void main(String[] args)
	{
		   System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	/*	String input = "input.jpg";
		String outputDir = "dale.jpg";
		Mat mat = Imgcodecs.imread(input);
		Controlador controlador = new Controlador(mat);
		/*
		 * try { BufferedImage image = ImageIO.read(new File(input)); Controlador
		 * controlador=new Controlador(image); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		
		
		Mat test = new Mat(100, 100, CvType.CV_8UC3);
		test.setTo(new Scalar(0, 0, 255)); // rojo en BGR
		Controlador controlador = new Controlador(test);
		
		
	}
}
