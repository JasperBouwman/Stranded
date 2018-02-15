package com.Stranded.commands.stranded;

import com.Stranded.Files;
import com.Stranded.GettingFiles;
import com.Stranded.commands.CmdManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.Stranded.GettingFiles.getFiles;

public class FilesCommand extends CmdManager {
    @Override
    public String getName() {
        return "files";
    }

    @Override
    public String getAlias() {
        return "f";
    }

    @Override
    public void run(String[] args, Player player) {

        if (args.length == 1) {
            player.sendMessage("send /stranded files help");

        } else if (args.length == 2) {
            switch (args[1].toLowerCase()) {
                case "autosave":
                    player.sendMessage(ChatColor.GREEN + "AutoSave is now " + ChatColor.DARK_GREEN + getFiles("pluginData").getConfig().getString("plugin.files.autoSave"));
                    return;
                case "reload":
                    new GettingFiles(p);
                    player.sendMessage(ChatColor.GREEN + "All files are reloaded");
                    return;
                case "save":
                    for (Files file : getFiles()) {
                        file.saveConfig(true);
                    }

                    getFiles("config.yml").saveConfig();
                    player.sendMessage(ChatColor.GREEN + "All files are saved");
            }
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("autoSave")) {
                switch (args[2].toLowerCase()) {
                    case "true":
                    case "false":
                        getFiles("pluginData").getConfig().set("plugin.files.autoSave", args[2].toLowerCase());
                        getFiles("pluginData").saveConfig();
                        player.sendMessage(ChatColor.GREEN + "AutoSave is now " + ChatColor.DARK_GREEN + args[2].toLowerCase());
                        return;
                    default:
                            player.sendMessage(ChatColor.RED + "You must use true or false");
                }
            }
        }
    }
}
