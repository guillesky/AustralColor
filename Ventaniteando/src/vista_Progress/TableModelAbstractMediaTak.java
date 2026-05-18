package vista_Progress;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

class TableModelAbstractMediaTak extends AbstractTableModel
{

	private List<AbstractMediaTask> lista = new ArrayList<AbstractMediaTask>();

	public void addAbstractMediaTask(AbstractMediaTask abstractMediaTask)
	{
		lista.add(abstractMediaTask);
		fireTableRowsInserted(lista.size() - 1, lista.size() - 1);

	}

	public void removeAbstractMediaTask(AbstractMediaTask abstractMediaTask)
	{
		int i = lista.indexOf(abstractMediaTask);
		lista.remove(abstractMediaTask);
		this.fireTableRowsDeleted(getRowCount(), getColumnCount());
		fireTableRowsInserted(i, i);
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
		AbstractMediaTask abstractMediaTask = lista.get(row);
		Object result = null;
		switch (col)
		{
		case 0:
			result = abstractMediaTask.getInputPath();
			break;
		case 1:
			result = abstractMediaTask.getPercentageCompleted();
			break;
		case 2:
			result = abstractMediaTask.getStatus();
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

}
