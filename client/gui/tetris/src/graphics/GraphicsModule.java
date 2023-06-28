package graphics;

public interface GraphicsModule {
    void draw();

    boolean isCloseRequested();

    void destroy();

    void sync(int fps);
}
