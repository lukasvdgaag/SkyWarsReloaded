package net.gcnt.skywarsreloaded.data.player;

import java.sql.Connection;
import java.sql.SQLException;

public interface MySQLStorage extends Storage {

    Connection getConnection() throws SQLException;

}
