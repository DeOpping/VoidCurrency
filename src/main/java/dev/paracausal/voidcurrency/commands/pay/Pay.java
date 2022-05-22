package dev.paracausal.voidcurrency.commands.pay;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.CurrencyManager;
import dev.paracausal.voidcurrency.utilities.Formatter;
import dev.paracausal.voidcurrency.utilities.PermissionManager;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pay implements CommandExecutor {

    Core core;
    StorageManager storageManager;
    CurrencyManager currencyManager;
    ConfigManager currencyYml;
    Formatter formatter;
    PermissionManager permissionManager;

    public Pay(Core core) {
        this.core = core;
        this.storageManager = core.getStorageManager();
        this.currencyManager = core.getCurrencyManager();
        this.currencyYml = core.getCurrencyYml();
        this.formatter = core.getFormatter();
        this.permissionManager = core.getPermissionManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            core.getLogger().info("This command is for players only!");
            core.getLogger().info("Try 'vc set/add/remove' instead!");
            return true;
        }

        if (!permissionManager.getPermission("pay").contains("{CURRENCY}") && !permissionManager.hasPermission(player, "pay")) {
            formatter.sendMessage(player, "no-permission");
            return true;
        }

        if (args.length == 0) {
            formatter.sendMessage(player, "select-player");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!target.hasPlayedBefore()) {
            formatter.sendMessage(player, "invalid-player");
            return true;
        }

        if (target.getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            formatter.sendMessage(player, "pay.yourself");
            return true;
        }

        if (args.length == 1) {
            formatter.sendMessage(player, "select-currency");
            return true;
        }

        String currency = args[1];

        if (!this.payAlias().contains(currency)) {
            formatter.sendMessage(player, "invalid-currency");
            return true;
        }

        // /pay <player> <currency> <amount> (confirm)

        if (args.length == 2) {
            formatter.sendMessage(player, "select-amount");
            return true;
        }

        BigDecimal amount;

        try {
            amount = BigDecimal.valueOf(Double.parseDouble(args[2]));
        } catch (NumberFormatException exception) {
            formatter.sendMessage(player, "invalid-amount");
            return true;
        }

        if (permissionManager.getPermission("pay").contains("{CURRENCY}") && !permissionManager.hasPermission(player, "pay", args[1])) {
            formatter.sendMessage(player, "no-permission");
            return true;
        }

        if (this.confirm(args[1]) && args.length == 3) {
            formatter.sendMessage(player, "pay.confirm", "{COMMAND}", "/pay " + args[0] + " " + args[1] + " " + args[2] + " confirm");
            return true;
        }

        if (storageManager.get("toggle", "Pay", "UUID", target.getUniqueId().toString()) == "false") {
            formatter.sendMessage(player, "pay.not-accepting", "{PLAYER}", target.getName());
            return true;
        }

        if (this.confirm(args[1]) && args.length == 4) {
            formatter.notify(player, "pay.success", args[1], amount, target);
        }

        return true;
    }

    private List<String> payAlias() {
        List<String> aliases = new ArrayList<>();

        for (String currency : currencyManager.getCurrencies()) {
            Object alias = currencyYml.getConfig().get("currencies." + currency + ".pay.aliases");

            if (alias instanceof List) {
                aliases.addAll(currencyYml.getConfig().getStringList("currencies." + currency + ".pay.aliases"));
            }

            else if (alias instanceof String) {
                aliases.add(alias.toString());
            }
        }

        return aliases;
    }

    private boolean confirm(String id) {
        return currencyYml.getConfig().getBoolean("currencies." + id + ".pay.confirm");
    }

    private void pay(Player player, OfflinePlayer target, String currency, BigDecimal amount) {

        BigDecimal playerBalance = currencyManager.getBalance(player.getUniqueId().toString(), currency);
        BigDecimal targetBalance = currencyManager.getBalance(target.getUniqueId().toString(), currency);

        if (amount.compareTo(playerBalance) > 0) {
            formatter.sendMessage(player, "pay.not-enough");
            return;
        }



    }

}
