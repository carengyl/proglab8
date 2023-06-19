package serverUtil;

import UDPutil.Request;
import UDPutil.Response;
import dataBaseUtil.DBManager;
import entities.CollectionManager;
import entities.HumanBeing;
import exceptions.DatabaseException;

import java.util.ArrayList;
import java.util.List;

public class CommandProcessor {
    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public CommandProcessor(DBManager dbManager, CollectionManager collectionManager) {
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public Response add(Request request) {
        try {
            if (dbManager.validateUser(request.getUserData().login(), request.getUserData().password())) {
                HumanBeing humanBeing = request.getCommandArgument().getHumanBeingArgument();
                long key = request.getCommandArgument().getLongArg();
                Long id = dbManager.insertElement(key, humanBeing, request.getUserData().login());
                humanBeing.setId(id);
                collectionManager.addByKey(key, humanBeing);
                return new Response("Element was successfully added to collection with ID: " + id);
            } else {
                return new Response("Login and password mismatch");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeById(Request request) {
        try {
            if (!dbManager.validateUser(request.getUserData().login(), request.getUserData().password())) {
                return new Response("Login and password mismatch");
            }
            if (!dbManager.checkExistence(request.getCommandArgument().getLongArg())) {
                return new Response("There is no element with such ID");
            }
            if (dbManager.removeById(request.getCommandArgument().getLongArg(), request.getUserData().login())) {
                collectionManager.removeById(request.getCommandArgument().getLongArg());
                return new Response("Element with ID " + request.getCommandArgument().getLongArg()
                        + " was removed from the collection");
            } else {
                return new Response("Element was created by another user, you don't "
                        + "have permission to remove it");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }

    public Response info(Request request) {
        try {
            if (dbManager.validateUser(request.getUserData().login(), request.getUserData().password())) {
                return new Response("Info about collection: \n" + collectionManager.returnInfo());
            } else {
                return new Response("Login and password mismatch");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }

    public Response show(Request request) {
        try {
            if (dbManager.validateUser(request.getUserData().login(), request.getUserData().password())) {
                if (collectionManager.getHumanBeings().isEmpty()) {
                    return new Response("Collection is empty");
                } else {
                    List<Long> ids = dbManager.getIdsOfUsersElements(request.getUserData().login());
                    return new Response("Elements of collection:",
                            collectionManager.getUsersElements(ids),
                            collectionManager.getAlienElements(ids));
                }
            } else {
                return new Response("Login and password mismatch");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateById(Request request) {
        try {
            if (!dbManager.validateUser(request.getUserData().login(), request.getUserData().password())) {
                return new Response("Login and password mismatch");
            }
            if (!dbManager.checkExistence(request.getCommandArgument().getLongArg())) {
                return new Response("There is no element with such ID");
            }
            if (dbManager.updateById(request.getCommandArgument().getHumanBeingArgument(), request.getCommandArgument().getLongArg(), request.getUserData().login())) {
                collectionManager.updateById(request.getCommandArgument().getLongArg(), request.getCommandArgument().getHumanBeingArgument());
                return new Response("Element with ID " + request.getCommandArgument().getLongArg()
                        + " was successfully updated");
            } else {
                return new Response("Element was created by another user, you don't "
                        + "have permission to update it");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }

    public Response clear(Request request) {
        try {
            if (!dbManager.validateUser(request.getUserData().login(), request.getUserData().password())) {
                return new Response("Login and password mismatch");
            }
            List<Long> deletedIDs = dbManager.clear(request.getUserData().login());
            if (deletedIDs.isEmpty()) {
                return new Response("You don't have elements in this collection");
            } else {
                deletedIDs.forEach(collectionManager::removeById);
                return new Response("Your elements were removed from the collection, their IDs:", deletedIDs);
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeGreater(Request request) {
        try {
            if (!dbManager.validateUser(request.getUserData().login(), request.getUserData().password())) {
                return new Response("Login and password mismatch");
            }
            List<Long> removedIDs = new ArrayList<>();
            List<Long> ids = collectionManager.returnIDsOFGreater(request.getCommandArgument().getHumanBeingArgument());
            for (Long id: ids) {
                if (dbManager.removeById(request.getCommandArgument().getLongArg(), request.getUserData().login())) {
                    removedIDs.add(id);
                    collectionManager.removeById(id);
                }
            }
            if (removedIDs.isEmpty()) {
                return new Response("There are no such elements, that belong to you in collection");
            } else {
                return new Response("Elements with this IDs were removed from the collection", removedIDs);
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}
