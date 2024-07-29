package me.realized.duels.validator.validators.match;

import java.util.Collection;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.setting.Settings;
import me.realized.duels.validator.BaseBiValidator;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PreventCreativeValidator extends BaseBiValidator<Collection<Player>, Settings> {

    private static final String MESSAGE_KEY = "DUEL.start-failure.in-creative-mode";
    private static final String PARTY_MESSAGE_KEY = "DUEL.party-start-failure.in-creative-mode";
    
    public PreventCreativeValidator(final DuelsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean shouldValidate() {
        return config.isPreventCreativeMode();
    }

    @Override
    public boolean validate(final Collection<Player> players, final Settings settings) {
        if (players.stream().anyMatch(player -> player.getGameMode() == GameMode.CREATIVE)) {
            lang.sendMessage(players, settings.isPartyDuel() ? PARTY_MESSAGE_KEY : MESSAGE_KEY);
            return false;
        }

        return true;
    }
}