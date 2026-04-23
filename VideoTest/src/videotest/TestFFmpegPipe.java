package videotest;

import java.io.OutputStream;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class TestFFmpegPipe {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception {

        int width = 640;
        int height = 480;
        int fps = 30;

        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-y",
                "-f", "rawvideo",
                "-pixel_format", "bgr24",
                "-video_size", width + "x" + height,
                "-framerate", String.valueOf(fps),
                "-i", "-",
                "-c:v", "libx264",
                "-pix_fmt", "yuv420p",
                "test_output.mp4"
        );

        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process process = pb.start();

        OutputStream os = process.getOutputStream();

        Mat frame = new Mat(height, width, CvType.CV_8UC3);

        for (int i = 0; i < fps * 5; i++) { // 5 segundos

            // generar imagen artificial (gradiente rojo)
            frame.setTo(new Scalar(0, 0, i % 255));

            byte[] data = new byte[(int) (frame.total() * frame.channels())];
            frame.get(0, 0, data);

            os.write(data);
        }

        os.close();
        process.waitFor();

        System.out.println("Video generado: test_output.mp4");
    }
}