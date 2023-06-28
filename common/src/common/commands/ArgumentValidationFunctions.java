package common.commands;

import common.entities.Mood;
import common.util.Validators;

import java.io.Serializable;

public enum ArgumentValidationFunctions implements Serializable {
    VALIDATE_NUMBER_OF_ARGS((arguments, commandData) -> {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        return arguments;
    }),

    VALIDATE_MOOD((arguments, commandData) -> {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        int moodNumber = Validators.validateArg(arg -> ((int) arg < Mood.values().length + 1) && ((int) arg > 0),
                "Pick a Mood number:\n" + Mood.show(),
                Integer::parseInt,
                arguments.getArg());
        arguments.setEnumNumber(moodNumber);
        return arguments;
    }),

    VALIDATE_MINUTES_OF_WAITING((arguments, commandData) -> {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        double minutesOfWaiting = Validators.validateArg(arg -> true,
                "Expected double",
                Double::parseDouble,
                arguments.getArg());
        arguments.setDoubleArg(minutesOfWaiting);
        return arguments;
    }),

    VALIDATE_FILE_NAME((argument, commandData) -> {
        Validators.validateNumberOfArgs(argument.getNumberOfArgs(), commandData.numberOfArgs());
        argument.setFileName(argument.getArg());
        return argument;
    }),

    VALIDATE_BOOLEAN((arguments, commandData) -> {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        Boolean car = Validators.validateArg(arg -> true,
                "should be true or false",
                Boolean::parseBoolean,
                arguments.getArg());
        arguments.setBooleanArg(car);
        return arguments;
    }),

    VALIDATE_KEY((arguments, commandData) -> {
        Validators.validateNumberOfArgs(arguments.getNumberOfArgs(), commandData.numberOfArgs());
        long key = Validators.validateArg(arg -> true,
                "Illegal key",
                Long::parseLong,
                arguments.getArg());
        arguments.setLongArg(key);
        return arguments;
    }),

    VALIDATE_ID((commandArgument, commandData) -> {
        Validators.validateNumberOfArgs(commandArgument.getNumberOfArgs(), commandData.numberOfArgs());
        long id = Validators.validateArg(arg -> true,
                "Illegal ID",
                Long::parseLong,
                commandArgument.getArg());
        commandArgument.setLongArg(id);
        return commandArgument;
    });

    private final SerializableBiFunction<CommandArgument, CommandData, CommandArgument> validationFunction;

    ArgumentValidationFunctions(SerializableBiFunction<CommandArgument, CommandData, CommandArgument> validationFunction) {
        this.validationFunction = validationFunction;
    }

    public SerializableBiFunction<CommandArgument, CommandData, CommandArgument> getValidationFunction() {
        return validationFunction;
    }
}
