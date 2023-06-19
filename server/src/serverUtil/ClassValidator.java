package serverUtil;

import commonUtil.OutputUtil;
import entities.Car;
import entities.CollectionManager;
import entities.Coordinates;
import entities.HumanBeing;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class ClassValidator {
    public static void validateClass(CollectionManager collection) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        validatorFactory.close();
        Set<Long> keysCopy = Set.copyOf(collection.getHumanBeings().keySet());
        for (long key: keysCopy) {
            HumanBeing humanBeing = collection.getHumanBeings().get(key);
            Set<ConstraintViolation<Coordinates>> validatedCoordinates = validator.validate(humanBeing.getCoordinates());
            Set<ConstraintViolation<Car>> validatedCar = new HashSet<>();

            if (humanBeing.getCar() != null) {
                validatedCar = validator.validate(humanBeing.getCar());
            }
            Set<ConstraintViolation<HumanBeing>> validatedHumanBeing = validator.validate(humanBeing);
            if (!validatedHumanBeing.isEmpty() || !validatedCoordinates.isEmpty() || !validatedCar.isEmpty()) {
                OutputUtil.printErrorMessage("HumanBeing is corrupted:");
                validatedHumanBeing.stream().map(ConstraintViolation::getMessage).forEach(OutputUtil::printErrorMessage);
                validatedCoordinates.stream().map(ConstraintViolation::getMessage).forEach(OutputUtil::printErrorMessage);
                validatedCar.stream().map(ConstraintViolation::getMessage).forEach(OutputUtil::printErrorMessage);
                collection.removeByKey(key);
            }
        }
        OutputUtil.printSuccessfulMessage("Successfully loaded collection from file. Waiting for commands...");
    }
}
