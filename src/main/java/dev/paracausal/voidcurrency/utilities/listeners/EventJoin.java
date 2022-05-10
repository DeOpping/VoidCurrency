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

        if (!storageManager.exists("players", "UUID", player.getUniqueId().toString())) {
            storageManager.addPlayer(player);
            debugger.debug("Added " + player.getName() + " to the database!");
        }

        if (!storageManager.getUsername(player.getUniqueId().toString()).equals(player.getName())) {
            storageManager.set("players", player.getUniqueId().toString(), "Username", player.getName());
            debugger.debug("Updated " + player.getName() + "'s username in the database!");
        }

        for (String currency : currencyManager.getCurrencies()) {
            if (!currencyManager.getCurrency(currency, "balance.starting").equals("0") && !storageManager.exists(currency, "UUID", player.getUniqueId().toString())) {
                storageManager.set(currency, player.getUniqueId().toString(), "Amount", currencyManager.getCurrency(currency, "balance.starting"));
                debugger.debug("Set " + player.getName() + "'s starting balance for " + currency + "!");
            }
        }
    }

}
