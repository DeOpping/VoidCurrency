package dev.paracausal.voidcurrency.commands.pay;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.Formatter;
import dev.paracausal.voidcurrency.utilities.PermissionManager;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PayToggle implements CommandExecutor {

    Core core;
    StorageManager storageManager;
    Formatter formatter;
    PermissionManager permissionManager;

    public PayToggle(Core core) {
        this.core = core;
        this.storageManager = core.getStorageManager();
        this.formatter = core.getFormatter();
        this.permissionManager = core.getPermissionManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            core.getLogger().info("This command is for players only!");
            return true;
        }

        if (!permissionManager.hasPermission(player, "pay-toggle")) {
            formatter.sendMessage(player, "no-permission");
            return true;
        }

        if ((storageManager.get("toggle", "Pay", "UUID", player.getUniqueId().toString()) == null) || (storageManager.get("toggle", "Pay", "UUID", player.getUniqueId().toString()).equalsIgnoreCase("true"))) {
            formatter.sendMessage(player, "pay-toggle.off");
            storageManager.set("toggle", player.getUniqueId().toString(), "Pay", "false");
            return true;
        }

        formatter.sendMessage(player, "pay-toggle.on");
        storageManager.set("toggle", player.getUniqueId().toString(), "Pay", "true");

        return true;
    }
}
