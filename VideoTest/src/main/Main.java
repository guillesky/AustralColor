package main;

import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import controlador.Controlador;

public class Main
{
	public static void main(String[] args)
	{
		   System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String input = "input.jpg";
		String outputDir = "dale.jpg";
		Mat mat = Imgcodecs.imread(input);
		Mat rgb = new Mat();
		Imgproc.cvtColor(mat, rgb, Imgproc.COLOR_BGR2RGB);
		Controlador controlador = new Controlador(rgb);
		byte[] datos=new byte[3];
		
		mat.get(0, 0, datos);
		System.out.println(Arrays.toString(datos));
		/*
		 * try { BufferedImage image = ImageIO.read(new File(input)); Controlador
		 * controlador=new Controlador(image); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		
		/*
		Mat test = new Mat(200, 200, CvType.CV_8UC3);
		test.setTo(new Scalar(0, 0, 255)); // rojo en BGR
		Controlador controlador = new Controlador(test);
		*/
		
	}
}
