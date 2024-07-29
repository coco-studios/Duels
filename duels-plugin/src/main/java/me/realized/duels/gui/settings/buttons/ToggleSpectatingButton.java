package me.realized.duels.gui.settings.buttons;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.Permissions;
import me.realized.duels.gui.BaseButton;
import me.realized.duels.setting.Settings;
import me.realized.duels.util.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ToggleSpectatingButton extends BaseButton {

	public ToggleSpectatingButton(final DuelsPlugin plugin) {
		super(plugin, ItemBuilder.of(Material.CHEST).name(plugin.getLang().getMessage("GUI.settings.buttons.use-own-inventory.name")).build());
	}

	@Override
	public void update(final Player player) {
		if (config.isToggleSpectatingPermission() && !player.hasPermission(Permissions.TOGGLE_SPECTATING) && !player.hasPermission(Permissions.SETTING_ALL)) {
			setLore(lang.getMessage("GUI.settings.buttons.allow-spectating.lore-no-permission").split("\n"));
			return;
		}

		final Settings settings = settingManager.getSafely(player);
		final String allowSpectating = settings.isAllowSpectating() ? lang.getMessage("GENERAL.enabled") : lang.getMessage("GENERAL.disabled");
		final String lore = plugin.getLang().getMessage("GUI.settings.buttons.allow-spectating.lore", "allow_spectating", allowSpectating);
		setLore(lore.split("\n"));
	}

	@Override
	public void onClick(final Player player) {
		if (config.isToggleSpectatingPermission() && !player.hasPermission(Permissions.TOGGLE_SPECTATING) && !player.hasPermission(Permissions.SETTING_ALL)) {
			lang.sendMessage(player, "ERROR.no-permission", "permission", Permissions.OWN_INVENTORY);
			return;
		}

		final Settings settings = settingManager.getSafely(player);
		settings.setAllowSpectating(!settings.isAllowSpectating());
		settings.updateGui(player);
	}
}