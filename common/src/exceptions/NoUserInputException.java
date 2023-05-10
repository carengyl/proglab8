package exceptions;

public class NoUserInputException extends Exception {
    public NoUserInputException() {
        super("Ctrl+D signal accepted. Shutting down...");
    }
}
