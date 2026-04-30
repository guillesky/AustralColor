package vista_Progress;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTable;

public class Ventana extends JFrame
{

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JSplitPane splitPaneArribaAbajo;
    private JSplitPane splitPaneDerechaIzquierda;
    private JPanel panel;
    private JButton btnNewButton;
    private JButton btnNewButton_1;
    private JPanel panel_1;
    private JPanel panel_2;
   
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JScrollPane scrollPane_1;
    private JTable table;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
	EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		try
		{
		    Ventana frame = new Ventana();
		    frame.setVisible(true);
		} catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the frame.
     */
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
	
	this.table = new JTable();
	this.scrollPane_1.setViewportView(this.table);
	this.textArea = new JTextArea();
	this.scrollPane.setViewportView(this.textArea);
	
	this.panel = new JPanel();
	this.contentPane.add(this.panel, BorderLayout.WEST);
	this.panel.setLayout(new GridLayout(0, 1, 0, 0));
	
	this.panel_1 = new JPanel();
	this.panel.add(this.panel_1);
	
	this.btnNewButton = new JButton("New button");
	this.panel_1.add(this.btnNewButton);
	
	this.panel_2 = new JPanel();
	this.panel.add(this.panel_2);
	
	this.btnNewButton_1 = new JButton("New button");
	this.panel_2.add(this.btnNewButton_1);

    }

}
