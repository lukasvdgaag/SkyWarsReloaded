package net.gcnt.skywarsreloaded.bukkit.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.gcnt.skywarsreloaded.data.SWPlayer;
import net.gcnt.skywarsreloaded.data.Storage;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLStorage implements Storage {

    private HikariDataSource ds;

    private Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public void setup() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + StreakRewards.getCfg().getMysqlHost() + ":" + StreakRewards.getCfg().getMysqlPort() + "/" + StreakRewards.getCfg().getMysqlDatabase());
        config.setUsername(StreakRewards.getCfg().getMysqlUsername());
        config.setPassword(StreakRewards.getCfg().getMysqlPassword());
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(4000);
        ds = new HikariDataSource(config);
    }

    @Override
    public void loadData(SWPlayer player) {

    }

    public void saveData() {

    }
}
