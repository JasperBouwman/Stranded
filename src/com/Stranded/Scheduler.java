package com.Stranded;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

import static com.Stranded.PlayerHide.playerHide;
import static com.Stranded.Scoreboard.scores;

public class Scheduler implements java.lang.Runnable {
    Main p;

    public Scheduler(Main main) {
        p = main;
    }

    public void startRun() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(p, this, 20, 20);
    }

    @Override
    public void run() {

        playerHide(p);

        scores(p);

    }
}
