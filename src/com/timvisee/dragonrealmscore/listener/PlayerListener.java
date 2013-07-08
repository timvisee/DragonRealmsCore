package com.timvisee.dragonrealmscore.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.timvisee.dragonrealmscore.DragonRealmsCore;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		final Player p = event.getPlayer();
		
		List<String> rankings = DragonRealmsCore.instance.getPermissionsManager().getGroups(p);
		
		boolean isGuest = false;
		
		if(rankings.size() == 1)
			if(rankings.get(0).equalsIgnoreCase("Guest"))
				isGuest = true;
		if(rankings.size() == 0)
			isGuest = true;
		
		if(isGuest) {
			// Show message that the player should register
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DragonRealmsCore.instance, new Runnable() {
					public void run() {
						printMessageBlock(p, new String[]{
								ChatColor.DARK_GREEN + " Register yourself on our website to get a free rankup,",
								ChatColor.DARK_GREEN + " a lot of other features like the ability to play in our arena's.",
								"",
								"                 " + ChatColor.DARK_GREEN + "Click on the link below to sign up:",
								"               " + ChatColor.BLUE + "http://mcdragonrealms.com/register.php"
						});
					}
				}, 20 * 15);
		}
	}
	
	public void printMessageBlock(CommandSender sender, String[] messages) {
		printMessageBlock(sender, Arrays.asList(messages));
	}
	
	public void printMessageBlock(CommandSender sender, List<String> message) {
		printMessageBlock(sender, message, true, true);
	}
	
	public void printMessageBlock(CommandSender sender, List<String> messages, boolean topLine, boolean bottomLine) {
		List<String> out = new ArrayList<String>();
		
		// Generate the message line
		String messageLine = "";
		for(int i = 0; i < 26; i++)
			messageLine += ChatColor.DARK_GREEN + "-" + ChatColor.DARK_GRAY + "-";
		messageLine += ChatColor.DARK_GREEN + "-";
		
		// Add a mesage line to the top if needed
		if(topLine)
			out.add(messageLine);
		
		// Add all the message
		out.addAll(messages);
		
		// Add a message line to the bottom if neede
		if(bottomLine)
			out.add(messageLine);
		
		for(String msg : out)
			sender.sendMessage(msg);
	}
}
