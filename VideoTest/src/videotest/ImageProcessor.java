package videotest;

import java.awt.image.BufferedImage;
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

public class ImageProcessor
{
	private static final int THRESHOLD_RATIO = 2000;
	private static final int MIN_AVG_RED = 60;
	private static final int MAX_HUE_SHIFT = 120;
	private static final double BLUE_MAGIC_VALUE = 1.2;

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
		// debugHueShift(h,mat,dst);
		r.release();
		g.release();
		b.release();
		return dst;
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

	public static double[] getFilterMatrix(Mat inputRgb)
	{

		// 1) Trabajar sobre copia redimensionada
		Mat matResized = new Mat();
		Imgproc.resize(inputRgb, matResized, new Size(256, 256));

		// Pasar a float para evitar problemas al multiplicar
		matResized.convertTo(matResized, CvType.CV_32FC3);

		// 2) Promedio RGB
		Scalar mean = Core.mean(matResized);

		Mat avgMat = new Mat(1, 1, CvType.CV_32FC3);

		avgMat.put(0, 0, (int) mean.val[0], (int) mean.val[1], (int) mean.val[2]);

		// 3) Buscar hue shift
		double newAvgR = mean.val[0];
		int hueShift = 0;

		while (newAvgR < MIN_AVG_RED)
		{

			Mat shiftedAvg = hueShiftRed(avgMat, hueShift);
			
			float[] avgVals = new float[3];
			shiftedAvg.get(0, 0, avgVals);

			newAvgR = avgVals[0] + avgVals[1] + avgVals[2];

			hueShift++;

			if (hueShift > MAX_HUE_SHIFT)
			{
				newAvgR = MIN_AVG_RED;
			}
			shiftedAvg.release();
		}
		
		// 4) Aplicar hue shift a la imagen

		Mat shiftedMat = hueShiftRed(matResized, hueShift);
		/*
		 * System.out.println("shiftedMat"); System.out.println("pixel[0,0]: " +
		 * Arrays.toString(shiftedMat.get(0,0))); System.out.println("pixel[100,100]: "
		 * + Arrays.toString(shiftedMat.get(100,100)));
		 * System.out.println("pixel[200,200]: " +
		 * Arrays.toString(shiftedMat.get(200,200)));
		 */

		List<Mat> channels = new ArrayList<>();
		Core.split(shiftedMat, channels);

		Mat newR = new Mat();

		Core.add(channels.get(0), channels.get(1), newR);
		Core.add(newR, channels.get(2), newR);

		Core.min(newR, new Scalar(255), newR);
		Core.max(newR, new Scalar(0), newR);

		// Reemplazar canal rojo (RGB → índice 0)

		Core.split(matResized, channels);

		channels.set(0, newR);
		/*
		 * System.out.println("newR"); System.out.println("pixel[0,0]: " +
		 * Arrays.toString(newR.get(0,0))); System.out.println("pixel[100,100]: " +
		 * Arrays.toString(newR.get(100,100))); System.out.println("pixel[200,200]: " +
		 * Arrays.toString(newR.get(200,200)));
		 */
		// Mat mmm=new Mat();

		Core.merge(channels, matResized);

		// 5) Histogramas
		Mat histR = new Mat();
		Mat histG = new Mat();
		Mat histB = new Mat();

		Imgproc.calcHist(Arrays.asList(matResized), new MatOfInt(0), new Mat(), histR, new MatOfInt(256),
				new MatOfFloat(0, 256));

		Imgproc.calcHist(Arrays.asList(matResized), new MatOfInt(1), new Mat(), histG, new MatOfInt(256),
				new MatOfFloat(0, 256));

		Imgproc.calcHist(Arrays.asList(matResized), new MatOfInt(2), new Mat(), histB, new MatOfInt(256),
				new MatOfFloat(0, 256));
		/*
		 * System.out.println("mmm"); System.out.println("pixel[0,0]: " +
		 * Arrays.toString(matResized.get(0,0))); System.out.println("pixel[100,100]: "
		 * + Arrays.toString(matResized.get(100,100)));
		 * System.out.println("pixel[200,200]: " +
		 * Arrays.toString(matResized.get(200,200)));
		 */
		double[][] normalizeMat = new double[256][3];

		double threshold = (matResized.rows() * matResized.cols()) / (double) THRESHOLD_RATIO;
/*
		debugMat("histR", histR);
		debugMat("histG", histG);
		debugMat("histB", histB);
*/
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

		
		shiftedOnes.release();
		histR.release();
		histG.release();
		histB.release();
		shiftedMat.release();
		matResized.release();
		avgMat.release();
		newR.release();
		
		// 11) Resultado
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

	private static void debugHueShift(double h, Mat mat, Mat result)
	{
		if ((mat.rows() == 1 && mat.cols() == 1 && mat.channels() == 3)
				|| (mat.rows() == 1 && mat.cols() == 3 && mat.channels() == 1))
		{
			float[] vals = new float[3];
			result.get(0, 0, vals);

			System.out.print("h=" + h + " -> ");

			for (int i = 0; i < vals.length; i++)
			{
				System.out.print(String.format("%.10f", vals[i]));
				if (i < vals.length - 1)
					System.out.print(",");
			}

			System.out.println();
		}
	}

	private static void debugMat(String variableName, Mat mat)
	{
		System.out.print(variableName + " width: " + mat.width() + " height: " + mat.height() + " = [");
		for (int j = 0; j < mat.cols(); j++)
		{
			for (int i = 0; i < mat.rows(); i++)
			{
				double value = mat.get(i, j)[0];
				System.out.print((int) value);
				if (i < mat.rows() - 1)
					System.out.print(", ");
			}
			System.out.println("]");
		}
	}
	
	
	
	public static BufferedImage matToBufferedImage(Mat mat)
	{

		int type = BufferedImage.TYPE_3BYTE_BGR;
		byte[] b = new byte[(int) (mat.total() * mat.channels())];
		mat.get(0, 0, b);

		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), b);

		return image;
	}

	public static Mat correct(Mat mat)
	{

		// calcular filtro
		double[] filterMatrix = ImageProcessor.getFilterMatrix(mat);

		// aplicar filtro
		Mat correctedMat = applyFilter(mat, filterMatrix);

		// convertir RGB → BGR
		// Imgproc.cvtColor(correctedMat, correctedMat, Imgproc.COLOR_RGB2BGR);

		return correctedMat;
	}

	private static Mat applyFilter(Mat mat, double[] filt)
	{

		Mat matFloat = new Mat();
		mat.convertTo(matFloat, CvType.CV_32F);

		// transformación lineal
		Mat transform = buildTransformMatrix(filt);

		Mat result = new Mat();
		Core.transform(matFloat, result, transform);

		// bias (offset)
		Scalar bias = buildBias(filt);
		Core.add(result, bias, result);

		// clip
		Core.min(result, new Scalar(255, 255, 255), result);
		Core.max(result, new Scalar(0, 0, 0), result);

		// volver a 8 bits
		result.convertTo(result, CvType.CV_8U);
		transform.release();
		matFloat.release();
		return result;
	}

	private static Mat buildTransformMatrix(double[] f)
	{

		Mat m = new Mat(3, 3, CvType.CV_32F);

		m.put(0, 0, f[0], f[1], f[2], // R'
				0, f[6], 0, // G'
				0, 0, f[12] // B'
		);

		return m;
	}

	private static Scalar buildBias(double[] f)
	{
		return new Scalar(f[4] * 255.0, // B
				f[9] * 255.0, // G
				f[14] * 255.0 // R
		);
	}

	private static Mat applyFilterRGB(Mat mat, double[] f)
	{
		// Convertir a float
		Mat matFloat = new Mat();
		mat.convertTo(matFloat, CvType.CV_32FC3);

		List<Mat> ch = new ArrayList<>();
		Core.split(matFloat, ch);

		// ⚠️ ASUMIMOS RGB
		Mat r = ch.get(0);
		Mat g = ch.get(1);
		Mat b = ch.get(2);

		Mat newR = new Mat();
		Mat newG = new Mat();
		Mat newB = new Mat();

		// R' = R*f0 + G*f1 + B*f2 + bias
		Core.multiply(r, new Scalar(f[0]), newR);
		Core.add(newR, multiply(g, f[1]), newR);
		Core.add(newR, multiply(b, f[2]), newR);
		Core.add(newR, new Scalar(f[4] * 255.0), newR);

		// G' = G*f6 + bias
		Core.multiply(g, new Scalar(f[6]), newG);
		Core.add(newG, new Scalar(f[9] * 255.0), newG);

		// B' = B*f12 + bias
		Core.multiply(b, new Scalar(f[12]), newB);
		Core.add(newB, new Scalar(f[14] * 255.0), newB);

		// Merge en RGB
		List<Mat> resultCh = Arrays.asList(newR, newG, newB);
		Mat result = new Mat();
		Core.merge(resultCh, result);

		// Clip 0–255

		Core.min(result, new Scalar(255.0, 255.0, 255.0, 255.0), result);
		Core.max(result, new Scalar(0.0), result);

		// Volver a 8 bits
		result.convertTo(result, CvType.CV_8UC3);
		return result;
	}

	private static Mat multiply(Mat src, double val)
	{
		Mat dst = new Mat();
		Core.multiply(src, new Scalar(val), dst);
		return dst;
	}

	
	
}
