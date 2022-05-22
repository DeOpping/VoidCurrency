package dev.paracausal.voidcurrency.utilities;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (core.placeholderAPI)
            string = PlaceholderAPI.setPlaceholders(player, string);

        return miniMessage.deserialize(string);
    }

    public String legacy(String string) {

        Component component = this.format(string);
        string = LegacyComponentSerializer.legacyAmpersand().serialize(component);

        return string;
    }

//    public String legacy(String string) {
//        if (core.getVersion() >= 16) {
//            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
//            Matcher matcher = pattern.matcher(string);
//            while (matcher.find()) {
//                String hexCode = string.substring(matcher.start(), matcher.end());
//                String replaceSharp = hexCode.replace('#', 'x');
//
//                char[] chars = replaceSharp.toCharArray();
//                StringBuilder stringBuilder = new StringBuilder();
//
//                for (char c : chars) {
//                    stringBuilder.append("&").append(c);
//                }
//
//                string = string.replace(hexCode, stringBuilder.toString());
//                matcher = pattern.matcher(string);
//            }
//        }
//
//        return ChatColor.translateAlternateColorCodes('&', string);
//    }

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

    public void sendMessage(Player target, Player player, String location) {
        Object value = messagesYml.getConfig().get(location);
        Audience audience = (Audience) player;

        if (value instanceof String) {
            audience.sendMessage(this.format(value.toString(), target));
            return;
        }

        for (String message : messagesYml.getConfig().getStringList(location)) {
            audience.sendMessage(this.format(message, target));
        }
    }

    public void sendMessage(Player player, String location, String replace, String replacement) {
        Object value = messagesYml.getConfig().get(location);
        Audience audience = (Audience) player;

        if (value instanceof String) {
            String message = value.toString().replace(replace, replacement);
            audience.sendMessage(this.format(message, player));
            return;
        }

        for (String message : messagesYml.getConfig().getStringList(location)) {
            message = message.replace(replace, replacement);
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
