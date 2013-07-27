package com.timvisee.dragonrealmscore;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.timvisee.dragonrealmscore.manager.DRCPermissionsManager;

public class DRCPlayerListManager {
	
	private ProtocolManager pm;

	private HashMap<Player, List<String>> lastPlayerInfo = new HashMap<Player, List<String>>();

	private List<String> playerListCache = new ArrayList<String>();
	
	private String[] rankingsOrder = new String[]{
			"guest",
			"peasant",
			"follower",
			"servant",
			"acolyte",
			"adept",
			"tyarix",
			"sorcerer",
			"merchant",
			"warlock",
			"knight",
			"overlord",
			"templar",
			"paladin",
			"king"
		};
	
	public DRCPlayerListManager() { }
	
	public void setUp() {
		this.pm = ProtocolLibrary.getProtocolManager();
		
		// Update the player list cache
		updatePlayerList();
		
		// Remove all the current players from the list
		for(Player p : Bukkit.getOnlinePlayers())
			for(Player p2 : Bukkit.getOnlinePlayers())
				sendPacketRemoveListItem(p, p2.getName());
	}
	
	public ProtocolManager getProtocolManager() {
		return this.pm;
	}
	
	public void setProtocolManager(ProtocolManager pm) {
		this.pm = pm;
	}
	
	public void update() {
		for(Player p : Bukkit.getOnlinePlayers())
			update(p);
	}
	
	public void update(Player p) {
		// Make sure the player isn't null
		if(p == null)
			return;
		
		// Get the player's player info and progress it
		List<String> playerInfo = progressNewPlayerInfo(getPlayerInfo(p));
		
		// TODO: Write better algorithm for the packet sending system, some names don't need to be updated!
		
		// Remove all the old ones
		clearList(p);
		
		// Send the new list
		for(String s : playerInfo) {
			try {
				PacketContainer playerInfoPacket = this.pm.createPacket(Packets.Server.PLAYER_INFO);
				playerInfoPacket.getStrings().write(0, s);
				playerInfoPacket.getBooleans().write(0, true);
				playerInfoPacket.getIntegers().write(0, 0);
				this.pm.sendServerPacket(p, playerInfoPacket);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		// Take a snapshot from the current send playerlist and and replace the old send playerlist
		this.lastPlayerInfo.remove(p);
		this.lastPlayerInfo.put(p, playerInfo);
	}
	
	public void sendPacketRemoveListItem(Player p, String item) {
		// Make sure the player isn't null
		if(p == null)
			return;
		
		// Make sure the item isn't longer than 16 chars
		if(item.length() > 16)
			item = item.substring(0, 16);
		
		try {
			PacketContainer playerInfoPacket = this.pm.createPacket(Packets.Server.PLAYER_INFO);
			playerInfoPacket.getStrings().write(0, item);
			playerInfoPacket.getBooleans().write(0, false);
			playerInfoPacket.getIntegers().write(0, 0);
			this.pm.sendServerPacket(p, playerInfoPacket);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private int getOccurencesBefore(List<String> list, int i) {
		// Return 0 if the current index is for the first item
		if(i <= 0)
			return 0;
		
		// Define the counter
		int occur = 0;
		
		// Check previous occurences
		for(int a = 0; a < i; a++)
			if(list.get(a).equals(list.get(i)))
				occur++;
		
		// Return the occurences
		return occur;
	}
	
	private List<String> getPlayerInfo(Player p) {
		List<String> l = new ArrayList<String>();
		
		l.add(ChatColor.DARK_GRAY + "------------");
		l.add(ChatColor.DARK_GRAY + "------------");
		l.add(ChatColor.DARK_GRAY + "------------");
		
		l.add("");
		l.add(ChatColor.GOLD + "Dragon Realms");
		l.add("");

		l.add(ChatColor.DARK_GRAY + "------------");
		l.add(ChatColor.DARK_GRAY + "------------");
		l.add(ChatColor.DARK_GRAY + "------------");
		
		l.add(ChatColor.DARK_GREEN + "Playtime");
		l.add(ChatColor.GRAY + "00:00:00");
		l.add("");
		
		double money = DragonRealmsCore.instance.getEconomyManager().getBalance(p.getName(), 0.00);
		String moneyStr = DragonRealmsCore.instance.getEconomyManager().toMoneyNotationProper(money, false);
		l.add(ChatColor.DARK_GREEN + "Money");
		l.add(ChatColor.GRAY + String.valueOf(moneyStr));
		l.add("");
		
		l.add(ChatColor.DARK_GREEN + "Points");
		l.add(ChatColor.GRAY + "0");
		l.add("");
		
		l.add(ChatColor.DARK_GREEN + "Online Players");
		l.add(ChatColor.GRAY + (Bukkit.getOnlinePlayers().length + " / " + Bukkit.getMaxPlayers()));
		l.add("");

		l.add(ChatColor.DARK_GRAY + "------------");
		l.add(ChatColor.DARK_GRAY + "------------");
		l.add(ChatColor.DARK_GRAY + "------------");
		
		for(String s : getPlayerList())
			l.add(s);
		
		// Return the player info
		return l;
	}
	
	private List<String> progressNewPlayerInfo(List<String> l) {
		// Define a list to put the processed list in
		List<String> pl = new ArrayList<String>();
		
		// Process each string
		for(int i = 0; i < l.size(); i++) {
			// Get the current string
			String s = l.get(i);
			
			// Get the amount of occurrences
			int occur = getOccurencesBefore(l, i);
			
			// Check if the string is unique, if not append some stuff to the end of the string to make it unique
			if(occur == 0) {
				if(s.length() > 16)
					pl.add(s.substring(0, 16));
				else
					pl.add(s);
				
			} else {
				// Generate a hexadecimal thing to append to the string to make it unieque
				String hex = Integer.toHexString(occur);
				
				// Calculate the maximum length of the string
				int maxStringLength = 16 - (hex.length() * 2);
				
				// Make sure the maximum length isn't 0 or bellow
				if(maxStringLength <= 0)
					continue;
				
				// TODO: Make sure the list items won't end with color codes
				
				// Make sure the string isn't longer than the maximum allowed length
				if(s.length() > maxStringLength)
					s = s.substring(0, maxStringLength);
				
				// Append the hexadecimal stuff to the end
				StringBuilder sb = new StringBuilder(s);
				for(int a = 0; a < hex.length(); a++)
					sb.append(ChatColor.getByChar(hex.charAt(a)));
				
				// Update the string
				s = sb.toString();
				
				// One last check for the string length, and add the string to the list
				if(s.length() > 16)
					pl.add(s.substring(0, 16));
				else
					pl.add(s);
			}
		}
		
		// Return the progressed list
		return pl;
	}
	
	public void unregisterPlayer(Player p) {
		this.lastPlayerInfo.remove(p);
	}
	
	public List<String> getPlayerList() {
		return this.playerListCache;
	}
	
	public void updatePlayerList() {
		// Get the current amount of online players
		List<Player> players = Arrays.asList(Bukkit.getOnlinePlayers());
		
		// Sort the player list
		Collections.sort(players, new Comparator<Player>() {
		    public int compare(Player p1, Player p2) {
		    	DRCPermissionsManager pm = DragonRealmsCore.instance.getPermissionsManager();
		    	int r1 = getRankingPriority(pm.getPrimaryGroup(p1));
		    	int r2 = getRankingPriority(pm.getPrimaryGroup(p2));
		        return Integer.valueOf(r2).compareTo(r1);
		    }
		});
		
		// Clear the current cache list
		this.playerListCache.clear();
		
		for(Player p : players) {
			String name = ChatColor.getByChar(DragonRealmsCore.instance.getPermissionsManager().getPrefix(p).trim().substring(1, 2)) + p.getName();
			if(name.length() > 16)
				this.playerListCache.add(name.substring(0, 16));
			else
				this.playerListCache.add(name);
		}
	}
	
	private int getRankingPriority(String ranking) {
		for(int i = 0; i < rankingsOrder.length; i++)
			if(rankingsOrder[i].equalsIgnoreCase(ranking))
				return i;
		return 0;
	}

	public void clearLists() {
		for(Player p : Bukkit.getOnlinePlayers())
			clearList(p);
	}
	
	public void clearList(Player p) {
		if(lastPlayerInfo.containsKey(p))
			for(String s : this.lastPlayerInfo.get(p))
				sendPacketRemoveListItem(p, s);
	}
}
