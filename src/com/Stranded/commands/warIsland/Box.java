package com.Stranded.commands.warIsland;

import com.Stranded.commands.CmdManager;
import org.bukkit.entity.Player;

public class Box extends CmdManager {

    @Override
    public String getName() {
        return "box";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //this works all within the warIslands
        //warIsland box get <theme> <warIslandID>
        //warIsland box add <lootTable> /*send player box ID*/
        //warIsland box remove /*removed box from your location*/
        //warIsland box remove <id>
        //warIsland box remove <id> [theme] [warIslandID] /*these args when you want to delete from distance*/
        //warIsland box removeAll
        //warIsland box removeAll [theme] [warIslandID] /*these args when you want to delete all from distance*/
        //warIsland box edit <id> /*return all data*/
        //warIsland box edit <id> location /*teleport lootBox to you*/
        //warIsland box edit <id> lootTable <lootTable>
        //warIsland box show <theme> <warIslandID> /*gives item, when hold lootBox boundaries are shown*/
        //warIsland box test <lootTable>

    }
}
