package client.util;

import common.util.PasswordEncryptor;
import common.util.UserData;
import common.util.Validators;
import common.exceptions.NoUserInputException;

import java.util.Scanner;

public class AuthManager {
    private final int MIN_LOGIN_LENGTH = 8;
    private final int MIN_PASSWORD_LENGTH = 8;
    private final Scanner scanner;
    public AuthManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public UserData getUserData() throws NoUserInputException {
        String login = Validators.validateInput(arg -> ((String) arg).length() >= MIN_LOGIN_LENGTH && !((String) arg).contains(" "),
                "Enter login",
                "",
                "Login length must be greater than " + MIN_LOGIN_LENGTH,
                String::toString,
                false,
                scanner);

        //noinspection ConstantConditions null pointer already handled in Validators class
        String password = PasswordEncryptor.encryptThisString(Validators.validateInput(arg -> ((String) arg).length() >= MIN_PASSWORD_LENGTH && !((String) arg).contains(" "),
                "Enter password",
                "",
                "Password length must be greater than " + MIN_PASSWORD_LENGTH,
                String::toString,
                false,
                scanner));

        return new UserData(login, password);
    }
}
