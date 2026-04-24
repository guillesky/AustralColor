package videotest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageAnalizer
{
	private static final int THRESHOLD_RATIO = 2000;
	private static final int MIN_AVG_RED = 60;
	private static final int MAX_HUE_SHIFT = 120;
	private static final double BLUE_MAGIC_VALUE = 1.2;
	private static final int SAMPLE_SECONDS = 2; // Extracts color correction from every N seconds

	private static Mat hueShiftRed(Mat mat, double h)
	{

		double U = Math.cos(h * Math.PI / 180.0);
		double W = Math.sin(h * Math.PI / 180.0);

		double rCoef = 0.299 + 0.701 * U + 0.168 * W;
		double gCoef = 0.587 - 0.587 * U + 0.330 * W;
		double bCoef = 0.114 - 0.114 * U - 0.497 * W;

		// separar canales (BGR)
		List<Mat> channels = new ArrayList<>();
		Core.split(mat, channels);

		Mat r = channels.get(0);
		Mat g = channels.get(1);
		Mat b = channels.get(2);

		// aplicar coeficientes
		Core.multiply(r, new Scalar(rCoef), r);
		Core.multiply(g, new Scalar(gCoef), g);
		Core.multiply(b, new Scalar(bCoef), b);
		Mat dst = new Mat();
		// volver a unir
		Core.merge(Arrays.asList(r, g, b), dst);
		lalala(h,mat,dst);
		return dst;
	}

	private static double[] hueShiftRedVec(double[] rgb, double h)
	{

		double U = Math.cos(h * Math.PI / 180);
		double W = Math.sin(h * Math.PI / 180);

		double r = (0.299 + 0.701 * U + 0.168 * W) * rgb[0];
		double g = (0.587 - 0.587 * U + 0.330 * W) * rgb[1];
		double b = (0.114 - 0.114 * U - 0.497 * W) * rgb[2];

		return new double[]
		{ r, g, b };
	}

	private static double[] normalizingInterval(double[] array)
	{

		double high = 255;
		double low = 0;
		double maxDist = 0;

		for (int i = 1; i < array.length; i++)
		{
			double dist = array[i] - array[i - 1];

			if (dist > maxDist)
			{
				maxDist = dist;
				high = array[i];
				low = array[i - 1];
			}
		}

		return new double[]
		{ low, high };
	}

	private Mat applyFilter(Mat mat, double[] filt)
	{

		Mat matFloat = new Mat();
		mat.convertTo(matFloat, CvType.CV_32F);

		List<Mat> channels = new ArrayList<>();
		Core.split(matFloat, channels);

		Mat b = channels.get(0);
		Mat g = channels.get(1);
		Mat r = channels.get(2);

		// ===== Aplicar fórmula =====

		Mat newR = new Mat();
		Mat newG = new Mat();
		Mat newB = new Mat();

		// R' = R*f0 + G*f1 + B*f2 + bias
		Core.multiply(r, new Scalar(filt[0]), newR);
		Core.add(newR, multiply(g, filt[1]), newR);
		Core.add(newR, multiply(b, filt[2]), newR);
		Core.add(newR, new Scalar(filt[4] * 255), newR);

		// G' = G*f6 + bias
		Core.multiply(g, new Scalar(filt[6]), newG);
		Core.add(newG, new Scalar(filt[9] * 255), newG);

		// B' = B*f12 + bias
		Core.multiply(b, new Scalar(filt[12]), newB);
		Core.add(newB, new Scalar(filt[14] * 255), newB);

		// ===== Merge =====
		List<Mat> resultChannels = Arrays.asList(newB, newG, newR);
		Mat result = new Mat();
		Core.merge(resultChannels, result);

		// ===== Clip 0–255 =====
		Core.min(result, new Scalar(255), result);
		Core.max(result, new Scalar(0), result);

		// ===== Convertir a 8 bits =====
		result.convertTo(result, CvType.CV_8U);

		return result;
	}

	private Mat multiply(Mat src, double value)
	{
		Mat dst = new Mat();
		Core.multiply(src, new Scalar(value), dst);
		return dst;
	}

	public static double[] getFilterMatrix(Mat inputRgb)
	{

		// 1) Trabajar sobre copia redimensionada
		Mat mat = new Mat();
		Imgproc.resize(inputRgb, mat, new Size(256, 256));

		// Pasar a float para evitar problemas al multiplicar
		mat.convertTo(mat, CvType.CV_32FC3);

		// 2) Promedio RGB
		Scalar mean = Core.mean(mat);

		Mat avgMat = new Mat(1, 1, CvType.CV_32FC3);
		
		avgMat.put(0, 0, (int) mean.val[0], (int) mean.val[1], (int) mean.val[2]);

		// 3) Buscar hue shift
		double newAvgR = mean.val[0];
		int hueShift = 0;

		while (newAvgR < MIN_AVG_RED)
		{

			Mat shiftedAvg = hueShiftRed(avgMat.clone(), hueShift);

			float[] avgVals = new float[3];
			shiftedAvg.get(0, 0, avgVals);

			newAvgR = avgVals[0] + avgVals[1] + avgVals[2];

			hueShift++;

			if (hueShift > MAX_HUE_SHIFT)
			{
				newAvgR = MIN_AVG_RED;
			}
		}

		// 4) Aplicar hue shift a la imagen
		Mat shiftedMat = hueShiftRed(mat.clone(), hueShift);

		List<Mat> channels = new ArrayList<>();
		Core.split(shiftedMat, channels);

		Mat newR = new Mat();
		Core.add(channels.get(0), channels.get(1), newR);
		Core.add(newR, channels.get(2), newR);

		Core.min(newR, new Scalar(255), newR);
		Core.max(newR, new Scalar(0), newR);

		// Reemplazar canal rojo (RGB → índice 0)
		channels.set(0, newR);
		Core.merge(channels, mat);

		// Convertir a 8 bits para histograma
		Mat histMat = new Mat();
		mat.convertTo(histMat, CvType.CV_8UC3);

		// 5) Histogramas
		Mat histR = new Mat();
		Mat histG = new Mat();
		Mat histB = new Mat();

		Imgproc.calcHist(Arrays.asList(histMat), new MatOfInt(0), new Mat(), histR, new MatOfInt(256),
				new MatOfFloat(0, 256));

		Imgproc.calcHist(Arrays.asList(histMat), new MatOfInt(1), new Mat(), histG, new MatOfInt(256),
				new MatOfFloat(0, 256));

		Imgproc.calcHist(Arrays.asList(histMat), new MatOfInt(2), new Mat(), histB, new MatOfInt(256),
				new MatOfFloat(0, 256));

		double[][] normalizeMat = new double[256][3];

		double threshold = (histMat.rows() * histMat.cols()) / (double) THRESHOLD_RATIO;

		for (int x = 0; x < 256; x++)
		{

			if (histR.get(x, 0)[0] < threshold)
				normalizeMat[x][0] = x;

			if (histG.get(x, 0)[0] < threshold)
				normalizeMat[x][1] = x;

			if (histB.get(x, 0)[0] < threshold)
				normalizeMat[x][2] = x;
		}

		normalizeMat[255][0] = 255;
		normalizeMat[255][1] = 255;
		normalizeMat[255][2] = 255;

		// 6) Intervalos
		double[] rInterval = normalizingInterval(column(normalizeMat, 0));
		double[] gInterval = normalizingInterval(column(normalizeMat, 1));
		double[] bInterval = normalizingInterval(column(normalizeMat, 2));

		double adjustRLow = rInterval[0];
		double adjustRHigh = rInterval[1];
		double adjustGLow = gInterval[0];
		double adjustGHigh = gInterval[1];
		double adjustBLow = bInterval[0];
		double adjustBHigh = bInterval[1];

		// 7) Hue shift sobre [1,1,1] como matriz
		Mat ones = new Mat(1, 1, CvType.CV_32FC3);
		ones.put(0, 0, 1f, 1f, 1f);

		Mat shiftedOnes = hueShiftRed(ones, hueShift);

		float[] vals = new float[3];
		shiftedOnes.get(0, 0, vals);

		double shiftedR = vals[0];
		double shiftedG = vals[1];
		double shiftedB = vals[2];

		// 8) Gains
		double redGain = 256.0 / (adjustRHigh - adjustRLow);
		double greenGain = 256.0 / (adjustGHigh - adjustGLow);
		double blueGain = 256.0 / (adjustBHigh - adjustBLow);

		// 9) Offsets
		double redOffset = (-adjustRLow / 256.0) * redGain;
		double greenOffset = (-adjustGLow / 256.0) * greenGain;
		double blueOffset = (-adjustBLow / 256.0) * blueGain;

		// 10) Ajustes finales
		double adjustRed = shiftedR * redGain;
		double adjustRedGreen = shiftedG * redGain;
		double adjustRedBlue = shiftedB * redGain * BLUE_MAGIC_VALUE;

		// 11) Resultado
		return new double[]
		{ adjustRed, adjustRedGreen, adjustRedBlue, 0, redOffset, 0, greenGain, 0, 0, greenOffset, 0, 0, blueGain, 0,
				blueOffset, 0, 0, 0, 1, 0 };
	}

	public static double[] getFilterMatrix2(Mat inputRgb)
	{
		// Trabajar sobre copia (NO tocar original)
		Mat mat = new Mat();
		Imgproc.resize(inputRgb, mat, new Size(256, 256));

		// 2. Promedio RGB
		Scalar mean = Core.mean(mat);
		double[] avg = new double[]
		{ mean.val[0], // R
				mean.val[1], // G
				mean.val[2] // B
		};

		// 3. Buscar hue shift
		double newAvgR = avg[0];
		int hueShift = 0;

		while (newAvgR < MIN_AVG_RED)
		{
			double[] shifted = hueShiftRedVec(avg, hueShift);

			newAvgR = shifted[0] + shifted[1] + shifted[2];

			hueShift++;

			if (hueShift > MAX_HUE_SHIFT)
			{
				newAvgR = MIN_AVG_RED;
			}
		}

		// 4. Aplicar hue shift (sobre copia)
		Mat shiftedMat = hueShiftRed(mat, hueShift);

		// 5. Nuevo canal R = suma de canales
		List<Mat> ch = new ArrayList<>();
		Core.split(shiftedMat, ch);

		Mat newR = new Mat();
		Core.add(ch.get(0), ch.get(1), newR);
		Core.add(newR, ch.get(2), newR);

		Core.min(newR, new Scalar(255), newR);
		Core.max(newR, new Scalar(0), newR);

		ch.set(0, newR); // canal R (recordá: estamos en RGB)
		Core.merge(ch, mat);

		// 6. Histogramas
		Mat histR = new Mat();
		Mat histG = new Mat();
		Mat histB = new Mat();

		Imgproc.calcHist(Arrays.asList(mat), new MatOfInt(0), new Mat(), histR, new MatOfInt(256),
				new MatOfFloat(0, 256));
		Imgproc.calcHist(Arrays.asList(mat), new MatOfInt(1), new Mat(), histG, new MatOfInt(256),
				new MatOfFloat(0, 256));
		Imgproc.calcHist(Arrays.asList(mat), new MatOfInt(2), new Mat(), histB, new MatOfInt(256),
				new MatOfFloat(0, 256));

		double[][] normalizeMat = new double[256][3];

		double threshold = (mat.rows() * mat.cols()) / THRESHOLD_RATIO;

		for (int x = 0; x < 256; x++)
		{

			if (histR.get(x, 0)[0] < threshold)
				normalizeMat[x][0] = x;

			if (histG.get(x, 0)[0] < threshold)
				normalizeMat[x][1] = x;

			if (histB.get(x, 0)[0] < threshold)
				normalizeMat[x][2] = x;
		}

		normalizeMat[255][0] = 255;
		normalizeMat[255][1] = 255;
		normalizeMat[255][2] = 255;

		// 7. Intervalos útiles
		double[] rInterval = normalizingInterval(column(normalizeMat, 0));
		double[] gInterval = normalizingInterval(column(normalizeMat, 1));
		double[] bInterval = normalizingInterval(column(normalizeMat, 2));

		double adjustRLow = rInterval[0];
		double adjustRHigh = rInterval[1];
		double adjustGLow = gInterval[0];
		double adjustGHigh = gInterval[1];
		double adjustBLow = bInterval[0];
		double adjustBHigh = bInterval[1];

		// 8. Hue shift sobre blanco
		double[] shifted = hueShiftRedVec(new double[]
		{ 1, 1, 1 }, hueShift);

		double shiftedR = shifted[0];
		double shiftedG = shifted[1];
		double shiftedB = shifted[2];

		// 9. Ganancias
		double redGain = 256.0 / (adjustRHigh - adjustRLow);
		double greenGain = 256.0 / (adjustGHigh - adjustGLow);
		double blueGain = 256.0 / (adjustBHigh - adjustBLow);

		// 10. Offsets
		double redOffset = (-adjustRLow / 256.0) * redGain;
		double greenOffset = (-adjustGLow / 256.0) * greenGain;
		double blueOffset = (-adjustBLow / 256.0) * blueGain;

		// 11. Ajuste final
		double adjustRed = shiftedR * redGain;
		double adjustRedGreen = shiftedG * redGain;
		double adjustRedBlue = shiftedB * redGain * BLUE_MAGIC_VALUE;

		// 12. Matriz final
		return new double[]
		{ adjustRed, adjustRedGreen, adjustRedBlue, 0, redOffset, 0, greenGain, 0, 0, greenOffset, 0, 0, blueGain, 0,
				blueOffset, 0, 0, 0, 1, 0 };
	}

	public static double[] getFilterMatrixOLD(Mat mat)
	{
		Mat resized = new Mat();
		// 1. Resize
		Imgproc.resize(mat, resized, new Size(256, 256));

		// 2. Promedio RGB
		Scalar mean = Core.mean(resized);
		double[] avg = new double[]
		{ mean.val[0], mean.val[1], mean.val[2] };

		// 3. Buscar hue shift
		double newAvgR = avg[0];
		int hueShift = 0;

		while (newAvgR < MIN_AVG_RED)
		{
			double[] shifted = hueShiftRedVec(avg, hueShift);

			newAvgR = shifted[0] + shifted[1] + shifted[2];
			hueShift++;

			if (hueShift > MAX_HUE_SHIFT)
			{
				newAvgR = MIN_AVG_RED;
			}
		}

		// 4. Aplicar hue shift a la imagen
		Mat shiftedMat = hueShiftRed(mat, hueShift);

		List<Mat> ch = new ArrayList<>();
		Core.split(shiftedMat, ch);

		Mat newR = new Mat();
		Core.add(ch.get(0), ch.get(1), newR);
		Core.add(newR, ch.get(2), newR);

		Core.min(newR, new Scalar(255), newR);
		Core.max(newR, new Scalar(0), newR);

		ch.set(0, newR);
		Core.merge(ch, mat);

		// 5. Histogramas
		Mat histR = new Mat();
		Mat histG = new Mat();
		Mat histB = new Mat();

		Imgproc.calcHist(Arrays.asList(mat), new MatOfInt(0), new Mat(), histR, new MatOfInt(256),
				new MatOfFloat(0, 256));
		Imgproc.calcHist(Arrays.asList(mat), new MatOfInt(1), new Mat(), histG, new MatOfInt(256),
				new MatOfFloat(0, 256));
		Imgproc.calcHist(Arrays.asList(mat), new MatOfInt(2), new Mat(), histB, new MatOfInt(256),
				new MatOfFloat(0, 256));

		double[][] normalizeMat = new double[256][3];

		double threshold = (mat.rows() * mat.cols()) / THRESHOLD_RATIO;

		for (int x = 0; x < 256; x++)
		{

			if (histR.get(x, 0)[0] < threshold)
				normalizeMat[x][0] = x;
			if (histG.get(x, 0)[0] < threshold)
				normalizeMat[x][1] = x;
			if (histB.get(x, 0)[0] < threshold)
				normalizeMat[x][2] = x;
		}

		normalizeMat[255][0] = 255;
		normalizeMat[255][1] = 255;
		normalizeMat[255][2] = 255;

		// 6. Intervalos
		double[] rInterval = normalizingInterval(column(normalizeMat, 0));
		double[] gInterval = normalizingInterval(column(normalizeMat, 1));
		double[] bInterval = normalizingInterval(column(normalizeMat, 2));

		double adjustRLow = rInterval[0];
		double adjustRHigh = rInterval[1];
		double adjustGLow = gInterval[0];
		double adjustGHigh = gInterval[1];
		double adjustBLow = bInterval[0];
		double adjustBHigh = bInterval[1];

		// 7. Hue shift sobre [1,1,1]
		Mat ones = new Mat(1, 1, CvType.CV_32FC3);
		ones.put(0, 0, 1, 1, 1);

		Mat shifted = hueShiftRed(ones, hueShift);
		float[] vals = new float[3];
		shifted.get(0, 0, vals);

		double shiftedR = vals[2];
		double shiftedG = vals[1];
		double shiftedB = vals[0];

		// 8. Gains
		double redGain = 256.0 / (adjustRHigh - adjustRLow);
		double greenGain = 256.0 / (adjustGHigh - adjustGLow);
		double blueGain = 256.0 / (adjustBHigh - adjustBLow);

		// 9. Offsets
		double redOffset = (-adjustRLow / 256.0) * redGain;
		double greenOffset = (-adjustGLow / 256.0) * greenGain;
		double blueOffset = (-adjustBLow / 256.0) * blueGain;

		// 10. Ajustes finales
		double adjustRed = shiftedR * redGain;
		double adjustRedGreen = shiftedG * redGain;
		double adjustRedBlue = shiftedB * redGain * BLUE_MAGIC_VALUE;

		// 11. Resultado
		return new double[]
		{ adjustRed, adjustRedGreen, adjustRedBlue, 0, redOffset, 0, greenGain, 0, 0, greenOffset, 0, 0, blueGain, 0,
				blueOffset, 0, 0, 0, 1, 0 };
	}

	private static double[] column(double[][] matrix, int col)
	{

		double[] result = new double[matrix.length];

		for (int i = 0; i < matrix.length; i++)
		{
			result[i] = matrix[i][col];
		}

		return result;
	}

	private Mat buildTransformMatrix(double[] f)
	{

		Mat m = new Mat(3, 3, CvType.CV_32F);

		m.put(0, 0, f[0], f[1], f[2], // R'
				0, f[6], 0, // G'
				0, 0, f[12] // B'
		);

		return m;
	}

	private Scalar buildBias(double[] f)
	{
		return new Scalar(f[14] * 255, // B
				f[9] * 255, // G
				f[4] * 255 // R
		);
	}
	
	private static void lalala(double h, Mat mat, Mat result) {
		if (
			    (mat.rows() == 1 && mat.cols() == 1 && mat.channels() == 3) ||
			    (mat.rows() == 1 && mat.cols() == 3 && mat.channels() == 1)
			) {
			    float[] vals = new float[3];
			    result.get(0, 0, vals);

			    System.out.print("h=" + h + " -> ");

			    for (int i = 0; i < vals.length; i++) {
			        System.out.print(String.format("%.10f", vals[i]));
			        if (i < vals.length - 1) System.out.print(",");
			    }

			    System.out.println();
			}
	}

}
