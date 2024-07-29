package me.realized.duels.command.commands.party.subcommands;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.Permissions;
import me.realized.duels.command.BaseCommand;
import me.realized.duels.party.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand extends BaseCommand {

    public LeaveCommand(final DuelsPlugin plugin) {
        super(plugin, "leave", null, null, Permissions.PARTY, 1, true, "l");
    }

    @Override
    protected void execute(CommandSender sender, String label, String[] args) {
        final Player player = (Player) sender;
        final Party party = partyManager.get(player);

        if (party == null) {
            lang.sendMessage(sender, "ERROR.party.not-in-party.sender");
            return;
        }

        if (party.isOwner(player)) {
            lang.sendMessage(sender, "ERROR.party.is-owner");
            return;
        }
        
        partyManager.remove(player, party);
        lang.sendMessage(player, "COMMAND.party.leave.sender", "name", party.getOwner().getName());
        lang.sendMessage(party.getOnlineMembers(), "COMMAND.party.leave.members", "name", player.getName());
    }
}
