package com.Stranded;

import org.bukkit.Bukkit;

import static com.Stranded.PlayerHide.playerHide;
import static com.Stranded.Scoreboard.scores;

public class Scheduler implements java.lang.Runnable {
    private Main p;

    Scheduler(Main main) {
        p = main;
    }

    void startRun() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(p, this, 20, 20);
    }

    @Override
    public void run() {

        playerHide(p);

        scores(p);

    }
}
