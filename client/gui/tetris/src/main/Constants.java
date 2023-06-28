package main;

import graphics.ReadableColor;

public class Constants {
    public static final int CELL_SIZE = 24;

    public static final int COUNT_CELLS_X = 10;
    public static final int COUNT_CELLS_Y = COUNT_CELLS_X * 2;

    public static final int OFFSET_TOP = 3;

    public static final int SCREEN_WIDTH = COUNT_CELLS_X * CELL_SIZE;
    public static final int SCREEN_HEIGHT = COUNT_CELLS_Y * CELL_SIZE;
    public static final String SCREEN_NAME = "Tetris";

    public static final int FPS = 60;

    public static final int BOOST_MULTIPLIER = 5;
    public static final int MOVE_DOWN_PER_SECOND = 3;
    public static final int FRAMES_PER_MOVE = FPS / MOVE_DOWN_PER_SECOND;

    public static final ReadableColor EMPTINESS_COLOR = ReadableColor.BLACK;
    public static final int BLOCKS_INITIAL_LEVEL = COUNT_CELLS_Y / 3;
    public static final int MISSING_BLOCKS_IN_INITIAL_LINE_MIN = COUNT_CELLS_X/3;
    public static final int MISSING_BLOCKS_IN_INITIAL_LINE_MAX = COUNT_CELLS_X/2;

    public static final int MAX_FIGURE_WIDTH = 4;
}
