import CommandLine.CommandReader;

public class Client {
    public static void main(String[] args) {
        CommandReader commandReader = new CommandReader();
        commandReader.readCommandsFromConsole();
    }
}