package me.realized.duels.validator.validators.match;

import java.util.Collection;

import org.bukkit.entity.Player;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.hook.hooks.CombatLogXHook;
import me.realized.duels.hook.hooks.CombatTagPlusHook;
import me.realized.duels.hook.hooks.PvPManagerHook;
import me.realized.duels.setting.Settings;
import me.realized.duels.validator.BaseBiValidator;

public class CombatTagValidator extends BaseBiValidator<Collection<Player>, Settings> {

    private static final String MESSAGE_KEY = "DUEL.start-failure.is-tagged";
    private static final String PARTY_MESSAGE_KEY = "DUEL.party-start-failure.is-tagged";
    
    private final CombatTagPlusHook combatTagPlus;
    private final PvPManagerHook pvpManager;
    private final CombatLogXHook combatLogX;

    public CombatTagValidator(final DuelsPlugin plugin) {
        super(plugin);
        this.combatTagPlus = plugin.getHookManager().getHook(CombatTagPlusHook.class);
        this.pvpManager = plugin.getHookManager().getHook(PvPManagerHook.class);
        this.combatLogX = plugin.getHookManager().getHook(CombatLogXHook.class);
    }

    @Override
    public boolean shouldValidate() {
        return (combatTagPlus != null && config.isCtpPreventDuel())
            || (pvpManager != null && config.isPmPreventDuel())
            || (combatLogX != null && config.isClxPreventDuel());
    }

    private boolean isTagged(final Player player) {
        return (combatTagPlus != null && combatTagPlus.isTagged(player))
            || (pvpManager != null && pvpManager.isTagged(player))
            || (combatLogX != null && combatLogX.isTagged(player));
    }

    @Override
    public boolean validate(final Collection<Player> players, final Settings settings) {
        if (players.stream().anyMatch(this::isTagged)) {
            lang.sendMessage(players, settings.isPartyDuel() ? PARTY_MESSAGE_KEY : MESSAGE_KEY);
            return false;
        }

        return true;
    }
}
