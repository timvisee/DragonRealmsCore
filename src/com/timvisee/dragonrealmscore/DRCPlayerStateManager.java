package com.timvisee.dragonrealmscore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class DRCPlayerStateManager {
	
	private HashMap<Player, HashMap<String, DRCPlayerState>> states = new HashMap<Player, HashMap<String, DRCPlayerState>>();
	private DRCPlayerStateGroupManager psgm;
	
	/**
	 * Constructor
	 */
	public DRCPlayerStateManager() {
		// Load the player state manager
		psgm = new DRCPlayerStateGroupManager();
		psgm.load();
	}
	
	/**
	 * Add a new player state to the list
	 * @param p The player to add the strates from
	 * @param states The states that should be added for this player
	 */
	public void addPlayerStates(Player p, HashMap<String, DRCPlayerState> states) {
		// Make sure the player is not null
		if(p == null)
			return;
		
		// Make sure the player doesn't already exist
		if(isPlayerLoaded(p))
			return;
		
		// Put the player into the states list
		this.states.put(p, states);
	}
	
	/**
	 * Get the player states of a loaded player
	 * @param p The player to get the player states from
	 * @return The player states of the loaded player, or null if the player wans't loaded
	 */
	public HashMap<String, DRCPlayerState> getPlayerStates(Player p) {
		return this.states.get(p);
	}
	
	/**
	 * Update the player state of a loaded player
	 * @param p The player
	 * @param state The updated state
	 * @return False if failed
	 */
	public boolean updatePlayerState(Player p, DRCPlayerState state) {
		// Make sure the params aren't null
		if(p == null)
			return false;
		
		// Get the player state group for the player
		DRCPlayerStateGroup group = getPlayerStateGroupManager().getPlayerStateGroup(p.getWorld());
		
		// Make sure the group isn't null
		if(group == null)
			return false;
		
		return updatePlayerState(p, group.getName(), state);
	}
	
	/**
	 * Update the player state of a loaded player
	 * @param p The player
	 * @param stateGroupName The state group to update
	 * @param state The updated state
	 * @return False if failed
	 */
	public boolean updatePlayerState(Player p, String stateGroupName, DRCPlayerState state) {
		// Make sure the params aren't null
		if(p == null || state == null)
			return false;
		
		// Make sure this player is loaded
		if(!isPlayerLoaded(p))
			return false;
		
		// Check if the player already has this state group
		if(hasPlayerState(p, stateGroupName))
			getPlayerStates(p).remove(stateGroupName);
		
		// Add the player state
		getPlayerStates(p).put(stateGroupName, state);
		
		// Return true
		return true;
	}
	
	/**
	 * Get the player state in a specific world
	 * @param p The player to get the state from
	 * @param w The world to get the state from
	 * @param createState True to create this state if this state doesn't exist for this player yet
	 * @return The player state, or null if the player isn't loaded, if the parameters equals to null or
	 * if the player state doesn't exist for this player
	 */
	public DRCPlayerState getPlayerState(Player p, World w, boolean createState) {
		// Make sure none of thep arams are null
		if(p == null || w == null)
			return null;
		
		// Check if the player is loaded
		if(!isPlayerLoaded(p))
			return null;
		
		// Get the state group
		DRCPlayerStateGroup g = getPlayerStateGroupManager().getPlayerStateGroup(w);
		
		return getPlayerState(p, g.getName(), createState);
	}
	
	/**
	 * Get the player state of a loaded player by it's player state group name
	 * @param p The player to get the state from
	 * @param stateGroupName The state group name
	 * @return The player state or null
	 */
	public DRCPlayerState getPlayerState(Player p, String stateGroupName, boolean createState) {
		// Check if the state exists
		if(hasPlayerState(p, stateGroupName))
			return getPlayerStates(p).get(stateGroupName);
		
		// Check if a new state should be created
		if(createState) {
			// Check if the player is loaded
			if(!isPlayerLoaded(p))
				return null;
			
			// Define a new state
			DRCPlayerState newState = new DRCPlayerState();
			
			// Add the state
			updatePlayerState(p, stateGroupName, newState);
			
			// Return the state
			return newState;
		}
		
		return null;
	}
	
	/**
	 * Check if the states of a player are loaded
	 * @param p The player to check
	 * @return True if the states of the player are loaded
	 */
	public boolean isPlayerLoaded(Player p) {
		return this.states.containsKey(p);
	}
	
	/**
	 * Check if a loaded player has a specific player state group
	 * @param p The loaded player to check
	 * @param stateGroupName The player state group name
	 * @return False if the player isn't loaded or if the player doesn't have this player state group 
	 */
	public boolean hasPlayerState(Player p, String stateGroupName) {
		// Make sure the player is loaded
		if(!isPlayerLoaded(p))
			return false;
		
		// Check if the player has this state
		return (this.states.get(p).containsKey(stateGroupName));
	}
	
	/**
	 * Get the player state group manager
	 * @return Player state group manager instance
	 */
	public DRCPlayerStateGroupManager getPlayerStateGroupManager() {
		return this.psgm;
	}
	
	/**
	 * Get the directory to store the player states in
	 * @return 
	 */
	public File getPlayerDirectory() {
		return new File(DragonRealmsCore.instance.getDataFolder(), "data/playerStates");
	}
	
	/**
	 * Get the file used for a player to store it's states in
	 * @param p The player to get the file from
	 * @return File to store the states of the player in
	 */
	public File getPlayerFile(Player p) {
		// Get the player name
		String name = p.getName();
		
		// Return the file
		return new File(this.getPlayerDirectory(), name + ".playerstates");
	}
	
	/**
	 * Load the states of a player
	 * @param p The player to load the states from
	 * @return False if failed
	 */
	public boolean loadPlayer(Player p) {
		// Make sure the player is not null
		if(p == null)
			return false;
		
		// Get the player file
		File f = this.getPlayerFile(p);
		
		// If the player file doesn't exist, load nothing but put the player in the list
		if(!f.exists()) {
			this.states.remove(p);
			this.states.put(p, new HashMap<String, DRCPlayerState>());
			return true;
		}
		
		// Load the configuration file
		YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
		
		// Make sure the configuration isn't null
		if(c == null)
			return false;
		
		// Get the states configuration section
		ConfigurationSection statesSect = c.getConfigurationSection("states");
		
		// Load the states for this player
		Set<String> keys = statesSect.getKeys(false);
		
		// Define a buffer HashMap
		HashMap<String, DRCPlayerState> buff = new HashMap<String, DRCPlayerState>();
		
		// Load each state
		for(String stateGroupName : keys) {
			// Get the configuration section
			ConfigurationSection sect = statesSect.getConfigurationSection(stateGroupName);
			
			// Load the player state
			DRCPlayerState state = DRCPlayerState.load(sect);
			
			// Make sure the loaded state isn't null
			if(state == null) {
				System.out.println(ChatColor.DARK_RED + "[DragonRealmsCore] Failed loading player state '" + stateGroupName +
						"' for '" + p.getName() + "'!");
				continue;
			}
			
			// Add the state to the buffer
			buff.put(stateGroupName, state);
		}
		
		// Put the player states into the list
		this.states.remove(p);
		this.states.put(p, buff);
		
		// Return true
		return true;
	}
	
	/**
	 * Save the player states of a single player
	 * @param p The player to save the player states from
	 * @return False if failed
	 */
	public boolean savePlayer(Player p) {
		// Make sure the player is not null
		if(p == null)
			return false;
		
		// Make sure the current player is loaded
		if(!isPlayerLoaded(p))
			return false;
		
		// Define a configuration to save the states in
		YamlConfiguration c = new YamlConfiguration();
		
		// Get the configuration section to save the states in
		ConfigurationSection statesSect = c.createSection("states");
		
		// Save each states
		for(String stateGroupName : this.getPlayerStates(p).keySet()) {
			// Get the corresponding player state
			DRCPlayerState state = getPlayerState(p, stateGroupName, false);
			
			// Make sure the state isn't null
			if(state == null)
				continue;
			
			// Get the current configuration section
			ConfigurationSection sect = statesSect.createSection(stateGroupName);
			
			// Save the state in the section
			state.save(sect);
		}
		
		// Get the file to save the config in
		File f = getPlayerFile(p);
		
		// Make sure the file isn't null
		if(f == null)
			return false;
		
		// Save the config
		try {
			c.save(f);
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Save all the loaded player states
	 * @return False if failed
	 */
	public boolean saveAll() {
		// Make sure there's anything to save
		if(this.states.size() == 0)
			return true;
		
		boolean failed = false;
		
		// Save each player indivitually
		for(Player p : this.states.keySet()) {
			// Save the current player
			boolean result = savePlayer(p);
			
			if(!result)
				failed = true;
		}
		
		return !failed;
	}
	
	/**
	 * Unload a player
	 * @param p The player to unload
	 * @param save Should the player be saved before it's being unloaded
	 */
	public void unloadPlayer(Player p, boolean save) {
		// Make sure the player is loaded
		if(!isPlayerLoaded(p))
			return;
				
		// Save the player states if required
		if(save)
			savePlayer(p);
				
		// Unload the player
		this.states.remove(p);
	}
	
	/**
	 * Unload all loaded player states
	 * @param save True if the player states should be saved
	 * @return Amount of player states unloaded
	 */
	public int unloadAll(boolean save) {
		// Make sure there's anything to unload or save
		if(this.states.size() == 0)
			return 0;
		
		// Store the current amount of player states available
		int count = this.states.size();
		
		// Save the player states if required
		if(save)
			saveAll();
		
		// Unload all the player states
		this.states.clear();
		
		// Return the amount of unloaded states
		return count;
	}
}
