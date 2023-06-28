package common.util;

import java.io.Serializable;

public record UserData(String login, String password) implements Serializable {
}
