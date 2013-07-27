package com.timvisee.dragonrealmscore;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class DRCPlayer {
	private String name;
	private String lastIp = "0.0.0.0";

	public DRCPlayer(Player p) {
		this(p.getName());
	}

	public DRCPlayer(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getLastIp() {
		return this.lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public static DRCPlayer load(ConfigurationSection config) {
		if (!config.isString("name")) {
			return null;
		}

		DRCPlayer p = new DRCPlayer(config.getString("name"));

		p.setLastIp(config.getString("lastIp", "0.0.0.0"));

		return p;
	}

	public void save(ConfigurationSection config) {
		config.set("name", this.name);
		config.set("lastIp", this.lastIp);
	}
}