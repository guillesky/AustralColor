package videotest;

import org.opencv.core.*;
import org.opencv.videoio.*;

import java.io.OutputStream;
import java.util.*;

public class VideoProcessToFFmpeg {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception {

        String input = "input.mp4";
        String output = "output.mp4";

        VideoCapture cap = new VideoCapture(input);

        if (!cap.isOpened()) {
            System.out.println("No se pudo abrir el video");
            return;
        }

        int width = (int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int height = (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        double fps = cap.get(Videoio.CAP_PROP_FPS);

        System.out.println("Resolución: " + width + "x" + height);
        System.out.println("FPS: " + fps);

        // ===== CONFIGURACIÓN FFMPEG =====
     
        
        
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-y",

                // ===== VIDEO DESDE JAVA =====
                "-f", "rawvideo",
                "-pixel_format", "bgr24",
                "-video_size", width + "x" + height,
                "-framerate", String.valueOf(fps),
                "-i", "-",   // stdin (frames procesados)

                // ===== AUDIO DESDE ARCHIVO ORIGINAL =====
                "-i", input,

                // ===== MAPEO =====
                "-map", "0:v:0",  // video del pipe
                "-map", "1:a:0",  // audio del archivo

                // ===== CODECS =====
                "-c:v", "libx264",
                "-pix_fmt", "yuv420p",
                "-c:a", "copy",

                // ===== OUTPUT =====
                output
        );

        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
       
        Process process = pb.start();

        OutputStream ffmpegInput = process.getOutputStream();

        Mat frame = new Mat();
        int frameCount = 0;

        while (cap.read(frame)) {

            frameCount++;

            // ===== PROCESAMIENTO: ROJO AL MÁXIMO =====
            List<Mat> channels = new ArrayList<>();
            Core.split(frame, channels);

            Mat red = channels.get(2); // BGR → rojo = índice 2
            red.setTo(new Scalar(255));

            Core.merge(channels, frame);
            // =======================================

            // convertir Mat → bytes
            byte[] data = new byte[(int) (frame.total() * frame.channels())];
            frame.get(0, 0, data);

            // enviar a FFmpeg
            ffmpegInput.write(data);

            if (frameCount % 30 == 0) {
                System.out.println("Frames procesados: " + frameCount);
            }
        }

        ffmpegInput.close();
        cap.release();

        int exitCode = process.waitFor();

        System.out.println("Proceso terminado");
        System.out.println("Frames totales: " + frameCount);
        System.out.println("FFmpeg exit code: " + exitCode);
    }
}