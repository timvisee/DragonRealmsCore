package com.timvisee.dragonrealmscore;

import de.bananaco.bpermissions.imp.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class DRCPlayerManager {
	private List<DRCPlayer> players = new ArrayList<DRCPlayer>();

	public List<DRCPlayer> getPlayers() {
		return this.players;
	}

	public DRCPlayer createPlayer(String player) {
		if (isPlayer(player)) {
			return getPlayer(player);
		}

		DRCPlayer p = new DRCPlayer(player);

		this.players.add(p);

		return p;
	}

	public DRCPlayer getPlayer(String player) {
		return getPlayer(player, false);
	}

	public DRCPlayer getPlayer(String player, boolean caseSensitive) {
		for (DRCPlayer p : this.players) {
			if (caseSensitive) {
				if (p.getName().equals(player.trim()))
					return p;
			} else if (p.getName().equalsIgnoreCase(player.trim())) {
				return p;
			}

		}

		return null;
	}

	public DRCPlayer getPlayer(Player player) {
		return getPlayer(player.getName(), true);
	}

	public DRCPlayer getPlayer(OfflinePlayer player) {
		return getPlayer(player.getName(), true);
	}

	public boolean isPlayer(String p) {
		return getPlayer(p) != null;
	}

	public boolean isPlayer(String p, boolean caseSensitive) {
		return getPlayer(p, caseSensitive) != null;
	}

	public boolean isPlayer(Player p) {
		return getPlayer(p) != null;
	}

	public boolean isPlayer(OfflinePlayer p) {
		return getPlayer(p) != null;
	}

	public int getPlayerCount() {
		return this.players.size();
	}

	public File getDefaultDataFile() {
		return new File(DragonRealmsCore.instance.getDataFolder(),
				"data/players.yml");
	}

	public boolean save() {
		File f = getDefaultDataFile();

		return save(f);
	}

	public boolean save(File f) {
		long t = System.currentTimeMillis();

		if (f == null) {
			return false;
		}

		System.out.println("[DragonRealmsCore] Saving player data...");

		YamlConfiguration config = new YamlConfiguration();
		ConfigurationSection sect = config.createSection("players");

		if (!save(sect)) {
			return false;
		}
		try {
			config.save(f);

			long duration = System.currentTimeMillis() - t;

			System.out.println("[DragonRealmsCore] Player data saved, took "
					+ String.valueOf(duration) + " ms!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[DragonRealmsCore] Saving failed!");
			return false;
		}

		return true;
	}

	public boolean save(ConfigurationSection config) {
		int i = 0;

		for (DRCPlayer p : this.players) {
			ConfigurationSection sect = config.createSection(String.valueOf(i));

			p.save(sect);

			i++;
		}

		return true;
	}

	public boolean load() {
		File f = getDefaultDataFile();

		return load(f);
	}

	public boolean load(File f) {
		if (!f.exists()) {
			return false;
		}

		long t = System.currentTimeMillis();

		System.out.println("[DragonRealmsCore] Loading player data...");

		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(f);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("[DragonRealmsCore] Loading failed!");
			return false;
		}

		if (!config.isConfigurationSection("players")) {
			return false;
		}

		ConfigurationSection sect = config.getConfigurationSection("players");

		boolean result = load(sect);

		if (result) {
			long duration = System.currentTimeMillis() - t;

			System.out.println("[DragonRealmsCore] Player data loaded, took "
					+ String.valueOf(duration) + " ms!");
		} else {
			System.out.println("[DragonRealmsCore] Loading failed!");
		}

		return result;
	}

	public boolean load(ConfigurationSection config) {
		Set<String> keys = config.getKeys(false);

		List<DRCPlayer> playersBuff = new ArrayList<DRCPlayer>();

		for (String key : keys) {
			ConfigurationSection sect = config.getConfigurationSection(key);

			DRCPlayer p = DRCPlayer.load(sect);

			if (p == null) {
				System.out.println("[DragonRealmsCore] Failed to load player!");
				return false;
			}

			playersBuff.add(p);
		}

		this.players.clear();
		this.players.addAll(playersBuff);

		return true;
	}
}