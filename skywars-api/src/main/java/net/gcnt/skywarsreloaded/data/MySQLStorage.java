package net.gcnt.skywarsreloaded.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public interface MySQLStorage extends Storage {

    Connection getConnection() throws SQLException;
}
