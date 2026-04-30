package vista_Progress;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

class VideoTableModel extends AbstractTableModel
{

    private List<VideoItem> lista = new ArrayList<>();

    public void addVideo(VideoItem v)
    {
	lista.add(v);
	fireTableRowsInserted(lista.size() - 1, lista.size() - 1);
    }

    @Override
    public int getRowCount()
    {
	return lista.size();
    }

    @Override
    public int getColumnCount()
    {
	return 3;
    }

    @Override
    public String getColumnName(int col)
    {
	String result = "";

	switch (col)
	{
	case 0:
	    result = "Archivo";
	    break;
	case 1:
	    result = "Progreso";
	    break;
	case 2:
	    result = "Estado";
	    break;

	}
	;
	return result;
    }

    @Override
    public Object getValueAt(int row, int col)
    {
	VideoItem v = lista.get(row);
	Object result = null;
	switch (col)
	{
	case 0:
	    result = v.archivo;
	    break;
	case 1:
	    result = v.progreso;
	    break;
	case 2:
	    result = v.estado;
	    break;

	}
	;
	return result;
    }

    public Class<?> getColumnClass(int col)
    {
	Class<?> result;
	if (col == 1)
	    result = Integer.class;
	else
	    result = String.class;
	return result;
    }

    public void setProgreso(int row, int val)
    {
	lista.get(row).progreso = val;
	fireTableCellUpdated(row, 1);
    }

    public void setEstado(int row, String estado)
    {
	lista.get(row).estado = estado;
	fireTableCellUpdated(row, 2);
    }
}
