package dev.paracausal.voidcurrency.utilities.listeners;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.CurrencyManager;
import dev.paracausal.voidcurrency.utilities.Debugger;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventJoin implements Listener {

    Core core;
    CurrencyManager currencyManager;
    StorageManager storageManager;
    Debugger debugger;

    public EventJoin(Core core) {
        this.core = core;
        this.currencyManager = core.getCurrencyManager();
        this.storageManager = core.getStorageManager();
        this.debugger = core.getDebugger();

        core.getServer().getPluginManager().registerEvents(this, core);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for (String currency : currencyManager.getCurrencies()) {
            String starting = currencyManager.getCurrency(currency, "balance.starting");

            if (!starting.equals("0") && (!starting.equalsIgnoreCase("none"))) {
                if (storageManager.exists(currency, "Amount", "UUID", player.getUniqueId().toString())) return;
                storageManager.set(currency, player.getUniqueId().toString(), "Amount", starting);
                debugger.debug("Set " + player.getName() + "'s starting balance for " + currency + " to " + starting + "!");
            }
        }
    }

}
