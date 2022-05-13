package dev.paracausal.voidcurrency.commands.voidcurrency;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.commands.CommandManager;
import dev.paracausal.voidcurrency.commands.voidcurrency.modules.*;
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

import java.math.BigDecimal;

public class VoidCurrency implements CommandExecutor {

    Core core;
    Formatter formatter;
    StorageManager storageManager;
    CurrencyManager currencyManager;
    Debugger debugger;
    PermissionManager permissionManager;
    CommandManager commandManager;

    VCInfo vcInfo;
    VCHelp vcHelp;
    VCReload vcReload;

    VCSet vcSet;
    VCAdd vcAdd;
    VCRemove vcRemove;

    public VoidCurrency(Core core) {
        this.core = core;
        this.formatter = core.getFormatter();
        this.storageManager = core.getStorageManager();
        this.currencyManager = core.getCurrencyManager();
        this.debugger = core.getDebugger();
        this.permissionManager = core.getPermissionManager();
        this.commandManager = core.getCommandManager();

        this.vcInfo = commandManager.getVcInfo();
        this.vcHelp = commandManager.getVcHelp();
        this.vcReload = commandManager.getVcReload();

        this.vcSet = commandManager.getVcSet();
        this.vcAdd = commandManager.getVcAdd();
        this.vcRemove = commandManager.getVcRemove();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help"))
                    vcHelp.help();

                if (args[0].equalsIgnoreCase("reload"))
                    vcReload.reload();

                if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove"))
                    core.getLogger().info("Please select a player!");

                return true;
            }

            if (args.length == 2 && ((args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")))) {
                core.getLogger().info("Please select a currency!");
                return true;
            }

            if (args.length == 3 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove"))) {
                core.getLogger().info("Please select an amount!");
                return true;
            }

            if (args.length > 3 && ((args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")))) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    core.getLogger().info("Invalid player!");
                    return true;
                }

                String currency = args[2];
                BigDecimal amount;

                try {
                    amount = BigDecimal.valueOf(Double.parseDouble(args[3]));
                } catch (NumberFormatException exception) {
                    core.getLogger().info("Invalid amount! You must input a number!");
                    return true;
                }

                boolean restrict = true;
                boolean silent = false;

                if (args.length == 5 || args.length == 6) {
                    if (args[4].equalsIgnoreCase("-r") || args[5].equalsIgnoreCase("-r")) restrict = false;
                    silent = (args[4].equalsIgnoreCase("-s") || args[5].equalsIgnoreCase("-s"));
                }

                if (args[0].equalsIgnoreCase("set"))
                    vcSet.set(target, currency, amount, restrict, silent);

                if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add"))
                    vcAdd.add(target, currency, amount, restrict, silent);

                if (args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove"))
                    vcRemove.remove(target, currency, amount, restrict, silent);

                return true;
            }

            vcInfo.info();
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help"))
                vcHelp.help(player);

            if (args[0].equalsIgnoreCase("reload"))
                vcReload.reload(player);

            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove"))
                formatter.sendMessage(player, "select-player");

            return true;
        }

        if (args.length == 2 && ((args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")))) {
            formatter.sendMessage(player, "select-currency");
            return true;
        }

        if (args.length == 3 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove"))) {
            formatter.sendMessage(player, "select-amount");
            return true;
        }

        if (args.length > 3 && ((args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")))) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            if (!target.hasPlayedBefore()) {
                formatter.sendMessage(player, "invalid-player");
                return true;
            }

            String currency = args[2];
            BigDecimal amount;

            try {
                amount = BigDecimal.valueOf(Double.parseDouble(args[3]));
            } catch (NumberFormatException exception) {
                formatter.sendMessage(player, "invalid-amount");
                return true;
            }

            boolean restrict = true;
            boolean silent = false;

            if (args.length == 5 || args.length == 6) {
                if (args[4].equalsIgnoreCase("-r") || args[5].equalsIgnoreCase("-r")) restrict = false;
                silent = (args[4].equalsIgnoreCase("-s") || args[5].equalsIgnoreCase("-s"));
            }

            if (args[0].equalsIgnoreCase("set"))
                vcSet.set(player, target, currency, amount, restrict, silent);

            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add"))
                vcAdd.add(player, target, currency, amount, restrict, silent);

            if (args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove"))
                vcRemove.remove(player, target, currency, amount, restrict, silent);

            return true;
        }

        vcInfo.info(player);
        return true;
    }

}
