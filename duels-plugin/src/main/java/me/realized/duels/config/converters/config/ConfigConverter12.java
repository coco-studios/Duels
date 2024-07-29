package me.realized.duels.config.converters.config;

import java.util.HashMap;
import java.util.Map;
import me.realized.duels.util.config.convert.Converter;

public class ConfigConverter12 implements Converter {

    @Override
    public Map<String, String> renamedKeys() {
        final Map<String, String> keys = new HashMap<>();
        keys.put("countdown.messages", "countdown.duel.messages");
        keys.put("countdown.titles", "countdown.duel.titles");
        return keys;
    }
}
