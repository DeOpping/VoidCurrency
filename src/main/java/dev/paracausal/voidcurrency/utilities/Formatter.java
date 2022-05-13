package dev.paracausal.voidcurrency.utilities;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class Formatter {

    Core core;
    ConfigManager messagesYml;
    CurrencyManager currencyManager;

    public Formatter(Core core) {
        this.core = core;
        this.messagesYml = core.getMessagesYml();
        this.currencyManager = core.getCurrencyManager();
    }

    public Component format(String string) {
        var miniMessage = MiniMessage.miniMessage();
        string = string.replace("{PREFIX}", messagesYml.getConfig().getString("prefix"));
        string = string.replace("{VERSION}", core.getDescription().getVersion());

        return miniMessage.deserialize(string);
    }

    public Component format(String string, Player player) {
        var miniMessage = MiniMessage.miniMessage();
        string = string.replace("{PREFIX}", messagesYml.getConfig().getString("prefix"));
        string = string.replace("{VERSION}", core.getDescription().getVersion());
        string = PlaceholderAPI.setPlaceholders(player, string);

        return miniMessage.deserialize(string);
    }

    public void sendMessage(Player player, String location) {
        Object value = messagesYml.getConfig().get(location);
        Audience audience = (Audience) player;

        if (value instanceof String) {
            audience.sendMessage(this.format(value.toString(), player));
            return;
        }

        for (String message : messagesYml.getConfig().getStringList(location)) {
            audience.sendMessage(this.format(message, player));
        }
    }

    public void notify(Player player, String location, String currency, BigDecimal amount, OfflinePlayer target) {

        Object value = messagesYml.getConfig().get(location);
        Audience audience = (Audience) player;

        if (value instanceof String) {
            audience.sendMessage(this.format(value.toString().replace("{CURRENCY}", currency)
                    .replace("{AMOUNT}", String.valueOf(amount))
                    .replace("{CURRENCY_NAME}", currencyManager.getCurrency(currency, "name"))
                    .replace("{CURRENCY_SYMBOL}", currencyManager.getCurrency(currency, "symbol"))
                    .replace("{CURRENCY_COLOR}", currencyManager.getCurrency(currency, "color"))
                    .replace("{PLAYER}", target.getName()), player));
            return;
        }

        for (String message : messagesYml.getConfig().getStringList(location)) {
            audience.sendMessage(this.format(message.replace("{CURRENCY}", currency)
                    .replace("{AMOUNT}", String.valueOf(amount))
                    .replace("{CURRENCY_NAME}", currencyManager.getCurrency(currency, "name"))
                    .replace("{CURRENCY_SYMBOL}", currencyManager.getCurrency(currency, "symbol"))
                    .replace("{CURRENCY_COLOR}", currencyManager.getCurrency(currency, "color"))
                    .replace("{PLAYER}", target.getName()), player));
        }
    }

    public void sendString(Player player, String string) {
        Audience audience = (Audience) player;
        audience.sendMessage(this.format(string, player));
    }

}
