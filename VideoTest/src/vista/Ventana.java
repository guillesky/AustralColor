package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import controlador.Controlador;

public class Ventana extends JFrame implements IVista
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane scrollPaneImagen;
	private JPanel panelBotones;
	private JPanel panelWest;
	private JButton btnProcesar;
	private JPanel panel_1;
	private JLabel lblImage;
	private JScrollPane scrollPaneLog;
	private JTextArea textArea;
	private JButton btnGuardar;
	private JPanel panel;
	private JPanel panel_2;
	private JButton btnArchivos;
	private JScrollPane scrollPane;
	private JList<File> list;
	private DefaultListModel<File> listModel=new DefaultListModel<File> ();

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
		this.panelWest = new JPanel();
		panelBotones = new JPanel();
		contentPane.add(panelWest, BorderLayout.WEST);
		panelWest.setLayout(new GridLayout(0, 1, 0, 0));
		this.panelWest.add(panelBotones);
		panelBotones.setLayout(new GridLayout(0, 1, 0, 0));
		panel_2 = new JPanel();
		panelBotones.add(panel_2);

		btnArchivos = new JButton("Seleccion Archivos");
		btnArchivos.setActionCommand(IVista.ARCHIVOS);
		panel_2.add(btnArchivos);

		panel_1 = new JPanel();
		panelBotones.add(panel_1);

		btnProcesar = new JButton("Procesar");
		panel_1.add(btnProcesar);
		this.btnProcesar.setActionCommand(IVista.PROCESAR);

		panel = new JPanel();
		panelBotones.add(panel);

		btnGuardar = new JButton("Guardar");
		panel.add(btnGuardar);
		btnGuardar.setActionCommand(IVista.GUARDAR);
		
		scrollPane = new JScrollPane();
		panelWest.add(scrollPane);
		
		list = new JList<File>();
		this.list.setModel(listModel);
		list.setBorder(new TitledBorder(null, "Archivos Selecciondos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setViewportView(list);

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
		this.btnProcesar.addActionListener(controlador);
		this.btnGuardar.addActionListener(controlador);
		this.btnArchivos.addActionListener(controlador);
	}

	@Override
	public void updateLog(String string)
	{
		this.textArea.append(string + "\n");

	}

	@Override
	public void addFiles(File[] selectedFiles)
	{
		for (File file : selectedFiles)
			this.listModel.addElement(file);
	}

}
