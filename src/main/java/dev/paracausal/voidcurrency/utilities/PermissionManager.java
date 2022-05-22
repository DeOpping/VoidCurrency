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
        String permission = permissionsYml.getConfig().getString(location);

        if (permission == null || permission.equalsIgnoreCase("")) return true;

        if (!permission.contains("{CURRENCY}") || location.equalsIgnoreCase("info") || location.equalsIgnoreCase("help") || location.equalsIgnoreCase("reload"))
            return this.hasPermission(player, location);

        return player.hasPermission(permission.replace("{CURRENCY}", currency));
    }

    public String getPermission(String location) {
        return permissionsYml.getConfig().getString(location);
    }

}
