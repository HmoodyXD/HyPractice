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
import com.hysteria.practice.game.tournament.Tournament;

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
        String[] args = commandArgs.getArgs();

        if (args.length > 0) {
            String tournamentType = args[0].toLowerCase();
            switch (tournamentType) {
                case "solo":
                    joinTournament(player, new TournamentSolo());
                    break;
                case "teams":
                    joinTournament(player, new TournamentTeams());
                    break;
                case "clans":
                    joinTournament(player, new TournamentClans());
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Invalid tournament type. Use: solo, teams, or clans.");
                    break;
            }
        } else {
            Tournament<?> activeTournament = Tournament.getTournament();
            if (activeTournament == null) {
                player.sendMessage(ChatColor.RED + "There is no active tournament at the moment.");
                return;
            }

            Profile profile = Profile.get(player.getUniqueId());
            if (profile.isBusy()) {
                player.sendMessage(ChatColor.RED + "You may not join the tournament in your current state.");
                return;
            }
            if (profile.isInTournament()) {
                player.sendMessage(ChatColor.RED + "You are already in a tournament.");
                return;
            }

            activeTournament.join(player);
        }
    }

    private void joinTournament(Player player, Tournament<?> tournament) {
        if (tournament == null) {
            player.sendMessage(ChatColor.RED + "This type of tournament is not available.");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        if (profile.isBusy()) {
            player.sendMessage(ChatColor.RED + "You may not join the tournament in your current state.");
            return;
        }
        if (profile.isInTournament()) {
            player.sendMessage(ChatColor.RED + "You are already in a tournament.");
            return;
        }

        tournament.join((Player) player);
    }
}
