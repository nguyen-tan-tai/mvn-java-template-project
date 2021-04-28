package framework.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;

import caphe.Config;

public class DataSource {

    @Inject
    public Config config;

    public Connection getConnection() {
        return this.getConnection(true);
    }

    public Connection getConnection(boolean autoCommit) {
        try {
            Connection conn = DriverManager.getConnection(config.getDatabase().getHost(),
                    config.getDatabase().getUsername(), config.getDatabase().getPassword());
            if (autoCommit == false) {
                conn.setAutoCommit(false);
            }
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
