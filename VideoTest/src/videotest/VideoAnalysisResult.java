package videotest;

import java.util.HashMap;
import java.util.Map;

public class VideoAnalysisResult
{
	private String inputPath;
	private String outputPath;
	private int fps;
	private int frameCount;
	private HashMap<Integer, double[]> filters = new HashMap<Integer, double[]>();

	public VideoAnalysisResult(String inputPath, String outputPath, int fps, int frameCount)
	{
		super();
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.fps = fps;
		this.frameCount = frameCount;
	}

	public String getInputPath()
	{
		return inputPath;
	}

	public String getOutputPath()
	{
		return outputPath;
	}

	public int getFps()
	{
		return fps;
	}

	public int getFrameCount()
	{
		return frameCount;
	}

	public double[] getFilter(int index)
	{
		double[] respuesta = null;
		if (this.filters.containsKey(index))
			respuesta = this.filters.get(index);
		else if (index < this.frameCount)
		{
			int lowerIndex = (int) (index / this.fps) * this.fps;
			int upperIndex = lowerIndex + fps;
			if (upperIndex > this.frameCount - 1)
				upperIndex = this.frameCount - 1;
			System.out.println(" lowerIndex " + lowerIndex);
			System.out.println(" upperIndex " + upperIndex);

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
		String cartel = "VideoAnalysisResult [inputPath=" + inputPath + ", outputPath=" + outputPath + ", fps=" + fps
				+ ", frameCount=" + frameCount + "]";

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

		String result = sb.toString();

		return cartel + "\n" + result;
	}

}
