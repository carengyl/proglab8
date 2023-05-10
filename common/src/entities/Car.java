package entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


/**
 * Data structure for Car
 *
 * @author carengyl
 */
public class Car {
    /**
     * Cool field
     */
    @NotNull
    private final boolean cool;
    /**
     * Name field
     */
    @NotNull
    private final String name;
    /**
     * Cost field
     */
    @NotNull
    @Positive
    private final int cost;
    /**
     * Horse powers field
     */
    @NotNull
    @Positive
    private final int horsePowers;
    /**
     * Car brand field
     */
    @NotNull
    private final CarBrand carBrand;

    /**
     * Constructs new instance from passed arguments.
     *
     * @param name validated Name
     * @param cost validated Cost
     * @param horsePowers validated Horse powers
     * @param carBrand Car brand
     */
    public Car(String name, int cost, int horsePowers, CarBrand carBrand) {
        this.name = name;
        this.cost = cost;
        this.horsePowers = horsePowers;
        this.carBrand = carBrand;

        cool = (cost >= 10000000) | (horsePowers > 440) | carBrand.isCool();
    }

    public static int getNumberOfFields() {
        return Car.class.getDeclaredFields().length - 1;
    }

    /**
     * @return Cool field
     */
    public boolean isCool() {
        return cool;
    }

    /**
     * Overrides {@code toString} of {@code Object}.
     *
     * @return string representation of Car
     */
    @Override
    public String toString() {
        return ((cool) ? "Cool ": "Not so cool ") + "car: "
                + "Name: " + name
                + "; Cost: " + cost + " tugriks"
                + "; Power: " + horsePowers + " h.p."
                + "; Brand: " + carBrand;
    }
}
