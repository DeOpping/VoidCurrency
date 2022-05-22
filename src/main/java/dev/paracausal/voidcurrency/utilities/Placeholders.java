package dev.paracausal.voidcurrency.utilities;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.utilities.storage.StorageManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Placeholders extends PlaceholderExpansion {

    Core core;
    Formatter formatter;
    CurrencyManager currencyManager;
    StorageManager storageManager;

    public Placeholders(Core core) {
        this.core = core;
        this.formatter = core.getFormatter();
        this.currencyManager = core.getCurrencyManager();
        this.storageManager = core.getStorageManager();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "voidcurrency";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Mantice";
    }

    @Override
    public @NotNull String getVersion() {
        return core.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        if (identifier.startsWith("balance_")) {
            String[] split = identifier.split("_");
            String currency = split[1];

            if (!currencyManager.getCurrencies().contains(currency)) return null;

            if (split.length == 3 && split[2].equalsIgnoreCase("round")) {
                String amount = storageManager.get(currency, "Amount", "UUID", player.getUniqueId().toString());

                if (amount == null) {
                    return "0";
                }

                BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(amount));
                BigDecimal rounded = bigDecimal.setScale(0, RoundingMode.HALF_UP);
                return String.valueOf(rounded);
            }

            if (split.length > 3) return null;

            String amount = storageManager.get(currency, "Amount", "UUID", player.getUniqueId().toString());

            if (amount == null) {
                return "0";
            }

            return amount;
        }

        if (identifier.startsWith("symbol_")) {
            String[] split = identifier.split("_");
            String currency = split[1];

            if (split.length != 2 || !currencyManager.getCurrencies().contains(currency)) return null;

            return currencyManager.getCurrency(currency, "symbol");
        }

        if (identifier.startsWith("color_")) {
            String[] split = identifier.split("_");
            String currency = split[1];

            if (split.length != 2 || !currencyManager.getCurrencies().contains(currency)) return null;

            String color = currencyManager.getCurrency(currency, "color");

            return formatter.legacy(color);
        }

        return null;
    }

}
