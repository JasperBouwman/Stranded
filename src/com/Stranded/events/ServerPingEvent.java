package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class ServerPingEvent implements Listener {

    private static HashMap<String, String> playerIP = new HashMap<>();
    private Files pluginData;
    private Main p;

    public ServerPingEvent(Main main) {
        p = main;
        pluginData = new Files(main, "pluginData.yml");
    }

    private String rep(String toReplace, long walk, long block, long fly, long pvp, String player, String island, int islandOnline, int islandCount, String islandLvl) {
        return toReplace.replace("%player%", player)
                .replace("%walk%", walk + "")
                .replace("%pvp%", pvp + "")
                .replace("%mining%", block + "")
                .replace("%fly%", fly + "")
                .replace("%islandonline%", islandOnline + "")
                .replace("%island%", island)
                .replace("%islandlvl%", islandLvl)
                .replace("%islandcount%", islandCount + "");
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void PingEvent(ServerListPingEvent e) {

        if (pluginData.getConfig().contains("plugin.server.MOTD")) {

            if (playerIP.containsKey(e.getAddress().toString().replace(".", ","))) {
                String player = playerIP.get(e.getAddress().toString().replace(".", ","));

                String uuid = PlayerUUID.getPlayerUUID(player, p);

                if (p.getConfig().contains("island." + uuid)) {

                    String islandMOTD = pluginData.getConfig().getString("plugin.server.MOTD.island");

                    if (!islandMOTD.equals("%default%")) {

                        Files playerData = new Files(p, "playerData.yml");
                        Files islands = new Files(p, "islands.yml");

                        long walk = playerData.getConfig().getLong("walk." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.walking.amplifier");
                        long block = playerData.getConfig().getLong("BlockBreak." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.mining.amplifier");
                        long pvp = playerData.getConfig().getLong("HitKill." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.pvp.amplifier");
                        long fly = playerData.getConfig().getLong("fly." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.flying.amplifier");

                        String islandLvl = "0";
                        int islandOnline = 0;
                        int islandCount = 0;

                        String island = p.getConfig().getString("island." + uuid);

                        for (String players : p.getConfig().getConfigurationSection("island").getKeys(false)) {
                            Player pl = Bukkit.getPlayer(UUID.fromString(players));
                            if (p.getConfig().getString("island." + players).equals(island)) {
                                islandCount += 1;
                                if (pl != null) {
                                    islandOnline += 1;
                                }
                            }
                        }
                        if (islands.getConfig().contains("island." + island + ".lvl")) {
                            islandLvl = islands.getConfig().getString("island." + island + ".lvl");
                        }


                        e.setMotd(rep(islandMOTD, walk, block, fly, pvp, player, island, islandOnline, islandCount, islandLvl));
                    }

                } else {
                    String playerMOTD = pluginData.getConfig().getString("plugin.server.MOTD.player");
                    if (!playerMOTD.equals("%default%")) {
                        e.setMotd(playerMOTD.replace("%player%", player));
                    }
                }

            } else {
                String randomMOTD = pluginData.getConfig().getString("plugin.server.MOTD.random");
                if (!randomMOTD.equals("%default%")) {
                    e.setMotd(randomMOTD);
                }

            }
        }

        try {
            if (!pluginData.getConfig().contains("plugin.server.MOTD.icon")) {
                return;
            }
            File file = new File(p.getDataFolder() + "/serverIcon/" + pluginData.getConfig().getString("plugin.server.MOTD.icon") + ".png");
            if (!file.exists()) {
                return;
            }
            BufferedImage image = ImageIO.read(file);
            if (image.getWidth() != 64 || !(image.getHeight() == 64)) {
                return;
            }

            e.setServerIcon(Bukkit.loadServerIcon(image));
        } catch (Exception ignore) {
        }

    }

    private String encode(String toEncode) {

        StringBuilder str = new StringBuilder();

        for (char c : toEncode.toCharArray()) {
            switch (c + "") {
                case "1":
                    str.append("w");
                    break;
                case "2":
                    str.append("g");
                    break;
                case "3":
                    str.append("i");
                    break;
                case "4":
                    str.append("b");
                    break;
                case "5":
                    str.append("n");
                    break;
                case "6":
                    str.append("2");
                    break;
                case "7":
                    str.append("c");
                    break;
                case "8":
                    str.append("k");
                    break;
                case "9":
                    str.append("s");
                    break;
                case "0":
                    str.append("x");
                    break;
                case "/":
                    str.append("h");
                    break;
                case ",":
                    str.append("r");
                    break;
                default:
                    str.append(c);
            }
        }

        return str.toString();
    }

    private String decode(String toDecode) {
        StringBuilder str = new StringBuilder();

        for (char c : toDecode.toCharArray()) {
            switch (c + "") {
                case ("w"):
                    str.append("1");
                    break;
                case ("g"):
                    str.append("2");
                    break;
                case ("i"):
                    str.append("3");
                    break;
                case ("b"):
                    str.append("4");
                    break;
                case ("n"):
                    str.append("5");
                    break;
                case ("2"):
                    str.append("6");
                    break;
                case ("c"):
                    str.append("7");
                    break;
                case ("k"):
                    str.append("8");
                    break;
                case ("s"):
                    str.append("9");
                    break;
                case ("x"):
                    str.append("0");
                    break;
                case ("h"):
                    str.append("/");
                    break;
                case ("r"):
                    str.append(",");
                    break;
                default:
                    str.append(c);
            }
        }

        return str.toString();
    }

    public void setList() {
        playerIP = new HashMap<>();

        for (String ip : pluginData.getConfig().getConfigurationSection("plugin.server.MOTD.ips").getKeys(false)) {
            playerIP.put(decode(ip), pluginData.getConfig().getString("plugin.server.MOTD.ips." + ip));
        }
    }

    public void saveList() {

        for (String ip : playerIP.keySet()) {

            pluginData.getConfig().set("plugin.server.MOTD.ips." + encode(ip), playerIP.get(ip));
        }

        pluginData.saveConfig();
    }

    public void setPlayer(Player player) {
        String ip = player.getAddress().getAddress().toString().replace(".", ",");
        if (!playerIP.containsKey(ip)) {
            playerIP.put(ip, player.getName());
        }
    }
}
