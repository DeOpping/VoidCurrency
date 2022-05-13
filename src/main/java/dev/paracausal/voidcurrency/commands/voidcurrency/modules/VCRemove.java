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

public class VCRemove {

    Core core;
    StorageManager storageManager;
    CurrencyManager currencyManager;
    Debugger debugger;
    Formatter formatter;
    PermissionManager permissionManager;

    public VCRemove(Core core) {
        this.core = core;
        this.storageManager = core.getStorageManager();
        this.currencyManager = core.getCurrencyManager();
        this.debugger = core.getDebugger();
        this.formatter = core.getFormatter();
        this.permissionManager = core.getPermissionManager();
    }

    public void remove(OfflinePlayer target, String currency, BigDecimal amount, boolean restrict, boolean silent) {

        String bal = storageManager.get(currency, "Amount", "UUID", target.getUniqueId().toString());
        BigDecimal balance = BigDecimal.valueOf(Double.parseDouble(bal));

        BigDecimal newAmount = balance.subtract(amount);

        if (restrict && currencyManager.restricted(currency, "minimum", newAmount)) {
            String minimum = currencyManager.getCurrency(currency, "balance.minimum");
            storageManager.set(currency, target.getUniqueId().toString(), "Amount", minimum);
            core.getLogger().info("Set " + target.getName() + "'s " + currency + " balance to " + minimum + " (currency's minimum)!");

            if (!silent && target.isOnline())
                formatter.notify(target.getPlayer(), "set-to", currency, BigDecimal.valueOf(Double.parseDouble(minimum)), target);

            return;
        }

        storageManager.set(currency, target.getUniqueId().toString(), "Amount", String.valueOf(newAmount));
        core.getLogger().info("Set " + target.getName() + "'s " + currency + " balance to " + newAmount + "!");

        if (!silent && target.isOnline())
            formatter.notify(target.getPlayer(), "set-to", currency, newAmount, target);
    }

    public void remove(Player player, OfflinePlayer target, String currency, BigDecimal amount, boolean restrict, boolean silent) {

        if (!permissionManager.hasPermission(player, "remove")) {
            formatter.sendMessage(player, "no-permission");
            return;
        }

        String bal = storageManager.get(currency, "Amount", "UUID", target.getUniqueId().toString());
        BigDecimal balance = BigDecimal.valueOf(Double.parseDouble(bal));

        BigDecimal newAmount = balance.subtract(amount);

        if (restrict && currencyManager.restricted(currency, "minimum", newAmount)) {
            String minimum = currencyManager.getCurrency(currency, "balance.minimum");
            storageManager.set(currency, target.getUniqueId().toString(), "Amount", minimum);

            if (!silent && target.isOnline())
                formatter.notify(target.getPlayer(), "set-to", currency, BigDecimal.valueOf(Double.parseDouble(minimum)), target);

            return;
        }

        storageManager.set(currency, target.getUniqueId().toString(), "Amount", String.valueOf(newAmount));
        formatter.notify(player, "set", currency, newAmount, target);

        if (!silent && target.isOnline())
            formatter.notify(target.getPlayer(), "set-to", currency, newAmount, target);
    }

}
