package me.galaxysmediaxz.joinmessages;

import java.util.List;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {
    public static String pr,reload,playeronly,reloading;
    public static main plugin;
            
    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getLogger().info("[JoinMessages] Checking config...");
        getServer().getLogger().info("[JoinMessages] Trying to register event...");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getLogger().info("[JoinMessages] Config has been checked! Config will replace with default config if it error!");
        getServer().getLogger().info("[JoinMessages] Events has been registered! Preparing plugins...");
        getServer().getLogger().info("[JoinMessages] Plugins has been fully started!");
        pr = getConfig().getString("Messages.Prefix").replace("&", "§");
        reload = getConfig().getString("Messages.Reload-Complete").replace("&", "§");
        playeronly = getConfig().getString("Messages.Player-Only").replace("&", "§");
        reloading = getConfig().getString("Messages.Reloading").replace("&", "§");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (int i = 0; i < 100; i++){
            p.sendMessage(" ");
        }
        List<String> Lines = getConfig().getStringList("Messages.MOTD");
        for (String s : Lines){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("{player}", p.getName()).replace("{server}", getServer().getServerName()));
        }
        
        PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE,
                ChatSerializer.a("{\"text\":\"" + this.getConfig().getString("Title.Title").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()) + "\"}"),40,20,20);
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
                ChatSerializer.a("{\"text\":\"" + this.getConfig().getString("Title.Subtitle").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()) + "\"}"),40,20,20);
        PacketPlayOutChat actionbar = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + this.getConfig().getString("Title.Actionbar").replace("&" , "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()) + "\"}"), (byte) 2);
        
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(actionbar);
   }

    @Override
    public void onDisable() {
        getServer().getLogger().info("[JoinMessages] Stopping a plugins..");
        reloadConfig();
        saveConfig();
		getServer().getLogger().info("[JoinMessages] JoinMessages just disabled!");
        super.onDisable();
    }

    @Override
    public void onLoad() {
        Bukkit.getServer().getLogger().info("[JoinMessages] Loading JoinMessages...");
        super.onLoad();
        reloadConfig();
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender instanceof Player)){
            sender.sendMessage(pr + playeronly);
            return true;
        }
        else{  
        Player p = (Player)sender;
        if(command.getName().equalsIgnoreCase("jm")){
            if(!sender.hasPermission("jm.admin")){
                sender.sendMessage(pr + getConfig().getString("Messages.No-Permission").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()).replace("%newline%", "\n"));
            }
            else{
                
            if(args.length == 0){
                sender.sendMessage(pr + "§6/jm - Show this message\n" + pr + "§6/jm reload - Reload JoinMessages");
            }
            else{
                if(args[0].equalsIgnoreCase("reload")){
                    getServer().getLogger().info("[JoinMessages] Reloading Joinmessages...");
                    sender.sendMessage(pr + reloading);
                    reloadConfig();
                    saveConfig();
                    setEnabled(false);
                    setEnabled(true);
                    sender.sendMessage(pr + reload);
                }
                if(args[0].equalsIgnoreCase("help")){
                sender.sendMessage(pr + "§6/jm - Show this message\n" + pr + "§6/jm reload - Reload JoinMessages");
            }
            }
            }
        }
        }
        return false;
    }
    
    
}

