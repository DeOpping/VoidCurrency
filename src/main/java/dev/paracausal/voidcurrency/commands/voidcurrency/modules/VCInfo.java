package dev.paracausal.voidcurrency.commands.voidcurrency.modules;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.Formatter;
import dev.paracausal.voidcurrency.utilities.PermissionManager;
import org.bukkit.entity.Player;

public class VCInfo {

    Core core;
    Formatter formatter;
    PermissionManager permissionManager;

    public VCInfo(Core core) {
        this.core = core;
        this.formatter = core.getFormatter();
        this.permissionManager = core.getPermissionManager();
    }

    public void info() {
        core.getLogger().info("VoidCurrency v" + core.getDescription().getVersion());
        core.getLogger().info("Developed by Mantice");
    }

    public void info(Player player) {
        formatter.sendString(player, "<bold><blue>VoidCurrency <reset>v" + core.getDescription().getVersion() + "<newline>Developed by Mantice");
    }

}
