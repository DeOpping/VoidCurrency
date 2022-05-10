package dev.paracausal.voidcurrency.commands;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.commands.voidcurrency.VoidCurrency;
import dev.paracausal.voidcurrency.commands.voidcurrency.VoidCurrencyModules;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;

public class CommandManager {

    Core core;
    ConfigManager configYml;

    public CommandManager(Core core) {
        this.core = core;
        this.configYml = core.getConfigYml();
    }

    public void initialize() {
        this.voidCurrencyModules = new VoidCurrencyModules(core);
        this.voidCurrency = new VoidCurrency(core);

        core.getServer().getPluginCommand("voidcurrency").setExecutor(voidCurrency);
        core.getServer().getPluginCommand("voidcurrency").setAliases(configYml.getConfig().getStringList("alias"));
    }

    private VoidCurrency voidCurrency;
    public VoidCurrency getVoidCurrency() { return voidCurrency; }

    private VoidCurrencyModules voidCurrencyModules;
    public VoidCurrencyModules getVoidCurrencyModules() { return voidCurrencyModules; }

}
