package com.Stranded.mapsUtil;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.Stranded.GettingFiles.getFiles;

@SuppressWarnings({"unused", "WeakerAccess", "deprecation"})
public class MapTool {

    private BufferedImage image;
    private ArrayList<Short> mapIDs = new ArrayList<>();
    private int width = 0;
    private int height = 0;
    private boolean keepCentered = true;
    private boolean keepRatio = false;
    private String mapToolName;
    private Main p;

    private String imageName;

    private static HashMap<String, MapTool> mapTools = new HashMap<>();

    public MapTool(Main p, String image, String name) throws IllegalArgumentException {
        this.p = p;
        this.imageName = image;
        this.mapToolName = name;

        if (mapTools.containsKey(name)) {
            throw new IllegalArgumentException("MapTool name '" + name + "' already exist");
        }

        try {
            File f = new File(p.getDataFolder() + "/maps", image);
            if (!f.exists()) {
                InputStream in = p.getResource("MapImage.jpg");

                if (in == null) {
                    return;
                }
                this.image = ImageIO.read(in);
            } else {
                this.image = ImageIO.read(f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapTools.put(name, this);
    }

    public static MapTool getMapTool(String name) {
        return mapTools.getOrDefault(name, null);
    }

    public static void saveMapTool(String name) {
        Files maps = getFiles("maps");
        MapTool tool = mapTools.get(name);

        maps.getConfig().set("mapTools." + name + ".width", tool.width);
        maps.getConfig().set("mapTools." + name + ".height", tool.height);
        maps.getConfig().set("mapTools." + name + ".keepCentered", tool.keepCentered);
        maps.getConfig().set("mapTools." + name + ".keepRatio", tool.keepRatio);
        maps.getConfig().set("mapTools." + name + ".imageName", tool.imageName);
        maps.getConfig().set("mapTools." + name + ".mapIDs", tool.mapIDs);

        maps.saveConfig();
    }

    public static void saveMapTools() {
        getFiles("maps").getConfig().set("maps", null);
        for (String name : mapTools.keySet()) {
            saveMapTool(name);
        }
    }

    public static void loadMaps(Main p) {
        Files maps = getFiles("maps");
        if (maps.getConfig().contains("mapTools")) {
            for (String name : maps.getConfig().getConfigurationSection("mapTools").getKeys(false)) {
                loadMap(p, name);
            }
        }
    }

    public static void loadMap(Main p, String name) {
        Files maps = getFiles("maps");

        int width = maps.getConfig().getInt("mapTools." + name + ".width");
        int height = maps.getConfig().getInt("mapTools." + name + ".height");
        String imageName = maps.getConfig().getString("mapTools." + name + ".imageName");
        boolean keepCentered = maps.getConfig().getBoolean("mapTools." + name + ".keepCentered");
        boolean keepRatio = maps.getConfig().getBoolean("mapTools." + name + ".keepRatio");
        ArrayList<Short> mapIDs = (ArrayList<Short>) maps.getConfig().getShortList("mapTools." + name + ".mapIDs");

        if (width <= 0 || height <= 0) {
            mapTools.remove(name);
            System.out.println("mapTool '" + name + "' does not have the correct dimensions. This mapTool is now removed");
            return;
        }

        new MapTool(p, imageName, name).loadMap(width, height, keepCentered, keepRatio, mapIDs);
    }

    public void loadMap(int width, int height, boolean keepCentered, boolean keepRatio, ArrayList<Short> mapIDs) throws IllegalArgumentException {

        //tests if had legal width and height
        if (width <= 0 || height <= 0) {
            return;
        }

        //tests if width/height is not reassigned
        if (this.width != 0) {
            if (this.width != width || this.height != height) {
                throw new IllegalArgumentException("Width or height can not be reassigned");
            }
        }

        int xOffset = 0;
        int yOffset = 0;

        if (keepRatio) {
            Dimension imgSize = new Dimension(image.getWidth(), image.getHeight());
            Dimension boundary = new Dimension(width * 128, height * 128);

            Dimension d = getScaledDimension(imgSize, boundary);

            resize((int) d.getWidth(), (int) d.getHeight());


            if (keepCentered) {
                xOffset = (width * 128 - image.getWidth()) / 2;
                yOffset = (height * 128 - image.getHeight()) / 2;
            }

        } else {
            resize(width * 128, height * 128);
        }

        int map = 0;

        while (map < mapIDs.size()) {

            for (int w = 0; w < height; w++) {
                for (int h = 0; h < width; h++) {

                    BufferedImage img = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
                    for (int x = 0; x < img.getWidth(); x++) {
                        for (int y = 0; y < img.getHeight(); y++) {
                            int tmpX = x + h * 128 - xOffset;
                            int tmpY = y + w * 128 - yOffset;

                            if (tmpX >= image.getWidth() || tmpY >= image.getHeight() || tmpX < 0 || tmpY < 0) {
                                img.setRGB(x, y, new Color(0, 0, 0).getRGB());
                            } else {
                                img.setRGB(x, y, image.getRGB(tmpX, tmpY));
                            }
                        }
                    }

                    MapView view = Bukkit.getMap(mapIDs.get(map));
                    for (MapRenderer m : view.getRenderers()) {
                        view.removeRenderer(m);
                    }
                    view.addRenderer(new ImageMapRenderer(img));

                    map++;

                }
            }
        }

        this.mapIDs = mapIDs;
        this.height = height;
        this.width = width;
        this.keepCentered = keepCentered;
        this.keepRatio = keepRatio;

    }

    public void loadMap() {

        try {
            File f = new File(p.getDataFolder() + "/maps", imageName);
            if (!f.exists()) {
                InputStream in = p.getResource("MapImage.jpg");

                if (in == null) {
                    return;
                }
                this.image = ImageIO.read(in);
            } else {
                this.image = ImageIO.read(f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadMap(width, height, keepCentered, keepRatio, mapIDs);
    }

    public void removeMapTool() {
        mapTools.remove(mapToolName);

        for (short mapID : mapIDs) {
            MapView view = Bukkit.getMap(mapID);
            for (MapRenderer renderer : view.getRenderers()) {
                view.removeRenderer(renderer);
            }
        }
    }

    private void resize(int w, int h) {
        BufferedImage img =
                new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int x, y;
        int ww = image.getWidth();
        int hh = image.getHeight();
        int[] ys = new int[h];
        for (y = 0; y < h; y++)
            ys[y] = y * hh / h;
        for (x = 0; x < w; x++) {
            int newX = x * ww / w;
            for (y = 0; y < h; y++) {
                int col = image.getRGB(newX, ys[y]);
                img.setRGB(x, y, col);
            }
        }
        image = img;
    }

    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    public void setMaps(int width, int height, boolean keepCentered, boolean keepRatio, Block mapLocation, Player player) throws IllegalArgumentException {
        //tests if had legal width and height
        if (width <= 0 || height <= 0) {
            return;
        }

        //tests if width/height is not reassigned
        if (this.width != 0) {
            if (this.width != width || this.height != height) {
                throw new IllegalArgumentException("Width or height can not be reassigned");
            }
        }

        ArrayList<ItemStack> maps = getMaps(width, height, keepCentered, keepRatio);

        BlockFace face = BlockFace.NORTH;
        float yaw = player.getLocation().getYaw();
        boolean invert = false;
        if (yaw < 0) {
            invert = true;
            yaw = Math.abs(yaw);
        }
        if (yaw < 45) {
            face = BlockFace.NORTH;
        } else if (yaw < 135) {
            if (invert) {
                face = BlockFace.EAST;
            } else {
                face = BlockFace.WEST;
            }
        } else if (yaw < 225) {
            face = BlockFace.SOUTH;
        } else if (yaw < 315) {
            if (invert) {
                face = BlockFace.WEST;
            } else {
                face = BlockFace.EAST;
            }
        }

        boolean b = testAccessibility(mapLocation, face, width, height);
        if (!b) {
            player.sendMessage("wrong!");
            return;
        }

        int i = 0;
        if (face.equals(BlockFace.EAST) || face.equals(BlockFace.WEST)) {
            for (int h = height - 1; h >= 0; h--) {
                for (int w = 0; w < width; w++) {
                    ItemStack map = maps.get(i);
                    spawnItemFrame(mapLocation, face, map, player, w, h);
                    i++;
                }
            }
        } else {
            for (int h = height - 1; h >= 0; h--) {
                for (int w = width - 1; w >= 0; w--) {
                    ItemStack map = maps.get(i);
                    spawnItemFrame(mapLocation, face, map, player, w - width + 1, h);
                    i++;
                }
            }
        }


    }

    public void setMaps(Block mapLocation, Player player) {
        setMaps(width, height, keepCentered, keepRatio, mapLocation, player);
    }

    private boolean testAccessibility(Block mapLocation, BlockFace face, int width, int height) {

        if (face.equals(BlockFace.EAST) || face.equals(BlockFace.WEST)) {
            for (int h = height - 1; h >= 0; h--) {
                for (int w = 0; w < width; w++) {

                    int x = 0;
                    int z = (face.equals(BlockFace.EAST) ? w : 0) + (face.equals(BlockFace.WEST) ? -w : 0);

                    int xOffset = 0;
                    int zOffset = 0;

                    if (face.equals(BlockFace.SOUTH)) {
                        zOffset += 1;
                    } else if (face.equals(BlockFace.NORTH)) {
                        zOffset += -1;
                    } else if (face.equals(BlockFace.WEST)) {
                        xOffset += 1;
                    } else if (face.equals(BlockFace.EAST)) {
                        xOffset += -1;
                    }

                    if (mapLocation.getLocation().clone().add(x, h, z).getBlock().isEmpty()) {
                        return false;
                    }
                    if (!mapLocation.getLocation().clone().add(x + xOffset, h, z + zOffset).getBlock().isEmpty()) {
                        return false;
                    }
                }
            }
        } else {
            for (int h = height - 1; h >= 0; h--) {
                for (int w = 0; w < width; w++) {

                    int x = (face.equals(BlockFace.NORTH) ? -w : 0) + (face.equals(BlockFace.SOUTH) ? w : 0);
                    int z = 0;

                    int xOffset = 0;
                    int zOffset = 0;

                    if (face.equals(BlockFace.SOUTH)) {
                        zOffset += 1;
                    } else if (face.equals(BlockFace.NORTH)) {
                        zOffset += -1;
                    } else if (face.equals(BlockFace.WEST)) {
                        xOffset += 1;
                    } else if (face.equals(BlockFace.EAST)) {
                        xOffset += -1;
                    }

                    if (mapLocation.getLocation().clone().add(x, h, z).getBlock().isEmpty()) {
                        return false;
                    }
                    if (!mapLocation.getLocation().clone().add(x + xOffset, h, z + zOffset).getBlock().isEmpty()) {
                        return false;
                    }

                }
            }
        }
        return true;
    }

    private void spawnItemFrame(Block mapLocation, BlockFace face, ItemStack map, Player player, int width, int height) {

        int x = (face.equals(BlockFace.NORTH) ? width : 0) + (face.equals(BlockFace.SOUTH) ? -width : 0);
        int z = (face.equals(BlockFace.EAST) ? width : 0) + (face.equals(BlockFace.WEST) ? -width : 0);

        if (face.equals(BlockFace.SOUTH)) {
            z += 1;
        } else if (face.equals(BlockFace.NORTH)) {
            z += -1;
        } else if (face.equals(BlockFace.WEST)) {
            x += 1;
        } else if (face.equals(BlockFace.EAST)) {
            x += -1;
        }

        try {
            ItemFrame frame = mapLocation.getWorld().spawn(mapLocation.getLocation().clone().add(x, height, z), ItemFrame.class);
            frame.setItem(map);
            frame.setFacingDirection(face);

            HangingPlaceEvent hEvent = new HangingPlaceEvent(frame, player, mapLocation, face.getOppositeFace());
            p.getServer().getPluginManager().callEvent(hEvent);
        } catch (IllegalArgumentException ignore) {
            player.sendMessage("This map could not been set");
            player.getInventory().addItem(map);
        }
    }

    public ArrayList<ItemStack> getMaps(int width, int height, boolean keepCentered, boolean keepRatio) throws IllegalArgumentException {

        //tests if had legal width and height
        if (width <= 0 || height <= 0) {
            return new ArrayList<>();
        }

        //tests if width/height is not reassigned
        if (this.width != 0) {
            if (this.width != width || this.height != height) {
                throw new IllegalArgumentException("Width or height can not be reassigned");
            }
        }

        int xOffset = 0;
        int yOffset = 0;

        if (keepRatio) {
            Dimension imgSize = new Dimension(image.getWidth(), image.getHeight());
            Dimension boundary = new Dimension(width * 128, height * 128);

            Dimension d = getScaledDimension(imgSize, boundary);

            resize((int) d.getWidth(), (int) d.getHeight());

            if (keepCentered) {
                xOffset = (width * 128 - image.getWidth()) / 2;
                yOffset = (height * 128 - image.getHeight()) / 2;

            }

        } else {
            resize(width * 128, height * 128);
        }

        this.height = height;
        this.width = width;
        this.keepRatio = keepRatio;
        this.keepCentered = keepCentered;

        ArrayList<ItemStack> mapItems = new ArrayList<>();

        for (int w = 0; w < height; w++) {
            for (int h = 0; h < width; h++) {

                BufferedImage img = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);

                for (int x = 0; x < img.getWidth(); x++) {
                    for (int y = 0; y < img.getHeight(); y++) {

                        int tmpX = x + h * 128 - xOffset;
                        int tmpY = y + w * 128 - yOffset;

                        if (tmpX >= image.getWidth() || tmpY >= image.getHeight() || tmpX < 0 || tmpY < 0) {
                            img.setRGB(x, y, new Color(0, 0, 0).getRGB());
                        } else {
                            img.setRGB(x, y, image.getRGB(tmpX, tmpY));
                        }
                    }
                }

                ItemStack is = new ItemStack(Material.MAP);
                MapView mapView = Bukkit.createMap(Bukkit.getWorld("world"));
                for (MapRenderer m : mapView.getRenderers()) {
                    mapView.removeRenderer(m);
                }

                ImageMapRenderer map = new ImageMapRenderer(img);
                mapView.addRenderer(map);

                short mapID = mapView.getId();
                is.setDurability(mapID);
                mapIDs.add(mapID);

                ItemMeta im = is.getItemMeta();
                im.setLore(Collections.singletonList("MapTool name: " + this.mapToolName));
                is.setItemMeta(im);

                mapItems.add(is);

//                try {
//                    ImageIO.write(img, "jpg", new File("D:\\Users\\jphbo\\Minecraft servers\\test server\\plugins\\Stranded\\images\\" + h + "X" + w + ".jpg"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }
        }

        return mapItems;
    }

    public ArrayList<ItemStack> getMaps(int width, int height) {
        return getMaps(width, height, true, true);
    }

    public ArrayList<ItemStack> getMaps() {
        return getMaps(width, height, keepCentered, keepRatio);
    }

    public void setKeepCentered(boolean keepCentered) {
        this.keepCentered = keepCentered;
        this.loadMap();
    }

    public void setKeepRatio(boolean keepRatio) {
        this.keepRatio = keepRatio;
        this.loadMap();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
