package me.realized.duels.countdown;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.realized.duels.DuelsPlugin;
import me.realized.duels.arena.ArenaImpl;
import me.realized.duels.config.Config;
import me.realized.duels.config.Lang;
import me.realized.duels.data.UserData;
import me.realized.duels.data.UserManagerImpl;
import me.realized.duels.match.DuelMatch;
import me.realized.duels.util.StringUtil;
import me.realized.duels.util.compat.Titles;
import me.realized.duels.util.function.Pair;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelCountdown extends BukkitRunnable {

    protected final Config config;
    protected final Lang lang;
    protected final UserManagerImpl userManager;
    protected final ArenaImpl arena;
    protected final DuelMatch match;
    
    private final List<String> messages;
    private final List<String> titles;

    private final Map<UUID, Pair<String, Integer>> info = new HashMap<>();
    private int index = 0;

    protected DuelCountdown(final DuelsPlugin plugin, final ArenaImpl arena, final DuelMatch match, final List<String> messages, final List<String> titles) {
        this.config = plugin.getConfiguration();
        this.lang = plugin.getLang();
        this.userManager = plugin.getUserManager();
        this.arena = arena;
        this.match = match;
        this.titles = titles;
        this.messages = messages;
    }

    public DuelCountdown(final DuelsPlugin plugin, final ArenaImpl arena, final DuelMatch match) {
        this(plugin, arena, match, plugin.getConfiguration().getCdDuelMessages(), plugin.getConfiguration().getCdDuelTitles());
        match.getAllPlayers().forEach(player -> {
            final Player opponent = arena.getOpponent(player);
            final UserData user = userManager.get(opponent);

            if (user == null) {
                return;
            }

            info.put(player.getUniqueId(), new Pair<>(opponent.getName(), user.getRatingUnsafe(match.getKit())));
        });
    }

    protected void sendMessage(final String rawMessage, final String message, final String title) {
        final String kitName = match.getKit() != null ? match.getKit().getName() : lang.getMessage("GENERAL.none");

        arena.getPlayers().forEach(player -> {
            config.playSound(player, rawMessage);

            final Pair<String, Integer> info = this.info.get(player.getUniqueId());

            if (info != null) {
                player.sendMessage(message
                    .replace("%opponent%", info.getKey())
                    .replace("%opponent_rating%", String.valueOf(info.getValue()))
                    .replace("%kit%", kitName)
                    .replace("%arena%", arena.getName())
                );
            } else {
                player.sendMessage(message);
            }

            if (title != null) {
                Titles.send(player, title, null, 0, 20, 50);
            }
        });
    }

    @Override
    public void run() {
        if (!arena.isUsed() || index >= messages.size()) {
            arena.setCountdown(null);
            cancel();
            return;
        }
        
        final String rawMessage = messages.get(index);
        final String message = StringUtil.color(rawMessage);
        final String title = (titles.size() >= index + 1) ? titles.get(index) : null;
        sendMessage(rawMessage, message, title);
        index++;
    }
}
