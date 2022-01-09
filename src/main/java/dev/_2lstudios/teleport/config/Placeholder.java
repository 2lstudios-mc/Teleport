package dev._2lstudios.teleport.config;

public class Placeholder {
    private String key;
    private String value;

    public Placeholder(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String replace(String text) {
        return text.replace(key, value);
    }
}
