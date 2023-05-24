package exceptions;

public class InvalidNumberOfArgsException extends RuntimeException {
    public InvalidNumberOfArgsException(int correctNumberOfArgs, int gotNumberOfArgs) {
        super("Expected arguments: " + correctNumberOfArgs + ", got: " + gotNumberOfArgs);
    }
}
