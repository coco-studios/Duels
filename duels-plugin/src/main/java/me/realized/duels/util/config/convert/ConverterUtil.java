package me.realized.duels.util.config.convert;

import java.util.HashMap;
import java.util.Map;

public final class ConverterUtil {
    
    public static Converter merge(final Converter... converters) {
        final Map<String, String> keys = new HashMap<>();

        for (final Converter converter : converters) {
            keys.putAll(converter.renamedKeys());
        }

        return new Converter() {

            @Override
            public Map<String, String> renamedKeys() {
                return keys;
            }
            
        };
    }

    private ConverterUtil() {}
}
