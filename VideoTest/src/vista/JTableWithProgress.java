package vista;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class JTableWithProgress extends JTable
{

    public JTableWithProgress(TableModel dm, int colWithProgress)
    {
	super(dm);
	// Renderer de progreso
	this.getColumnModel().getColumn(1).setCellRenderer(new ProgressBarRenderer());

    }

}
