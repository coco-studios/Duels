package me.realized.duels.command.commands.party.subcommands;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.Permissions;
import me.realized.duels.command.BaseCommand;
import me.realized.duels.party.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendlyfireCommand extends BaseCommand {

    public FriendlyfireCommand(final DuelsPlugin plugin) {
        super(plugin, "friendlyfire", null, null, Permissions.PARTY, 1, true, "ff");
    }

    @Override
    protected void execute(final CommandSender sender, final String label, final String[] args) {
        final Player player = (Player) sender;
        final Party party = partyManager.get(player);

        if (party == null) {
            lang.sendMessage(sender, "ERROR.party.not-in-party.sender");
            return;
        }

        if (!party.isOwner(player)) {
            lang.sendMessage(sender, "ERROR.party.is-not-owner");
            return;
        }

        party.setFriendlyFire(!party.isFriendlyFire());
        lang.sendMessage(party.getOnlineMembers(), "COMMAND.party.friendly-fire." + (party.isFriendlyFire() ? "enabled" : "disabled"));
    }
}
