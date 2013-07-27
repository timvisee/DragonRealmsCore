package com.timvisee.dragonrealmscore.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.timvisee.dragonrealmscore.DRCPlayerListManager;
import com.timvisee.dragonrealmscore.DRCPlayerState;
import com.timvisee.dragonrealmscore.DRCPlayerStateGroup;
import com.timvisee.dragonrealmscore.DRCPlayerStateGroupManager;
import com.timvisee.dragonrealmscore.DRCPlayerStateManager;
import com.timvisee.dragonrealmscore.DragonRealmsCore;
import com.timvisee.dragonrealmscore.manager.DRCPermissionsManager;

public class DRCPlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		
		DRCPlayerStateManager psm = DragonRealmsCore.instance.getPlayerStateManager();
		if(!psm.isPlayerLoaded(p)) {
			System.out.println("[DragonRealmsCore] Loading player states for '" + p.getName() + "'");
			long t = System.currentTimeMillis();
			psm.loadPlayer(p);
			long duration = System.currentTimeMillis() - t;
			System.out.println("[DragonRealmsCore] Player states loaded, took " + String.valueOf(duration) + " ms!");
		} else
			System.out.println("[DragonRealmsCore] Player states for '" + p.getName() + "' already loaded!");
		
		
		
		// Update the player list and resend the list to every player
		final DRCPlayerListManager plm = DragonRealmsCore.instance.getPlayerListManager();
		plm.updatePlayerList();
		plm.update();
		
		// Remove the current joined player from the list
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DragonRealmsCore.instance, new Runnable() {
					public void run() {
						for(Player entry : Bukkit.getOnlinePlayers())
							plm.sendPacketRemoveListItem(entry, p.getName());
					}
				}, 1);

		
		
		
		
		
		
		

		List<String> rankings = DragonRealmsCore.instance.getPermissionsManager().getGroups(p);

		boolean isGuest = false;

		if ((rankings.size() == 1) && (((String) rankings.get(0)).equalsIgnoreCase("Guest")))
			isGuest = true;
		
		if (rankings.size() == 0)
			isGuest = true;
		
		if (isGuest) {
			Bukkit.getServer()
					.getScheduler()
					.scheduleSyncDelayedTask(DragonRealmsCore.instance,
							new Runnable() {
								public void run() {
									DRCPlayerListener.this
											.printMessageBox(
													p,
													new String[] {
															ChatColor.DARK_GREEN
																	+ " Register yourself on our website to get a free rankup,",
															ChatColor.DARK_GREEN
																	+ " a lot of other features like the ability to play in our arena's.",
															"",
															"                 "
																	+ ChatColor.DARK_GREEN
																	+ "Click on the link below to sign up:",
															"               "
																	+ ChatColor.BLUE
																	+ "http://mcdragonrealms.com/register.php" });
								}
							}, 300L);
		}

		DRCPermissionsManager pm = DragonRealmsCore.instance.getPermissionsManager();

		List<String> groups = pm.getGroups(p);

		boolean showEffect = false;

		for (String g : groups) {
			if ((g.equalsIgnoreCase("King")) || (g.equalsIgnoreCase("Paladin"))) {
				showEffect = true;
				break;
			}
			
			String suffix = pm.getSuffix(p);
			if(suffix.contains("[*]")) {
				showEffect = true;
				break;
			}
		}

		if (showEffect) {
			Bukkit.getServer()
					.getScheduler()
					.scheduleSyncDelayedTask(DragonRealmsCore.instance,
							new Runnable() {
								public void run() {
									for (int i = 0; i <= 8; i++) {
										p.getWorld().playEffect(
												p.getLocation(), Effect.SMOKE,
												i);
									}
									for (int i = 0; i <= 1; i++) {
										p.getWorld().playEffect(
												p.getLocation(),
												Effect.MOBSPAWNER_FLAMES, i);
									}
									p.getWorld().playSound(p.getLocation(),
											Sound.LEVEL_UP, 0.5F, 2.0F);
								}
							}, 5L);
		}

		if (!p.getName().equalsIgnoreCase("timvisee"))
			Bukkit.getServer()
					.getScheduler()
					.scheduleSyncDelayedTask(DragonRealmsCore.instance,
							new Runnable() {
								public void run() {
									for (Player entry : Bukkit
											.getOnlinePlayers())
										if (entry.getName().equals("timvisee"))
											entry.chat("hi");
								}
							}, 5L);
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		World from = e.getFrom();
		World to = p.getWorld();

		// Make sure the player param isn't null
		if(p == null)
			return;
		
		DRCPlayerStateManager psm = DragonRealmsCore.instance.getPlayerStateManager();
		DRCPlayerStateGroupManager psgm = psm.getPlayerStateGroupManager();
		
		if(!psm.isPlayerLoaded(p)) {
			System.out.println("[DragonRealmsCore] Loading player states for '" + p.getName() + "'");
			long t = System.currentTimeMillis();
			psm.loadPlayer(p);
			long duration = System.currentTimeMillis() - t;
			System.out.println("[DragonRealmsCore] Player states loaded, took " + String.valueOf(duration) + " ms!");
		}
		
		// Check if the player is moving to a different state group
		DRCPlayerStateGroup fromGroup = psgm.getPlayerStateGroup(from);
		DRCPlayerStateGroup toGroup = psgm.getPlayerStateGroup(to);
		
		// Update the state from the old group
		psm.updatePlayerState(p, fromGroup.getName(), new DRCPlayerState(p));
		
		if(!fromGroup.getName().equals(toGroup.getName())) {
			// Close the player's inventory, so he can't block the gamemode modification
			p.closeInventory();
			
			// Apply the state from the new group
			DRCPlayerState newState = psm.getPlayerState(p, toGroup.getName(), true);
			newState.apply(p);

			// Close the player's inventory, so he can't block the gamemode modification
			p.closeInventory();
			
			// Show a status message
			p.sendMessage(ChatColor.GOLD + "[Core] Your player state has been changed to " + toGroup.getDisplayName());
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		World from = e.getFrom().getWorld();
		World to = e.getTo().getWorld();

		// Make sure the player param isn't null
		if(p == null || from == null || to == null)
			return;
		
		DRCPlayerStateManager psm = DragonRealmsCore.instance.getPlayerStateManager();
		DRCPlayerStateGroupManager psgm = psm.getPlayerStateGroupManager();
		
		if(!psm.isPlayerLoaded(p)) {
			System.out.println("[DragonRealmsCore] Loading player states for '" + p.getName() + "'");
			long t = System.currentTimeMillis();
			psm.loadPlayer(p);
			long duration = System.currentTimeMillis() - t;
			System.out.println("[DragonRealmsCore] Player states loaded, took " + String.valueOf(duration) + " ms!");
		}
		
		// Check if the player is moving to a different state group
		DRCPlayerStateGroup fromGroup = psgm.getPlayerStateGroup(from);
		DRCPlayerStateGroup toGroup = psgm.getPlayerStateGroup(to);
		
		if(!fromGroup.getName().equals(toGroup.getName())) {
			// Close the player's inventory, so he can't block the gamemode modification
			p.closeInventory();
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();

		DRCPlayerStateManager psm = DragonRealmsCore.instance.getPlayerStateManager();
		
		if(!psm.isPlayerLoaded(p)) {
			System.out.println("[DragonRealmsCore] Loading player states for '" + p.getName() + "'");
			long t = System.currentTimeMillis();
			psm.loadPlayer(p);
			long duration = System.currentTimeMillis() - t;
			System.out.println("[DragonRealmsCore] Player states loaded, took " + String.valueOf(duration) + " ms!");
		}
		
		if(psm.isPlayerLoaded(p)) {
			System.out.println("[DragonRealmsCore] Saving player states for '" + p.getName() + "'");
			long t = System.currentTimeMillis();
			psm.unloadPlayer(p, true);
			long duration = System.currentTimeMillis() - t;
			System.out.println("[DragonRealmsCore] Player states saved, took " + String.valueOf(duration) + " ms!");
			System.out.println("[DragonRealmsCore] Unloading player '" + p.getName() + "'!");
		}
		
		// Unregister the player from the player list manager and update the player lists
		DRCPlayerListManager plm = DragonRealmsCore.instance.getPlayerListManager();
		plm.unregisterPlayer(p);
		plm.updatePlayerList();
		plm.update();
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		Player p = event.getPlayer();

		DRCPlayerStateManager psm = DragonRealmsCore.instance.getPlayerStateManager();
		
		if(!psm.isPlayerLoaded(p)) {
			System.out.println("[DragonRealmsCore] Loading player states for '" + p.getName() + "'");
			long t = System.currentTimeMillis();
			psm.loadPlayer(p);
			long duration = System.currentTimeMillis() - t;
			System.out.println("[DragonRealmsCore] Player states loaded, took " + String.valueOf(duration) + " ms!");
		}
		
		if(psm.isPlayerLoaded(p)) {
			System.out.println("[DragonRealmsCore] Saving player states for '" + p.getName() + "'");
			long t = System.currentTimeMillis();
			psm.unloadPlayer(p, true);
			long duration = System.currentTimeMillis() - t;
			System.out.println("[DragonRealmsCore] Player states saved, took " + String.valueOf(duration) + " ms!");
			System.out.println("[DragonRealmsCore] Unloading player '" + p.getName() + "'!");
		}
		
		// Unregister the player from the player list manager and update the player lists
		DRCPlayerListManager plm = DragonRealmsCore.instance.getPlayerListManager();
		plm.unregisterPlayer(p);
		plm.updatePlayerList();
		plm.update();
	}

	public void printMessageBox(CommandSender sender, String[] messages) {
		printMessageBox(sender, Arrays.asList(messages));
	}

	public void printMessageBox(CommandSender sender, List<String> message) {
		printMessageBox(sender, message, true, true);
	}

	public void printMessageBox(CommandSender sender, List<String> messages,
			boolean topLine, boolean bottomLine) {
		List<String> out = new ArrayList<String>();

		String messageLine = "";
		for (int i = 0; i < 26; i++)
			messageLine = messageLine + ChatColor.DARK_GREEN + "-"
					+ ChatColor.DARK_GRAY + "-";
		messageLine = messageLine + ChatColor.DARK_GREEN + "-";

		if (topLine) {
			out.add(messageLine);
		}

		out.addAll(messages);

		if (bottomLine) {
			out.add(messageLine);
		}
		for (String msg : out)
			sender.sendMessage(msg);
	}
}