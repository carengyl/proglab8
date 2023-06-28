package server.database.util.interfaces;

import common.exceptions.DatabaseException;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLFunction<T, R> {
    R apply(T t) throws SQLException, DatabaseException;
}
