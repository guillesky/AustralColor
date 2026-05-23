package vista;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import core.AbstractMediaTask;
import i18n.Messages;

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
		return 4;
	}

	@Override
	public String getColumnName(int col)
	{
		String result = "";

		switch (col)
		{
		case 0:
			result = Messages.FILE.getValue();
			break;
		case 1:
			result = Messages.PROGRESS.getValue();
			break;
		case 2:
			result = Messages.OUTPUT_FILE.getValue();
			break;

		case 3:
			result = Messages.STATUS.getValue();
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
			result = abstractMediaTask.getInputFileName();
			break;
		case 1:
			result = abstractMediaTask.getPercentageCompleted();
			break;
		case 2:
			result = abstractMediaTask.getOutputPath();
			break;
		case 3:
			result = abstractMediaTask.getStatus();
			break;

		}
		;
		return result;
	}

	/*
	 * public Class<?> getColumnClass(int col) { Class<?> result; if (col == 1)
	 * result = Integer.class; else result = String.class; return result; }
	 */
	protected AbstractMediaTask getAbstractMediaTask(int row)
	{
		return this.lista.get(row);
	}

	protected int getRowOf(AbstractMediaTask abstractMediaTask)
	{
		return this.lista.indexOf(abstractMediaTask);
	}

}
