package me.realized.duels.validator.validators.match;

import java.util.Collection;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.hook.hooks.worldguard.WorldGuardHook;
import me.realized.duels.setting.Settings;
import me.realized.duels.validator.BaseBiValidator;

import org.bukkit.entity.Player;

public class DuelZoneValidator extends BaseBiValidator<Collection<Player>, Settings> {

    private static final String MESSAGE_KEY = "DUEL.start-failure.not-in-duelzone";
    private static final String PARTY_MESSAGE_KEY = "DUEL.party-start-failure.not-in-duelzone";
    
    private final WorldGuardHook worldGuard;

    public DuelZoneValidator(final DuelsPlugin plugin) {
        super(plugin);
        this.worldGuard = plugin.getHookManager().getHook(WorldGuardHook.class);
    }

    @Override
    public boolean shouldValidate() {
        return config.isDuelzoneEnabled() && worldGuard != null;
    }

    private boolean notInDuelZone(final Player player, final String duelzone) {
        return duelzone != null && !duelzone.equals(worldGuard.findDuelZone(player));
    }

    @Override
    public boolean validate(final Collection<Player> players, final Settings settings) {
        if (players.stream().anyMatch(player -> notInDuelZone(player, settings.getDuelzone(player)))) {
            lang.sendMessage(players, settings.isPartyDuel() ? PARTY_MESSAGE_KEY : MESSAGE_KEY);
            return false;
        }

        return true;
    }
}
