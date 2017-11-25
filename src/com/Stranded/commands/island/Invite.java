package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.stranded.Reload;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Invite extends CmdManager {

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getAlias() {
        return "inv";
    }

    @Override
    public void run(String[] args, Player player) {

        //island invite <player name>

        String uuid = player.getUniqueId().toString();

        if (Main.reloadPending) {
            player.sendMessage("the server is trying to reload, please wait just a second to let the server reload");
            return;
        }

        if (!p.getConfig().contains("island." + uuid)) {
            player.sendMessage(ChatColor.RED + "You can't invite a player while you aren't in an island");
            return;
        }

        Files f = new Files(p, "islands.yml");
        if (!f.getConfig().getString("island." + p.getConfig().getString("island." + uuid) + ".owner").equals(uuid)) {
            player.sendMessage(ChatColor.RED + "You are not the owner of this island, so you can't invite someone");
            return;
        }

        if (args.length == 2) {

            String playerName = args[1];
            //get player uuid
            String invitedUUID = PlayerUUID.getPlayerUUID(playerName, p);
            //test if player is found
            if (invitedUUID == null) {
                ArrayList<String> tempPlayers = PlayerUUID.getGlobalPlayerUUID(args[1], p);
                if (tempPlayers.size() > 1) {
                    player.sendMessage(ChatColor.RED + "The player '" + args[1] + "' is not found, but there are more players found with the same name (not case sensitive)");
                    return;
                } else if (tempPlayers.size() == 0) {
                    player.sendMessage(ChatColor.RED + "There is no player found with this name");
                    return;
                } else {
                    //set full on player uuid
                    invitedUUID = tempPlayers.get(0);
                }
            }
            //test if player is found (not really necessary)
            if (invitedUUID == null) {
                player.sendMessage(ChatColor.RED + "This player isn't found, please make sure that you gave the right player name. this is case sensitive");
                return;
            }
            //get final player name (case sensitive)
            String invitedPlayerName = PlayerUUID.getPlayerName(invitedUUID, p);
            //get player
            Player invited = PlayerUUID.getPlayerExact(playerName, p);

            if (invited != null) {

                if (p.getConfig().contains("island." + invitedUUID) && p.getConfig().getString("island." + invitedUUID).equals(p.getConfig().getString("island." + uuid))) {
                    player.sendMessage(ChatColor.RED + invitedPlayerName + " is already in your island");
                    return;
                }

                if (p.getConfig().contains("invited." + invitedUUID)) {
                    player.sendMessage(ChatColor.RED + "This player has already an invitation to join another island");
                    return;
                }

                String island = p.getConfig().getString("island." + uuid);

//                invited.sendMessage(ChatColor.DARK_GREEN + player.getName() + " asks if you want to join " + island + "?");
                FancyMessage fm = new FancyMessage();
                fm.addText(player.getName() + " asks if you want to join ", Colors.GREEN);
                fm.addText(island, Colors.DARK_GREEN);
                fm.addText("? To join click ", Colors.GREEN);
                fm.addText("here", Colors.DARK_GREEN);
                fm.addCommand("/island join");
                fm.addHover(new String[]{"/island join"}, new Colors[]{Colors.GREEN});
                fm.addText(" or to deny click ", Colors.AQUA);
                fm.addText("here", Colors.DARK_GREEN);
                fm.addCommand("/island ignore");
                fm.addHover(new String[]{"/island ignore"}, new Colors[]{Colors.GREEN});

                fm.sendMessage(invited);

                String islandOld = p.getConfig().getString("island." + invitedUUID);
                ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
                if (old.size() == 1) {
                    invited.sendMessage(ChatColor.DARK_RED + "NOTE: If you leave you island, this will get deleted. there can't be less than 1 player in an island");
                }
                player.sendMessage(ChatColor.GREEN + "Your invitation has been sent to " + invited.getName());

                Main.reloadHolds += 1;

                String finalInvitedUUID = invitedUUID;

                int taskID = Bukkit.getScheduler().runTaskLater(p, () -> {

                    if (Bukkit.getPlayer(UUID.fromString(p.getConfig().getString("invited." + finalInvitedUUID + ".inviter"))) != null) {
                        Bukkit.getPlayer(UUID.fromString(p.getConfig().getString("invited." + finalInvitedUUID + ".inviter"))).sendMessage(invitedPlayerName + " hasn't reacted in time to join your island");
                    }
                    if (Bukkit.getPlayerExact(invitedPlayerName) != null) {
                        Bukkit.getPlayerExact(invitedPlayerName).sendMessage(
                                ChatColor.RED + "You ignored the inventation of the island " + p.getConfig().getString("invited." + finalInvitedUUID + ".newIsland"));
                    }

                    p.getConfig().set("invited." + finalInvitedUUID, null);
                    p.saveConfig();

                    Main.reloadHolds -= 1;
                    if (Main.reloadPending && Main.reloadHolds == 0) {
                        Reload.reload(p);
                    }

                }, 1000).getTaskId();

                p.getConfig().set("invited." + invitedUUID + ".newIsland", island);
                p.getConfig().set("invited." + invitedUUID + ".pendingID", taskID);
                p.getConfig().set("invited." + invitedUUID + ".inviter", uuid);

                p.saveConfig();

            } else {
                player.sendMessage("this player is not online");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /island invite <player>");
        }

    }
}