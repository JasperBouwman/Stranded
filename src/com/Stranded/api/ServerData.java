package com.Stranded.api;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;

import static com.Stranded.GettingFiles.getFiles;

@SuppressWarnings("unused")
public class ServerData {

    private Files pluginData;
    private Files playerData;

    public ServerData() {
        pluginData = getFiles( "pluginData.yml");
        playerData = getFiles( "playerData.yml");
    }

    public long getWalkingScore(String uuid) {
        if (!playerData.getConfig().contains("walk." + uuid) || !pluginData.getConfig().contains("plugin.scoreboard.walking.amplifier") ) {
            return 0;
        }
        return playerData.getConfig().getLong("walk." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.walking.amplifier");
    }

    public long getMiningScore(String uuid) {
        if (!playerData.getConfig().contains("BlockBreak." + uuid) || !pluginData.getConfig().contains("plugin.scoreboard.mining.amplifier") ) {
            return 0;
        }
        return playerData.getConfig().getLong("BlockBreak." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.mining.amplifier");
    }

    public long getPvpScore(String uuid) {
        if (!playerData.getConfig().contains("HitKill." + uuid) || !pluginData.getConfig().contains("plugin.scoreboard.pvp.amplifier") ) {
            return 0;
        }
        return playerData.getConfig().getLong("HitKill." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.pvp.amplifier");
    }

    public long getFlyingScore(String uuid) {
        if (!playerData.getConfig().contains("fly." + uuid) || !pluginData.getConfig().contains("plugin.scoreboard.flying.amplifier") ) {
            return 0;
        }
        return playerData.getConfig().getLong("fly." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.flying.amplifier");
    }
}