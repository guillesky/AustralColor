package main;

import java.awt.EventQueue;

import javax.swing.UIManager;

import org.opencv.core.Core;

import controlador.Controlador;
import core.Environment;
import i18n.Messages;
import vista.Ventana;

public class MainDMC
{
	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		Environment.getInstance();

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Ventana v = new Ventana();
					Controlador controlador = new Controlador(v);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

}
