package dataBaseUtil;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
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
import java.util.Properties;

public class DBSSHConnector implements DBConnectable {
    private static Session session;
    private final String dbBase = "jdbc:postgresql://";
    private final String dbName = "studs";
    private final int dbPort = 5432;
    private final String dbHost = "pg";

    private String svLogin;
    private String svPass;
    private String svAddr;

    private final int sshPort = 2222;
    private int forwardingPort;

    public DBSSHConnector() {
        try {
            this.svLogin = System.getenv("SV_LOGIN");
            this.svPass = System.getenv("SV_PASS");
            this.svAddr = System.getenv("SV_ADDR");
            this.forwardingPort = Integer.parseInt(System.getenv("FORWARDING_PORT"));
            connectSSH();
            initializeDB();
        } catch (SQLException e) {
            OutputUtil.printErrorMessage("Error occurred during initializing tables!" + e.getMessage());
            System.exit(1);
        } catch (JSchException e) {
            OutputUtil.printErrorMessage("Troubles during connecting to DB with ssh!");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            OutputUtil.printErrorMessage("Mistakes in environment variables!");
            System.exit(1);
        }
    }

    public static void closeSSH() {
        if (session != null) {
            session.disconnect();
        }
    }

    private void connectSSH() throws JSchException {
        Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        session = jsch.getSession(svLogin, svAddr, sshPort);
        session.setPassword(svPass);
        session.setConfig(config);
        session.connect();
        session.setPortForwardingL(forwardingPort, dbHost, dbPort);
    }

    public void handleQuery(SQLConsumer<Connection> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbBase + "localhost:" + forwardingPort + "/" + dbName, svLogin, svPass)) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbBase + "localhost:" + forwardingPort + "/" + dbName, svLogin, svPass)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void initializeDB() throws SQLException {

        Connection connection = DriverManager.getConnection(dbBase + "localhost:" + forwardingPort + "/" + dbName, svLogin, svPass);

        Statement statement = connection.createStatement();

        statement.execute(UsersDAO.getInitIdSequenceStatement());

        statement.execute(UsersDAO.getInitTableStatement());

        statement.execute(HumanBeingDAO.getInitIdSequenceStatement());

        statement.execute(HumanBeingDAO.getInitTableStatement());

        connection.close();
    }
}
