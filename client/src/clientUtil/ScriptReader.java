package clientUtil;

import commonUtil.StreamUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class responsible for reading Commands from script
 */
public class ScriptReader {

    /**
     * Field @code ArrayDeque} of String Commands read from script
     */
    private ArrayList<String> commandsFromScript = new ArrayList<>();

    /**
     * Reads Commands from script and parses them to String commands
     *
     * @param fileName String filename
     * @throws IOException if no access or file not found
     */
    public void readCommandsFromFile(String fileName) throws IOException{
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName))) {
            final StreamUtil streamUtil = new StreamUtil();
            commandsFromScript = streamUtil.streamToArrayOfCommands(input);
        }
    }

    public ArrayList<String> getCommandsFromScript() {
        return commandsFromScript;
    }
}
