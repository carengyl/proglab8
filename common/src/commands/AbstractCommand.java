package commands;

import exceptions.ValidationException;

public abstract class AbstractCommand {
    private final String commandName;
    private final String commandDescription;
    private final int numberOfArgs;
    private final String argsDescription;

    public AbstractCommand(String name, String description) {
        this.commandName = name;
        this.commandDescription = description;
        this.numberOfArgs = 0;
        this.argsDescription = "";
    }

    public AbstractCommand(String commandName, String commandDescription, int numberOfArgs, String argsDescription) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.numberOfArgs = numberOfArgs;
        this.argsDescription = argsDescription;
    }

    public abstract void executeCommand(CommandArgument argument);

    public abstract CommandArgument validateArguments(CommandArgument arguments) throws ValidationException;
}
