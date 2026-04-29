package core;

import java.util.Map;
import java.util.TreeMap;

public class VideoAnalysisResult
{
	

	private TreeMap<Integer, double[]> filters = new TreeMap<Integer, double[]>();

	

	

	

	public double[] getFilter(int index)
	{
		double[] respuesta = null;
		if (this.filters.containsKey(index))
			respuesta = this.filters.get(index);
		else if (index < this.filters.lastKey())
		{
			int lowerIndex = this.filters.lowerKey(index);
			int upperIndex = this.filters.higherKey(index);
			respuesta = this.interpolated(index, lowerIndex, upperIndex);

		}

		return respuesta;
	}

	private double[] interpolated(int index, int lowerIndex, int upperIndex)
	{
		double t = (double) (index - lowerIndex) / (double) (upperIndex - lowerIndex);

		double[] lowerFilter=this.filters.get(lowerIndex);
		double[] upperFilter=this.filters.get(upperIndex);
		double[] interpolated = new double[lowerFilter.length];

		for (int x = 0; x < lowerFilter.length; x++)
		{
			interpolated[x] = lowerFilter[x] * (1 - t) + upperFilter[x] * t;
		}
		return interpolated;
	}

	public void addFilter(int index, double[] filter)
	{
		this.filters.put(index, filter);
	}

	public String detalle()
	{
		

		StringBuilder sb = new StringBuilder();

		for (Map.Entry<Integer, double[]> entry : this.filters.entrySet())
		{
			sb.append(entry.getKey()).append(": ");

			double[] f = entry.getValue();
			for (int i = 0; i < f.length; i++)
			{
				sb.append(String.format("%.10f", f[i]));
				if (i < f.length - 1)
					sb.append(",");
			}

			sb.append("\n");
		}


		return sb.toString();
	}

}
