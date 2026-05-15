package vista;

import java.awt.image.BufferedImage;
import java.io.File;

import controlador.ControladorOLD;

public interface IVistaOLD
{

	final String PROCESAR = "PROCESAR";
	final String GUARDAR = "GUARDAR";
	final String ARCHIVOS = "ARCHIVOS";

	void setImage(BufferedImage bufferedImage);

	void setControlador(ControladorOLD controlador);

	void updateLog(String string);

	void addFiles(File[] selectedFiles);

}