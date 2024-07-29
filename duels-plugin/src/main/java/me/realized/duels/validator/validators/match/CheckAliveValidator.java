package me.realized.duels.validator.validators.match;

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.setting.Settings;
import me.realized.duels.validator.BaseBiValidator;

public class CheckAliveValidator extends BaseBiValidator<Collection<Player>, Settings> {

    private static final String MESSAGE_KEY = "DUEL.start-failure.player-is-dead";
    private static final String PARTY_MESSAGE_KEY = "DUEL.party-start-failure.player-is-dead";
    
    public CheckAliveValidator(final DuelsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean validate(final Collection<Player> players, final Settings settings) {
        if (players.stream().anyMatch(Entity::isDead)) {
            lang.sendMessage(players, settings.isPartyDuel() ? PARTY_MESSAGE_KEY : MESSAGE_KEY);
            return false;
        }
        
        return true;
    }
    
}
