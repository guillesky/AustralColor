package videotest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class VideoTestFrames
{

	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	 public static void main(String[] args) {

	        String input = "input.mp4";
	        String outputDir = "frames";

	        // Crear carpeta si no existe
	        File dir = new File(outputDir);
	        if (!dir.exists()) {
	            boolean created = dir.mkdirs();
	            if (!created) {
	                System.out.println("No se pudo crear la carpeta de salida");
	                return;
	            }
	        }

	        VideoCapture cap = new VideoCapture(input);

	        if (!cap.isOpened()) {
	            System.out.println("No se pudo abrir el video de entrada");
	            return;
	        }

	        System.out.println("Backend captura: " + cap.getBackendName());

	        double fps = cap.get(Videoio.CAP_PROP_FPS);
	        int width = (int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH);
	        int height = (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT);

	        System.out.println("FPS: " + fps);
	        System.out.println("Resolución: " + width + "x" + height);

	        Mat frame = new Mat();
	        int frameCount = 0;

	        // Parámetros de calidad JPG
	        MatOfInt params = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 95);

	        while (cap.read(frame)) {

	            frameCount++;

	            // ===== PROCESAMIENTO (TEST: saturar rojo) =====
	            List<Mat> channels = new ArrayList<>();
	            Core.split(frame, channels);

	            Mat red = channels.get(2); // BGR → rojo = índice 2
	            red.setTo(new Scalar(255));

	            Core.merge(channels, frame);
	            // ============================================

	            String filename = String.format("%s/frame_%05d.jpg", outputDir, frameCount);

	            boolean ok = Imgcodecs.imwrite(filename, frame, params);

	            if (!ok) {
	                System.out.println("Error guardando: " + filename);
	            }

	            if (frameCount % 30 == 0) {
	                System.out.println("Procesados: " + frameCount + " frames");
	            }
	        }

	        cap.release();

	        System.out.println("Proceso terminado");
	        System.out.println("Total frames: " + frameCount);
	    }

}