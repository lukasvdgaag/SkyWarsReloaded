package net.gcnt.skywarsreloaded.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface MySQLStorage<DataType> extends Storage {

    Connection getConnection() throws SQLException;

    void setProperty(String property, Object value, DataType object);

    void bindPropertyValue(PreparedStatement statement, int index, Object value) throws SQLException;

    void createDatabase(Connection connection, String database) throws SQLException;

    void createTable(Connection connection) throws SQLException;

}
