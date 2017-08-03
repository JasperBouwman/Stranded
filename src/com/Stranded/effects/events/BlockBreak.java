package com.Stranded.effects.events;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    private Main p;

    public BlockBreak(Main instance) {
        p = instance;
    }

    @EventHandler
    public void Block(BlockBreakEvent e) {
        Files save = new Files(p, "playerData.yml");
        Files pluginData = new Files(p, "pluginData.yml");
        Player player = e.getPlayer();

        if (save.getConfig().getLong("HitKill." + player.getName()) / pluginData.getConfig().getInt("plugin.scoreboard.mining.amplifier") != 100) {
            save.getConfig().set("BlockBreak." + player.getName(), save.getConfig().getLong("BlockBreak." + player.getName()) + 1);
            save.saveConfig();
        }
    }
}
