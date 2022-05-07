package dev.deopping.mysqltest.database;

import dev.deopping.mysqltest.Core;
import dev.deopping.mysqltest.configurations.ConfigManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

public class MySQL {

    Core core;
    ConfigManager configYml;

    Connection connection;
    String tablePrefix;

    public MySQL(Core core) {
        this.core = core;
        this.configYml = core.getConfigYml();
        this.tablePrefix = configYml.getConfig().getString("mysql.table-prefix");

        (new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.createStatement().execute("SELECT 1");
                    }
                } catch (SQLException e) {
                    connection = getNewConnection();
                }
            }
        }).runTaskTimerAsynchronously(core, 60 * 20, 60 * 20);
    }

    private Connection getNewConnection() {
        String host = configYml.getConfig().getString("mysql.host");
        String port = configYml.getConfig().getString("mysql.port");
        String database = configYml.getConfig().getString("mysql.database");
        String username = configYml.getConfig().getString("mysql.username");
        String password = configYml.getConfig().getString("mysql.password");

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public boolean execute(String sql) {
        try {
            return connection.createStatement().execute(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getNewConnection();

                if (connection == null || connection.isClosed()) {
                    return false;
                }

                execute("CREATE TABLE IF NOT EXISTS " + tablePrefix + "players ("
                        + "UUID varchar(32) NOT NULL"
                        + ")"
                );
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    public String get(String table, String column, String value) {
        try {
            checkConnection();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        String sql = "SELECT " + column + " FROM " + tablePrefix + table + " WHERE " + column + "=?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, value);

            ResultSet set = statement.executeQuery();
            String uuid = null;

            while (set.next()) {
                uuid = set.getString("UUID");
            }
            set.close();

            return uuid;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void update(String table, String column, String value) {
        try {
            checkConnection();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        String sql = "UPDATE " + tablePrefix + table + " SET " + column + "=? WHERE " + column + "=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, value);
            statement.setString(2, value);
            int update = statement.executeUpdate();

            if (update == 0) {
                sql = "INSERT INTO " + tablePrefix + table + " ( " + column + ") VALUES (?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, value);
                statement.execute();
                statement.close();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
