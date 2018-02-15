package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.war.Accept;
import com.Stranded.commands.war.Ready;
import com.Stranded.commands.war.Start;
import com.Stranded.fancyMassage.book.FancyBook;
import com.Stranded.fancyMassage.book.FancyBookPage;
import com.Stranded.fancyMassage.book.FancyTextComponent;
import com.Stranded.fancyMassage.book.events.FancyClickEvent;
import com.Stranded.fancyMassage.book.events.FancyHoverEvent;
import com.Stranded.mapsUtil.MapTool;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.Stranded.fancyMassage.book.FancyBookPage.fancyBookPage;
import static com.Stranded.fancyMassage.book.FancyTextComponent.fancyTextComponent;
import static com.Stranded.fancyMassage.book.FancyTextComponent.italic;
import static com.Stranded.fancyMassage.book.events.FancyClickEvent.fancyClickEvent;
import static com.Stranded.fancyMassage.book.events.FancyClickEvent.runCommand;
import static com.Stranded.fancyMassage.book.events.FancyHoverEvent.fancyHoverEvent;

public class War implements CommandExecutor {

    public List<CmdManager> actions = new ArrayList<>();
    private Main p;

    public War(Main main) {
        p = main;

        actions.add(new Start());
        actions.add(new Accept());
        actions.add(new Ready());
    }

    private double pi(long accuracy) {
        double pi = 0;
        boolean b = true;
        for (long i = 1; i < accuracy; i += 2) {
            if (b) {
                pi += 4D / i;
            } else {
                pi -= 4D / i;
            }
            b = !b;
        }
        return pi;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //war
        //war start <war theme> [playerNames...]
        //war accept <island2>
        //war accept <island1> [players...]
        //war ready

        if (!(sender instanceof Player)) {
            sender.sendMessage("I don't know what you are!");
            return false;
        }

        Player player = (Player) sender;

        if (Main.reloadPending) {
            player.sendMessage("the server is trying to reload, please wait just a second to let the server reload");
            return false;
        }

        if (args.length == 0) {
            player.sendMessage("ping is: " + ((CraftPlayer) player).getHandle().ping);
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("book")) {

            FancyBook book = new FancyBook("book", player.getName());

            FancyBookPage page = fancyBookPage();

            FancyTextComponent text = fancyTextComponent("test ", ChatColor.GREEN, "feds");

            FancyHoverEvent hEvent = fancyHoverEvent(fancyTextComponent("HOVER!!!"));
            hEvent.addText(fancyTextComponent("\nMORE HOVER!!!", ChatColor.BLUE));

            FancyClickEvent cEvent = fancyClickEvent(runCommand, "/stranded");

            page.addText(text);
            page.addText(fancyTextComponent("hover ", "blue", hEvent));
            page.addText(fancyTextComponent("clickEvent", "red", cEvent));


            FancyBookPage page2 = new FancyBookPage();
            page2.addText(fancyTextComponent("new page :D", ChatColor.DARK_AQUA, fancyClickEvent(runCommand, "/stranded"), fancyHoverEvent(fancyTextComponent("/stranded"))));

            book.addPage(page, page2);

            System.out.println(book.translateBook());

            book.openBook(player);

        }

        if (args.length == 3) {

//            player.openInventory(new LootBox(p, args[0]).getLootBox(LootBox.lootBoxSize.SMALL));

            MapTool map = new MapTool(p, args[0], "test");

//            ItemStack[] is = map.getMaps(Integer.parseInt(args[1]), Integer.parseInt(args[2]), true).toArray(new ItemStack[0]);
//            player.getInventory().addItem(is);

            Block b = player.getTargetBlock(null, 10);
            map.setMaps(Integer.parseInt(args[1]), Integer.parseInt(args[2]), false, true, b, player);

//            ItemStack is = new ItemStack(Material.MAP);
//            MapView mapView = Bukkit.createMap(Bukkit.getWorld("world"));
//            for (MapRenderer m : mapView.getRenderers()) {
//                mapView.removeRenderer(m);
//            }
//            mapView.addRenderer(new GifMapRenderer(args[0]));
//            short mapID = mapView.getId();
//            is.setDurability(mapID);
//
//            player.getInventory().addItem(is);

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
