package entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data structure for Coordinates
 */
public class Coordinates implements Comparable<Coordinates> {
    /**
     * X coordinate field
     */
    @NotNull
    private final int x; //Поле не может быть null
    /**
     * Y coordinate field, that can't be greater than {@code MIN_Y}
     */
    @Min(value = MIN_Y+1, message = "Y coordinate must be greater than -540")
    private final int y; //Значение поля должно быть больше -540, Поле не может быть null
    /**
     * Constant minimal Y coordinate field
     */
    private static final int MIN_Y = -540;

    /**
     * Constructs new instance by given X and Y.
     *
     * @param x parsed x field
     * @param y parsed y field
     */
    public Coordinates (int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return Y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @return minimal Y coordinate
     */
    public static int getMIN_Y() {
        return MIN_Y;
    }

    /**
     * Overrides {@code toString} of {@code Object}.
     *
     * @return string representation of Coordinate
     */
    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }

    @Override
    public int compareTo(Coordinates o) {
        return Integer.max(Integer.compare(this.getX(), o.getX()), Integer.compare(this.getY(), o.getY()));
    }
}

