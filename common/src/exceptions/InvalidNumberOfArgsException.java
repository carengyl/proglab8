package exceptions;

public class InvalidNumberOfArgsException extends Exception {
    public InvalidNumberOfArgsException(int correctNumberOfArgs, int gotNumberOfArgs) {
        super("Expected arguments: " + correctNumberOfArgs + ", got: " + gotNumberOfArgs);
    }
}
