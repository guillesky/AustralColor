package vista_Progress;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;


import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import core.AbstractMediaTask;
import core.ConsoleMediaTaskListener;
import core.MediaTaskListener;
import core.MediaTaskManager;
import core.Util;
import core.VideoAnalysisResult;
import core.VideoTask;

public class VideoBatchUI implements MediaTaskListener
{

	private JFrame frame;
	private JTable tabla;
	private TableModelAbstractMediaTak modelo;

	private MediaTaskManager mediaTaskManager;

	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> new VideoBatchUI().init());
	}

	private void init()
	{
		frame = new JFrame("Procesador de videos");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 400);

		modelo = new TableModelAbstractMediaTak();

		tabla = new JTableWithProgress(modelo, 1);

		JButton btnAgregar = new JButton("Agregar videos");
		btnAgregar.addActionListener(this::agregarArchivos);

		JButton btnProcesar = new JButton("Procesar");
		btnProcesar.addActionListener(e -> procesarCola());

		JPanel panelTop = new JPanel();
		panelTop.add(btnAgregar);
		panelTop.add(btnProcesar);

		frame.add(panelTop, BorderLayout.NORTH);
		frame.add(new JScrollPane(tabla), BorderLayout.CENTER);

		frame.setVisible(true);

		this.mediaTaskManager = new MediaTaskManager();
		ConsoleMediaTaskListener cpl = new ConsoleMediaTaskListener();

		mediaTaskManager.addMediaTaskListener(cpl);
		mediaTaskManager.addMediaTaskListener(this);

	}

	private Object procesarCola()
	{
		this.mediaTaskManager.startTask();
		return null;
	}

	private void agregarArchivos(ActionEvent e)
	{

		File currentDir = new File(System.getProperty("user.dir"));

		JFileChooser chooser = new JFileChooser(currentDir);
		FileFilter imageFilter = new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				return (f.isDirectory()||Util.getTypeExtension(f)==Util.TYPE_IMAGE);
			}
			@Override
			public String getDescription()
			{
				return "Image files";
			}
		};
		chooser.setMultiSelectionEnabled(true);

		chooser.addChoosableFileFilter(imageFilter);

		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
		{
			for (File f : chooser.getSelectedFiles())
			{

				VideoTask vt = new VideoTask(f.getAbsolutePath(), f.getAbsolutePath() + ".corrected.mp4",
						mediaTaskManager);
				mediaTaskManager.addTask(vt);
				modelo.addVideo(vt);
			}
		}
	}

	@Override
	public void videoAnalized(VideoTask videoTask, VideoAnalysisResult videoAnalysisResult)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void frameProcessed(VideoTask videoTask, Mat frame, int frameIndex)
	{
	}

	@Override
	public void updatePercentageCompleted(AbstractMediaTask abstractMediaTask)
	{

		SwingUtilities.invokeLater(() ->
		{

			this.modelo.fireTableDataChanged();

		});
	}

	@Override
	public void mediaCorrectCompleted(AbstractMediaTask abstractMediaTask, double elapsedMs)
	{

	}

	@Override
	public void mediaCorrectInitiated(AbstractMediaTask abstractMediaTask)
	{

	}

	@Override
	public void exceptionThrowed(Exception e)
	{
		// TODO Auto-generated method stub

	}

}