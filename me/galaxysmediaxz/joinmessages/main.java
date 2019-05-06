package me.galaxysmediaxz.joinmessages;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {
    public static String pr,reload;
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
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.sendMessage(getConfig().getString("Messages.Welcome-Message").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()).replace("%newline%", "\n"));
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
                    reloadConfig();
                    saveConfig();
                    sender.sendMessage(pr + reload);
                }
                if(args[0].equalsIgnoreCase("help")){
                sender.sendMessage(pr + "§6/jm - Show this message\n" + pr + "§6/jm reload - Reload JoinMessages");
            }
            }
            }
        }
        return false;
    }
}

