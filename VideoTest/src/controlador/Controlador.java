package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import videotest.ClaseImagen;
import videotest.ImageAnalizer;
import vista.IVista;
import vista.Ventana;

public class Controlador implements ActionListener
{
	private IVista vista;
	private BufferedImage originalImage;
	private Mat mat;

	public Controlador(BufferedImage image)
	{
		this.setVista(new Ventana());
		this.originalImage = image;
		this.vista.setImage(originalImage);
		this.mat = ClaseImagen.bufferedImageToMat(image);

	}

	public Controlador(Mat mat)
	{
		this.mat = mat;
		this.setVista(new Ventana());
		this.originalImage = ClaseImagen.matToBufferedImage(mat);
		this.vista.setImage(originalImage);

	}

	public IVista getVista()
	{
		return vista;
	}

	public void setVista(IVista vista)
	{
		this.vista = vista;
		this.vista.setControlador(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
		case IVista.ANALIZAR:
		{
			this.analizar();
			break;
		}
		case IVista.GUARDAR:
		{
			this.guardar();
			break;
		}
		}

	}

	private void guardar()
	{
		Mat bgr = new Mat();
		Imgproc.cvtColor(mat, bgr, Imgproc.COLOR_RGB2BGR);
		Imgcodecs.imwrite("salida.jpg", bgr);

	}

	private void analizar()
	{
		double[] filtros = ImageAnalizer.getFilterMatrix(mat);
		this.vista.updateLog(Arrays.toString(filtros));

	}

}
