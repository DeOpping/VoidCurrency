package dev.paracausal.voidcurrency.commands.voidcurrency.modules;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.CurrencyManager;
import dev.paracausal.voidcurrency.utilities.Debugger;
import dev.paracausal.voidcurrency.utilities.Formatter;
import dev.paracausal.voidcurrency.utilities.PermissionManager;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class VCSet {

    Core core;
    StorageManager storageManager;
    CurrencyManager currencyManager;
    Debugger debugger;
    Formatter formatter;
    PermissionManager permissionManager;

    public VCSet(Core core) {
        this.core = core;
        this.storageManager = core.getStorageManager();
        this.currencyManager = core.getCurrencyManager();
        this.debugger = core.getDebugger();
        this.formatter = core.getFormatter();
        this.permissionManager = core.getPermissionManager();
    }

    public void set(OfflinePlayer target, String currency, BigDecimal amount, boolean restrict, boolean silent) {

        if (restrict && currencyManager.restricted(currency, "maximum", amount)) {
            String maximum = currencyManager.getCurrency(currency, "balance.maximum");
            storageManager.set(currency, target.getUniqueId().toString(), "Amount", maximum);

            if (!silent && target.isOnline())
                formatter.notify(target.getPlayer(), "set-to", currency, BigDecimal.valueOf(Double.parseDouble(maximum)), target);

            return;
        }

        if (restrict && currencyManager.restricted(currency, "minimum", amount)) {
            String minimum = currencyManager.getCurrency(currency, "balance.minimum");
            storageManager.set(currency, target.getUniqueId().toString(), "Amount", minimum);
            core.getLogger().info("Set " + target.getName() + "'s " + currency + " balance to " + minimum + " (currency's minimum)!");

            if (!silent && target.isOnline())
                formatter.notify(target.getPlayer(), "set-to", currency, BigDecimal.valueOf(Double.parseDouble(minimum)), target);

            return;
        }

        storageManager.set(currency, target.getUniqueId().toString(), "Amount", String.valueOf(amount));
        core.getLogger().info("Set " + target.getName() + "'s " + currency + " balance to " + amount + "!");

        if (!silent && target.isOnline())
            formatter.notify(target.getPlayer(), "set-to", currency, amount, target);
    }

    public void set(Player player, OfflinePlayer target, String currency, BigDecimal amount, boolean restrict, boolean silent) {

        if (!permissionManager.hasPermission(player, "set")) {
            formatter.sendMessage(player, "no-permission");
            return;
        }

        if (restrict && currencyManager.restricted(currency, "maximum", amount)) {
            String maximum = currencyManager.getCurrency(currency, "balance.maximum");
            storageManager.set(currency, target.getUniqueId().toString(), "Amount", maximum);

            if (!silent && target.isOnline())
                formatter.notify(target.getPlayer(), "set-to", currency, BigDecimal.valueOf(Double.parseDouble(maximum)), target);

            return;
        }

        if (restrict && currencyManager.restricted(currency, "minimum", amount)) {
            String minimum = currencyManager.getCurrency(currency, "balance.minimum");
            storageManager.set(currency, target.getUniqueId().toString(), "Amount", minimum);
            formatter.notify(player, "set", currency, BigDecimal.valueOf(Double.parseDouble(minimum)), target);

            if (!silent && target.isOnline())
                formatter.notify(target.getPlayer(), "set-to", currency, BigDecimal.valueOf(Double.parseDouble(minimum)), target);

            return;
        }

        storageManager.set(currency, target.getUniqueId().toString(), "Amount", String.valueOf(amount));
        formatter.notify(player, "set", currency, amount, target);

        if (!silent && target.isOnline())
            formatter.notify(target.getPlayer(), "set-to", currency, amount, target);
    }

}
