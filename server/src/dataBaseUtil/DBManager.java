package dataBaseUtil;

import commonUtil.PasswordEncryptor;
import dataBaseUtil.DAOs.HumanBeingDAO;
import dataBaseUtil.DAOs.UsersDAO;
import dataBaseUtil.interfaces.DBConnectable;
import entities.*;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class DBManager {

    private final DBConnectable dbConnector;
    private final ReentrantLock reentrantLock = new ReentrantLock();

    public DBManager(DBConnectable dbConnector) {
        this.dbConnector = dbConnector;
    }

    public HashMap<Long, HumanBeing> loadCollection() throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String selectCollectionQuery = "SELECT * FROM " + HumanBeingDAO.HumanBeingTableName + ";";
            Statement statement = connection.createStatement();
            ResultSet collectionSet = statement.executeQuery(selectCollectionQuery);
            HashMap<Long, HumanBeing> resultMap = new HashMap<>();
            while (collectionSet.next()) {
                long key = collectionSet.getLong("key");
                HumanBeing humanBeing = new HumanBeing(collectionSet.getLong("id"));

                humanBeing.setName(collectionSet.getString("name"));
                humanBeing.setCreationDate(collectionSet.getDate("creationDate").toLocalDate());

                humanBeing.setCoordinates(new Coordinates(
                        collectionSet.getInt("x"),
                        collectionSet.getInt("y")));

                humanBeing.setRealHero(collectionSet.getBoolean("realHero"));
                humanBeing.setHasToothpick(collectionSet.getBoolean("hasToothpick"));
                Double impactSpeed = collectionSet.getDouble("impactSpeed");
                if (collectionSet.wasNull()) {
                    impactSpeed = null;
                }
                humanBeing.setImpactSpeed(impactSpeed);
                humanBeing.setMinutesOfWaiting(collectionSet.getDouble("minutesOfWaiting"));

                humanBeing.setWeaponType(WeaponType.valueOf(collectionSet.getString("weaponType")));
                humanBeing.setMood(Mood.valueOf(collectionSet.getString("mood")));

                Car car = null;
                if (collectionSet.getString("carName") != null) {
                    car = new Car(collectionSet.getString("carName"),
                            collectionSet.getInt("carCost"),
                            collectionSet.getInt("carHorsePowers"),
                            CarBrand.valueOf(collectionSet.getString("carCarBrand")));
                }
                humanBeing.setCar(car);

                resultMap.put(key, humanBeing);
            }
            return resultMap;
        });
    }

    public Long insertElement(long key, HumanBeing humanBeing, String userName) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String addElementQuery = "INSERT INTO " + HumanBeingDAO.HumanBeingTableName + " " +
                    "(key, " +
                    "creationDate, " +
                    "name, " +
                    "x, " +
                    "y, " +
                    "realHero, " +
                    "hasToothPick, " +
                    "impactSpeed, " +
                    "minutesOfWaiting, " +
                    "weaponType," +
                    "mood," +
                    "carName," +
                    "carCost," +
                    "carHorsePowers," +
                    "carCarBrand," +
                    "owner_id)" +
                    "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, id " +
                    "FROM " + UsersDAO.UsersTableName +
                    "WHERE " + UsersDAO.UsersTableName + ".login = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(addElementQuery, Statement.RETURN_GENERATED_KEYS);
            Coordinates coordinates = humanBeing.getCoordinates();

            preparedStatement.setLong(1, key);
            preparedStatement.setDate(2, Date.valueOf(humanBeing.getCreationDate()));
            preparedStatement.setString(3, humanBeing.getName());
            preparedStatement.setInt(4, coordinates.getX());
            preparedStatement.setInt(5, coordinates.getY());
            preparedStatement.setBoolean(6, humanBeing.isRealHero());
            preparedStatement.setBoolean(7, humanBeing.isHasToothpick());
            if (humanBeing.getImpactSpeed() != null) {
                preparedStatement.setLong(8, humanBeing.getImpactSpeed().longValue());
            } else {
                preparedStatement.setNull(9, Types.DOUBLE);
            }
            preparedStatement.setString(10, humanBeing.getWeaponType().toString());
            preparedStatement.setString(11, humanBeing.getMood().toString());
            if (humanBeing.getCar() == null) {
                preparedStatement.setNull(12, Types.VARCHAR);
                preparedStatement.setNull(13, Types.INTEGER);
                preparedStatement.setNull(14, Types.INTEGER);
                preparedStatement.setNull(15, Types.VARCHAR);
            } else {
                preparedStatement.setString(12, humanBeing.getCar().getName());
                preparedStatement.setInt(13, humanBeing.getCar().getCost());
                preparedStatement.setInt(14, humanBeing.getCar().getHorsePowers());
                preparedStatement.setString(15, humanBeing.getCar().getCarBrand().toString());
            }
            preparedStatement.setString(16, userName);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong(1);
        });
    }

    public boolean removeById(Long id, String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
           String removeQuery = "DELETE FROM " + HumanBeingDAO.HumanBeingTableName +
                   " USING " + UsersDAO.UsersTableName +
                   " WHERE " + HumanBeingDAO.HumanBeingTableName + ".id = ? " +
                   " AND " + HumanBeingDAO.HumanBeingTableName + ".owner_id = " + UsersDAO.UsersTableName + ".id" +
                   " AND " + UsersDAO.UsersTableName + ".login = ?;";
           PreparedStatement preparedStatement = connection.prepareStatement(removeQuery);
           preparedStatement.setLong(1, id);
           preparedStatement.setString(2, username);

           int deletedBands = preparedStatement.executeUpdate();
           return deletedBands > 0;
        });
    }

    public boolean updateById(HumanBeing humanBeing, long id, String userName) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            connection.createStatement().execute("BEGIN TRANSACTION;");
            String updateQuery = "UPDATE " + HumanBeingDAO.HumanBeingTableName + " " +
                    "SET creationDate=?, " +
                    "name=?, " +
                    "x=?, " +
                    "y=?, " +
                    "realHero=?, " +
                    "hasToothPick=?, " +
                    "impactSpeed=?, " +
                    "minutesOfWaiting=?, " +
                    "weaponType=?, " +
                    "mood=?, " +
                    "carName=?, " +
                    "carCost=?, " +
                    "carHorsePowers=?, " +
                    "carCarBrand=?, " +
                    "FROM " + HumanBeingDAO.HumanBeingTableName + ".id = ? " +
                    "AND " + HumanBeingDAO.HumanBeingTableName + ".owner_id = " + UsersDAO.UsersTableName + ".id " +
                    "AND " + UsersDAO.UsersTableName + ".login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            Coordinates coordinates = humanBeing.getCoordinates();

            preparedStatement.setDate(1, Date.valueOf(humanBeing.getCreationDate()));
            preparedStatement.setString(2, humanBeing.getName());
            preparedStatement.setInt(3, coordinates.getX());
            preparedStatement.setInt(4, coordinates.getY());
            preparedStatement.setBoolean(5, humanBeing.isRealHero());
            preparedStatement.setBoolean(6, humanBeing.isHasToothpick());
            if (humanBeing.getImpactSpeed() != null) {
                preparedStatement.setLong(7, humanBeing.getImpactSpeed().longValue());
            } else {
                preparedStatement.setNull(8, Types.DOUBLE);
            }
            preparedStatement.setString(9, humanBeing.getWeaponType().toString());
            preparedStatement.setString(10, humanBeing.getMood().toString());
            if (humanBeing.getCar() == null) {
                preparedStatement.setNull(11, Types.VARCHAR);
                preparedStatement.setNull(12, Types.INTEGER);
                preparedStatement.setNull(13, Types.INTEGER);
                preparedStatement.setNull(14, Types.VARCHAR);
            } else {
                preparedStatement.setString(11, humanBeing.getCar().getName());
                preparedStatement.setInt(12, humanBeing.getCar().getCost());
                preparedStatement.setInt(13, humanBeing.getCar().getHorsePowers());
                preparedStatement.setString(14, humanBeing.getCar().getCarBrand().toString());
            }
            preparedStatement.setLong(15, id);
            preparedStatement.setString(16, userName);

            int updatedRows = preparedStatement.executeUpdate();
            connection.createStatement().execute("COMMIT;");

            return updatedRows > 0;
        });
    }

    public boolean checkExistence(Long id) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
           String existenceQuery = "SELECT COUNT (*) " +
                   " FROM " + HumanBeingDAO.HumanBeingTableName +
                   " WHERE " + HumanBeingDAO.HumanBeingTableName + ".id = ?;";

           PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
           preparedStatement.setLong(1, id);
           ResultSet resultSet = preparedStatement.executeQuery();

           resultSet.next();
           return resultSet.getInt("count") > 0;
        });
    }

    public List<Long> clear(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
           String clearQuery = "DELETE FROM " + HumanBeingDAO.HumanBeingTableName + "" +
                   " USING " + UsersDAO.UsersTableName + " " +
                   " WHERE " + HumanBeingDAO.HumanBeingTableName + ".owner_id = " + UsersDAO.UsersTableName + ".id " +
                   " AND " + UsersDAO.UsersTableName + ".login = ? " +
                   " RETURNING " + HumanBeingDAO.HumanBeingTableName + ".id;";
            return getLongs(username, connection, clearQuery);
        });
    }

    public void addUser(String username, String password) throws DatabaseException {
            dbConnector.handleQuery((Connection connection) -> {
            String addUserQuery = "INSERT INTO" + UsersDAO.UsersTableName + "(login, password) "
                    + "VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, PasswordEncryptor.encryptThisString(password));

            preparedStatement.executeUpdate();
        });
    }

    public String getPassword(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String getPasswordQuery = "SELECT (password) "
                    + "FROM " + UsersDAO.UsersTableName
                    + "WHERE " + UsersDAO.UsersTableName + ".login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(getPasswordQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
            return null;
        });
    }

    public boolean checkUsersExistence(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String existenceQuery = "SELECT COUNT (*) "
                    + "FROM" + UsersDAO.UsersTableName
                    + "WHERE " + UsersDAO.UsersTableName + ".login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt("count") > 0;
        });
    }

    public List<Long> getIdsOfUsersElements(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String getIdsQuery = "SELECT" + HumanBeingDAO.HumanBeingTableName + ".id FROM" + HumanBeingDAO.HumanBeingTableName + " " + UsersDAO.UsersTableName
                    + " WHERE" + HumanBeingDAO.HumanBeingTableName + ".owner_id = " + UsersDAO.UsersTableName + ".id " +
                    " AND " + UsersDAO.UsersTableName + ".login = ?;";
            return getLongs(username, connection, getIdsQuery);
        });
    }

    private List<Long> getLongs(String username, Connection connection, String getIdsQuery) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getIdsQuery);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Long> resultingList = new ArrayList<>();
        while (resultSet.next()) {
            resultingList.add(resultSet.getLong("id"));
        }

        return resultingList;
    }

    public boolean validateUser(String username, String password) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) ->
                PasswordEncryptor.encryptThisString(password).equals(getPassword(username)));
    }
}
