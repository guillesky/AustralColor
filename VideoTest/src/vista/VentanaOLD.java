package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import controlador.Controlador;
import controlador.IVista;
import core.AbstractMediaTask;
import core.Environment;
import core.MediaTaskManager;
import core.ThreadWaiter;
import core.ThreadWaiterListener;
import i18n.Messages;

public class VentanaOLD extends JFrame implements IVista, ActionListener, TableModelListener
{

    private static final long serialVersionUID = 1L;
    private Controlador controlador;
    private JPanel contentPane;
    private JSplitPane splitPaneArribaAbajo;
    private JPanel panelWest;
    private JButton btnAgregar;
    private JButton btnProcesar;
    private JPanel panel_botones;

    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JScrollPane scrollPaneTable;
    private JTable table;

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
    private JLabel lblProcesosSimultaneos;
    private JPanel panel_1;
    private JButton btnEliminar;
    private JPanel panel_4;
    private JButton btnCancelar;
    private JPanel panel_5;
    private JButton btnDetener;
    private JPanel panel_North;
    private JPanel panel_Sur;
    private JLabel lblOutputDirectoryLabel;
    private JLabel lblOutputPath;
    private JLabel lblFpsLegend;
    private JLabel lblFpsValue;

    private JPanel panel_6;
    private JButton btnChangeDirectory;
    private TitledBorder titledBorderOpciones;
    private boolean shuttingDown = false;

    public VentanaOLD()
    {
	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	setBounds(100, 100, 911, 600);
	this.addWindowListener(new WindowAdapter()
	{
	    @Override
	    public void windowClosing(WindowEvent e)
	    {
		shutdown();
	    }
	});
	this.contentPane = new JPanel();
	this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(this.contentPane);
	this.contentPane.setLayout(new BorderLayout(0, 0));
	this.splitPaneArribaAbajo = new JSplitPane();
	this.splitPaneArribaAbajo.setOrientation(JSplitPane.VERTICAL_SPLIT);
	this.contentPane.add(this.splitPaneArribaAbajo);
	this.scrollPaneTable = new JScrollPane();
	this.splitPaneArribaAbajo.setLeftComponent(scrollPaneTable);
	this.scrollPane = new JScrollPane();
	this.splitPaneArribaAbajo.setRightComponent(this.scrollPane);
	this.splitPaneArribaAbajo.setResizeWeight(0.8);
	this.modelo = new TableModelAbstractMediaTak();
	this.table = new JTableWithProgress(modelo, 1);
	this.scrollPaneTable.setViewportView(this.table);
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
	this.btnAgregar = new JButton();
	this.panel_2.add(this.btnAgregar);
	this.btnAgregar.addActionListener(this);
	this.btnAgregar.setActionCommand(IVista.ADD_FILES);
	panel_1 = new JPanel();
	panel_botones.add(panel_1);
	btnEliminar = new JButton();
	btnEliminar.setActionCommand((String) null);
	panel_1.add(btnEliminar);
	this.panel_3 = new JPanel();
	this.panel_botones.add(this.panel_3);
	this.btnProcesar = new JButton();
	this.panel_3.add(this.btnProcesar);
	panel_4 = new JPanel();
	panel_botones.add(panel_4);
	btnCancelar = new JButton();
	panel_4.add(btnCancelar);
	panel_5 = new JPanel();
	panel_botones.add(panel_5);
	btnDetener = new JButton();
	panel_5.add(btnDetener);
	this.panel_opciones = new JPanel();
	this.titledBorderOpciones = new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null);
	panel_opciones.setBorder(this.titledBorderOpciones);
	this.panelWest.add(this.panel_opciones);
	panel_opciones.setLayout(new GridLayout(0, 1, 0, 0));
	panel = new JPanel();
	panel_opciones.add(panel);
	lblProcesosSimultaneos = new JLabel();
	panel.add(lblProcesosSimultaneos);
	this.spinner = new JSpinner();
	spinner.setModel(new SpinnerNumberModel(MediaTaskManager.getInstance().getMaxSimultaneousProcessing(),
		Integer.valueOf(1), null, Integer.valueOf(1)));
	panel.add(spinner);

	rdbtnSaltar = new JRadioButton();
	rdbtnSaltar.setSelected(true);
	panel_opciones.add(rdbtnSaltar);
	rdbtnSobreescribir = new JRadioButton();
	panel_opciones.add(rdbtnSobreescribir);
	rdbtnRenombrar = new JRadioButton();
	panel_opciones.add(rdbtnRenombrar);
	File currentDir = new File(System.getProperty("user.dir"));
	this.mediaFileChooser = new MediaFileChooser(currentDir);
	this.folderFileChooser = new FolderFileChooser(currentDir);
	this.group.add(this.rdbtnSaltar);
	this.group.add(this.rdbtnRenombrar);
	this.group.add(this.rdbtnSobreescribir);

	panel_6 = new JPanel();
	panel_opciones.add(panel_6);

	btnChangeDirectory = new JButton();
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

	this.panel_Sur = new JPanel(new FlowLayout(FlowLayout.LEFT));
	contentPane.add(panel_Sur, BorderLayout.SOUTH);
	this.lblFpsLegend = new JLabel();
	this.lblFpsValue = new JLabel();

	this.panel_Sur.add(this.lblFpsLegend);
	this.panel_Sur.add(this.lblFpsValue);

	lblOutputDirectoryLabel = new JLabel();
	panel_North.add(lblOutputDirectoryLabel);

	lblOutputPath = new JLabel();
	panel_North.add(lblOutputPath);
	this.checkEnableButtons();
	this.setTextComponents();
	this.modelo.addTableModelListener(this);
	
	
	this.set3Icons(this.btnProcesar, "icons/accept_normal.png", "icons/accept_rollover.png", "icons/accept_disabled.png", 32);
	this.set3Icons(this.btnCancelar, "icons/cancel_normal.png", "icons/cancel_rollover.png", "icons/cancel_disabled.png", 32);
	this.set3Icons(this.btnAgregar, "icons/add_normal.png", "icons/add_rollover.png", "icons/add_disabled.png", 32);
	this.set3Icons(this.btnDetener, "icons/stop_normal.png", "icons/stop_rollover.png", "icons/stop_disabled.png", 32);
	this.set3Icons(this.btnEliminar, "icons/delete_normal.png", "icons/delete_rollover.png", "icons/delete_disabled.png", 32);
	
	this.setVisible(true);

    }
    
    
    
    private void set3Icons(JButton button, String resourceNormal, String resourceRollover, String reourceDisabled,int height)
    {
	button.setIcon(getScaledImageIcon(resourceNormal,height));
	button.setDisabledIcon(getScaledImageIcon(reourceDisabled,height));
	button.setRolloverIcon(getScaledImageIcon(resourceRollover,height));
	button.setFocusPainted(false);
    }

    private ImageIcon getScaledImageIcon(String resource, int eight)
    {
	ImageIcon iOriginal = new ImageIcon(resource);
	Image imagenEscalada = iOriginal.getImage().getScaledInstance(-1, eight, Image.SCALE_SMOOTH);
	return new ImageIcon(imagenEscalada);
    }

    protected void shutdown()
    {
	if (!this.shuttingDown)
	{
	    String text = null;
	    if (!MediaTaskManager.getInstance().isWorking())
		text = Messages.CONFIRM_EXIT_MESSAGE.getValue();
	    else
		text = Messages.CONFIRM_EXIT_MESSAGE_WITH_PENDING_THREADS.getValue();
	    int result = JOptionPane.showConfirmDialog(this, text, Messages.EXIT_DIALOG_TITLE.getValue(),
		    JOptionPane.YES_NO_OPTION);
	    if (result == JOptionPane.YES_OPTION)
	    {
		this.shuttingDown = true;
		if (MediaTaskManager.getInstance().isWorking())
		{

		    MediaTaskManager.getInstance().emitStopSignal();
		    ThreadWaiter t = new ThreadWaiter();
		    t.addThreadWaiterListener(new ThreadWaiterListener()
		    {
			public void allThreadStop()
			{
			    System.exit(0);
			}
		    });
		    t.start();
		    this.createShutdownDialog();

		} else
		    System.exit(0);
	    }
	}
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
	this.btnEliminar.setEnabled(this.modelo.getRowCount() > 0);
	this.btnCancelar.setEnabled(MediaTaskManager.getInstance().isWorking());
	this.btnDetener.setEnabled(MediaTaskManager.getInstance().isWorking());

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
	this.updateLogText("Correccion de archivos finalizada en " + elapsedMs + " ms");

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
	this.setTitle(Environment.APP_NAME+"\t"+Environment.VERSION);
	this.folderFileChooser.setDialogTitle(Messages.SELECT_OUTPUT_FOLDER.getValue());
	this.lblOutputPath.setText(Environment.getInstance().getOutputPath());
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
	this.titledBorderOpciones.setTitle(Messages.OPTIONS.getValue());
	this.lblFpsLegend.setText(Messages.FPS_LEGEND.getValue());
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

    @Override
    public void tableChanged(TableModelEvent e)
    {
	this.checkEnableButtons();
    }

    @Override
    public void updateFPS(double fps)
    {
	this.lblFpsValue.setText(String.format("%.4f", fps));

    }

    private JDialog createShutdownDialog()
    {
	JDialog dialog = new JDialog(this, Messages.EXITING.getValue(), true);

	dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

	dialog.setResizable(false);

	JPanel panel = new JPanel();

	panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

	panel.setLayout(new BorderLayout(10, 10));

	JLabel label = new JLabel(Messages.EXITING_MESSAGE.getValue());

	JProgressBar progressBar = new JProgressBar();

	progressBar.setIndeterminate(true);

	panel.add(label, BorderLayout.NORTH);
	panel.add(progressBar, BorderLayout.CENTER);

	dialog.getContentPane().add(panel);

	dialog.pack();

	dialog.setLocationRelativeTo(this);
	dialog.setVisible(true);
	return dialog;
    }

}
