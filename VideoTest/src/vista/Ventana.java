package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;

import controlador.Controlador;
import java.awt.Dimension;
import javax.swing.JTextArea;

public class Ventana extends JFrame implements IVista
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane scrollPaneImagen;
	private JPanel panelBotones;
	private JButton btnAnalize;
	private JPanel panel_1;
	private JLabel lblImage;
	private JScrollPane scrollPaneLog;
	private JTextArea textArea;
	private JButton btnGuardar;
	private JPanel panel;
	
	
	
	


	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public Ventana()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		scrollPaneImagen = new JScrollPane();
		contentPane.add(scrollPaneImagen, BorderLayout.CENTER);

		lblImage = new JLabel("");
		scrollPaneImagen.setViewportView(lblImage);

		panelBotones = new JPanel();
		contentPane.add(panelBotones, BorderLayout.WEST);
		panelBotones.setLayout(new GridLayout(0, 1, 0, 0));

		panel_1 = new JPanel();
		panelBotones.add(panel_1);

		btnAnalize = new JButton("Analizar");
		panel_1.add(btnAnalize);
		this.btnAnalize.setActionCommand(IVista.ANALIZAR);
		
		panel = new JPanel();
		panelBotones.add(panel);
		
		btnGuardar = new JButton("Guardar");
		panel.add(btnGuardar);
		btnGuardar.setActionCommand(IVista.GUARDAR);
		scrollPaneLog = new JScrollPane();
		scrollPaneLog.setPreferredSize(new Dimension(2, 200));
		contentPane.add(scrollPaneLog, BorderLayout.SOUTH);
		
		textArea = new JTextArea();
		scrollPaneLog.setViewportView(textArea);
		this.setVisible(true);

	}

	@Override
	public void setImage(BufferedImage bufferedImage)
	{
		this.lblImage.setIcon(new ImageIcon(bufferedImage));
		this.repaint();
	}

	@Override
	public void setControlador(Controlador controlador)
	{
		this.btnAnalize.addActionListener(controlador);
		this.btnGuardar.addActionListener(controlador);
	}

	@Override
	public void updateLog(String string)
	{
		this.textArea.append(string+"\n");
		
	}

}
