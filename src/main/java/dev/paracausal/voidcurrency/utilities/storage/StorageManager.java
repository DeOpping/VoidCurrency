package dev.paracausal.voidcurrency.utilities.storage;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.CurrencyManager;
import dev.paracausal.voidcurrency.utilities.Debugger;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;
import dev.paracausal.voidcurrency.utilities.storage.sqlite.SQLite;
import org.bukkit.entity.Player;

import java.util.List;

public class StorageManager {

    Core core;
    SQLite sqlite;
    ConfigManager configYml;
    CurrencyManager currencyManager;
    Debugger debugger;

    String storage;

    public StorageManager(Core core) {
        this.core = core;
        this.sqlite = core.getSQLite();
        this.configYml = core.getConfigYml();
        this.currencyManager = core.getCurrencyManager();
        this.debugger = core.getDebugger();
        this.storage = configYml.getConfig().getString("storage.type");

        if (storage.equalsIgnoreCase("mysql")) {

            return;
        }

        sqlite.load();
    }

    public boolean exists(String table, String column, String row, String value) {
        if (storage.equals("mysql")) {

            return false;
        }

        return sqlite.get(table, column, row, value) != null;
    }

    /**
     * Set a specific value in a specific table!
     * @param table SQLite table
     * @param uuid Player's UUID
     * @param column SQLite column
     * @param value Value you are setting
     * @example set(currencyID, uuid, "Amount", "100"); This will set 100 tokens for the specified player UUID!
     */
    public void set(String table, String uuid, String column, String value) {
        if (storage.equalsIgnoreCase("mysql")) {

            return;
        }

        sqlite.set(table, uuid, column, value);
    }

    /**
     * Get a value from a row in a table
     * @param table SQL table
     * @param column SQL column
     * @param row SQL row
     * @param value The value the row should equal
     * @example get("players", "UUID", "Username", "Mantice"); this will return the UUID of "Mantice"
     * @return String
     */
    public String get(String table, String column, String row, String value) {
        return sqlite.get(table, column, row, value);
    }

    public void refreshCurrencyTables() {
        boolean purge = configYml.getConfig().getBoolean("storage.purge-tables");

        if (storage.equalsIgnoreCase("mysql")) {
            return;
        }

        List<String> currencies = currencyManager.getCurrencies();
        List<String> tables = sqlite.getTables();
        tables.remove("connection");

        if (purge) {
            for (String table : tables) {
                if (!currencies.contains(table)) {
                    sqlite.executeStatement("DROP TABLE " + table);
                    debugger.debug("Purged " + table + " table!");
                }
            }
        }

        for (String currency : currencies) {
            if (!tables.contains(currency)) {

                String create = "CREATE TABLE IF NOT EXISTS " + currency + " (" +
                        "`UUID` varchar(36) NOT NULL," +
                        "`Amount` varchar(128)," +
                        "PRIMARY KEY (`UUID`)" +
                        ");";

                sqlite.executeStatement(create);
                debugger.debug("Created " + currency + " table!");
            }
        }
    }

}
