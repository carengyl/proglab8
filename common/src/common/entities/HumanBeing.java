package common.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Data structure for HumanBeing.
 *
 * @author carengyl
 */
@XStreamAlias("humanBeing")
public class HumanBeing implements Comparable<HumanBeing>, Serializable {

    /**
     * Current ID field for ID generation
     */
    private static Long currentId = 1L;

    /**
     * Sets current ID to greater than maximal in collection
     *
     * @param maxId max ID in collection
     */
    public static void setCurrentId(long maxId) {
        currentId = maxId + 1;
    }
    /**
     * ID field
     */
    @NotNull
    @Positive
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /**
     * Name field
     */
    @NotNull
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * Coordinates field
     */
    @NotNull
    private Coordinates coordinates; //Поле не может быть null
    /**
     * Creation date field
     */
    @NotNull
    @PastOrPresent
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**
     * Real hero field
     */
    @NotNull
    private boolean realHero; //Поле не может быть null
    /**
     * Has toothpick field
     */
    private boolean hasToothpick;
    /**
     * Impact speed field
     */
    private Double impactSpeed; //Значение поля должно быть больше -611, Поле может быть null
    /**
     * Minimal Impact speed constant
     */
    private static final double MIN_IMPACT_SPEED = -611;

    /**
     * Checks that {@code impactSpeed} is greater than {@code MIN_IMPACT_SPEED}
     *
     * @return true if that
     */
    @AssertTrue(message = "impactSpeed must be greater than -611")
    private boolean checkImpactSpeed() {
        if (impactSpeed != null) {
            return impactSpeed > MIN_IMPACT_SPEED;
        } else {
            return true;
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Minutes of waiting field
     */
    private Double minutesOfWaiting; //Поле может быть null
    /**
     * Weapon type field
     */
    @NotNull
    private WeaponType weaponType; //Поле не может быть null
    /**
     * Mood field
     */
    @NotNull
    private Mood mood; //Поле не может быть null
    /**
     * Car field
     */
    private Car car; //Поле может быть null

    /**
     * Constructs new instance with known id.
     * Creates existing Human being from file with existing id.
     *
     * @param id id of Human being from file.
     */
    public HumanBeing(long id) {
        checkImpactSpeed();
        this.id = id;
    }

    /**
     * Constructs new instance of Human being.
     * Uses currentId field to generate new id, also generates creationDate field.
     */
    public HumanBeing() {
        checkImpactSpeed();
        creationDate = LocalDate.now();
        this.id = currentId++;
    }

    /**
     * @return id field
     */
    public Long getId() {
        return id;
    }


    /**
     * @return min impact speed
     */
    public static Double getMinImpactSpeed() {
        return MIN_IMPACT_SPEED;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Sets name field
     *
     * @param name parsed name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets coordinates
     *
     * @param coordinates parsed coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * @return Coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Sets real hero
     *
     * @param realHero parsed real hero
     */
    public void setRealHero(Boolean realHero) {
        this.realHero = realHero;
    }

    public boolean isRealHero() {
        return realHero;
    }

    /**
     * Sets has toothpick
     *
     * @param hasToothpick parsed has toothpick
     */
    public void setHasToothpick(boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    public boolean isHasToothpick() {
        return hasToothpick;
    }

    /**
     * Sets impact speed
     *
     * @param impactSpeed parsed impact speed
     */
    public void setImpactSpeed(Double impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    public Double getImpactSpeed() {
        return impactSpeed;
    }

    /**
     * Sets minutes of waiting
     *
     * @param minutesOfWaiting parsed minutes of waiting
     */
    public void setMinutesOfWaiting(Double minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }
    /**
     * @return minutes of waiting
     */
    public Double getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    /**
     * Sets weapon type
     *
     * @param weaponType parsed weapon type
     */
    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * Sets mood
     *
     * @param mood parsed mood
     */

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    /**
     * @return mood
     */
    public Mood getMood() {
        return mood;
    }

    /**
     * Sets car
     *
     * @param car parsed car
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * @return car
     */
    public Car getCar() {
        return car;
    }

    /**
     * Compares this HumanBeing to another given by Coordinates
     *
     * @param humanBeing given HumanBeing
     * @return 1 if greater, -1 if less, 0 if equal
     */
    public int compareTo(HumanBeing humanBeing) {
        return coordinates.compareTo(humanBeing.getCoordinates());
    }

    public static int getNumberOfFields() {
        return HumanBeing.class.getDeclaredFields().length + Coordinates.class.getDeclaredFields().length + Car.class.getDeclaredFields().length - 8;
    }

    /**
     * Overrides {@code toString} of {@code Object}.
     *
     * @return string representation of Human Being
     */
    @Override
    public String toString() {
        return "ID: " + this.id
                + "; Name: " + this.name
                + "; Coordinates: " + this.coordinates
                + "; Created: " + creationDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                + "; " + ((this.realHero) ? "Hero" : "Villain")
                + ((this.hasToothpick) ? " with toothpick" : "")
                + "; Impact speed: " + ((this.impactSpeed == null) ? "-" : this.impactSpeed + " m/s")
                + "; Minutes of waiting: " + ((this.minutesOfWaiting == null) ? "-" : this.minutesOfWaiting + " min")
                + "; Weapon: " + this.weaponType
                + "; Mood: " + this.mood
                + "; " + ((this.car == null) ? "Has no car": "Transport: " + this.car);
    }
}
