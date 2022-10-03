package net.gcnt.skywarsreloaded.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLTable<DataType> {

    void createTable(Connection connection) throws SQLException;

    void bindPropertyValue(PreparedStatement statement, int index, Object value) throws SQLException;

    void setProperty(String property, Object value, DataType object);

}
