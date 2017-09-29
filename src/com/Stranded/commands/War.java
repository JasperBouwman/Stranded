package com.Stranded.commands;

import com.Stranded.FancyMessageUtil;
import com.Stranded.Main;
import com.Stranded.commands.war.Accept;
import com.Stranded.commands.war.Ready;
import com.Stranded.commands.war.Start;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class War implements CommandExecutor {

    public List<CmdManager> actions = new ArrayList<>();
    private Main p;

    public War(Main main) {
        p = main;

        actions.add(new Start());
        actions.add(new Accept());
        actions.add(new Ready());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("I don't know what you are!");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {

            Location l = player.getLocation();
            player.sendMessage(" " + l.getWorld().getName() + "\n " + l.getX() + "\n " + l.getY() + "\n " + l.getZ());

            FancyMessageUtil fm = new FancyMessageUtil();
            fm.addText("hover\n", FancyMessageUtil.Colors.AQUA);
            fm.addHover(new String[]{"hover", "\nhover", "\nhover"}, new FancyMessageUtil.Colors[]{FancyMessageUtil.Colors.DARK_PURPLE, FancyMessageUtil.Colors.GREEN, FancyMessageUtil.Colors.BLUE}, new FancyMessageUtil.Attributes[]{FancyMessageUtil.Attributes.BOLD, FancyMessageUtil.Attributes.ITALIC, FancyMessageUtil.Attributes.UNDERLINE});
            fm.addText("command\n", FancyMessageUtil.Colors.RED);
            fm.addCommand("/say hai");
            fm.addText("url\n", FancyMessageUtil.Colors.DARK_BLUE);
            fm.addUrl("https://google.com");
            fm.addText("suggestion", FancyMessageUtil.Colors.DARK_GRAY);
            fm.addSuggest("suggested");

            fm.sendMessage(player);

            return false;
        }

        for (CmdManager action : this.actions) {
            if (args[0].toLowerCase().equals(action.getName()) || args[0].toLowerCase().equals(action.getAlias())) {
                action.setMain(p);
                action.run(args, player);
                return false;
            }
        }

        player.sendMessage("this is not a sub-command");
        return false;

    }
}
