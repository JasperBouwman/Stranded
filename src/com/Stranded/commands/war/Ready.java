package com.Stranded.commands.war;

import com.Stranded.FancyMessageUtil;
import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Ready extends CmdManager implements Runnable {
    @Override
    public String getName() {
        return "ready";
    }

    @Override
    public String getAlias() {
        return "r";
    }

    @Override
    public void run(String[] args, Player player) {

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("you aren't in an island");
            return;
        }

        Long PendingTime;

        Files war = new Files(p, "warData.yml");
        try {
            PendingTime = Long.parseLong(war.getConfig().getString("war.pending.timeIslandWarriors"));
        } catch (NumberFormatException nfe) {
            PendingTime = (long) 180;
        }

        String island = p.getConfig().getString("island." + player.getName());

        if (!war.getConfig().contains("war.pending.memberInvite." + island)) {
            player.sendMessage("your island isn't pending for a war");
            return;
        }

        if (war.getConfig().getBoolean("war.pending.memberInvite." + island + ".warriors." + player.getName())) {
            player.sendMessage("you already accepted");
        } else {
            war.getConfig().set("war.pending.memberInvite." + island + ".warriors." + player.getName(), true);
            war.saveConfig();
            boolean complete = true;
            for (String s : war.getConfig().getConfigurationSection("war.pending.memberInvite." + island + ".warriors").getKeys(false)) {
                if (!war.getConfig().getBoolean("war.pending.memberInvite." + island + ".warriors." + s)) {
                    complete = false;
                }
            }

            for (String s : war.getConfig().getConfigurationSection("war.pending.memberInvite." + island + ".warriors").getKeys(false)) {

                if (!s.equals(player.getName())) {
                    if (Bukkit.getPlayerExact(s) != null) {
                        Bukkit.getPlayerExact(s).sendMessage(player.getName() + " has accepted the war inventation");
//                        if (complete) {
//                            Bukkit.getPlayerExact(s).sendMessage("everybody has accepted, war request is now sending to the other islands");
//                        }
                    }
                }
                /*else if (s.equals(player.getName())) {
                    if (complete) {
                        Bukkit.getPlayerExact(s).sendMessage("everybody has accepted, war request is now sending to the other islands");
                    }
                }*/
            }

            if (complete) {
                int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, this, PendingTime * 20);

                sendWarMessage(island, p);

                war.getConfig().set("war.pending.islandInvite." + island, war.getConfig().getConfigurationSection("war.pending.memberInvite." + island));
                war.getConfig().set("war.pending.islandInvite." + p.getConfig().getString("island." + player.getName()) + ".pendingID", pendingID);
                war.getConfig().set("war.pending.memberInvite." + island, null);
                war.saveConfig();



            } else {
                for (String s : war.getConfig().getConfigurationSection("war.pending.memberInvite." + island + ".warriors").getKeys(false)) {
                    if (Bukkit.getPlayerExact(s) != null) {

                        StringBuilder str = new StringBuilder().append("still waiting for:");

                        for (String ss : war.getConfig().getConfigurationSection("war.pending.memberInvite." + island + ".warriors").getKeys(false)) {
                            if (!war.getConfig().getBoolean("war.pending.memberInvite." + island + ".warriors." + ss)) {
                                str.append(" ");
                                str.append(ss);
                            }
                        }
                        Bukkit.getPlayerExact(s).sendMessage(str.toString());
                    }
                }
            }
        }
    }

    private void sendWarMessage(String island, Main p) {
        Files war = new Files(p, "warData.yml");
        Files pluginData = new Files(p, "pluginData.yml");

        FancyMessageUtil fm = new FancyMessageUtil();

        fm.addText(island + " want war: ", FancyMessageUtil.Colors.BLUE);

        for (String s : war.getConfig().getConfigurationSection("war.pending.memberInvite." + island + ".warriors").getKeys(false)) {
//        for (OfflinePlayer ss : Bukkit.getOfflinePlayers()) {
//            String s = ss.getName();
            fm.addText(s + " ", FancyMessageUtil.Colors.RED);

            int walk;
            int mine;
            int pvp;

            Files playerData = new Files(p, "playerData.yml");
            if (playerData.getConfig().contains("walk." + s)) {
                walk = playerData.getConfig().getInt("walk." + s) / pluginData.getConfig().getInt("plugin.scoreboard.walking.amplifier");
            } else {
                walk = 0;
            }
            if (playerData.getConfig().contains("BlockBreak." + s)) {
                mine = (playerData.getConfig().getInt("BlockBreak." + s) / pluginData.getConfig().getInt("plugin.scoreboard.mining.amplifier"));
            } else {
                mine = 0;
            }
            if (playerData.getConfig().contains("HitKill." + s)) {
                pvp = (playerData.getConfig().getInt("HitKill." + s) / pluginData.getConfig().getInt("plugin.scoreboard.pvp.amplifier"));
            } else {
                pvp = 0;
            }

            fm.addHover(new String[]{"Walking: " + walk, "\nMining: " + mine, "\nPVP: " + pvp}, new FancyMessageUtil.Colors[]{FancyMessageUtil.Colors.GREEN, FancyMessageUtil.Colors.GREEN, FancyMessageUtil.Colors.GREEN});

        }

//        sendWarMessage(player, players, fitness);

//        str = fm.addText(str, "hover", FancyMessage.Colors.black, FancyMessage.Attributes.strikethrough);
//        str = fm.addHover(str, new String[]{"hover"}, new FancyMessage.Colors[]{FancyMessage.Colors.red});
//        str = fm.addText(str, "suggestion",FancyMessage.Colors.gray);
//        str = fm.addSuggest(str, "suggested");
//        str = fm.addText(str, "google", FancyMessage.Colors.yellow);
//        str = fm.addUrl(str, "https://google.com");
//        str = fm.addText(str, "run command", FancyMessage.Colors.blue);
//        str = fm.addCommand(str, "/say I ran a command");

        for (Player pl : Bukkit.getOnlinePlayers()) {
            fm.sendMessage(pl);
        }

    }


    @Override
    public void run() {

//        Files war = new Files(p, "warData.yml");
//
//        ArrayList<String> list = (ArrayList<String>) war.getConfig().getStringList("war.pending.memberInvite." + p.getConfig().getString("island." + player.getName()) + ".warriors");
//
//        for (String s : list) {
//            if (Bukkit.getPlayerExact(s) != null) {
//                Bukkit.getPlayerExact(s).sendMessage("no island has accepted this war request");
//            }
//        }
//
//        war.getConfig().set("war.pending.islandInvite." + p.getConfig().contains("island." + player.getName()), null);
//        war.saveConfig();

    }
}
