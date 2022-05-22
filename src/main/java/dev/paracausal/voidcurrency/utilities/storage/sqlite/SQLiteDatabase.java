package dev.paracausal.voidcurrency.utilities.storage.sqlite;

import dev.paracausal.voidcurrency.Core;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class SQLiteDatabase {

    Core core;
    Connection connection;

    public SQLiteDatabase(Core core) {
        this.core = core;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM connection WHERE UUID = ?");
            ResultSet resultSet = preparedStatement.executeQuery();
            close(preparedStatement, resultSet);

        } catch (SQLException ex) {
            core.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    /**
     * Get a value from a row in a table
     * @param table SQLite table
     * @param column SQLite column
     * @param row SQLite row
     * @param value The value the row should equal
     * @example get("tokens", "UUID", "Amount", uuid); this will return the amount of tokens the user has
     * @return String
     */
    public String get(String table, String column, String row, String value) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        try {
            conn = getSQLConnection();
            preparedStatement = conn.prepareStatement("SELECT * FROM `" + table + "` WHERE `" + row + "`='" + value +"';");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getString(row).equalsIgnoreCase(value))
                    return resultSet.getString(column);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public List<String> getTables() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        List<String> tables = new ArrayList<>();

        try {
            conn = getSQLConnection();
            preparedStatement = conn.prepareStatement("SELECT name FROM sqlite_schema WHERE type IN ('table','view') AND name NOT LIKE 'sqlite_%' ORDER BY 1;");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tables.add(resultSet.getString("name"));
            }

            return tables;

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Set a column's value for a player!
     * @param table SQLite table
     * @param uuid Bukkit player UUID
     * @param column SQLite column
     * @param value The new value you're setting
     * @example set("tokens", uuid, "Amount", "100"); This will set the player's token balance to 100
     */
    public void set(String table, String uuid, String column, String value) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = getSQLConnection();
            preparedStatement = conn.prepareStatement("REPLACE INTO `" + table + "` (`UUID`,`" + column + "`) VALUES (?,?)");

            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, value);

            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            core.getLogger().log(Level.SEVERE, SQLiteErrors.sqlConnectionExecute(), ex);
        } finally {
            try {

                if (preparedStatement != null)
                    preparedStatement.close();

                if (conn != null)
                    conn.close();

            } catch (SQLException ex) {
                core.getLogger().log(Level.SEVERE, SQLiteErrors.sqlConnectionClose(), ex);
            }
        }
    }

    public void close(PreparedStatement preparedStatement, ResultSet resultSet) {
        try {

            if (preparedStatement != null)
                preparedStatement.close();

            if (resultSet != null)
                resultSet.close();

        } catch (SQLException ex) {
            SQLiteError.close(core, ex);
        }
    }
}