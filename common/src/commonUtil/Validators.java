package commonUtil;

import exceptions.InvalidNumberOfArgsException;
import exceptions.NoUserInputException;
import exceptions.ValidationException;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Validators {
    private Validators() {}

    public static void validateNumberOfArgs(int gotNumberOfArgs, int numberOfArgs) throws InvalidNumberOfArgsException {
        if (gotNumberOfArgs != numberOfArgs) {
            throw new InvalidNumberOfArgsException(numberOfArgs, gotNumberOfArgs);
        }
    }

    public static String validateStringInput(String inputMessage, boolean nullable, Scanner scanner) throws NoUserInputException {
        OutputUtil.printSuccessfulMessage(inputMessage + ((nullable) ? " (or press Enter to skip):": ":"));
        String input = "";
        do {
            try {
                input = scanner.nextLine();
                if (input.equals("") && nullable) {
                    return null;
                } else if (input.equals("")) {
                    OutputUtil.printErrorMessage("This variable can't be null, try again:");
                    continue;
                }
            } catch (NoSuchElementException e) {
                throw new NoUserInputException();
            }
            return input;
        } while (true);
    }

    public static Boolean validateBooleanInput(String inputMessage, Scanner scanner) throws NoUserInputException {
        OutputUtil.printSuccessfulMessage(inputMessage + ", y/n? ");
        String input = "";
        do {
            try {
                input = scanner.nextLine();
                if (input.equals("")) {
                    OutputUtil.printErrorMessage("This variable can't be null, try again:");
                    continue;
                }
            } catch (NoSuchElementException e) {
                throw new NoUserInputException();
            }
            input = input.strip();
            switch (input.toLowerCase()) {
                case "y": return true;
                case "n": return false;
                default: OutputUtil.printErrorMessage("Type 'y' or 'n', where 'y' is for 'yes, 'n' is for 'no'");
            }
        } while (true);
    }

    public static Integer validateEnumInput(String inputMessage, int enumLength, boolean nullable, Scanner scanner) throws NoUserInputException {
        OutputUtil.printSuccessfulMessage(inputMessage);
        OutputUtil.printSuccessfulMessage("Pick a number" + ((nullable) ? " or press Enter to skip: ": ": "));
        String input;
        int value;
        do {
            try {
                input = scanner.nextLine();
                if (input.equals("") && nullable) {
                    return null;
                } else if (input.equals("")) {
                    OutputUtil.printErrorMessage("This variable can't be null, try again:");
                    continue;
                }
                value = Integer.parseInt(input);
                if (value > 0 && value <= enumLength) {
                    return value - 1;
                } else {
                    OutputUtil.printErrorMessage("Pick an Enum element number, it can't be less than 0 and greater than " + enumLength);
                }
            } catch (IllegalArgumentException e) {
                OutputUtil.printErrorMessage("Pick a number: ");
            } catch (NoSuchElementException e) {
                throw new NoUserInputException();
            }
        } while (true);
    }

    public static <T> T validateInput(Predicate<Object> predicate,
                                      String inputMessage,
                                      String errorMessage,
                                      String wrongCaseMessage,
                                      Function<String, T> function,
                                      Boolean nullable,
                                      Scanner scanner) throws NoUserInputException {
        OutputUtil.printSuccessfulMessage(inputMessage + ((nullable) ? " (press Enter to skip):": ":"));
        String input;
        T value;
        do {
            try {
                input = scanner.nextLine();
                if (input.equals("") && nullable) {
                    return null;
                } else if (input.equals("")) {
                    OutputUtil.printErrorMessage("This variable can't be hull, try again:");
                    continue;
                }
                value = function.apply(input);
            } catch (IllegalArgumentException e) {
                OutputUtil.printErrorMessage(errorMessage);
                continue;
            } catch (NoSuchElementException e) {
                throw new NoUserInputException();
            }
            if (predicate.test(value)) {
                return value;
            } else {
                OutputUtil.printErrorMessage(wrongCaseMessage);
            }
        } while(true);
    }

    public static <T> T validateArg(Predicate<Object> predicate,
                                    String wrongCaseMessage,
                                    Function<String, T> function,
                                    String arg) throws ValidationException, IllegalArgumentException {
        T value = function.apply(arg);
        if (predicate.test(value)) {
            return value;
        } else {
            throw new ValidationException(wrongCaseMessage);
        }
    }
}
