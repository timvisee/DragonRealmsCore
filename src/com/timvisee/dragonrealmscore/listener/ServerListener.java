package com.timvisee.dragonrealmscore.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import com.timvisee.dragonrealmscore.DragonRealmsCore;

public class ServerListener implements Listener {
	
	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
	public void onServerListPing(ServerListPingEvent event) {
	    event.setMotd(ChatColor.DARK_GRAY + getMinecraftVersion() + " - " + ChatColor.GOLD + DragonRealmsCore.instance.getServer().getMotd());
	    
	    //event.setMaxPlayers(1);
	}
	
	private String getMinecraftVersion() {
		return DragonRealmsCore.instance.getServer().getBukkitVersion().split("-")[0];
	}
}
