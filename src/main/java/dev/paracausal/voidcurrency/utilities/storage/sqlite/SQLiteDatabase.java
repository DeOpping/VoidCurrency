package dev.paracausal.voidcurrency.utilities.storage.sqliteold;

import dev.paracausal.voidcurrency.Core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE UUID = ?");
            ResultSet resultSet = preparedStatement.executeQuery();
            close(preparedStatement, resultSet);

        } catch (SQLException ex) {
            core.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    public boolean exists(String table, String row) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        try {
            conn = getSQLConnection();
            preparedStatement = conn.prepareStatement("SELECT EXISTS(SELECT 1 FROM " + table + " WHERE UUID = " + row + ");");

            resultSet = preparedStatement.executeQuery();
            return resultSet.equals("1");
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
        return false;
    }

    public String get(String table, String row, String column) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        try {
            conn = getSQLConnection();
            preparedStatement = conn.prepareStatement("SELECT * FROM " + table + " WHERE UUID = '" + row + "';");

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("UUID").equalsIgnoreCase(row)) {
                    return resultSet.getString(column);
                }
            }
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
        return null;
    }

    public void set(String table, String row, String column, String value) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = getSQLConnection();
            preparedStatement = conn.prepareStatement("REPLACE INTO " + table + "(UUID," + column + ") VALUES(?,?)");

            preparedStatement.setString(1, row);
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

    public void update(String table, String row, String column, String value) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = getSQLConnection();
            preparedStatement = conn.prepareStatement("UPDATE " + table + " SET " + column + " = \"" + value + "\" WHERE UUID = \"" + row + "\"");

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

    public void replaceAll(String table, String column, String replace, String replacement) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = getSQLConnection();
            preparedStatement = conn.prepareStatement("UPDATE " + table + " SET " + column + " = \"" + replacement + "\" WHERE " + column + " = \"" + replace + "\";");

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