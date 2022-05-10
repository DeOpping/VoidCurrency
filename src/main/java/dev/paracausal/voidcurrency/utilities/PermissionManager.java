package dev.paracausal.voidcurrency.utilities;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;
import org.bukkit.entity.Player;

public class PermissionManager {

    Core core;
    ConfigManager permissionsYml;

    public PermissionManager(Core core) {
        this.core = core;
        this.permissionsYml = core.getPermissionsYml();
    }

    public boolean hasPermission(Player player, String location) {
        String permission = permissionsYml.getConfig().getString(location);

        if (permission == null || permission.equalsIgnoreCase("")) {
            return true;
        }

        return player.hasPermission(permission);
    }

    public boolean hasPermission(Player player, String location, String currency) {
        if (location.equalsIgnoreCase("admin") || location.equalsIgnoreCase("reload"))
            return this.hasPermission(player, location);

        String permission = permissionsYml.getConfig().getString(location);

        if (permission == null || permission.equalsIgnoreCase("")) return true;

        return player.hasPermission(permission.replace("{CURRENCY}", currency));
    }

}
