package common.commands;

import java.io.Serializable;
import java.util.function.BiFunction;

public record CommandData(String commandName,
                          String commandDescription,
                          int numberOfArgs,
                          String argumentDescription,
                          BiFunction<CommandArgument, CommandData, CommandArgument> validationFunction)
        implements Serializable {
    @Override
    public String toString() {
        return "Command name: " + commandName + ", description: " + commandDescription
                + ((numberOfArgs == 0) ? "" : ", args: " + argumentDescription);
    }
}
