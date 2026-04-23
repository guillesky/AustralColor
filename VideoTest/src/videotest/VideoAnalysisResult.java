package videotest;

import java.util.List;

class VideoAnalysisResult {
    String inputPath;
    String outputPath;
    int fps;
    int frameCount;
    List<double[]> filters;
    List<Integer> filterIndices;
}
