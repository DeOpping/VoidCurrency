package dev.paracausal.voidcurrency.utilities.storage.sqlite;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.CurrencyManager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends SQLiteDatabase {

    CurrencyManager currencyManager;
    String dbname = "storage";

    public SQLite(Core core) {
        super(core);

        currencyManager = core.getCurrencyManager();
    }

    public Connection getSQLConnection() {
        File dataFolder = new File(core.getDataFolder(), dbname + ".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                core.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            if(connection != null && !connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            core.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            core.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void executeStatement(String sql) {
        connection = getSQLConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement statement = connection.createStatement();

            String playerTable = "CREATE TABLE IF NOT EXISTS players (" +
                    "`UUID` varchar(36) NOT NULL," +
                    "`Username` varchar(16) NOT NULL," +
                    "PRIMARY KEY (`UUID`)" +
                    ");";
            statement.execute(playerTable);

            for (String string : currencyManager.getCurrencies()) {
                String create = "CREATE TABLE IF NOT EXISTS " + string + " (" +
                        "`UUID` varchar(36) NOT NULL," +
                        "`Amount` varchar(36)," +
                        "PRIMARY KEY (`UUID`)" +
                        ");";

                statement.executeUpdate(create);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            core.getServer().getPluginManager().disablePlugin(core);
        }
        initialize();
    }

}
