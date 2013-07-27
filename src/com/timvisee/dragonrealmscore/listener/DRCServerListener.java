package com.timvisee.dragonrealmscore.listener;

import com.timvisee.dragonrealmscore.DragonRealmsCore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class DRCServerListener implements Listener {
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onServerListPing(ServerListPingEvent event) {
		int defMaxPlayers = event.getMaxPlayers();

		event.setMotd(ChatColor.DARK_GRAY + getMinecraftVersion() + " - "
				+ ChatColor.GOLD
				+ DragonRealmsCore.instance.getServer().getMotd());

		if (event.getMaxPlayers() < event.getNumPlayers()) {
			event.setMaxPlayers(event.getNumPlayers());
		}

		if (event.getAddress().getHostAddress().equals("83.163.53.147")) {
			event.setMotd(ChatColor.DARK_GRAY + getMinecraftVersion() + " - "
					+ ChatColor.LIGHT_PURPLE + "Reserved slot available!");

			int maxPlayers = Math.max(defMaxPlayers, event.getNumPlayers());
			if (maxPlayers == event.getNumPlayers())
				event.setMaxPlayers(maxPlayers + 1);
		}
	}

	private String getMinecraftVersion() {
		return DragonRealmsCore.instance.getServer().getBukkitVersion()
				.split("-")[0];
	}
}