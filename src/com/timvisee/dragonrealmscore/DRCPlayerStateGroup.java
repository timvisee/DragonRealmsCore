package com.timvisee.dragonrealmscore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class DRCPlayerStateGroup {

	private String name = "";
	private String dispName = "";
	private boolean isDefault = false;
	private List<String> worlds = new ArrayList<String>();
	
	/**
	 * Constructor
	 */
	public DRCPlayerStateGroup() { }
	
	/**
	 * Constructor
	 * @param name State group name
	 * @param isDefault 
	 * @param worlds
	 */
	public DRCPlayerStateGroup(String name, String dispName, boolean isDefault, List<String> worlds) {
		this.name = name;
		this.dispName = dispName;
		this.isDefault = isDefault;
		if(worlds != null)
			this.worlds = worlds;
		else
			this.worlds.clear();
	}
	
	/**
	 * Get the name
	 * @return Name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set the name
	 * @param name New name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the display name
	 * @return New display name
	 */
	public String getDisplayName() {
		return this.dispName;
	}
	
	/**
	 * Set the display name
	 * @param dispName New display name
	 */
	public void setDisplayName(String dispName) {
		this.dispName = dispName;
	}
	
	/**
	 * Check whether this state group is the default
	 * @return True if this state group is the default
	 */
	public boolean isDefault() {
		return this.isDefault;
	}
	
	/**
	 * Set if this state group is the default
	 * @param isDefault True if this state group is the default
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	/**
	 * Get the list of worlds of this this state group
	 * @return List of worlds
	 */
	public List<String> getWorlds() {
		return this.worlds;
	}
	
	/**
	 * Set the list of worlds of this state group
	 * @param worlds List of worls for this state group
	 */
	public void setWorlds(List<String> worlds) {
		// Make sure the list is not null
		if(worlds == null) {
			this.worlds.clear();
			return;
		}
		
		this.worlds = worlds;
	}
	
	/**
	 * Save the state group in a configuration sectoin
	 * @param c The configuration section to save the state group in
	 * @return False if failed
	 */
	public boolean save(ConfigurationSection c) {
		// Make sure the configuration section isn't null
		if(c == null)
			return false;
		
		// Save the state group in the configuration section
		c.set("name", this.name);
		c.set("dispName", this.dispName);
		c.set("isDefault", this.isDefault);
		c.set("worlds", this.worlds);
		
		// Return true
		return true;
	}
	
	/**
	 * Load a state group from a configuration section
	 */
	public static DRCPlayerStateGroup load(ConfigurationSection c) {
		// Make sure the configuration section isn't null
		if(c == null)
			return null;
		
		// Load the data from the configuration section
		String name = c.getString("name", "");
		String dispName = c.getString("dispName", name);
		boolean isDefault = c.getBoolean("isDefault", false);
		List<String> worlds = c.getStringList("worlds");
		
		// Construct and return the object
		return new DRCPlayerStateGroup(
				name, dispName,
				isDefault, worlds);
	}
}
