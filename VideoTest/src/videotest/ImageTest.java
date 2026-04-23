package videotest;

import java.io.File;
import java.io.IOException;

import org.opencv.core.Core;

public class ImageTest
{

	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args)
	{

		String input = "input.jpg";
		String outputDir = "dale.jpg";

		// Crear carpeta si no existe
		File dir = new File(outputDir);
		if (!dir.exists())
		{
			boolean created = dir.mkdirs();
			if (!created)
			{
				System.out.println("No se pudo crear la carpeta de salida");
				return;
			}
		}
		try
		{
			ClaseImagen.correctImage(input, outputDir);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Proceso terminado");

	}

}