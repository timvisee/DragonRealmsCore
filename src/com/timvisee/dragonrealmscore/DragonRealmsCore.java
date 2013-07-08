package com.timvisee.dragonrealmscore;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.timvisee.dragonrealmscore.command.CommandHandler;
import com.timvisee.dragonrealmscore.listener.PlayerListener;
import com.timvisee.dragonrealmscore.listener.PluginListener;
import com.timvisee.dragonrealmscore.listener.ServerListener;
import com.timvisee.dragonrealmscore.manager.PermissionsManager;

public class DragonRealmsCore extends JavaPlugin {

	// The logger
	private Logger log;
	
	// Dragon Realms Core static instance
	public static DragonRealmsCore instance;
	
	// Listeners
	private final PlayerListener playerListener = new PlayerListener();
	private final PluginListener pluginListener = new PluginListener();
	private final ServerListener serverListener = new ServerListener();
	
	// Managers and Handlers
	private PermissionsManager pm;
	
	/**
	 * Constructor
	 */
	public DragonRealmsCore() {
		// Define the Dragon Realms Core static instance variable
		instance = this;
	}
	
	/**
	 * Called when plugin is being enabled
	 */
	public void onEnable() {
		long t = System.currentTimeMillis();
		
		this.log = Logger.getLogger("Minecraft");
		
		// Define the plugin manager
		PluginManager pm = getServer().getPluginManager();
		
		// Setup managers and handlers
	    setupPermissionsManager();
		
		// Register event listeners
		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.pluginListener, this);
		pm.registerEvents(this.serverListener, this);
		
		// Plugin sucesfuly enabled, show console message
		PluginDescriptionFile pdfFile = getDescription();
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;

		log.info("[DragonRealmsCore] Cave Joshnon here!");
		log.info("[DragonRealmsCore] Dragon Realms Core v" + pdfFile.getVersion() + " enabled, took " + String.valueOf(duration) + " ms!");
	}

	/**
	 * Called when plugin is being disabled
	 */
	public void onDisable() {
		// Plugin disabled, show console message
		log.info("[DragonRealmsCore] Dragon Realms Core Disabled");
	}
	
	/**
	 * Setup the permissions manager
	 */
	public void setupPermissionsManager() {
		// Setup the permissions manager
		this.pm = new PermissionsManager(this.getServer(), this, this.log);
		this.pm.setup();
	}
	
	/**
	 * Get the permissions manager
	 * @return permissions manager
	 */
	public PermissionsManager getPermissionsManager() {
		return this.pm;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Run the command trough the command handler
		CommandHandler ch = new CommandHandler();
		return ch.onCommand(sender, cmd, commandLabel, args);
	}
}
