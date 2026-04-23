package vista;

import java.awt.image.BufferedImage;

import controlador.Controlador;

public interface IVista
{

	String ANALIZAR = "ANALIZAR";

	void setImage(BufferedImage bufferedImage);

	void setControlador(Controlador controlador);

	void updateLog(String string);

}