package dev.paracausal.voidcurrency.commands.balance;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.CurrencyManager;
import dev.paracausal.voidcurrency.utilities.Formatter;
import dev.paracausal.voidcurrency.utilities.PermissionManager;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Balance implements CommandExecutor {

    Core core;
    Formatter formatter;
    PermissionManager permissionManager;
    CurrencyManager currencyManager;
    StorageManager storageManager;

    public Balance(Core core) {
        this.core = core;
        this.formatter = core.getFormatter();
        this.permissionManager = core.getPermissionManager();
        this.currencyManager = core.getCurrencyManager();
        this.storageManager = core.getStorageManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            if (args.length != 1) {
                core.getLogger().info("Please select a player!");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (!target.hasPlayedBefore()) {
                core.getLogger().info("Invalid player!");
                return true;
            }

            core.getLogger().info(target.getName() + "'s balance:");
            for (String currency : currencyManager.getCurrencies()) {
                core.getLogger().info(currencyManager.getCurrency(currency, "name") + ": " + storageManager.get(currency, "Amount", "UUID", target.getUniqueId().toString()));
            }

            return true;
        }

        if (!permissionManager.hasPermission(player, "balance")) {
            formatter.sendMessage(player, "no-permission");
            return true;
        }

        if (args.length == 1 && permissionManager.hasPermission(player, "balance-other")) {

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (target.hasPlayedBefore()) {
                formatter.sendMessage(target.getPlayer(), player, "balance.other");
                return true;
            }
        }

        formatter.sendMessage(player, "balance.self");
        return true;
    }

}
