package vista_Progress;

class VideoItem {
    String archivo;
    int progreso;
    String estado;

    public VideoItem(String archivo) {
        this.archivo = archivo;
        this.progreso = 0;
        this.estado = "En cola";
    }
}
