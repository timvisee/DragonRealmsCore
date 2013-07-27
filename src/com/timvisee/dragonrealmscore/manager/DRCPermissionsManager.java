package com.timvisee.dragonrealmscore.manager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.timvisee.dragonrealmscore.DragonRealmsCore;
import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class DRCPermissionsManager {
	
	private Server s;
	private Plugin p;
	private Logger log;
	private PermissionsSystemType permsType = PermissionsSystemType.NONE;
	private PermissionManager pexPerms;
	private GroupManager groupManagerPerms;
	private PermissionHandler defaultPerms;
	private ZPermissionsService zPermissionsService;
	public Permission vaultPerms = null;

	public DRCPermissionsManager(Server s, Plugin p, Logger log) {
		this.s = s;
		this.p = p;
		this.log = log;
	}

	public PermissionsSystemType getUsedPermissionsSystemType() {
		return this.permsType;
	}

	public boolean isEnabled() {
		return !this.permsType.equals(PermissionsSystemType.NONE);
	}

	public PermissionsSystemType setup() {
		// Define the plugin manager
		final PluginManager pm = this.s.getPluginManager();
		
		// Reset used permissions system type
		permsType = PermissionsSystemType.NONE;
		
		// PermissionsEx
		// Check if PermissionsEx is allowed to be used
		if(DragonRealmsCore.instance.getConfig().getBoolean("permissions.permissionsSystems.PermissionsEx.enabled", true)) {
			// Check if PermissionsEx is available
			try {
				Plugin pex = pm.getPlugin("PermissionsEx");
				if(pex != null) {
					pexPerms = PermissionsEx.getPermissionManager();
					if(pexPerms != null) {
						permsType = PermissionsSystemType.PERMISSIONS_EX;
						
						System.out.println("[" + p.getName() + "] Hooked into PermissionsEx!");
						return permsType;
					}
				}
			} catch(Exception ex) {
				// An error occured, show a warning message
				System.out.println("[" + p.getName() + "] Error while hooking into PermissionsEx!");
			}
		} else {
			// Show a warning message
			System.out.println("[" + p.getName() + "] Not checking for PermissionsEx, disabled in config file!");
		}
			
		// PermissionsBukkit
		// Check if PermissionsBukkit is allowed to be used
		if(DragonRealmsCore.instance.getConfig().getBoolean("permissions.permissionsSystems.PermissionsBukkit.enabled", true)) {
			// Check if PermissionsBukkit is available
			try {
				Plugin bukkitPerms = pm.getPlugin("PermissionsBukkit");
				if(bukkitPerms != null) {
					permsType = PermissionsSystemType.PERMISSIONS_BUKKIT;
					System.out.println("[" + p.getName() + "] Hooked into PermissionsBukkit!");
					return permsType;
				}
			} catch(Exception ex) {
				// An error occured, show a warning message
				System.out.println("[" + p.getName() + "] Error while hooking into PermissionsBukkit!");
			}
		} else {
			// Show a warning message
			System.out.println("[" + p.getName() + "] Not checking for PermissionsBukkit, disabled in config file!");
		}
		
		// bPermissions
		// Check if bPermissions is allowed to be used
		if(DragonRealmsCore.instance.getConfig().getBoolean("permissions.permissionsSystems.bPermissions.enabled", true)) {
			// Check if bPermissions is available
			try {
				Plugin testBPermissions = pm.getPlugin("bPermissions");
				if(testBPermissions != null) {
					permsType = PermissionsSystemType.B_PERMISSIONS;
					System.out.println("[" + p.getName() + "] Hooked into bPermissions!");
					return permsType;
				}
			} catch(Exception ex) {
				// An error occured, show a warning message
				System.out.println("[" + p.getName() + "] Error while hooking into bPermissions!");
			}
		} else {
			// Show a warning message
			System.out.println("[" + p.getName() + "] Not checking for bPermissions, disabled in config file!");
		}
		
		// Essentials Group Manager
		// Check if Essentials Group Manager is allowed to be used
		if(DragonRealmsCore.instance.getConfig().getBoolean("permissions.permissionsSystems.EssentialsGroupManager.enabled", true)) {
			// Check if Essentials Group Manager is available
			try {
				final Plugin GMplugin = pm.getPlugin("GroupManager");
				if (GMplugin != null && GMplugin.isEnabled()) {
					permsType = PermissionsSystemType.ESSENTIALS_GROUP_MANAGER;
					groupManagerPerms = (GroupManager)GMplugin;
		            System.out.println("[" + p.getName() + "] Hooked into Essentials Group Manager!");
		            return permsType;
				}
			} catch(Exception ex) {
				// An error occured, show a warning message
				System.out.println("[" + p.getName() + "] Error while hooking into Essentials Group Manager!");
			}
		} else {
			// Show a warning message
			System.out.println("[" + p.getName() + "] Not checking for Essentials Group Manager, disabled in config file!");
		}

		// zPermissions
		// Check if zPermissions is allowed to be used
		if(DragonRealmsCore.instance.getConfig().getBoolean("permissions.permissionsSystems.zPermissions.enabled", true)) {
			// Check if zPermissions is available
			try {
				Plugin testzPermissions = pm.getPlugin("zPermissions");
				if(testzPermissions != null){
					zPermissionsService = Bukkit.getServicesManager().load(ZPermissionsService.class);
					if(zPermissionsService != null){
						permsType = PermissionsSystemType.Z_PERMISSIONS;
						System.out.println("[" + p.getName() + "] Hooked into zPermissions!");
						return permsType;
					}
				}
			} catch(Exception ex) {
				// An error occured, show a warning message
				System.out.println("[" + p.getName() + "] Error while hooking into zPermissions!");
			}
		} else {
			// Show a warning message
			System.out.println("[" + p.getName() + "] Not checking for zPermissions, disabled in config file!");
		}
		
		// Vault
		// Check if Vault is allowed to be used
		if(DragonRealmsCore.instance.getConfig().getBoolean("permissions.permissionsSystems.Vault.enabled", true)) {
			// Check if Vault is available
			try {
				final Plugin vaultPlugin = pm.getPlugin("Vault");
				if (vaultPlugin != null && vaultPlugin.isEnabled()) {
					RegisteredServiceProvider<Permission> permissionProvider = this.s.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			        if (permissionProvider != null) {
			            vaultPerms = permissionProvider.getProvider();
			            if(vaultPerms.isEnabled()) {
			            	permsType = PermissionsSystemType.VAULT;
			            	System.out.println("[" + p.getName() + "] Hooked into Vault Permissions!");
			    		    return permsType;
			            } else {
			            	System.out.println("[" + p.getName() + "] Not using Vault Permissions, Vault Permissions is disabled!");
			            }
			        }
				}
			} catch(Exception ex) {
				// An error occured, show a warning message
				System.out.println("[" + p.getName() + "] Error while hooking into Vault Permissions!");
			}
		} else {
			// Show a warning message
			System.out.println("[" + p.getName() + "] Not checking for Vault, disabled in config file!");
		}
        
		// Permissions
		// Check if Vault is allowed to be used
		if(DragonRealmsCore.instance.getConfig().getBoolean("permissions.permissionsSystems.Permissions.enabled", true)) {
			// Check if Permissions is available
			try {
			    Plugin testPerms = pm.getPlugin("Permissions");
		        if (testPerms != null) {
		        	permsType = PermissionsSystemType.PERMISSIONS;
		            this.defaultPerms = ((Permissions) testPerms).getHandler();
		            System.out.println("[" + p.getName() + "] Hooked into Permissions!");
		            return PermissionsSystemType.PERMISSIONS;
		        }
			} catch(Exception ex) {
				// An error occured, show a warning message
				System.out.println("[" + p.getName() + "] Error while hooking into Permissions!");
			}
		} else {
			// Show a warning message
			System.out.println("[" + p.getName() + "] Not checking for Permissions, disabled in config file!");
		}
	    
	    // No recognized permissions system found
	    permsType = PermissionsSystemType.NONE;
	    System.out.println("[" + p.getName() + "] No supported permissions system found! Permissions disabled!");
	    
	    return PermissionsSystemType.NONE;
	}

	public void unhook() {
		this.permsType = PermissionsSystemType.NONE;

		if (!this.permsType.equals(PermissionsSystemType.NONE))
			this.log.info("Unhooked from " + this.permsType.getName() + "!");
		else
			this.log.info("Unhooked from Permissions!");
	}

	public void onPluginEnable(PluginEnableEvent e) {
		Plugin p = e.getPlugin();
		String pn = p.getName();

		if ((pn.equals("PermissionsEx")) || (pn.equals("PermissionsBukkit")) ||
				(pn.equals("bPermissions")) || (pn.equals("GroupManager")) ||
				(pn.equals("zPermissions")) || (pn.equals("Vault")) ||
				(pn.equals("Permissions"))) {
			this.log.info(pn + " plugin enabled, updating hooks!");
			setup();
		}
	}

	public void onPluginDisable(PluginDisableEvent e) {
		Plugin p = e.getPlugin();
		String pn = p.getName();

		if ((pn.equals("PermissionsEx")) || (pn.equals("PermissionsBukkit")) ||
				(pn.equals("bPermissions")) || (pn.equals("GroupManager")) ||
				(pn.equals("zPermissions")) || (pn.equals("Vault")) ||
				(pn.equals("Permissions"))) {
			this.log.info(pn + " plugin disabled, updating hooks!");
			setup();
		}
	}

	public Logger getLogger() {
		return this.log;
	}

	public void setLogger(Logger log) {
		this.log = log;
	}

	public boolean hasPermission(Player p, String permsNode) {
		return hasPermission(p, permsNode, p.isOp());
	}

	public boolean hasPermission(Player p, String permsNode, boolean def) {
		// No permissions system is used, return default
		if(!isEnabled())
			return def;
		
		switch (this.permsType) {
		case PERMISSIONS_EX:
			// Permissions Ex
			PermissionUser user  = PermissionsEx.getUser(p);
			return user.has(permsNode);
			
		case PERMISSIONS_BUKKIT:
			// Permissions Bukkit
			return p.hasPermission(permsNode);
			
		case B_PERMISSIONS:
			// bPermissions
			return ApiLayer.hasPermission(p.getWorld().getName(), CalculableType.USER, p.getName(), permsNode);
			
		case ESSENTIALS_GROUP_MANAGER:
			// Essentials Group Manager
			final AnjoPermissionsHandler handler = groupManagerPerms.getWorldsHolder().getWorldPermissions(p);
			if (handler == null)
				return false;
			return handler.has(p, permsNode);
		case Z_PERMISSIONS:
			// zPermissions
			Map<String, Boolean> perms = zPermissionsService.getPlayerPermissions(p.getWorld().getName(), null, p.getName());
			if(perms.containsKey(permsNode)){
				return perms.get(permsNode);
			} else {
				return def;
			}
		case VAULT:
			// Vault
			return vaultPerms.has(p, permsNode);
			
		case PERMISSIONS:
			// Permissions by nijiko
			return this.defaultPerms.has(p, permsNode);
			
		case NONE:
			// Not hooked into any permissions system, return default
			return def;
			
		default:
			// Something went wrong, return false to prevent problems
			return false;
		}
	}

	public String getPrimaryGroup(Player p) {
		List<String> groups = getGroups(p);
		if(groups.size() == 0)
			return "";
		return groups.get(0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> getGroups(Player p) {
		// No permissions system is used, return an empty list
		if(!isEnabled())
			return new ArrayList<String>();
		
		switch (this.permsType) {
		case PERMISSIONS_EX:
			// Permissions Ex
			PermissionUser user  = PermissionsEx.getUser(p);
			return Arrays.asList(user.getGroupsNames());
			
		case PERMISSIONS_BUKKIT:
			// Permissions Bukkit
			// Permissions Bukkit doesn't support group, return an empty list
			return new ArrayList<String>();
			
		case B_PERMISSIONS:
			// bPermissions
			return Arrays.asList(ApiLayer.getGroups(p.getName(), CalculableType.USER, p.getName()));
			
		case ESSENTIALS_GROUP_MANAGER:
			// Essentials Group Manager
			final AnjoPermissionsHandler handler = groupManagerPerms.getWorldsHolder().getWorldPermissions(p);
			if (handler == null)
				return new ArrayList<String>();
			return Arrays.asList(handler.getGroups(p.getName()));
			
		case Z_PERMISSIONS:
			//zPermissions
			return new ArrayList(zPermissionsService.getPlayerGroups(p.getName()));
			
		case VAULT:
			// Vault
			return Arrays.asList(vaultPerms.getPlayerGroups(p));
			
		case PERMISSIONS:
			// Permissions by nijiko
			return new ArrayList(this.defaultPerms.getGroups(p.getName()));
			
		case NONE:
			// Not hooked into any permissions system, return an empty list
			return new ArrayList<String>();
			
		default:
			// Something went wrong, return an empty list to prevent problems
			return new ArrayList<String>();
		}
	}

	public String getPrefix(Player p) {
		if (!isEnabled())
			return "";

		switch (this.permsType) {
		case PERMISSIONS_EX:
			// Permissions Ex
			PermissionUser user  = PermissionsEx.getUser(p);
			return user.getPrefix();
			
		default:
			// Something went wrong, return an empty list to prevent problems
			return "";
		}
	}

	public String getSuffix(Player p) {
		if (!isEnabled())
			return "";

		switch (this.permsType) {
		case PERMISSIONS_EX:
			// Permissions Ex
			PermissionUser user = PermissionsEx.getUser(p);
			return user.getSuffix();
			
		default:
			// Something went wrong, return an empty list to prevent problems
			return "";
		}
	}

	public static enum PermissionsSystemType {
		NONE("None"),
		PERMISSIONS_EX("Permissions Ex"),
		PERMISSIONS_BUKKIT("Permissions Bukkit"),
		B_PERMISSIONS("bPermissions"),
		ESSENTIALS_GROUP_MANAGER("Essentials Group Manager"),
		Z_PERMISSIONS("zPermissions"),
		VAULT("Vault"),
		PERMISSIONS("Permissions");

		public String name;

		private PermissionsSystemType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
}