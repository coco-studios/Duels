package me.realized.duels.config.converters.config;

import java.util.HashMap;
import java.util.Map;
import me.realized.duels.util.config.convert.Converter;

public class ConfigConverter10 implements Converter {

    @Override
    public Map<String, String> renamedKeys() {
        final Map<String, String> keys = new HashMap<>();
        keys.put("duel.use-own-inventory.enabled", "request.use-own-inventory.enabled");
        return keys;
    }
}
