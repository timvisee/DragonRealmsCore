package com.timvisee.dragonrealmscore;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public abstract class DRCPluginHandler {
	
	private String pluginName;
	
	/**
	 * Constructor
	 * @param String pluginName
	 * @param log SCLogger
	 */
	public DRCPluginHandler(String pluginName) {
		this.pluginName = pluginName;
		
		// Register the plugin manager
		DragonRealmsCore.instance.getPluginListener().registerPluginManager(this);
	}
	
	/**
	 * Set up the plugin hook
	 */
	public void setUp() {
		// Hook into the plugin
		hook();
	}
	
	/**
	 * Try to hook into the plugin
	 */
	public abstract void hook();
	
	/**
	 * Check if Safe Creeper is hooked into the plugin
	 * @return True if hooked
	 */
	public abstract boolean isHooked();
	
	/**
	 * Unhook the plugin
	 */
	public abstract void unhook();
	
	/**
	 * Method called when a plugin is being enabled
	 * @param e Event instance
	 */
	public void onPluginEnable(PluginEnableEvent e) {
		Plugin p = e.getPlugin();
		String pn = p.getName();
		
		// Is the plugin enabled
		if(pn.equals(pluginName)) {
			System.out.println(pluginName + " plugin enabled!");
			hook();
		}
	}
	
	/**
	 * Method called when a plugin is being disabled
	 * @param e Event instance
	 */
	public void onPluginDisable(PluginDisableEvent e) {
		Plugin p = e.getPlugin();
		String pn = p.getName();
		
		// Is the plugin disabled
		if(pn.equals(pluginName)) {
			System.out.println(pluginName + " plugin disabled unexpectedly!");
			unhook();
		}
	}
	
	/**
	 * Get the plugin name
	 * @return Plugin name
	 */
	public String getPluginName() {
		return this.pluginName;
	}
}
