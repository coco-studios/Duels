package me.realized.duels.config.converters.lang;

import java.util.HashMap;
import java.util.Map;

import me.realized.duels.util.config.convert.Converter;

public class LangConverter12 implements Converter {

    @Override
    public Map<String, String> renamedKeys() {
        final Map<String, String> keys = new HashMap<>();
        keys.put("ERROR.spectate.already-spectating.sender", "ERROR.duel.already-spectating.sender");
        keys.put("ERROR.spectate.already-spectating.target", "ERROR.duel.already-spectating.target");
        keys.put("ERROR.spectate.not-in-match", "ERROR.duel.not-in-match");
        return keys;
    }
    
}
