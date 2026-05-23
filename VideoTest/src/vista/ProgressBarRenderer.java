package vista;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class ProgressBarRenderer extends JProgressBar implements TableCellRenderer
{

    public ProgressBarRenderer()
    {
	this.setMinimum(0);
	this.setMaximum(100);
	this.setStringPainted(true);
	
	
	
	setForeground(Color.BLUE);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
	    int row, int column)
    {
	setValue((int) value);
	return this;
    }


    
    
    
}
