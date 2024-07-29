package me.realized.duels.command.commands.party.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.Permissions;
import me.realized.duels.command.BaseCommand;
import me.realized.duels.party.Party;

public class DisbandCommand extends BaseCommand {

    public DisbandCommand(final DuelsPlugin plugin) {
        super(plugin, "disband", null, null, Permissions.PARTY, 1, true);
    }

    @Override
    protected void execute(CommandSender sender, String label, String[] args) {
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
        
        lang.sendMessage(party.getOnlineMembers(), "COMMAND.party.disband", "owner", player.getName());
        partyManager.remove(party);
    }
}
