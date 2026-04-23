package videotest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ClaseImagen
{
	public static byte[] correctImage(String inputPath, String outputPath) throws IOException
	{

		// ===== 1. Leer imagen =====
		BufferedImage image = ImageIO.read(new File(inputPath));

		// Convertir a Mat (BGR)
		Mat mat = bufferedImageToMat(image);

		// ===== 2. Convertir a RGB =====
		Mat rgbMat = new Mat();
		Imgproc.cvtColor(mat, rgbMat, Imgproc.COLOR_BGR2RGB);

		// ===== 3. Corregir =====
		Mat correctedMat = correct(rgbMat);

		// ===== 4. Guardar imagen =====
		Mat correctedBGR = new Mat();
		Imgproc.cvtColor(correctedMat, correctedBGR, Imgproc.COLOR_RGB2BGR);

		BufferedImage outputImage = matToBufferedImage(correctedBGR);
		ImageIO.write(outputImage, "jpg", new File(outputPath));

		// ===== 5. Preview =====
		Mat preview = mat.clone();

		int width = preview.cols() / 2;

		Rect rightHalf = new Rect(width, 0, width, preview.rows());
		correctedMat.submat(0, preview.rows(), width, preview.cols()).copyTo(preview.submat(rightHalf));

		// resize
		Mat resized = new Mat();
		Imgproc.resize(preview, resized, new Size(960, 540));

		// ===== 6. Encode PNG a bytes =====
		MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(".png", resized, mob);

		return mob.toArray();
	}

	public static Mat bufferedImageToMat(BufferedImage bi)
	{

		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);

		int[] data = bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), null, 0, bi.getWidth());

		byte[] bytes = new byte[bi.getWidth() * bi.getHeight() * 3];

		for (int i = 0; i < data.length; i++)
		{
			int val = data[i];
			bytes[i * 3] = (byte) (val & 0xFF); // B
			bytes[i * 3 + 1] = (byte) ((val >> 8) & 0xFF); // G
			bytes[i * 3 + 2] = (byte) ((val >> 16) & 0xFF); // R
		}

		mat.put(0, 0, bytes);
		double[] datag = mat.get(140, 1000);

		System.out.println(datag[0]);
		System.out.println(datag[1]);
		System.out.println(datag[2]);

		return mat;
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

		// copiar imagen original
		Mat originalMat = mat.clone();

		// calcular filtro
		double[] filterMatrix = ImageAnalizer.getFilterMatrix(mat);

		// aplicar filtro
		Mat correctedMat = applyFilter(originalMat, filterMatrix);

		// convertir RGB → BGR
		Imgproc.cvtColor(correctedMat, correctedMat, Imgproc.COLOR_RGB2BGR);

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
		Core.min(result, new Scalar(255), result);
		Core.max(result, new Scalar(0), result);

		// volver a 8 bits
		result.convertTo(result, CvType.CV_8U);

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
		return new Scalar(f[14] * 255, // B
				f[9] * 255, // G
				f[4] * 255 // R
		);
	}
}
