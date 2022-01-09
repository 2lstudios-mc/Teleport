package dev._2lstudios.teleport.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class MessagesConfig {
    private Map<String, String> messages;

    public void readPart(ConfigurationSection section, String currentPath) {
        for (Entry<String, Object> entry : section.getValues(false).entrySet()) {
            String key = entry.getKey();
            String fullPath = currentPath.isEmpty() ? key : currentPath + "." + key;
            Object value = entry.getValue();

            if (value instanceof ConfigurationSection) {
                ConfigurationSection subSection = (ConfigurationSection) value;

                readPart(subSection, fullPath);
            } else if (value instanceof String) {
                messages.put(fullPath, ChatColor.translateAlternateColorCodes('&', (String) value));
            }
        }
    }

    public MessagesConfig(Configuration config) {
        this.messages = new HashMap<>();
        readPart(config.getConfigurationSection("messages"), "");
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, "null");
    }

    public String getMessage(String key, Placeholder ...placeholders) {
        String message = getMessage(key);

        for (Placeholder placeholder : placeholders) {
            message = placeholder.replace(message);
        }

        return message;
    }
}
