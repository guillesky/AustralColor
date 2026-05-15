package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.swing.JFileChooser;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import core.ImageProcessor;
import vista.IVistaOLD;
import vista.VentanaOLD;

public class ControladorOLD implements ActionListener
{
	private IVistaOLD vista;
	private BufferedImage originalImage;
	private Mat originalMat;
	private Mat correctedMat;

	public ControladorOLD(Mat mat)
	{
		this.originalMat = mat;
		this.setVista(new VentanaOLD());
		this.originalImage = ImageProcessor.matToBufferedImage(mat);
		this.vista.setImage(originalImage);

	}

	public IVistaOLD getVista()
	{
		return vista;
	}

	public void setVista(IVistaOLD vista)
	{
		this.vista = vista;
		this.vista.setControlador(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
		case IVistaOLD.PROCESAR:
		{
			this.procesar();
			break;
		}
		case IVistaOLD.GUARDAR:
		{
			this.guardar();
			break;
		}
		case IVistaOLD.ARCHIVOS:
		{
			this.archivos();
			break;
		}
		}

	}

	private void guardar()
	{
		Mat bgr = new Mat();
		Imgproc.cvtColor(originalMat, bgr, Imgproc.COLOR_RGB2BGR);
		Imgcodecs.imwrite("salida.jpg", bgr);

	}

	private void procesar()
	{
		double[] filtros = ImageProcessor.getFilterMatrix(originalMat);
		this.vista.updateLog(Arrays.toString(filtros));
		this.correctedMat = ImageProcessor.correct(originalMat);
		this.vista.setImage(ImageProcessor.matToBufferedImage(this.correctedMat));
		Mat bgr = new Mat();
		Imgproc.cvtColor(this.correctedMat, bgr, Imgproc.COLOR_RGB2BGR);
		Imgcodecs.imwrite("salida.jpg", bgr);

	}

	private void archivos()
	{
		JFileChooser jfc = new JFileChooser();
		jfc.setMultiSelectionEnabled(true);
		int response = jfc.showOpenDialog(null);
		if (response == JFileChooser.APPROVE_OPTION)
		{
			File[] selectedFiles = jfc.getSelectedFiles();
			this.vista.addFiles(selectedFiles);
		}
	}

}
