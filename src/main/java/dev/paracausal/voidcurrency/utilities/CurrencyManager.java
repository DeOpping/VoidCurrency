package dev.paracausal.voidcurrency.utilities;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;

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

    private boolean isRestriction(String id, String restriction) {
        if ((restriction.equalsIgnoreCase("maximum")) && (!this.getCurrency(id, "balance.maximum").equalsIgnoreCase("none"))) return true;

        if ((restriction.equalsIgnoreCase("minimum")) && (!this.getCurrency(id, "balance.minimum").equalsIgnoreCase("none"))) return true;

        return false;
    }

    /**
     * Checks if an amount is above or below the currency's maximum or minimum amount!
     * @param id Currency ID
     * @param amount Amount to check
     * @example restricted(id, 100); This will return null if it is not above or below the max/min, or it will return "maximum" or "minimum" depending on which it violates
     * @return string
     */
    public String restricted(String id, double amount) {
        if (this.isRestriction(id, "maximum") && (amount > Double.parseDouble(this.getCurrency(id, "balance.maximum"))))
            return "maximum";

        if (this.isRestriction(id, "minimum") && (amount > Double.parseDouble(this.getCurrency(id, "balance.maximum"))))
            return "minimum";

        return null;
    }

}
