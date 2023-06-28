package graphics;

import java.util.Random;

public enum MyReadableColor {
    BLACK, RED, GREEN, BLUE, AQUA, YELLOW, ORANGE, PURPLE;

    public static final MyReadableColor[] colorByNumber = {BLACK, RED, GREEN, BLUE, AQUA, YELLOW, ORANGE, PURPLE, };

    public static MyReadableColor getRandomColor() {
        int colorNumber = new Random().nextInt(colorByNumber.length);
        return colorByNumber[colorNumber];
    }
}
