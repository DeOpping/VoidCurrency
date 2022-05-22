package dev.paracausal.voidcurrency.utilities;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;
import org.bukkit.OfflinePlayer;

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
        String setting = this.getCurrency(id, "balance." + type.toLowerCase());

        BigDecimal amount = null;

        if (!setting.equalsIgnoreCase("none")) {
            try {
                amount = BigDecimal.valueOf(Double.parseDouble(setting));
            } catch (NumberFormatException exception) {
                core.getLogger().info("A balance minimum or maximum amount for " + id + " is not a number!");
            }
        } else {
            return false;
        }

        if (type.equalsIgnoreCase("maximum"))
            return (checked.compareTo(amount) > 0);

        if (type.equalsIgnoreCase("minimum"))
            return (checked.compareTo(amount) < 0);

        return false;
    }

    public BigDecimal getBalance(String uuid, String currency) {
        BigDecimal start;
        String starting = this.getCurrency(currency, "balance.starting");
        if (starting.equalsIgnoreCase("none") || starting.equals("0") || starting.equals("0.0")) {
            start = BigDecimal.valueOf(0);
        } else {
            start = BigDecimal.valueOf(Double.parseDouble(starting));
        }

        if (!storageManager.exists(currency, "Amount", "UUID", uuid)) return start;
        return BigDecimal.valueOf(Double.parseDouble(storageManager.get(currency, "Amount", "UUID", uuid)));
    }

}
