package com.Stranded.commands;

import com.Stranded.Main;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public abstract class CmdManager {

    protected Player player;

    protected Main p;

    public abstract String getName();

    public abstract String getAlias();

    public abstract void run(String[] args);

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMain(Main main) {
        this.p = main;
    }

}
