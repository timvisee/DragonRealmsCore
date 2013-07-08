package com.timvisee.dragonrealmscore.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.PluginDisableEvent;

import com.timvisee.dragonrealmscore.DragonRealmsCore;

public class PluginListener implements Listener {
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent e) {
		DragonRealmsCore.instance.getPermissionsManager().onPluginEnable(e);
	}
	
	@EventHandler
	public void onPluginDisable(PluginDisableEvent e) {
		DragonRealmsCore.instance.getPermissionsManager().onPluginDisable(e);
	}
}
