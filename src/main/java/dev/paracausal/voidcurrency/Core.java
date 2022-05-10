package dev.paracausal.voidcurrency;

import dev.paracausal.voidcurrency.commands.CommandManager;
import dev.paracausal.voidcurrency.utilities.CurrencyManager;
import dev.paracausal.voidcurrency.utilities.Debugger;
import dev.paracausal.voidcurrency.utilities.Formatter;
import dev.paracausal.voidcurrency.utilities.PermissionManager;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;
import dev.paracausal.voidcurrency.utilities.listeners.EventJoin;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;
import dev.paracausal.voidcurrency.utilities.storage.sqlite.SQLite;
import me.clip.placeholderapi.libs.kyori.adventure.Adventure;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    @Override
    public void onLoad() {
        this.configYml = new ConfigManager(this, "config");
        configYml.saveDefaultConfig();
        configYml.updateConfig();

        this.debugger = new Debugger(this);

        debugger.debug("Saved and updated config.yml!");

        this.messagesYml = new ConfigManager(this, "messages");
        messagesYml.saveDefaultConfig();
        messagesYml.updateConfig();
        debugger.debug("Saved and updated messages.yml!");

        this.permissionsYml = new ConfigManager(this, "permissions");
        permissionsYml.saveDefaultConfig();
        permissionsYml.updateConfig();
        debugger.debug("Saved and updated permissions.yml!");

        this.currencyYml = new ConfigManager(this, "currency");
        currencyYml.saveDefaultConfig();
        debugger.debug("Saved currency.yml!");

        this.currencyManager = new CurrencyManager(this);

        this.sqlite = new SQLite(this);
        this.storageManager = new StorageManager(this);

    }

    @Override
    public void onEnable() {
        this.formatter = new Formatter(this);
        this.permissionManager = new PermissionManager(this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            debugger.debug("PlaceholderAPI hook enabled!");
        }

        this.commandManager = new CommandManager(this);
        commandManager.initialize();

        new EventJoin(this);

        getLogger().info("VoidCurrency enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("VoidCurrency disabled!");
    }

    private SQLite sqlite;
    public SQLite getSQLite() { return sqlite; }

    private StorageManager storageManager;
    public StorageManager getStorageManager() { return storageManager; }

    private ConfigManager configYml;
    public ConfigManager getConfigYml() { return configYml; }
    private ConfigManager messagesYml;
    public ConfigManager getMessagesYml() { return messagesYml; }
    private ConfigManager permissionsYml;
    public ConfigManager getPermissionsYml() { return permissionsYml; }
    private ConfigManager currencyYml;
    public ConfigManager getCurrencyYml() { return currencyYml; }

    private Formatter formatter;
    public Formatter getFormatter() { return formatter; }

    private CurrencyManager currencyManager;
    public CurrencyManager getCurrencyManager() { return currencyManager; }

    private Debugger debugger;
    public Debugger getDebugger() { return debugger; }

    private PermissionManager permissionManager;
    public PermissionManager getPermissionManager() { return permissionManager; }

    private CommandManager commandManager;
    public CommandManager getCommandManager() { return commandManager; }

}
