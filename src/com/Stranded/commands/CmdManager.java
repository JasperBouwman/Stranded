package com.Stranded.commands;

import com.Stranded.Main;
import org.bukkit.entity.Player;

public abstract class CmdManager {

    protected Main p;

    public abstract String getName();

    public abstract String getAlias();

    public abstract void run(String[] args, Player player);

    public void setMain(Main main) {
        this.p = main;
    }



}
