package dev.paracausal.voidcurrency.commands.voidcurrency.modules;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.Debugger;
import dev.paracausal.voidcurrency.utilities.Formatter;
import dev.paracausal.voidcurrency.utilities.PermissionManager;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;
import org.bukkit.entity.Player;

public class VCReload {

    Core core;
    Formatter formatter;
    PermissionManager permissionManager;
    StorageManager storageManager;
    Debugger debugger;

    public VCReload(Core core) {
        this.core = core;
        this.formatter = core.getFormatter();
        this.permissionManager = core.getPermissionManager();
        this.storageManager = core.getStorageManager();
        this.debugger = core.getDebugger();
    }

    public void reload() {
        debugger.debug("Reloading config.yml...");
        core.getConfigYml().saveDefaultConfig();
        core.getConfigYml().reloadConfig();
        debugger.debug("Reloaded config.yml!");

        debugger.debug("Reloading messages.yml...");
        core.getMessagesYml().saveDefaultConfig();
        core.getMessagesYml().reloadConfig();
        debugger.debug("Reloaded messages.yml!");

        debugger.debug("Reloading permissions.yml...");
        core.getPermissionsYml().saveDefaultConfig();
        core.getPermissionsYml().reloadConfig();
        debugger.debug("Reloaded permissions.yml!");

        debugger.debug("Reloading currencies.yml...");
        core.getCurrencyYml().saveDefaultConfig();
        core.getCurrencyYml().reloadConfig();
        debugger.debug("Reloaded currencies.yml!");

        debugger.debug("Refreshing database tables...");
        storageManager.refreshCurrencyTables();
        debugger.debug("Database tables refreshed!");
    }

    public void reload(Player player) {
        if (!permissionManager.hasPermission(player, "reload")) {
            formatter.sendMessage(player, "no-permission");
            return;
        }

        this.reload();
        formatter.sendMessage(player, "reload");
    }

}
