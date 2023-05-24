package commands;

import java.io.Serializable;

public record CommandData(String commandName, String commandDescription, int numberOfArgs, String argumentDescription, SerializableBiFunction<CommandArgument, CommandData, CommandArgument> validationFunction) implements Serializable {
    @Override
    public String toString() {
        return "Command name: " + commandName + ", description: " + commandDescription
                + ((numberOfArgs == 0) ? "" : ", args: " + argumentDescription);
    }
}
