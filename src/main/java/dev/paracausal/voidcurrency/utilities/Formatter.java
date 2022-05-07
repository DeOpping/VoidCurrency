package dev.paracausal.voidprivatemines.utilities;

import dev.paracausal.voidprivatemines.Core;
import dev.paracausal.voidprivatemines.utilities.configurations.ConfigManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

    Core core;
    ConfigManager messagesYml;

    public Formatter(Core core) {
        this.core = core;
        this.messagesYml = core.getMessagesYml();
    }

    public String formatLegacy(String string) {
        if (core.getVersion() >= 16) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                String hexCode = string.substring(matcher.start(), matcher.end());
                String replaceSharp = hexCode.replace('#', 'x');

                char[] chars = replaceSharp.toCharArray();
                StringBuilder stringBuilder = new StringBuilder();

                for (char c : chars) {
                    stringBuilder.append("&").append(c);
                }

                string = string.replace(hexCode, stringBuilder.toString());
                matcher = pattern.matcher(string);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public String formatLegacy(String string, Player player) {
        string = string.replace("{VERSION}", core.getDescription().getVersion());
        string = PlaceholderAPI.setPlaceholders(player, string);
        return this.formatLegacy(string);
    }

    public List<String> formatLegacy(List<String> list, Player player) {
        List<String> newList = new ArrayList<>();

        for (String string : list) {
            string = string.replace("{VERSION}", core.getDescription().getVersion());
            string = PlaceholderAPI.setPlaceholders(player, string);
            string = this.formatLegacy(string);
            newList.add(string);
        }

        return newList;
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
            String message = PlaceholderAPI.setPlaceholders(player, value.toString());
            audience.sendMessage(this.format(message));
            return;
        }

        for (String message : messagesYml.getConfig().getStringList(location)) {
            message = PlaceholderAPI.setPlaceholders(player, message);
            audience.sendMessage(this.format(message));
        }
    }

    public void sendString(Player player, String string) {
        Audience audience = (Audience) player;
        audience.sendMessage(this.format(string));
    }

}
