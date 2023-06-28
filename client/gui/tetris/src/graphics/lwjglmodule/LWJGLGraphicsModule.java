package graphics.lwjglmodule;

import graphics.GraphicsModule;
import graphics.MyReadableColor;
import main.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

import static org.lwjgl.opengl.GL11.*;

public class LWJGLGraphicsModule implements GraphicsModule {
    public LWJGLGraphicsModule() {
        initOpengl();
    }

    private void initOpengl() {
        try {
            Display.setDisplayMode(new DisplayMode(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
            Display.setTitle(Constants.SCREEN_NAME);
            Display.create();
        } catch (LWJGLException e) {
            ErrorCatcher.graphicsFailure(e);
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Constants.SCREEN_WIDTH, 0, Constants.SCREEN_HEIGHT, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        glEnable(GL_TEXTURE_2D);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glClearColor(1,1,1,1);
    }

    private void drawCell(int x, int y,  Color color) {
        glColor3b(color.getRedByte(), color.getGreenByte(), color.getBlueByte());

        glBegin(GL_QUADS);
        glTexCoord2f(0,0);
        glVertex2f(x,y+ Constants.CELL_SIZE);
        glTexCoord2f(1,0);
        glVertex2f(x + Constants.CELL_SIZE,y+ Constants.CELL_SIZE);
        glTexCoord2f(1,1);
        glVertex2f(x + Constants.CELL_SIZE, y);
        glTexCoord2f(0,1);
        glVertex2f(x, y);
        glEnd();
    }
    @Override
    public void draw(GameField field) {
        glClear(GL_COLOR_BUFFER_BIT);

        for (int x = 0; x < Constants.COUNT_CELLS_X; x++) {
            for (int y = 0; y < Constants.COUNT_CELLS_Y; y++) {
                MyReadableColor color = field.getColor(x, y);
                drawCell(x*Constants.CELL_SIZE, y*Constants.CELL_SIZE, convertColor(color));
            }
        }

        Figure figure = field.getFigure();

        MyReadableColor color = figure.getColor();
        for (Coordinates coordinates: figure.getCoordinates()) {
            drawCell(coordinates.x*Constants.CELL_SIZE, coordinates.y*Constants.CELL_SIZE, convertColor(color));
        }

        Display.update();
    }

    private Color convertColor(MyReadableColor color) {
        switch (color) {
            case RED -> {
                return new Color(ReadableColor.RED);
            }
            case GREEN -> {
                return new Color(ReadableColor.GREEN);
            }
            case BLUE -> {
                return new Color(ReadableColor.BLUE);
            }
            case AQUA -> {
                return new Color(ReadableColor.CYAN);
            }
            case YELLOW -> {
                return new Color(ReadableColor.YELLOW);
            }
            case ORANGE -> {
                return new Color(ReadableColor.ORANGE);
            }
            case PURPLE -> {
                return new Color(ReadableColor.PURPLE);
            }
            case BLACK -> {
                return new Color(ReadableColor.BLACK);
            }
            default -> {
                return new Color(ReadableColor.WHITE);
            }
        }
    }

    @Override
    public boolean isCloseRequested() {
        return false;
    }

    @Override
    public void destroy() {
        Display.destroy();
    }

    @Override
    public void sync(int fps) {
        Display.sync(fps);
    }
}
