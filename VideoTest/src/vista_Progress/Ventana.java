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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import controlador.Controlador;
import controlador.IVista;
import core.AbstractMediaTask;
import core.Environment;
import core.MediaTaskManager;
import core.VideoTask;
import i18n.Messages;

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
	private FolderFileChooser folderFileChooser;
	private JPanel panel_2;
	private JPanel panel_3;
	private ButtonGroup group = new ButtonGroup();
	private JSpinner spinner;
	private JPanel panel_opciones;
	private JRadioButton rdbtnSaltar;
	private JRadioButton rdbtnSobreescribir;
	private JRadioButton rdbtnRenombrar;
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
	private int duplicateFilePolicy = Environment.getInstance().getDuplicateFilePolicy();

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

		btnCancelar = new JButton("Cancelar");
			panel_4.add(btnCancelar);

		panel_5 = new JPanel();
		panel_botones.add(panel_5);

		btnDetener = new JButton("Detener");
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

		rdbtnRenombrar = new JRadioButton("Renombrar Archivos Repetidos");
		rdbtnRenombrar.setToolTipText("Renombrar Archivos Repetidos");
		panel_opciones.add(rdbtnRenombrar);

		File currentDir = new File(System.getProperty("user.dir"));
		this.mediaFileChooser = new MediaFileChooser(currentDir);
		this.folderFileChooser = new FolderFileChooser(currentDir);
		this.group.add(this.rdbtnSaltar);
		this.group.add(this.rdbtnRenombrar);
		this.group.add(this.rdbtnSobreescribir);

		panel_6 = new JPanel();
		panel_opciones.add(panel_6);

		btnChangeDirectory = new JButton("Cambiar Carpeta Destino");
		btnChangeDirectory.setActionCommand(IVista.CHANGE_OUTPUT);
		panel_6.add(btnChangeDirectory);
		this.btnChangeDirectory.addActionListener(this);

		this.btnProcesar.setActionCommand(IVista.START_TASK);
		this.btnDetener.setActionCommand(IVista.STOP_ALL);
		this.btnCancelar.setActionCommand(IVista.CANCEL_TASK);
		this.btnEliminar.setActionCommand(IVista.DELETE_TASK);
		this.rdbtnSaltar.setActionCommand(IVista.IGNORE_DUPLICATED_FILES);
		this.rdbtnRenombrar.setActionCommand(IVista.RENAME_DUPLICATED_FILES);
		this.rdbtnSobreescribir.setActionCommand(IVista.OVERWRITE_DUPLICATED_FILES);
		panel_North = new JPanel();
		contentPane.add(panel_North, BorderLayout.NORTH);

		lblOutputDirectoryLabel = new JLabel("Ouput Directory:");
		panel_North.add(lblOutputDirectoryLabel);

		lblNewLabel_1 = new JLabel("New label");
		panel_North.add(lblNewLabel_1);
		this.checkEnableButtons();
		this.setTextComponents();
		this.setVisible(true);

	}

	@Override
	public void setControlador(Controlador controlador)
	{
		this.controlador = controlador;
		this.btnProcesar.addActionListener(controlador);
		this.btnDetener.addActionListener(controlador);
		this.btnCancelar.addActionListener(controlador);
		this.btnEliminar.addActionListener(controlador);
		this.rdbtnRenombrar.addActionListener(controlador);
		this.rdbtnSaltar.addActionListener(controlador);
		this.rdbtnSobreescribir.addActionListener(controlador);

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
		case IVista.CHANGE_OUTPUT:
			this.changeDirectory();
			break;

		}

	}

	private void changeDirectory()
	{

		int result = this.folderFileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION)
		{
			File folder = this.folderFileChooser.getSelectedFile();

			Environment.getInstance().setOutputPath(folder.getAbsolutePath());
			this.folderFileChooser.setCurrentDirectory(folder);
			this.setTextComponents();
		}

	}

	@Override
	public void updateTaskStatus(AbstractMediaTask abstractMediaTask)
	{
		this.updateCell(abstractMediaTask, 3);
		this.checkEnableButtons();
	}

	@Override
	public void addTasks(ArrayList<AbstractMediaTask> abstractMediaTasks)
	{
		for (AbstractMediaTask m : abstractMediaTasks)
			this.modelo.addAbstractMediaTask(m);
		this.table.clearSelection();
		this.checkEnableButtons();
	}

	private void checkEnableButtons()
	{

		this.btnProcesar.setEnabled(
				MediaTaskManager.getInstance().getQueuedTaskCount() > 0 && !MediaTaskManager.getInstance().isWorking());
		this.rdbtnRenombrar.setEnabled(!MediaTaskManager.getInstance().isWorking());
		this.rdbtnSaltar.setEnabled(!MediaTaskManager.getInstance().isWorking());
		this.rdbtnSobreescribir.setEnabled(!MediaTaskManager.getInstance().isWorking());

	}

	@Override
	public void removeTasks(AbstractMediaTask abstractMediaTask)
	{
		this.modelo.removeAbstractMediaTask(abstractMediaTask);
		this.table.clearSelection();
		this.checkEnableButtons();
	}

	@Override
	public ArrayList<AbstractMediaTask> getSelectedMediaTask()
	{
		ArrayList<AbstractMediaTask> result = new ArrayList<AbstractMediaTask>();
		int[] indexs = this.table.getSelectedRows();

		for (int i : indexs)
		{
			result.add(this.modelo.getAbstractMediaTask(i));
		}
		return result;
	}

	@Override
	public void updatePercentageCompleted(AbstractMediaTask abstractMediaTask)
	{
		this.updateCell(abstractMediaTask, 1);
	}

	private void updateCell(AbstractMediaTask abstractMediaTask, int col)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				int row = modelo.getRowOf(abstractMediaTask);
				if (row >= 0)
					modelo.fireTableCellUpdated(row, col);

			}
		});
	}

	@Override
	public void allTaskFinished(double elapsedMs)
	{
		this.checkEnableButtons();
		this.updateLogText("Todos los procesos completos en " + elapsedMs + " ms");

	}

	@Override
	public void updateLogText(String text)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				textArea.append(text + "\n");
			}
		});

	}

	private void setTextComponents()
	{
		this.folderFileChooser.setDialogTitle(Messages.SELECT_OUTPUT_FOLDER.getValue());
		this.lblNewLabel_1.setText(Environment.getInstance().getOutputPath());
		this.btnAgregar.setText(Messages.ADD_FILES.getValue());
		this.btnCancelar.setText(Messages.CANCEL.getValue());
		this.btnChangeDirectory.setText(Messages.CHANGE_OUTPUT_FOLDER.getValue());
		this.btnDetener.setText(Messages.STOP.getValue());
		this.btnEliminar.setText(Messages.DELETE.getValue());
		this.btnProcesar.setText(Messages.START_TASKS.getValue());

		this.rdbtnRenombrar.setText(Messages.RENAME.getValue());
		this.rdbtnSaltar.setText(Messages.IGNORE.getValue());
		this.rdbtnSobreescribir.setText(Messages.OVERWRITE.getValue());
		this.mediaFileChooser.setDialogTitle(Messages.SELECT_MEDIA_FILES.getValue());
		this.lblOutputDirectoryLabel.setText(Messages.OUTPUT_FOLDER.getValue());

	}

	@Override
	public int getDuplicateFilePolicy()
	{
		int result = 0;
		switch (this.group.getSelection().getActionCommand())
		{
		case IVista.IGNORE_DUPLICATED_FILES:
			result = Environment.IGNORE_DUPLICATED_FILES;
			break;
		case IVista.RENAME_DUPLICATED_FILES:
			result = Environment.RENAME_DUPLICATED_FILES;
			break;
		case IVista.OVERWRITE_DUPLICATED_FILES:
			result = Environment.OVERWRITE_DUPLICATED_FILES;
			break;

		}

		return result;
	}
}
