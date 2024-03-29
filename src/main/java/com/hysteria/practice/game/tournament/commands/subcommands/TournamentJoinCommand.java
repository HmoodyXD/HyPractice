package com.hysteria.practice.game.tournament.commands.subcommands;

import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.hysteria.practice.game.tournament.Tournament;

public class TournamentJoinCommand extends BaseCommand {

    @Command(name = "tournament.join")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        
        Tournament<?> activeTournament = Tournament.getTournament();
        if (activeTournament == null) {
            sendErrorMessage(player, "There is no active tournament at the moment.");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        if (profile.isBusy()) {
            sendErrorMessage(player, "You may not join the tournament in your current state.");
            return;
        }
        if (profile.isInTournament()) {
            sendErrorMessage(player, "You are already in a tournament.");
            return;
        }

        activeTournament.join(player);
    }

    private void sendErrorMessage(Player player, String message) {
        player.sendMessage(ChatColor.RED + message);
    }
}
