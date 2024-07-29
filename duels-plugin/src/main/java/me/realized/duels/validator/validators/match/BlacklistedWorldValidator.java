package me.realized.duels.validator.validators.match;

import java.util.Collection;

import org.bukkit.entity.Player;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.setting.Settings;
import me.realized.duels.validator.BaseBiValidator;

public class BlacklistedWorldValidator extends BaseBiValidator<Collection<Player>, Settings> {

    private static final String MESSAGE_KEY = "DUEL.start-failure.in-blacklisted-world";
    private static final String PARTY_MESSAGE_KEY = "DUEL.party-start-failure.in-blacklisted-world";
    
    public BlacklistedWorldValidator(final DuelsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean shouldValidate() {
        return !config.getBlacklistedWorlds().isEmpty();
    }

    private boolean isBlacklistedWorld(final Player player) {
        return config.getBlacklistedWorlds().contains(player.getWorld().getName());
    }

    @Override
    public boolean validate(final Collection<Player> players, final Settings settings) {
        if (players.stream().anyMatch(this::isBlacklistedWorld)) {
            lang.sendMessage(players, settings.isPartyDuel() ? PARTY_MESSAGE_KEY : MESSAGE_KEY);
            return false;
        }

        return true;
    }
}
