package dev.paracausal.voidcurrency.commands.voidcurrency;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.CurrencyManager;
import dev.paracausal.voidcurrency.utilities.Debugger;
import dev.paracausal.voidcurrency.utilities.Formatter;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoidCurrencyModules {

    Core core;
    Formatter formatter;
    StorageManager storageManager;
    CurrencyManager currencyManager;
    Debugger debugger;

    public VoidCurrencyModules(Core core) {
        this.core = core;
        this.formatter = core.getFormatter();
        this.storageManager = core.getStorageManager();
        this.currencyManager = core.getCurrencyManager();
        this.debugger = core.getDebugger();
    }

    public void notify(OfflinePlayer target, String location) {
        if (!target.isOnline()) {
            return;
        }

        formatter.sendMessage(target.getPlayer(), location);
    }

    public void add(CommandSender sender, String currency, OfflinePlayer target, Double amount, boolean restrict, boolean silent) {
        double current = Double.parseDouble(storageManager.get(currency, "Amount", "UUID", target.getUniqueId().toString()));
        double newAmount = current + amount;

        if (restrict) {
            if (currencyManager.restricted(currency, newAmount).equalsIgnoreCase("maximum")) {
                storageManager.set(currency, target.getUniqueId().toString(), "Amount", currencyManager.getCurrency(currency, "balance.maximum"));
                debugger.debug("Set " + target.getName() + "'s " + currency + " balance to " + currencyManager.getCurrency(currency, "balance.maximum"));
                return;
            }

            if (currencyManager.restricted(currency, newAmount).equalsIgnoreCase("minimum")) {
                storageManager.set(currency, target.getUniqueId().toString(), "Amount", currencyManager.getCurrency(currency, "balance.minimum"));
                debugger.debug("Set " + target.getName() + "'s " + currency + " balance to " + currencyManager.getCurrency(currency, "balance.minimum"));
                return;
            }
        }

        storageManager.set(currency, target.getUniqueId().toString(), "Amount", String.valueOf(newAmount));
        debugger.debug("Set " + target.getName() + "'s " + currency + " balance to " + newAmount);
        if (!silent) this.notify(target, "given");

        if (!(sender instanceof Player player)) {
            if (!silent) core.getLogger().info("Success!");
            return;
        }

        formatter.sendMessage(player, "give");
    }

    public void remove(CommandSender sender, String currency, OfflinePlayer target, Double amount, boolean restrict, boolean silent) {
        double current = Double.parseDouble(storageManager.get(currency, "Amount", "UUID", target.getUniqueId().toString()));
        double newAmount = current - amount;

        if (restrict) {
            if (currencyManager.restricted(currency, newAmount).equalsIgnoreCase("maximum")) {
                storageManager.set(currency, target.getUniqueId().toString(), "Amount", currencyManager.getCurrency(currency, "balance.maximum"));
                debugger.debug("Set " + target.getName() + "'s " + currency + " balance to " + currencyManager.getCurrency(currency, "balance.maximum"));
                return;
            }

            if (currencyManager.restricted(currency, newAmount).equalsIgnoreCase("minimum")) {
                storageManager.set(currency, target.getUniqueId().toString(), "Amount", currencyManager.getCurrency(currency, "balance.minimum"));
                debugger.debug("Set " + target.getName() + "'s " + currency + " balance to " + currencyManager.getCurrency(currency, "balance.minimum"));
                return;
            }
        }

        storageManager.set(currency, target.getUniqueId().toString(), "Amount", String.valueOf(newAmount));
        debugger.debug("Set " + target.getName() + "'s " + currency + " balance to " + newAmount);
        if (!silent) this.notify(target, "taken");

        if (!(sender instanceof Player player)) {
            if (!silent) core.getLogger().info("Success!");
            return;
        }

        formatter.sendMessage(player, "take");
    }

    public void set(CommandSender sender, String currency, OfflinePlayer target, Double amount, boolean restrict, boolean silent) {
        if (restrict) {
            if (currencyManager.restricted(currency, amount).equalsIgnoreCase("maximum")) {
                storageManager.set(currency, target.getUniqueId().toString(), "Amount", currencyManager.getCurrency(currency, "balance.maximum"));
                debugger.debug("Set " + target.getName() + "'s " + currency + " balance to " + currencyManager.getCurrency(currency, "balance.maximum"));
                return;
            }

            if (currencyManager.restricted(currency, amount).equalsIgnoreCase("minimum")) {
                storageManager.set(currency, target.getUniqueId().toString(), "Amount", currencyManager.getCurrency(currency, "balance.minimum"));
                debugger.debug("Set " + target.getName() + "'s " + currency + " balance to " + currencyManager.getCurrency(currency, "balance.minimum"));
                return;
            }
        }

        storageManager.set(currency, target.getUniqueId().toString(), "Amount", String.valueOf(amount));
        debugger.debug("Set " + target.getName() + "'s " + currency + " balance to " + amount);
        if (!silent) this.notify(target, "set-to");

        if (!(sender instanceof Player player)) {
            if (!silent) core.getLogger().info("Success!");
            return;
        }

        formatter.sendMessage(player, "set");
    }

}
