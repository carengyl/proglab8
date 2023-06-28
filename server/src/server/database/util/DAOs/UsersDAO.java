package server.database.util.DAOs;

public class UsersDAO {
    public static final String UsersIdSequenceName = " s368587users_id_seq";
    public static final String UsersTableName = " s368587users";

    public static String getInitIdSequenceStatement() {
        return"CREATE SEQUENCE IF NOT EXISTS " + UsersIdSequenceName + " INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1";
    }

    public static String getInitTableStatement() {
        return "CREATE TABLE IF NOT EXISTS " + UsersTableName
                + "("
                + "login varchar(255) NOT NULL UNIQUE CHECK(login<>''),"
                + "password varchar(255) NOT NULL CHECK(password<>''),"
                + "id bigint NOT NULL PRIMARY KEY DEFAULT nextval('" + UsersIdSequenceName + "')"
                + ");";
    }
}
