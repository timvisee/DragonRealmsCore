package com.timvisee.dragonrealmscore;

import com.timvisee.dragonrealmscore.command.DRCCommandHandler;
import com.timvisee.dragonrealmscore.listener.DRCPlayerListener;
import com.timvisee.dragonrealmscore.listener.DRCPluginListener;
import com.timvisee.dragonrealmscore.listener.DRCServerListener;
import com.timvisee.dragonrealmscore.manager.DRCEconomyManager;
import com.timvisee.dragonrealmscore.manager.DRCPermissionsManager;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class DragonRealmsCore extends JavaPlugin {
	
	private Logger log;
	public static DragonRealmsCore instance;
	private final DRCPlayerListener playerListener = new DRCPlayerListener();
	private final DRCPluginListener pluginListener = new DRCPluginListener();
	private final DRCServerListener serverListener = new DRCServerListener();
	private DRCPermissionsManager pm;
	private DRCEconomyManager em;
	private DRCPlayerManager playerMan;
	private DRCPlayerStateManager psm;
	private DRCPlayerListManager plm;

	public DragonRealmsCore() {
		instance = this;
	}

	public void onEnable() {
		long t = System.currentTimeMillis();

		this.log = Logger.getLogger("Minecraft");

		PluginManager pm = getServer().getPluginManager();

		setUpPermissionsManager();
		setUpEconomyManager();
		setUpPlayerStateManager();
		setUpPlayerListManager();
		
		// TODO: Do stuff for already loaded players ... (with their player states!)

		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.pluginListener, this);
		pm.registerEvents(this.serverListener, this);

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
					public void run() {
						DragonRealmsCore.instance.getPlayerManager().save();
					}
				}, 6000L, 6000L);

		PluginDescriptionFile pdfFile = getDescription();

		long duration = System.currentTimeMillis() - t;

		this.log.info("[DragonRealmsCore] Cave Joshnon here!");
		this.log.info("[DragonRealmsCore] Dragon Realms Core v" + pdfFile.getVersion() + " enabled, took " + String.valueOf(duration) + " ms!");
	}

	public void onDisable() {
		this.log.info("[DragonRealmsCore] Clearnig player info...");
		getPlayerListManager().clearLists();
		this.log.info("[DragonRealmsCore] Player info cleared!");
		
		DRCPlayerStateManager psm = DragonRealmsCore.instance.getPlayerStateManager();
		this.log.info("[DragonRealmsCore] Saving all player states...");
		long t = System.currentTimeMillis();
		for(Player p : Bukkit.getOnlinePlayers())
			psm.updatePlayerState(p, new DRCPlayerState(p));
		psm.unloadAll(true);
		long duration = System.currentTimeMillis() - t;
		this.log.info("[DragonRealmsCore] All player states saved, took " + String.valueOf(duration) + " ms!");
		
		this.log.info("[DragonRealmsCore] Stopping all Dragon Realms Core tasks...");
		for (BukkitTask task : Bukkit.getScheduler().getPendingTasks())
			if (task.getOwner().equals(this))
				task.cancel();
		this.log.info("[DragonRealmsCore] Tasks stopped!");

		this.log.info("[DragonRealmsCore] Dragon Realms Core Disabled");
	}

	public void setUpPermissionsManager() {
		this.pm = new DRCPermissionsManager(getServer(), this, this.log);
		this.pm.setup();
	}

	public DRCPermissionsManager getPermissionsManager() {
		return this.pm;
	}
	
	public void setUpEconomyManager() {
		this.em = new DRCEconomyManager(getServer(), "DragonRealmsCore");
		this.em.setUp();
	}
	
	public DRCEconomyManager getEconomyManager() {
		return this.em;
	}

	public void setUpPlayerManager() {
		this.playerMan = new DRCPlayerManager();
		this.playerMan.load();
	}

	public DRCPlayerManager getPlayerManager() {
		return this.playerMan;
	}
	
	public void setUpPlayerStateManager() {
		this.psm = new DRCPlayerStateManager();
	}
	
	public DRCPlayerStateManager getPlayerStateManager() {
		return this.psm;
	}
	
	public void setUpPlayerListManager() {
		this.plm = new DRCPlayerListManager();
		this.plm.setUp();
		this.plm.update();
	}
	
	public DRCPlayerListManager getPlayerListManager() {
		return this.plm;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		DRCCommandHandler ch = new DRCCommandHandler();
		return ch.onCommand(sender, cmd, commandLabel, args);
	}

	public DRCPluginListener getPluginListener() {
		return this.pluginListener;
	}
}