package dev.paracausal.voidcurrency.commands.voidcurrency;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.commands.CommandManager;
import dev.paracausal.voidcurrency.utilities.CurrencyManager;
import dev.paracausal.voidcurrency.utilities.Debugger;
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

import java.util.UUID;

public class VoidCurrency implements CommandExecutor {

    Core core;
    Formatter formatter;
    StorageManager storageManager;
    CurrencyManager currencyManager;
    Debugger debugger;
    PermissionManager permissionManager;
    CommandManager commandManager;
    VoidCurrencyModules voidCurrencyModules;

    public VoidCurrency(Core core) {
        this.core = core;
        this.formatter = core.getFormatter();
        this.storageManager = core.getStorageManager();
        this.currencyManager = core.getCurrencyManager();
        this.debugger = core.getDebugger();
        this.permissionManager = core.getPermissionManager();
        this.commandManager = core.getCommandManager();
        this.voidCurrencyModules = commandManager.getVoidCurrencyModules();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // /vc    <set/add/give/take/remove> <currency> <player> <amount> (true/false) (-s)
        // Arg[#]              0                 1          2       3          4         5
        // Arg Length          1                 2          3       4          5         6
        // <required> (optional)

        if (sender instanceof Player player && !permissionManager.hasPermission(player, "admin")) {
            formatter.sendMessage(player, "no-permission");
            return true;
        }

        if (args.length < 4) {
            if (!(sender instanceof Player player)) {
                core.getLogger().info("Syntax: vc <set/add/give/take/remove> <currency ID> <player> <amount> (true/false) (-s)");
                core.getLogger().info("<required> (optional)");
                return true;
            }

            formatter.sendMessage(player, "syntax");
            return true;
        }

        String type = args[0];

        String currency = args[1];
        if (!currencyManager.getCurrencies().contains(currency)) {
            if (!(sender instanceof Player player)) {
                core.getLogger().info("Invalid currency!");
                return true;
            }

            formatter.sendMessage(player, "invalid-currency");
            return true;
        }

        if (!storageManager.exists("players", "Username", args[2])) {
            if (!(sender instanceof Player player)) {
                core.getLogger().info("That user has never played on here before!");
                return true;
            }

            formatter.sendMessage(player, "invalid-player");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(storageManager.getUUID(args[2])));

        double amount;
        try {
            amount = Double.parseDouble(args[3]);
        } catch (NumberFormatException exception) {
            if (!(sender instanceof Player player)) {
                core.getLogger().info("Invalid amount!");
                return true;
            }

            formatter.sendMessage(player, "invalid-number");
            return true;
        }

        boolean restricted = true;
        boolean silent = false;

        if (args.length > 4 && args.length < 7) {
            try {
                restricted = Boolean.parseBoolean(args[4]);
            } catch (IllegalArgumentException exception) {
                return true;
            }

            if (args[5].equalsIgnoreCase("-s")) silent = true;
        }

        if (type.equalsIgnoreCase("add") || type.equalsIgnoreCase("give")) {
            if (sender instanceof Player player && !permissionManager.hasPermission(player, "give", currency)) {
                formatter.sendMessage(player, "no-permission");
                return true;
            }

            voidCurrencyModules.add(sender, currency, target, amount, restricted, silent);
        }

        if (type.equalsIgnoreCase("take") || type.equalsIgnoreCase("remove")) {
            if (sender instanceof Player player && !permissionManager.hasPermission(player, "take", currency)) {
                formatter.sendMessage(player, "no-permission");
                return true;
            }

            voidCurrencyModules.remove(sender, currency, target, amount, restricted, silent);
        }

        if (type.equalsIgnoreCase("set")) {
            if (sender instanceof Player player && !permissionManager.hasPermission(player, "set", currency)) {
                formatter.sendMessage(player, "no-permission");
                return true;
            }

            voidCurrencyModules.set(sender, currency, target, amount, restricted, silent);
        }

        return true;
    }

}
