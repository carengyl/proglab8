package UDPutil;

import java.io.Serializable;
import java.util.Optional;

public class Request implements Serializable {

    private final String commandName;

    /**
     * Constructs dummy-instance to receive packet with AVAILABLE_COMMANDS
     */
    public Request() {
        commandName = "";
    }
}
