package vista_Progress;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import controlador.Controlador;
import controlador.IVista;
import core.AbstractMediaTask;
import core.VideoTask;

public class Ventana extends JFrame implements IVista, ActionListener
{

	private static final long serialVersionUID = 1L;
	private Controlador controlador;
	private JPanel contentPane;
	private JSplitPane splitPaneArribaAbajo;
	private JSplitPane splitPaneDerechaIzquierda;
	private JPanel panel;
	private JButton btnAgregar;
	private JButton btnProcesar;
	private JPanel panel_1;
	private JPanel panel_2;

	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JScrollPane scrollPane_1;
	private JTable table;
	private JPanel panel_Derecha;
	private TableModelAbstractMediaTak modelo;

	private MediaFileChooser mediaFileChooser;

	public Ventana()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 911, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(0, 0));

		this.splitPaneArribaAbajo = new JSplitPane();
		this.splitPaneArribaAbajo.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.contentPane.add(this.splitPaneArribaAbajo);
		this.splitPaneDerechaIzquierda = new JSplitPane();

		this.splitPaneArribaAbajo.setLeftComponent(splitPaneDerechaIzquierda);

		this.scrollPane = new JScrollPane();
		this.splitPaneArribaAbajo.setRightComponent(this.scrollPane);

		this.splitPaneArribaAbajo.setResizeWeight(0.8);
		this.splitPaneDerechaIzquierda.setResizeWeight(0.8);

		this.scrollPane_1 = new JScrollPane();
		this.splitPaneDerechaIzquierda.setLeftComponent(this.scrollPane_1);
		this.modelo = new TableModelAbstractMediaTak();
		this.table = new JTableWithProgress(modelo, 1);
		this.scrollPane_1.setViewportView(this.table);

		panel_Derecha = new JPanel();
		splitPaneDerechaIzquierda.setRightComponent(panel_Derecha);
		this.textArea = new JTextArea();
		this.scrollPane.setViewportView(this.textArea);

		this.panel = new JPanel();
		this.contentPane.add(this.panel, BorderLayout.WEST);
		this.panel.setLayout(new GridLayout(0, 1, 0, 0));

		this.panel_1 = new JPanel();
		this.panel.add(this.panel_1);

		this.btnAgregar = new JButton("Agregar Archivos");
		this.btnAgregar.addActionListener(this);
		this.btnAgregar.setActionCommand(IVista.ADD_FILES);
		this.panel_1.add(this.btnAgregar);

		this.panel_2 = new JPanel();
		this.panel.add(this.panel_2);

		this.btnProcesar = new JButton("Procesar");
		this.btnProcesar.setActionCommand(IVista.START_TASK);
		this.panel_2.add(this.btnProcesar);

		File currentDir = new File(System.getProperty("user.dir"));
		this.mediaFileChooser = new MediaFileChooser(currentDir);

		this.setVisible(true);

	}

	@Override
	public void setControlador(Controlador controlador)
	{
		this.controlador = controlador;
		this.btnProcesar.addActionListener(controlador);

	}

	private void agregarArchivos()
	{
		if (this.mediaFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			this.controlador.addFiles(mediaFileChooser.getSelectedFiles());

		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
		case IVista.ADD_FILES:
			this.agregarArchivos();
			break;

		}

	}

	@Override
	public void updateTaskVisualization()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				modelo.fireTableDataChanged();
			}
		});
	}

	@Override
	public void addTasks(ArrayList<AbstractMediaTask> abstractMediaTasks)
	{
		for(AbstractMediaTask m: abstractMediaTasks )
			this.modelo.addAbstractMediaTask(m);
		
	}

	@Override
	public void removeTasks(AbstractMediaTask abstractMediaTask)
	{
		this.modelo.removeAbstractMediaTask(abstractMediaTask);
		
	}
	
	

}
