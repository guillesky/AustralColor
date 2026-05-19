package vista_Progress;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import javax.swing.JRadioButton;
import javax.swing.JLabel;

public class Ventana extends JFrame implements IVista, ActionListener
{

	private static final long serialVersionUID = 1L;
	private Controlador controlador;
	private JPanel contentPane;
	private JSplitPane splitPaneArribaAbajo;
	private JSplitPane splitPaneDerechaIzquierda;
	private JPanel panelWest;
	private JButton btnAgregar;
	private JButton btnProcesar;
	private JPanel panel_botones;

	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JScrollPane scrollPane_1;
	private JTable table;
	private JPanel panel_Derecha;
	private TableModelAbstractMediaTak modelo;

	private MediaFileChooser mediaFileChooser;
	private JPanel panel_2;
	private JPanel panel_3;
	private ButtonGroup group = new ButtonGroup();
	private JSpinner spinner;
	private JPanel panel_opciones;
	private JRadioButton rdbtnSaltar;
	private JRadioButton rdbtnSobreescribir;
	private JRadioButton rdbtnNewRenombrar;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JPanel panel_1;
	private JButton btnEliminar;
	private JPanel panel_4;
	private JButton btnCancelar;
	private JPanel panel_5;
	private JButton btnDetener;
	private JPanel panel_North;
	private JLabel lblOutputDirectoryLabel;
	private JLabel lblNewLabel_1;
	private JPanel panel_6;
	private JButton btnChangeDirectory;

	public Ventana()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 911, 600);
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

		this.panelWest = new JPanel();
		this.contentPane.add(this.panelWest, BorderLayout.WEST);
		this.panelWest.setLayout(new GridLayout(0, 1, 0, 0));

		this.panel_botones = new JPanel();
		this.panelWest.add(this.panel_botones);
		this.panel_botones.setLayout(new GridLayout(0, 1, 0, 0));

		this.panel_2 = new JPanel();
		this.panel_botones.add(this.panel_2);

		this.btnAgregar = new JButton("Agregar Archivos");
		this.panel_2.add(this.btnAgregar);
		this.btnAgregar.addActionListener(this);
		this.btnAgregar.setActionCommand(IVista.ADD_FILES);

		panel_1 = new JPanel();
		panel_botones.add(panel_1);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setActionCommand((String) null);
		panel_1.add(btnEliminar);

		this.panel_3 = new JPanel();
		this.panel_botones.add(this.panel_3);

		this.btnProcesar = new JButton("Procesar");
		this.panel_3.add(this.btnProcesar);

		panel_4 = new JPanel();
		panel_botones.add(panel_4);

		btnCancelar = new JButton("Cancelar\n");
		btnCancelar.setActionCommand((String) null);
		panel_4.add(btnCancelar);

		panel_5 = new JPanel();
		panel_botones.add(panel_5);

		btnDetener = new JButton("Detener\n");
		btnDetener.setActionCommand((String) null);
		panel_5.add(btnDetener);

		this.panel_opciones = new JPanel();
		panel_opciones
				.setBorder(new TitledBorder(null, "Opciones", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.panelWest.add(this.panel_opciones);
		panel_opciones.setLayout(new GridLayout(0, 1, 0, 0));

		panel = new JPanel();
		panel_opciones.add(panel);

		lblNewLabel = new JLabel("Procesos Simultaneos:");
		panel.add(lblNewLabel);

		this.spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(Integer.valueOf(4), Integer.valueOf(1), null, Integer.valueOf(1)));
		panel.add(spinner);
		this.spinner.setToolTipText("");

		rdbtnSaltar = new JRadioButton("Omitir Archivos Repetidos");
		rdbtnSaltar.setSelected(true);
		panel_opciones.add(rdbtnSaltar);

		rdbtnSobreescribir = new JRadioButton("Sobreescribir Archivos Repetidos");
		panel_opciones.add(rdbtnSobreescribir);

		rdbtnNewRenombrar = new JRadioButton("Renombrar Archivos Repetidos");
		rdbtnNewRenombrar.setToolTipText("Renombrar Archivos Repetidos");
		panel_opciones.add(rdbtnNewRenombrar);

		File currentDir = new File(System.getProperty("user.dir"));
		this.mediaFileChooser = new MediaFileChooser(currentDir);
		this.group.add(this.rdbtnSaltar);
		this.group.add(this.rdbtnNewRenombrar);
		this.group.add(this.rdbtnSobreescribir);

		panel_6 = new JPanel();
		panel_opciones.add(panel_6);

		btnChangeDirectory = new JButton("Cambiar Carpeta Destino");
		panel_6.add(btnChangeDirectory);
		this.btnProcesar.setActionCommand(IVista.START_TASK);
		this.btnDetener.setActionCommand(IVista.STOP_ALL);
		this.btnCancelar.setActionCommand(IVista.CANCEL_TASK);
		this.btnEliminar.setActionCommand(IVista.DELETE_TASK);
		this.btnChangeDirectory.setActionCommand(IVista.CHANGE_OUTPUT);
		this.btnChangeDirectory.addActionListener(this);
		panel_North = new JPanel();
		contentPane.add(panel_North, BorderLayout.NORTH);

		lblOutputDirectoryLabel = new JLabel("Ouput Directory:");
		panel_North.add(lblOutputDirectoryLabel);

		lblNewLabel_1 = new JLabel("New label");
		panel_North.add(lblNewLabel_1);
		this.checkEnableButtons();
		this.setVisible(true);

	}

	private void checkEnableButtons()
	{
		// TODO Auto-generated method stub

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

		this.agregarArchivos();

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
		for (AbstractMediaTask m : abstractMediaTasks)
			this.modelo.addAbstractMediaTask(m);

	}

	@Override
	public void removeTasks(AbstractMediaTask abstractMediaTask)
	{
		this.modelo.removeAbstractMediaTask(abstractMediaTask);

	}

	public static void main(String[] args)
	{
		Ventana v = new Ventana();
	}
}
