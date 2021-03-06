package com.Stranded.nexus;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.Scoreboard;
import com.Stranded.gamble.inv.InvGamble;
import com.Stranded.nexus.inv.InvMain;
import com.Stranded.towers.inventory.InvTower;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.Stranded.GettingFiles.getFiles;

public class InventoryEvent implements Listener {

    private final Main p;

    public InventoryEvent(Main instance) {
        p = instance;
    }

    public static ItemStack newItemStack(Material m, int damage, String displayName) {

        ItemStack is = new ItemStack(m, (byte) 1, (short) damage);
        ItemMeta im = is.getItemMeta();
        if (displayName != null) {
            im.setDisplayName(displayName);
        }
        is.setItemMeta(im);
        return is;
    }

    private static String upgradeIsland(Player player) {

        Files islands = getFiles("islands.yml");
        Files config = getFiles("config.yml");

        String island = config.getConfig().getString("island." + player.getUniqueId().toString());

        double x = islands.getConfig().getDouble("island." + island + ".lvl");

        double y = Math.sinh((x + 35) / 20);

        Scoreboard.updateIslandScoreboard(island);

        if (x == 100) {
            return "max upgraded";
        }

        if (player.getLevel() >= (int) y) {

            player.setLevel(player.getLevel() - (int) y);

            islands.getConfig().set("island." + island + ".lvl", (int) x + 1);
            islands.saveConfig();

            return "upgraded";
        }

        return "not enough XP";
    }

    private static String upgradeNexus(Player player, Main p) {
        Files islands = getFiles( "islands.yml");
        Files config = getFiles("config.yml");

        String island = config.getConfig().getString("island." + player.getUniqueId().toString());

        double x = islands.getConfig().getDouble("island." + island + ".nexusLvl");

        double y = Math.sinh((x + 35) / 20);

        Scoreboard.updateIslandScoreboard(island);

        if (x == 50) {
            return "max upgraded";
        }

        if (player.getLevel() >= (int) y) {

            player.setLevel(player.getLevel() - (int) y);

            islands.getConfig().set("island." + island + ".nexusLvl", (int) x + 1);
            islands.getConfig().set("island." + island + ".nexusHealth", (int) (x * 2) + 20);
            islands.saveConfig();

            return "upgraded";
        }

        return "not enough XP";
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();

        if (inv.getTitle().equals("§2Nexus")) {
            ItemStack item = e.getCurrentItem();
            if (item == null) {
                return;
            }

            ItemMeta meta = item.getItemMeta();

            if (meta == null) {
                return;
            }
            if (meta.getDisplayName() == null) {
                return;
            }

            Files pluginData = getFiles( "pluginData.yml");
            Files islands = getFiles( "islands.yml");

            int rawSlot = e.getRawSlot();

            switch (rawSlot) {
                case 0:
                    if (item.equals(InvMain.islandUpgrade(islands, player))) {
                        player.sendMessage(upgradeIsland(player));
                        e.setCancelled(true);
                        InvMain.openInv(player);
                    } else {
                        InvMain.openInv(player);
                    }
                    break;
                case 1:
                    if (item.equals(InvMain.nexusUpgrade(islands, player))) {
                        player.sendMessage(upgradeNexus(player, p));
                        e.setCancelled(true);
                        InvMain.openInv(player);
                    } else {
                        InvMain.openInv(player);
                    }
                    break;
                case 2:
                    InvGamble.openInv(player);
                    break;
                case 3:
                    InvTower.openInv(player);
                    break;
            }
        }
    }
}
