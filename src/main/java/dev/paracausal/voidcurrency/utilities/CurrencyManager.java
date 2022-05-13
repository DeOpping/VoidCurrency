package dev.paracausal.voidcurrency.utilities;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CurrencyManager {

    Core core;
    ConfigManager currencyYml;
    StorageManager storageManager;
    Debugger debugger;

    public CurrencyManager(Core core) {
        this.core = core;
        this.currencyYml = core.getCurrencyYml();
        this.storageManager = core.getStorageManager();
        this.debugger = core.getDebugger();
    }

    /**
     * A string list of all the currency IDs in currency.yml
     * @return List
     */
    public List<String> getCurrencies() {
        return new ArrayList<>(currencyYml.getConfig().getConfigurationSection("currencies").getKeys(false));
    }

    /**
     * Returns the value for the specified currency
     * @param id Currency ID
     * @param value Value you're getting
     * @example getCurrency(id, "symbol"); This will return the currency's symbol!
     * @return String
     */
    public String getCurrency(String id, String value) {
        return currencyYml.getConfig().getString("currencies." + id + "." + value);
    }

    public boolean restricted(String id, String type, BigDecimal checked) {
        String setting = this.getCurrency(id, "balance." + type);

        BigDecimal amount = null;

        if (!setting.equalsIgnoreCase("none")) {
            try {
                amount = BigDecimal.valueOf(Double.parseDouble(setting));
            } catch (NumberFormatException exception) {
                core.getLogger().info("A balance minimum or maximum amount for " + id + " is not a number!");
            }
        }

        if (type.equalsIgnoreCase("maximum"))
            return (amount.compareTo(checked) > 0);

        if (type.equalsIgnoreCase("minimum"))
            return (amount.compareTo(checked) < 0);

        return false;
    }

}
