package serverUtil;

import UDPutil.Request;
import UDPutil.Response;
import dataBaseUtil.DBManager;
import exceptions.DatabaseException;

public class UsersManager {
    private final DBManager dbManager;

    public UsersManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Response registerNewUser(Request request) {
        try {
            if (!dbManager.checkUsersExistence(request.getUserData().login())) {
                dbManager.addUser(request.getUserData().login(), request.getUserData().password());
                return new Response("Registration was completed successfully!");
            } else {
                return new Response("This username already exists!");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }

    public Response loginUser(Request request) {
        try {
            boolean check = dbManager.validateUser(request.getUserData().login(), request.getUserData().password());
            if (check) {
                return new Response("Login successful!");
            } else {
                return new Response("Wrong login or password!");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}
