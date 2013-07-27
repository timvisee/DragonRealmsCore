package com.timvisee.dragonrealmscore.listener;

import java.util.ArrayList;
import java.util.List;

import com.timvisee.dragonrealmscore.DRCPluginHandler;
import com.timvisee.dragonrealmscore.DragonRealmsCore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class DRCPluginListener implements Listener {
	
	private List<DRCPluginHandler> pluginManagers = new ArrayList<DRCPluginHandler>();
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent e) {
		DragonRealmsCore.instance.getPermissionsManager().onPluginEnable(e);
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent e) {
		DragonRealmsCore.instance.getPermissionsManager().onPluginDisable(e);
	}
	
	/**
	 * Register a plugin manager
	 * @param pm Plugin manager to register
	 */
	public void registerPluginManager(DRCPluginHandler pm) {
		if(!isPluginManagerRegistered(pm))
			this.pluginManagers.add(pm);
	}
	
	/**
	 * Check if a plugin manager is registered
	 * @param pm Plugin manager to check for
	 * @return True if registered
	 */
	public boolean isPluginManagerRegistered(DRCPluginHandler pm) {
		return this.pluginManagers.contains(pm);
	}
	
	/**
	 * Unregister a plugin manager
	 * @param pm Plugin manager to unregsiter
	 */
	public void unregisterPluginManager(DRCPluginHandler pm) {
		if(isPluginManagerRegistered(pm))
			this.pluginManagers.remove(pm);
	}
}