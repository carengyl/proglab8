package server.util;

import common.util.udp.Request;
import common.util.udp.Response;
import server.database.util.DBManager;
import common.exceptions.DatabaseException;

public class UsersManager {
    private final DBManager dbManager;

    public UsersManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Response registerNewUser(Request request) {
        try {
            if (!dbManager.checkUsersExistence(request.getUserData().login())) {
                dbManager.addUser(request.getUserData().login(), request.getUserData().password());
                return new Response("Registration was completed successfully!", true);
            } else {
                return new Response("This username already exists!", false);
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }

    public Response loginUser(Request request) {
        try {
            boolean check = dbManager.validateUser(request.getUserData().login(), request.getUserData().password());
            if (check) {
                return new Response("Login successful!", true);
            } else {
                return new Response("Wrong login or password!", false);
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}
