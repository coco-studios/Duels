package me.realized.duels.match;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import me.realized.duels.DuelsPlugin;
import me.realized.duels.api.match.Match;
import me.realized.duels.arena.ArenaImpl;
import me.realized.duels.kit.KitImpl;
import me.realized.duels.party.PartyManagerImpl;
import me.realized.duels.queue.Queue;

public class DuelMatch implements Match {
    
    protected final PartyManagerImpl partyManager;

    @Getter
    private final long creation;

    @Getter
    private final ArenaImpl arena;
    @Getter
    private final KitImpl kit;
    private final Map<UUID, List<ItemStack>> items;
    @Getter
    private final int bet;
    @Getter
    private final Queue source;

    @Getter
    private boolean finished;

    // Default value for players is false, which is set to true if player is killed in the match.
    private final Map<Player, Boolean> players = new HashMap<>();

    public DuelMatch(final DuelsPlugin plugin, final ArenaImpl arena, final KitImpl kit, final Map<UUID, List<ItemStack>> items, final int bet, final Queue source) {
        this.partyManager = plugin.getPartyManager();
        this.creation = System.currentTimeMillis();
        this.arena = arena;
        this.kit = kit;
        this.items = items;
        this.bet = bet;
        this.source = source;
    }
    
    public long getDurationInMillis() {
        return System.currentTimeMillis() - creation;
    }

    public boolean isFromQueue() {
        return source != null;
    }

    public boolean isOwnInventory() {
        return kit == null;
    }
    
    public void setFinished() {
        finished = true;
    }

    public void addPlayer(final Player player) {
        players.put(player, false);
    }

    public void markAsDead(final Player player) {
        if (players.containsKey(player)) {
            players.put(player, true);
        }
    }

    public boolean isDead(final Player player) {
        return players.getOrDefault(player, true);
    }

    public Set<Player> getAlivePlayers() {
        return players.entrySet().stream().filter(entry -> !entry.getValue()).map(Entry::getKey).collect(Collectors.toSet());
    }

    public Set<Player> getAllPlayers() {
        return players.keySet();
    }

    public int size() {
        return getAlivePlayers().size();
    }

    public List<ItemStack> getItems() {
        return items != null ? items.values().stream().flatMap(Collection::stream).collect(Collectors.toList()) : Collections.emptyList();
    }

    @NotNull
    @Override
    public List<ItemStack> getItems(@NotNull final Player player) {
        Objects.requireNonNull(player, "player");

        if (this.items == null) {
            return Collections.emptyList();
        }

        final List<ItemStack> items = this.items.get(player.getUniqueId());
        return items != null ? items : Collections.emptyList();
    }

    @NotNull
    @Override
    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(getAlivePlayers());
    }

    @NotNull
    @Override
    public Set<Player> getStartingPlayers() {
        return Collections.unmodifiableSet(getAllPlayers());
    }

    @Override
    public long getStart() {
        return creation;
    }
}
