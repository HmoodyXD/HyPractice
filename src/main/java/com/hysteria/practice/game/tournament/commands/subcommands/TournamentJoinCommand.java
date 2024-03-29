package com.hysteria.practice.game.tournament.commands.subcommands;

import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.hysteria.practice.game.tournament.impl.TournamentSolo;
import com.hysteria.practice.game.tournament.impl.TournamentTeams;
import com.hysteria.practice.game.tournament.impl.TournamentClans;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

public class TournamentJoinCommand extends BaseCommand {

    @Command(name = "tournament.join")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        TournamentSolo soloTournament = (TournamentSolo) TournamentSolo.getTournament();
		TournamentTeams teamsTournament = (TournamentTeams) TournamentTeams.getTournament();
		TournamentClans clansTournament = (TournamentClans) TournamentClans.getTournament();

        if (soloTournament == null && teamsTournament == null && clansTournament == null) {
            player.sendMessage(ChatColor.RED + "No tournament found.");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        if(profile.isBusy()) {
            player.sendMessage(ChatColor.RED + "You may not join the tournament in your current state.");
            return;
        }
        if (profile.isInTournament()) {
            player.sendMessage(ChatColor.RED + "You are already in the tournament.");
            return;
        }

        if (soloTournament != null) {
            soloTournament.join(player);
        } else if (teamsTournament != null) {
            teamsTournament.join(player);
        } else if (clansTournament != null) {
            clansTournament.join(player);
        }
    }
}
