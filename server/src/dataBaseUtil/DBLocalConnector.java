package dataBaseUtil;

import commonUtil.OutputUtil;
import dataBaseUtil.DAOs.HumanBeingDAO;
import dataBaseUtil.DAOs.UsersDAO;
import dataBaseUtil.interfaces.DBConnectable;
import dataBaseUtil.interfaces.SQLConsumer;
import dataBaseUtil.interfaces.SQLFunction;
import exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DBLocalConnector implements DBConnectable {

    private final String dbUrl = "jdbc:postgresql://pg:5432/studs";
    private final String user = System.getenv("LOGIN");
    private final String pass = System.getenv("PASSWORD");

    public DBLocalConnector() {
        try {
            Class.forName("org.postgresql.Driver");
            initializeDB();
        } catch (ClassNotFoundException e) {
            OutputUtil.printErrorMessage("No DataBase driver!");
            System.exit(1);
        } catch (SQLException e) {
            OutputUtil.printErrorMessage("Error occurred during initializing tables!" + e.getMessage());
            System.exit(1);
        }
    }

    private void initializeDB() throws SQLException {

        Connection connection = DriverManager.getConnection(dbUrl, user, pass);

        Statement statement = connection.createStatement();

        statement.execute(UsersDAO.getInitIdSequenceStatement());

        statement.execute(UsersDAO.getInitTableStatement());

        statement.execute(HumanBeingDAO.getInitIdSequenceStatement());

        statement.execute(HumanBeingDAO.getInitTableStatement());

        connection.close();
    }

    @Override
    public void handleQuery(SQLConsumer<Connection> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
