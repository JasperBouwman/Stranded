package com.Stranded.worldGeneration.warIsland;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Location;

public class WarIslandUtil {

    public Files f;

    public boolean testWarIslandValidation(Main p, String warIslandName) {

        if (!warIslandName.endsWith(".yml")) {
            warIslandName += ".yml";
        }

        Files warIsland = new Files(p, warIslandName);
        if (!warIsland.isWarIsland()) {
            return false;
        }

        warIsland.setWarIsland();

        f = warIsland;

        boolean y;
        boolean blueSpawn;
        boolean redSpawn;
        boolean first;
        boolean second;
        boolean theme;
        boolean maxPlayers;
        boolean minPlayers;
        boolean islandRegion;

        try {

            y = warIsland.getConfig().contains("warIsland.y");
            y = y && warIsland.getConfig().get("warIsland.y") instanceof Integer;

            theme = warIsland.getConfig().contains("warIsland.theme");
            theme = theme && warIsland.getConfig().get("warIsland.theme") instanceof String;

            maxPlayers = warIsland.getConfig().contains("warIsland.maxPlayers");
            maxPlayers = maxPlayers && warIsland.getConfig().get("warIsland.maxPlayers") instanceof Integer;

            minPlayers = warIsland.getConfig().contains("warIsland.minPlayers");
            minPlayers = minPlayers && warIsland.getConfig().get("warIsland.minPlayers") instanceof Integer;

            blueSpawn = warIsland.getConfig().contains("warIsland.blueSpawn");
            blueSpawn = blueSpawn && warIsland.getConfig().get("warIsland.blueSpawn") instanceof Location;

            redSpawn = warIsland.getConfig().contains("warIsland.redSpawn");
            redSpawn = redSpawn && warIsland.getConfig().get("warIsland.redSpawn") instanceof Location;

            first = warIsland.getConfig().contains("warIsland.islandSize.first");
            first = first && warIsland.getConfig().get("warIsland.islandSize.first") instanceof Location;

            second = warIsland.getConfig().contains("warIsland.islandSize.second");
            second = second && warIsland.getConfig().get("warIsland.islandSize.second") instanceof Location;

            islandRegion = warIsland.getConfig().contains("warIsland.region");

        } catch (Exception e) {
            return false;
        }

        return theme && maxPlayers && minPlayers && blueSpawn && redSpawn && islandRegion && y && first && second;
    }
}
