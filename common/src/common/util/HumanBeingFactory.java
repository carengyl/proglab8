package common.util;

import common.entities.*;
import common.exceptions.NoUserInputException;
import common.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class responsible for creating Human being
 */

public class HumanBeingFactory {
    private final HumanBeing createdHumanBeing;
    private final Scanner scanner = new Scanner(System.in);

    public HumanBeingFactory(long id) {
        createdHumanBeing = new HumanBeing(id);
    }

    public HumanBeingFactory() {
        createdHumanBeing = new HumanBeing();
    }

    private void setName() throws NoUserInputException {
        String name = Validators.validateStringInput("Enter human's name", false, scanner);
        createdHumanBeing.setName(name);
    }

    private void setName(String name) throws ValidationException {
        createdHumanBeing.setName(Validators.validateArg(Objects::nonNull,
                "This variable can't be null",
                String::toString,
                name));
    }

    private void setCoordinates() throws NoUserInputException {
        createdHumanBeing.setCoordinates(new Coordinates(setX(), setY()));
    }

    private void setCoordinates(String x, String y) throws ValidationException {
        createdHumanBeing.setCoordinates(new Coordinates(this.setX(x), this.setY(y)));
    }

    private Integer setY() throws NoUserInputException {
        return Validators.validateInput(arg -> ((int) arg) > Coordinates.getMIN_Y(),
                "Enter integer (long) Y coordinate, it should be greater than " + Coordinates.getMIN_Y(),
                "Expected long type number, try again:",
                "Y coordinate should be greater than " + Coordinates.getMIN_Y() + ", try again:",
                Integer::parseInt,
                false,
                scanner);
    }

    private Integer setY(String y) throws ValidationException {
        return Validators.validateArg(arg -> ((int) arg) > Coordinates.getMIN_Y(),
                "Y coordinate should be greater than " + Coordinates.getMIN_Y() + ", try again:",
                Integer::parseInt,
                y);
    }

    private Integer setX() throws NoUserInputException {
        return Validators.validateInput(arg -> true,
                "Enter integer (long) X",
                "Expected long type number, try again:",
                "",
                Integer::parseInt,
                false,
                scanner);
    }

    private Integer setX(String x) throws ValidationException {
        return Validators.validateArg(arg -> true,
                "",
                Integer::parseInt,
                x);
    }

    private void setRealHero() throws NoUserInputException {
        boolean realHero = Validators.validateBooleanInput("Is he/she a real hero", scanner);
        createdHumanBeing.setRealHero(realHero);
    }

    private void setRealHero(String realHero) throws ValidationException {
        createdHumanBeing.setRealHero(Validators.validateArg(arg -> true,
                "Expected boolean",
                Boolean::parseBoolean,
                realHero));
    }

    private void setHasToothpick() throws NoUserInputException {
        boolean hasToothpick = Validators.validateBooleanInput("Has a toothpick", scanner);
        createdHumanBeing.setHasToothpick(hasToothpick);
    }

    private void setHasToothpick(String hasToothPick) throws ValidationException {
        createdHumanBeing.setHasToothpick(Validators.validateArg(arg -> true,
                "Expected boolean",
                Boolean::parseBoolean,
                hasToothPick));
    }

    private void setImpactSpeed() throws NoUserInputException {
        Double impactSpeed = Validators.validateInput(arg -> ((Double) arg) > HumanBeing.getMinImpactSpeed(),
                "Enter real (double) impact speed m/s, it should be greater than " + HumanBeing.getMinImpactSpeed(),
                "Expected double type, try again:",
                "Impact speed should be greater than " + HumanBeing.getMinImpactSpeed() + ", try again:",
                Double::parseDouble,
                true,
                scanner);
        createdHumanBeing.setImpactSpeed(impactSpeed);
    }

    private void setImpactSpeed(String impactSpeed) throws ValidationException {
        Double parsedImpactSpeed;
        if (impactSpeed.equals("")) {
            parsedImpactSpeed = null;
        } else {
            parsedImpactSpeed = Validators.validateArg(arg -> (double) arg > HumanBeing.getMinImpactSpeed(),
                    "Impact speed should be greater than " + HumanBeing.getMinImpactSpeed(),
                    Double::parseDouble,
                    impactSpeed);
        }
        createdHumanBeing.setImpactSpeed(parsedImpactSpeed);
    }

    private void setMinutesOfWaitingSpeed() throws NoUserInputException {
        Double minutesOfWaiting = Validators.validateInput(arg -> true,
                "Enter real (double) minutes of waiting",
                "Expected double type, try again:",
                "",
                Double::parseDouble,
                true,
                scanner);
        createdHumanBeing.setMinutesOfWaiting(minutesOfWaiting);
    }

    private void setMinutesOfWaiting(String minutesOfWaiting) throws ValidationException {
        Double parsedMinutesOfWaiting;
        if (minutesOfWaiting.equals("")) {
           parsedMinutesOfWaiting = null;
        } else {
            parsedMinutesOfWaiting = Validators.validateArg(arg -> true,
                    "",
                    Double::parseDouble,
                    minutesOfWaiting);
        }
        createdHumanBeing.setMinutesOfWaiting(parsedMinutesOfWaiting);
    }

    private void setWeaponType() throws NoUserInputException {
        Integer weaponTypeNumber = Validators.validateEnumInput("Pick a weapon:\n" + WeaponType.show(),
                WeaponType.values().length,
                false,
                scanner);
        createdHumanBeing.setWeaponType(WeaponType.values()[weaponTypeNumber]);
    }

    private void setWeaponType(String weaponType) throws ValidationException {
        Integer weaponTypeNumber = Validators.validateArg(arg -> (((int) arg) <= WeaponType.values().length) && ((((int) arg) >= 1)),
                "",
                Integer::parseInt,
                weaponType);
        createdHumanBeing.setWeaponType(WeaponType.values()[weaponTypeNumber]);
    }

    private void setMood() throws NoUserInputException {
        Integer moodNumber = Validators.validateEnumInput("Pick a mood:\n" + Mood.show(),
                Mood.values().length,
                false,
                scanner);
        createdHumanBeing.setMood(Mood.values()[moodNumber]);
    }

    private void setMood(String mood) throws ValidationException {
        Integer moodNumber = Validators.validateArg(arg -> (((int) arg) <= Mood.values().length) && ((((int) arg) >= 1)),
                "",
                Integer::parseInt,
                mood);
        createdHumanBeing.setMood(Mood.values()[moodNumber]);
    }

    private void setCar() throws NoUserInputException {
        String carName = Validators.validateStringInput("Enter car's name, if skipped, whole car creation will be skipped",
                true,
                scanner);
        if (carName == null) {
            createdHumanBeing.setCar(null);
        } else {
            int cost = setCarCost();
            int horsePower = setCarHorsePowers();
            CarBrand carBrand = setCarBrand();
            createdHumanBeing.setCar(new Car(carName, cost, horsePower, carBrand));
        }
    }

    private void setCar(List<String> carData) throws ValidationException {
        if (Objects.equals(carData.get(0), "")) {
            createdHumanBeing.setCar(null);
        } else {
            int cost = setCarCost(carData.get(1));
            int horsePower = setCarHorsePowers(carData.get(2));
            CarBrand carBrand = CarBrand.values()[setCarBrand(carData.get(3))];
            createdHumanBeing.setCar(new Car(carData.get(0), cost, horsePower, carBrand));
        }
    }

    private Integer setCarCost() throws NoUserInputException {
        return Validators.validateInput(arg -> ((int) arg) > 0,
                "Enter car's cost in tugriks (positive integer)",
                "Expected integer type, try again:",
                "Car's cost must be positive number, try again:",
                Integer::parseInt,
                false,
                scanner);
    }

    private int setCarCost(String cost) throws ValidationException {
        return Validators.validateArg(arg -> true,
                "",
                Integer::parseInt,
                cost);
    }

    private Integer setCarHorsePowers() throws NoUserInputException {
        return Validators.validateInput(arg -> ((int) arg) > 0,
                "Enter car's engine power in h.p. (positive integer):",
                "Expected integer type, try again:",
                "Engine power must be positive number, try again:",
                Integer::parseInt,
                false,
                scanner);
    }

    private int setCarHorsePowers(String horsePowers) throws ValidationException {
        return Validators.validateArg(arg -> true,
                "",
                Integer::parseInt,
                horsePowers);
    }

    private CarBrand setCarBrand() throws NoUserInputException {
        Integer carBrandNumber = Validators.validateEnumInput("Pick a car brand:\n" + CarBrand.show(),
                CarBrand.values().length,
                false,
                scanner);
        return CarBrand.values()[carBrandNumber];
    }

    private Integer setCarBrand(String carBrand) throws ValidationException {
        return Validators.validateArg(arg -> (((int) arg) <= CarBrand.values().length) && ((((int) arg) >= 1)),
                "",
                Integer::parseInt,
                carBrand);
    }

    public void setVariables() throws NoUserInputException {
        this.setName();
        this.setCoordinates();
        this.setRealHero();
        this.setHasToothpick();
        this.setImpactSpeed();
        this.setMinutesOfWaitingSpeed();
        this.setWeaponType();
        this.setMood();
        this.setCar();
    }

    public void setVariables(ArrayList<String> data) throws ValidationException {
        if (data.size() >= HumanBeing.getNumberOfFields() - Car.getNumberOfFields() + 1) {
            this.setName(data.get(0));
            this.setCoordinates(data.get(1), data.get(2));
            this.setRealHero(data.get(3));
            this.setHasToothpick(data.get(4));
            this.setImpactSpeed(data.get(5));
            this.setMinutesOfWaiting(data.get(6));
            this.setWeaponType(data.get(7));
            this.setMood(data.get(8));
            if (data.size() >= HumanBeing.getNumberOfFields()) {
                this.setCar(data.subList(9, data.size()));
            } else {
                createdHumanBeing.setCar(null);
            }
        } else {
            throw new ValidationException("Not enough fields, expected: " + HumanBeing.getNumberOfFields()
                    + ", got: " + data.size());
        }
    }


    public HumanBeing getCreatedHumanBeing() {
        return createdHumanBeing;
    }
}
