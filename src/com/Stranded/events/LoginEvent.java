package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.Scoreboard;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class LoginEvent implements Listener {

    private Main p;

    public LoginEvent(Main main) {
        p = main;
    }

    @SuppressWarnings("All")
    private Object isConnection(Player player) {
        Object nmsPlayer;
        try {
            nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException ignore) {
            return null;
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onOnline(PlayerLoginEvent e) {

        PlayerUUID.setPlayerUUID(e.getPlayer(), p);

        Bukkit.getScheduler().runTaskLater(p, () -> {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                Scoreboard.scores(p, pl);
            }

            Player player = e.getPlayer();
            String uuid = player.getUniqueId().toString();

            for (int i = 0; i < 10; i++) {
                if (isConnection(player) == null) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e1) {
                        //well... fuck after the tenth time, this means that the player isn't logged in, so he/she wont get the messages
                    }
                }
            }

            new ServerPingEvent(p).setPlayer(e.getPlayer());

            ArrayList<String> listRemoved = (ArrayList<String>) p.getConfig().getStringList("online.removedIsland");
            ArrayList<String> listEvict = (ArrayList<String>) p.getConfig().getStringList("online.evict");
            ArrayList<String> listNewOwner = (ArrayList<String>) p.getConfig().getStringList("online.newOwner");
            if (p.getConfig().contains("online.removeAll." + uuid)) {
                p.getConfig().set("online.removeAll." + uuid, null);
                p.saveConfig();

                player.setLevel(0);
                player.getInventory().clear();
            }
            if (p.getConfig().contains("online.warWin." + uuid)) {
                String msg = p.getConfig().getString("online.warWin." + uuid);
                player.sendMessage(msg);
                p.getConfig().set("online.warWin." + uuid, null);
                p.saveConfig();

                Files islands = new Files(p, "islands.yml");
                Location l = (Location) islands.getConfig().get("island." + p.getConfig().getStringList("island." + uuid) + ".home");
                player.teleport(l);
            }
            if (p.getConfig().contains("online.warLose." + uuid)) {
                String msg = p.getConfig().getString("online.warLose." + uuid);
                player.sendMessage(msg);
                p.getConfig().set("online.warLose." +uuid, null);
                p.saveConfig();

                Files islands = new Files(p, "islands.yml");
                Location l = (Location) islands.getConfig().get("island." + p.getConfig().getStringList("island." + uuid) + ".home");
                player.teleport(l);
            }

            if (p.getConfig().contains("online.removedIslandByMod." + uuid)) {
                player.sendMessage("you island is removed by an moderator with the reason: " + p.getConfig().getString("online.removedIslandByMod." + uuid + ".reason"));

                Files pluginData = new Files(p, "pluginData.yml");
                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
                player.teleport(l);

                p.getConfig().set("online.removedIslandByMod." + uuid, null);
                p.saveConfig();
                return;
            }
            if (listRemoved.contains(uuid)) {
                player.sendMessage("you island is removed by the owner");

                Files pluginData = new Files(p, "pluginData.yml");
                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
                player.teleport(l);

                listRemoved.remove(uuid);
                p.getConfig().set("online.removedIsland", listRemoved);
                p.saveConfig();
                return;
            }
            if (listEvict.contains(uuid)) {
                player.sendMessage("you where removed by the owner from this island");

                Files pluginData = new Files(p, "pluginData.yml");
                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
                player.teleport(l);

                listEvict.remove(uuid);
                p.getConfig().set("online.evict", listEvict);
                p.saveConfig();
                return;
            }

            if (listNewOwner.contains(uuid)) {
                player.sendMessage("you are now the new owner if your island");
                listNewOwner.remove(uuid);
                p.getConfig().set("online.newOwner", listNewOwner);
                p.saveConfig();
            }
            if (p.getConfig().contains("online.newIslandOwner." + uuid)) {
                player.sendMessage(p.getConfig().getString("online.newIslandOwner." + uuid));
                p.getConfig().set("online.newIslandOwner." + uuid, null);
                p.saveConfig();
            }

            if (p.getConfig().getStringList("playersInWar").contains(uuid)) {
                Files warData = new Files(p, "warData.yml");
                for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                    if (warData.getConfig().getStringList("war.war." + warID + ".blue.players").contains(uuid)) {
                        player.teleport((Location) warData.getConfig().get("war.war." + warID + ".blueSpawn"));
                        return;
                    }
                    if (warData.getConfig().getStringList("war.war." + warID + ".red.players").contains(uuid)) {
                        player.teleport((Location) warData.getConfig().get("war.war." + warID + ".redSpawn"));
                        return;
                    }
                }
            }
        }, 1);

    }
}
