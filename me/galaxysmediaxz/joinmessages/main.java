package me.galaxysmediaxz.joinmessages;

import java.lang.reflect.Constructor;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class main extends JavaPlugin implements Listener {
    public static String pr,reload,playeronly,reloading,titleon,titleoff,motdoff,motdon,title,subtitle,actionbar;
    public static main plugin;
    public String version;
    public String cversion;

   
    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getLogger().info("This server using" + getServer().getServerId() + "Version" + getServer().getVersion());
        getServer().getLogger().info("[JoinMessages] Checking config...");
        getServer().getLogger().info("[JoinMessages] Trying to register event...");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getLogger().info("[JoinMessages] Config has been checked! Config will replace with default config if it error!");
        getServer().getLogger().info("[JoinMessages] Events has been registered! Preparing plugins...");

        pr = getConfig().getString("Messages.Prefix").replace("&", "§");
        reload = getConfig().getString("Messages.Reload-Complete").replace("&", "§");
        playeronly = getConfig().getString("Messages.Player-Only").replace("&", "§");
        reloading = getConfig().getString("Messages.Reloading").replace("&", "§");
        titleon = getConfig().getString("Messages.Title-On").replace("&", "§");
        titleoff = getConfig().getString("Messages.Title-Off").replace("&", "§");
        motdon = getConfig().getString("Messages.MOTD-On").replace("&", "§");
        motdoff = getConfig().getString("Messages.MOTD-Off").replace("&", "§");
        if(getConfig().getBoolean("Settings.Check-Update")){
try {
        Bukkit.getConsoleSender().sendMessage("[JoinMessages] Checking for updates...");
        UpdateChecker updater = new UpdateChecker(this, 66307);
        if(updater.checkForUpdates()) {
        getServer().getLogger().info("[JoinMessages] An update found!");
        getServer().getLogger().info("[JoinMessages] Lastest version is: " + updater.getLatestVersion() + " Download at: " + updater.getResourceUrl());
        }else{
        // this will print when no updates
        getServer().getLogger().info("[JoinMessages] Your JoinMessages is lastest version!");
        return;
        }
        }catch(Exception e) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not proceed update-checking, Ignoring...");
        }
        }
        else{
            getServer().getLogger().info("[JoinMessages] Check for update has been disabled! Ignoring...");
            getServer().getLogger().info("[JoinMessages] Plugins has been fully started! Have fun!");
            return;
        }
        getServer().getLogger().info("[JoinMessages] Plugins has been fully started! Have fun!");
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        
        title = getConfig().getString("Title.Title").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName());
        subtitle = getConfig().getString("Title.Subtitle").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName());
        actionbar = getConfig().getString("Title.Actionbar").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName());
        
        if(getConfig().getBoolean("Settings.MOTD-Enable")){
        for (int i = 0; i < 100; i++){
            p.sendMessage(" ");
        
        }
        List<String> Lines = getConfig().getStringList("Messages.MOTD");
        for (String s : Lines){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("{player}", p.getName()).replace("{server}", getServer().getServerName()));
        }
        }
        e.setJoinMessage(getConfig().getString("Messages.Join-Message").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()));
    }
      

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent e){
        
        Player p = e.getPlayer();
        
        e.setQuitMessage(getConfig().getString("Messages.Quit-Message").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()));
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
                sender.sendMessage(pr + "§6/jm - Show this message\n" + pr + "§6/jm reload - Reload JoinMessages\n" + pr + "§6/jm titleon - Enable Join Title\n" + pr + "§6/jm titleoff - Disable Join Title");
                return true;
            }
                if(args[0].equalsIgnoreCase("reload")){
                    getServer().getLogger().info("[JoinMessages] Reloading Joinmessages...");
                    sender.sendMessage(pr + reloading);
                    reloadConfig();
                    saveConfig();
                    setEnabled(false);
                    setEnabled(true);
                    sender.sendMessage(pr + reload);
                    return true;
                }
                
                if(args[0].equalsIgnoreCase("title")){
                    if(args.length == 1){
                        sender.sendMessage(pr + getConfig().getString("Messages.Usage-Title").replace("&", "§"));
                        return true;
                    }
                    if(args[1].equalsIgnoreCase("on")){
                        getConfig().set("Settings.Title-Enable", true);
                        sender.sendMessage(pr + titleon);
                        return true;
                }
                    if(args.length == 1){
                        sender.sendMessage(pr + getConfig().getString("Messages.Usage-Title").replace("&", "§"));
                        return true;
                    }
                    if(args[1].equalsIgnoreCase("off")){
                        getConfig().set("Settings.Title-Enable", false);
                        sender.sendMessage(pr + titleoff);
                        return true;
                    }
                    if(args.length == 1){
                        sender.sendMessage(pr + getConfig().getString("Messages.Usage-Title").replace("&", "§"));
                        return true;
                   }
                    else{
                        sender.sendMessage(pr + getConfig().getString("Messages.Usage-Title").replace("&", "§"));
                        return true; 
                    }
                }
                
                if(args[0].equalsIgnoreCase("motd")){
                    if(args.length == 1){
                        sender.sendMessage(pr + getConfig().getString("Messages.Usage-MOTD").replace("&", "§"));
                        return true;
                    }
                    if(args[1].equalsIgnoreCase("off")){
                        getConfig().set("Settings.MOTD-Enable", false);
                        sender.sendMessage(pr + motdoff);
                        return true;
                        
                    }
                    if(args[1].equalsIgnoreCase("on")){
                        getConfig().set("Settings.MOTD-Enable", true);
                        sender.sendMessage(pr + motdon);
                        return true;
                    }
                    else{
                        sender.sendMessage(pr + getConfig().getString("Messages.Usage-MOTD").replace("&", "§"));
                        return true;
                    }
                }
                
                else{
                    sender.sendMessage(pr + getConfig().getString("Messages.Unknown-Command").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()));
                    return true;
                }
            }
        }
        }
        return false;
    }

	@EventHandler
	public void onJoinTitle(PlayerJoinEvent e) {
            Player p = e.getPlayer();
        if(getConfig().getBoolean("Settings.Title-Enable")){
			try {
				Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
				Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + this.getConfig().getString("Title.Title").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()).replace("%newline%", "\n") +"\"}");
				
				Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
				Object packet = titleConstructor.newInstance(enumTitle, chat, 20, 40, 20);
				
				sendPacket(e.getPlayer(), packet);

			}
			
			catch (Exception e1) {
				getLogger().severe("Opps! Packet can't be send to player!");
                                getLogger().severe("It seem plugin is not support your server version!");
                                getLogger().warning("Your server version is " + getServer().getBukkitVersion());
                                getLogger().warning("Sadly but we need to disable plugins! Please change server version to continue using JoinMessages");
                                setEnabled(false);
			}
		}

        }
	@EventHandler
	public void onJoinSub(PlayerJoinEvent e) {
            Player p = e.getPlayer();
        if(getConfig().getBoolean("Settings.Title-Enable")){
			try {
				Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
				Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + this.getConfig().getString("Title.Subtitle").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()).replace("%newline%", "\n") +"\"}");
				
				Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
				Object packet = titleConstructor.newInstance(enumTitle, chat, 20, 40, 20);
				
				sendPacket(e.getPlayer(), packet);

			}
			
			catch (Exception e1) {
				getLogger().severe("Opps! Packet can't be send to player!");
                                getLogger().severe("It seem plugin is not support your server version!");
                                getLogger().warning("Your server version is " + getServer().getBukkitVersion());
                                getLogger().warning("Sadly but we need to disable plugins! Please change server version to continue using JoinMessages");
                                setEnabled(false);
			}
		}

        }
	@EventHandler
	public void onJoinAction(PlayerJoinEvent e) {
            Player p = e.getPlayer();
        if(getConfig().getBoolean("Settings.Title-Enable")){
        try {
            Constructor<?> constructor = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class);
              
            Object icbc = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + this.getConfig().getString("Title.Actionbar").replace("&", "§").replace("{player}", p.getName()).replace("{server}", getServer().getServerName()).replace("%newline%", "\n") + "\"}");
            Object packet = constructor.newInstance(icbc, (byte) 2);
            Object entityPlayer= p.getClass().getMethod("getHandle").invoke(p);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e3) {
              e3.printStackTrace();
        }
    }
        }

	public void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		}
		
		catch (Exception e) {
				getLogger().severe("Opps! Packet can't be send to player!");
                                getLogger().severe("It seem plugin is not support your server version!");
                                getLogger().warning("Your server version is " + getServer().getBukkitVersion());
                                getLogger().warning("Sadly but we need to disable plugins! Please change server version to continue using JoinMessages");
                                setEnabled(false);
		}
	}
	
	public Class<?> getNMSClass(String name) {
		// org.bukkit.craftbukkit.v1_8_R1...
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		}
		
		catch (ClassNotFoundException e) {
				getLogger().severe("Opps! Can't getNMSClass");
                                getLogger().severe("It seem plugin is not support your server version!");
                                getLogger().warning("Your server version is " + getServer().getBukkitVersion());
                                    getLogger().warning("Sadly but we need to disable plugins! Please change server version to continue using JoinMessages");
                                setEnabled(false);
			return null;
		}
	}
}
    

