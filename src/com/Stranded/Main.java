package com.Stranded;

import com.Stranded.events.LoginEvent;
import com.Stranded.islandBorder.events.*;
import com.Stranded.worldGeneration.WorldGenerate;
import com.Stranded.commands.*;
import com.Stranded.effects.PlayerEffects;
import com.Stranded.effects.events.BlockBreak;
import com.Stranded.effects.events.HitKill;
import com.Stranded.effects.events.WalkRun;
import com.Stranded.events.CobbleGenEvent;
import com.Stranded.nexus.DamageEvent;
import com.Stranded.nexus.InventoryEvent;
import com.Stranded.nexus.VillagerInteract;
import com.Stranded.towers.TowerManager;
import com.Stranded.towers.events.BlockPlace;
import com.Stranded.towers.events.PlayerInteract;
import com.Stranded.towers.events.Tnt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public void onEnable() {

        getCommand("nexus").setExecutor(new Nexus(this));
        getCommand("edit").setExecutor(new Edit());
        getCommand("war").setExecutor(new War(this));
        getCommand("island").setExecutor(new Island(this));
//        getCommand("i").setAliases(new  ArrayList<String>().add("i"));

        PluginManager pm = getServer().getPluginManager();
        //boarder
        pm.registerEvents(new BorderEvents(this), this);
        //effects
        pm.registerEvents(new BlockBreak(this), this);
        pm.registerEvents(new WalkRun(this), this);
        pm.registerEvents(new HitKill(this), this);
        //nexus
        pm.registerEvents(new InventoryEvent(this), this);
        pm.registerEvents(new VillagerInteract(this), this);
        pm.registerEvents(new DamageEvent(), this);
        //towers
        pm.registerEvents(new PlayerInteract(this), this);
        pm.registerEvents(new BlockPlace(this), this);
        pm.registerEvents(new Tnt(), this);
        //other
        pm.registerEvents(new CobbleGenEvent(), this);
        pm.registerEvents(new LoginEvent(this), this);


        PlayerEffects pe = new PlayerEffects(this);
        pe.Effects();

        TowerManager tm = new TowerManager(this);
        tm.Tower();

        Scheduler run = new Scheduler(this);
        run.startRun();

        WorldGenerate.generate();

        Files pd = new Files(this, "pluginData.yml");
        pd.saveDefaultConfig();

        Files islands = new Files(this, "islands.yml");
        islands.saveDefaultConfig();

        Files war = new Files(this, "warData.yml");
        war.saveDefaultConfig();



//        String jsonMessage = "{text: \"This is some text!\"}";
//        ReflectionUtils reflectChat = new ReflectionUtils(null, getNMSClass("ChatSerializer"));
//        Object chatComponent = ReflectionUtils.invokeMethod(reflectChat.getMethodDeclared("a", String.class), null, jsonMessage);
//        Object packetPlayOutChat = ReflectionUtils.constructObject(new ReflectionUtils(null, getNMSClass("PacketPlayOutChat")).getConstructor(getNMSClass("IChatBaseComponent")), chatComponent);
//        Field playerConnection = new ReflectionUtils(null, getNMSClass("EntityPlayer")).getFieldDeclared("playerConnection");
//        Method sendPacket = new ReflectionUtils(null, getNMSClass("PlayerConnection")).getMethodDeclared("sendPacket", getNMSClass("Packet"));
//
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            Object handle = ReflectionUtils.invokeMethod(new ReflectionUtils(player, player.getClass()).getMethodDeclared("getHandle"), player);
//            Object connection = ReflectionUtils.getFieldValue(playerConnection, handle);
//            ReflectionUtils.invokeMethod(sendPacket, connection, packetPlayOutChat);
//        }



    }

//    public static final String bukkitVersion;
//    static {
//        bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
//    }
//
//    private Class<?> getNMSClass(String name) {
//        return ReflectionUtils.getClassForName("net.minecraft.server." + bukkitVersion + "." + name);
//    }

}
