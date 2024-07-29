package me.realized.duels.inventories;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.realized.duels.DuelsPlugin;
import me.realized.duels.config.Config;
import me.realized.duels.config.Lang;
import me.realized.duels.gui.inventory.InventoryGui;
import me.realized.duels.match.DuelMatch;
import me.realized.duels.util.Loadable;
import me.realized.duels.util.StringUtil;
import me.realized.duels.util.TextBuilder;
import me.realized.duels.util.gui.GuiListener;
import net.md_5.bungee.api.chat.ClickEvent.Action;

import org.bukkit.entity.Player;

public class InventoryManager implements Loadable {

    private final DuelsPlugin plugin;
    private final Config config;
    private final Lang lang;
    private final GuiListener<DuelsPlugin> guiListener;
    private final Map<UUID, InventoryGui> inventories = new HashMap<>();

    private int expireTask;

    public InventoryManager(final DuelsPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
        this.lang = plugin.getLang();
        this.guiListener = plugin.getGuiListener();
    }

    @Override
    public void handleLoad() {
        this.expireTask = plugin.doSyncRepeat(() -> {
            final long now = System.currentTimeMillis();

            inventories.entrySet().removeIf(entry -> {
                if (now - entry.getValue().getCreation() >= 1000L * 60 * 5) {
                    guiListener.removeGui(entry.getValue());
                    return true;
                }

                return false;
            });
        }, 20L, 20L * 5).getTaskId();
    }

    @Override
    public void handleUnload() {
        plugin.cancelTask(expireTask);
        inventories.clear();
    }

    public InventoryGui get(final UUID uuid) {
        return inventories.get(uuid);
    }

    public void create(final Player player, final boolean dead) {
        // Remove previously existing gui
        InventoryGui gui = inventories.remove(player.getUniqueId());

        if (gui != null) {
            guiListener.removeGui(gui);
        }

        gui = new InventoryGui(plugin, player, dead);
        guiListener.addGui(gui);
        inventories.put(player.getUniqueId(), gui);
    }

    public void handleMatchEnd(final DuelMatch match) {
        if (!config.isDisplayInventories()) {
            return;
        }

        String color = lang.getMessage("DUEL.inventories.name-color");
        final TextBuilder builder = TextBuilder.of(lang.getMessage("DUEL.inventories.message"));
        final Set<Player> players = match.getAllPlayers();
        final Iterator<Player> iterator = players.iterator();

        while (iterator.hasNext()) {
            final Player player = iterator.next();
            builder.add(StringUtil.color(color + player.getName()), Action.RUN_COMMAND, "/duel _ " + player.getUniqueId());

            if (iterator.hasNext()) {
                builder.add(StringUtil.color(color + ", "));
            }
        }

        builder.send(players);
    }
}
