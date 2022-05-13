package dev.paracausal.voidcurrency.utilities;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;

public class Debugger {

    Core core;
    ConfigManager configYml;

    public Debugger(Core core) {
        this.core = core;
        this.configYml = core.getConfigYml();
    }

    public void debug(String string) {
        if (configYml.getConfig().getBoolean("debug-mode"))
            core.getServer().getLogger().info("[VoidCurrency] " + string);
    }

}
