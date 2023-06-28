package graphics;

import java.util.Random;

public enum ReadableColor {
    BLACK, RED, GREEN, BLUE, AQUA, YELLOW, ORANGE, PURPLE;

    public static final ReadableColor[] colorByNumber = {BLACK, RED, GREEN, BLUE, AQUA, YELLOW, ORANGE, PURPLE};

    public static ReadableColor getRandomColor() {
        int colorNumber = new Random().nextInt(colorByNumber.length);
        return colorByNumber[colorNumber];
    }
}
