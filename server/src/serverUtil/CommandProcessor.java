package serverUtil;

import UDPutil.Request;
import UDPutil.Response;
import dataBaseUtil.DBManager;
import entities.CollectionManager;
import entities.HumanBeing;
import exceptions.DatabaseException;

public class CommandProcessor {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public CommandProcessor(DBManager dbManager, CollectionManager collectionManager) {
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    public Response add(Request request) {
        try {
            if (dbManager.validateUser(request.getUserData().login(), request.getUserData().password())) {
                HumanBeing humanBeing = request.getCommandArgument().getHumanBeingArgument();
                long key = request.getCommandArgument().getLongArg();
                Long id = dbManager.insertElement(key, humanBeing, request.getUserData().login());
                humanBeing.setId(id);
                collectionManager.addByKey(key, humanBeing);
                return new Response("Element was successfully added to collection with ID: "
                        + id);
            } else {
                return new Response("Login and password mismatch");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}
