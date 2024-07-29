package me.realized.duels.command.commands.party.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.Permissions;
import me.realized.duels.command.BaseCommand;
import me.realized.duels.data.UserData;

public class ToggleCommand extends BaseCommand {
    
    public ToggleCommand(final DuelsPlugin plugin) {
        super(plugin, "toggle", null, null, Permissions.PARTY_TOGGLE, 1, true);
    }

    @Override
    protected void execute(final CommandSender sender, final String label, final String[] args) {
        final UserData user = userManager.get((Player) sender);

        if (user == null) {
            lang.sendMessage(sender, "ERROR.data.load-failure");
            return;
        }

        user.setPartyRequests(!user.canPartyRequest());
        lang.sendMessage(sender, "COMMAND.party.toggle." + (user.canPartyRequest() ? "enabled" : "disabled"));
    }
}
