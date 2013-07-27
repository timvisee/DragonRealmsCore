package com.timvisee.dragonrealmscore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class DRCPlayerStateGroupManager {
	
	private List<DRCPlayerStateGroup> groups = new ArrayList<DRCPlayerStateGroup>();
	
	/**
	 * Constructor
	 */
	public DRCPlayerStateGroupManager() { }
	
	/**
	 * Get the default player state group
	 * @return
	 */
	public DRCPlayerStateGroup getDefaultPlayerStateGroup() {
		// Make sure any groups are loaded
		if(this.groups.size() == 0)
			return null;
		
		// Check for each player state group if it's the default one
		for(DRCPlayerStateGroup group : this.groups)
			if(group.isDefault())
				return group;
		
		// No default group found, return the first one
		return this.groups.get(0);
	}
	
	/**
	 * Get the player state group to use in a world
	 * @param w The world to get the player state group from
	 * @return The player state group to use
	 */
	public DRCPlayerStateGroup getPlayerStateGroup(World w) {
		return this.getPlayerStateGroup(w.getName());
	}
	
	/**
	 * Get the player state group to use in a world
	 * @param w The world name to get the player state group from
	 * @return The player state group to use
	 */
	public DRCPlayerStateGroup getPlayerStateGroup(String w) {
		// Return the default group if the world is null
		if(w == null)
			return getDefaultPlayerStateGroup();
		
		// Check each group if it contains the current world
		for(DRCPlayerStateGroup group : this.groups)
			if(group.getWorlds().contains(w))
				return group;
		
		// No group found, return the default one
		return getDefaultPlayerStateGroup();
	}
	
	/**
	 * Get the amount of loaded player state groups
	 * @return Amount of loaded player state groups
	 */
	public int getPlayerStateGroupsCount() {
		return this.groups.size();
	}
	
	/**
	 * Load the player state groups from the default configuration section
	 * @return False if failed
	 */
	public boolean load() {
		return this.load(getConfigurationSection());
	}
	
	/**
	 * Get the configuration section the player state groups are set up in
	 * @return Configuration Section the player state groups are set up in
	 */
	private ConfigurationSection getConfigurationSection() {
		FileConfiguration c = DragonRealmsCore.instance.getConfig();
		return c.getConfigurationSection("playerStates.groups");
	}
	
	/**
	 * Load the player state groups from an configuration section
	 * @param c The configuration section to load the player state groups from
	 * @return False if failed
	 */
	public boolean load(ConfigurationSection c) {
		// Make sure the configuration section isn't null
		if(c == null)
			return false;
		
		// Load the states
		Set<String> keys = c.getKeys(false);
		
		// Create a buffer to load the player states in
		List<DRCPlayerStateGroup> buff = new ArrayList<DRCPlayerStateGroup>();
		
		// Load each state
		for(String key : keys) {
			// Get the current configuration section
			ConfigurationSection sect = c.getConfigurationSection(key);
			
			// Load the player state group
			DRCPlayerStateGroup psg = DRCPlayerStateGroup.load(sect);
			
			// Make sure the loaded state isn't null
			if(psg == null)
				continue;
			
			// Add the state to the list
			buff.add(psg);
		}
		
		// Update the player state groups list
		this.groups = buff;
		
		// Return true
		return true;
	}
}
