package dev.paracausal.voidcurrency.commands.voidcurrency.modules;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.Formatter;
import dev.paracausal.voidcurrency.utilities.PermissionManager;
import org.bukkit.entity.Player;

public class VCHelp {

    Core core;
    Formatter formatter;
    PermissionManager permissionManager;

    public VCHelp(Core core) {
        this.core = core;
        this.formatter = core.getFormatter();
        this.permissionManager = core.getPermissionManager();
    }

    public void help() {
        core.getLogger().info("VoidCurrency Commands:");
        core.getLogger().info("| 'vc' - Shows plugin information");
        core.getLogger().info("| 'vc help' - Shows this information");
        core.getLogger().info("| 'vc reload' - Reloads configuration files");
        core.getLogger().info("| 'vc set <player> <currencyID> <amount> (-r -s)");
        core.getLogger().info("| 'vc <give/add> <player> <currencyID> <amount> (-r -s)");
        core.getLogger().info("| 'vc <take/remove> <player> <currencyID> <amount> (-r -s)");
        core.getLogger().info("<required arg> (optional arg)");
    }

    public void help(Player player) {
        if (!permissionManager.hasPermission(player, "help")) {
            formatter.sendMessage(player, "no-permission");
            return;
        }

        formatter.sendMessage(player, "help");
    }

}
