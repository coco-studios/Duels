package me.realized.duels.command.commands.party;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.Permissions;
import me.realized.duels.command.BaseCommand;
import me.realized.duels.command.commands.party.subcommands.*;
import me.realized.duels.data.UserData;
import me.realized.duels.party.Party;
import me.realized.duels.util.TextBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyCommand extends BaseCommand {
    
    public PartyCommand(final DuelsPlugin plugin) {
        super(plugin, "party", Permissions.PARTY, true);
        child(
            new ToggleCommand(plugin),
            new AcceptCommand(plugin),
            new ListCommand(plugin),
            new LeaveCommand(plugin),
            new KickCommand(plugin),
            new FriendlyfireCommand(plugin),
            new TransferCommand(plugin),
            new DisbandCommand(plugin)
        );
    }

    @Override
    protected boolean executeFirst(final CommandSender sender, final String label, final String[] args) {
        final Player player = (Player) sender;

        if (args.length == 0) {
            lang.sendMessage(sender, "COMMAND.party.usage", "command", label);
            return true;
        }

        if (isChild(args[0])) {
            return false;
        }

        final Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null || !player.canSee(target)) {
            lang.sendMessage(sender, "ERROR.player.not-found", "name", args[0]);
            return true;
        }

        if (player.equals(target)) {
            lang.sendMessage(sender, "ERROR.party.is-self");
            return true;
        }

        final UserData user = userManager.get(target);

        if (user == null) {
            lang.sendMessage(sender, "ERROR.data.not-found", "name", target.getName());
            return true;
        }

        if (!user.canPartyRequest()) {
            lang.sendMessage(sender, "ERROR.party.requests-disabled", "name", target.getName());
            return true;
        }

        if (partyManager.isInParty(target)) {
            lang.sendMessage(sender, "ERROR.party.already-in-party.target", "name", target.getName());
            return true;
        }

        if (partyManager.hasInvite(player, target)) {
            lang.sendMessage(sender, "ERROR.party.already-has-invite", "name", target.getName());
            return true;
        }
        
        final Party party = partyManager.getOrCreate(player);

        if (!party.isOwner(player)) {
            lang.sendMessage(sender, "ERROR.party.is-not-owner");
            return true;
        }

        if (!partyManager.sendInvite(player, target, party)) {
            lang.sendMessage(sender, "ERROR.party.max-size-reached.sender");
            return true;
        }

        lang.sendMessage(party.getOnlineMembers(), "COMMAND.party.invite.send.members", "owner", player.getName(), "name", target.getName());
//        lang.sendMessage(target, "COMMAND.party.invite.send.receiver", "name", sender.getName());
        TextBuilder
                .of(lang.getMessage("COMMAND.party.invite.send.receiver", "name", sender.getName()))
                .add(lang.getMessage("COMMAND.party.invite.send.clickable-text.accept.text"))
                    .setClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept "+sender.getName())
                    .setHoverEvent(HoverEvent.Action.SHOW_TEXT, lang.getMessage("COMMAND.party.invite.send.clickable-text.accept.hover-text"))
                .send(target);
        return true;
    }

    @Override
    protected void execute(final CommandSender sender, final String label, final String[] args) {}

    // Disables default TabCompleter
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return null;
    }
}
