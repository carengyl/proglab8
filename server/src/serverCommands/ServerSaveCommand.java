package serverCommands;

import UDPutil.Response;
import commands.AbstractCommand;
import commands.CommandArgument;
import commands.CommandData;
import commonUtil.OutputUtil;
import commonUtil.Validators;
import entities.CollectionManager;
import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;
import serverUtil.XMLParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;

public class ServerSaveCommand extends AbstractCommand {
    private final CollectionManager collection;

    public ServerSaveCommand(CollectionManager collection) {
        super("save", "Save collection to XML file");
        this.collection = collection;
    }

    @Override
    public Optional<Response> executeCommand(CommandArgument argument) throws NoUserInputException {
        try {
            XMLParser xmlParser = new XMLParser();
            xmlParser.writeToXML(collection);
            OutputUtil.printSuccessfulMessage("Collection saved to file " + collection.getFileName());
        } catch (NullPointerException | IOException e) {
            OutputUtil.printErrorMessage("Unable to get to the file.");
            Scanner scanner = new Scanner(System.in);
            String newFile = Validators.validateStringInput("Please enter path to backup file", true, scanner);
            if (newFile != null) {
                collection.setFileName(Path.of(newFile));
                this.executeCommand(argument);
            }
        }
        return Optional.empty();
    }

    @Override
    public CommandArgument validateArguments(CommandArgument argument, CommandData commandData) throws ValidationException, InvalidNumberOfArgsException {
        Validators.validateNumberOfArgs(argument.getNumberOfArgs(), commandData.numberOfArgs());
        return argument;
    }
}
