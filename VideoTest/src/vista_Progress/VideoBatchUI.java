package vista_Progress;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class VideoBatchUI
{

    private JFrame frame;
    private JTable tabla;
    private VideoTableModel modelo;

    public static void main(String[] args)
    {
	SwingUtilities.invokeLater(() -> new VideoBatchUI().init());
    }

    private void init()
    {
	frame = new JFrame("Procesador de videos");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(700, 400);

	modelo = new VideoTableModel();
	
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
    }

    private void agregarArchivos(ActionEvent e)
    {
	JFileChooser chooser = new JFileChooser();
	chooser.setMultiSelectionEnabled(true);

	if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
	{
	    for (File f : chooser.getSelectedFiles())
	    {
		modelo.addVideo(new VideoItem(f.getAbsolutePath()));
	    }
	}
    }

    private void procesarCola()
    {
	new Thread(() ->
	{
	    for (int i = 0; i < modelo.getRowCount(); i++)
	    {
		procesarVideo(i);
	    }
	}).start();
    }

    private void procesarVideo(int fila)
    {
	SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>()
	{

	    protected Void doInBackground() throws Exception
	    {
		modelo.setEstado(fila, "Procesando");

		// Acá iría TU procesamiento real con OpenCV
		for (int i = 0; i <= 1000; i++)
		{
		    publish(i);
		    Thread.sleep(3); // simulación
		}

		return null;
	    }

	    protected void process(List<Integer> chunks)
	    {
		int val = chunks.get(chunks.size() - 1);
		modelo.setProgreso(fila, val);
	    }

	    protected void done()
	    {
		modelo.setEstado(fila, "Terminado");
	    }
	};

	worker.execute();

	try
	{
	    worker.get(); // bloquea para hacerlo secuencial
	} catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }
}