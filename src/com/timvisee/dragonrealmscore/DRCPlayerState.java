package com.timvisee.dragonrealmscore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DRCPlayerState {
	
	private double health = 20;
	private int food = 20;
	private int level = 0;
	private float exp = 0.0f;
	private int fireTicks = 0;
	private float exhaustion = 0.0f;
	private GameMode gm = GameMode.SURVIVAL;
	private boolean allowFly = false;
	private boolean flying = false;
	private List<ItemStack> inv = new ArrayList<ItemStack>();
	private List<ItemStack> invEquip = new ArrayList<ItemStack>();
	private List<ItemStack> enderchest = new ArrayList<ItemStack>();
	private Collection<PotionEffect> potionEffects = new HashSet<PotionEffect>();

	/**
	 * Constructor
	 */
	public DRCPlayerState() { }

	/**
	 * Constructor
	 * @param p The player to store the state from
	 */
	public DRCPlayerState(Player p) {
		fromPlayer(p);
	}

	/**
	 * Constructor
	 * @param health Health
	 * @param food Food
	 * @param exp Exp
	 * @param fireTicks Fire ticks
	 * @param exhaustion Exhaustion
	 * @param gm GameMode
	 * @param allowFly Allow flight
	 * @param flying Is flying
	 * @param inv Inventory contents
	 * @param invEquip Equipment contents
	 * @param enderchest Enderchest contents
	 * @param potionEffects Active potion effects
	 */
	public DRCPlayerState(double health, int food, int level, float exp, int fireTicks,
			float exhaustion, GameMode gm, boolean allowFly, boolean flying,
			List<ItemStack> inv, List<ItemStack> invEquip,
			List<ItemStack> enderchest, Collection<PotionEffect> potionEffects) {
		this.health = health;
		this.food = food;
		this.level = level;
		this.exp = exp;
		this.fireTicks = fireTicks;
		this.exhaustion = exhaustion;
		this.gm = gm;
		this.allowFly = allowFly;
		this.flying = flying;
		this.inv = inv;
		this.invEquip = invEquip;
		this.enderchest = enderchest;
		this.potionEffects = potionEffects;
	}

	public double getHealth() {
		return this.health;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public int getFood() {
		return this.food;
	}

	public void setFood(int food) {
		this.food = food;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	public float getExp() {
		return this.exp;
	}

	public void setExp(float exp) {
		this.exp = exp;
	}

	public int getFireTicks() {
		return this.fireTicks;
	}

	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	public float getExhaustion() {
		return this.exhaustion;
	}

	public void setExhaustion(float exhaustion) {
		this.exhaustion = exhaustion;
	}

	public GameMode getGameMode() {
		return this.gm;
	}

	public void setGameMode(GameMode gm) {
		this.gm = gm;
	}

	public boolean getAllowFlying() {
		return this.allowFly;
	}

	public void setAllowFlying(boolean allowFlying) {
		this.allowFly = allowFlying;
	}

	public boolean getFlying() {
		return this.flying;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}

	public List<ItemStack> getInventory() {
		return this.inv;
	}

	public void setInventory(List<ItemStack> inv) {
		this.inv = inv;
	}

	public List<ItemStack> getInventoryEquipment() {
		return this.invEquip;
	}

	public void setInventoryEquipment(List<ItemStack> invEquip) {
		this.invEquip = invEquip;
	}

	public List<ItemStack> getEnderchest() {
		return this.enderchest;
	}

	public void setEnderchest(List<ItemStack> enderchest) {
		this.enderchest = enderchest;
	}

	public Collection<PotionEffect> getActivePotionEffects() {
		return this.potionEffects;
	}

	public void setActivePotionEffects(Collection<PotionEffect> potionEffects) {
		this.potionEffects = potionEffects;
	}

	public boolean fromPlayer(Player p) {
		if (p == null)
			return false;
			
		try {
			this.health = p.getHealth();
			this.food = p.getFoodLevel();
			this.exp = p.getExp();
			this.level = p.getLevel();
			this.fireTicks = p.getFireTicks();
			this.exhaustion = p.getExhaustion();
			this.gm = p.getGameMode();
			this.allowFly = p.getAllowFlight();
			this.flying = p.isFlying();
			this.inv = Arrays.asList(p.getInventory().getContents());
			this.invEquip = Arrays.asList(p.getInventory().getArmorContents());
			this.enderchest = Arrays.asList(p.getEnderChest().getContents());
			this.potionEffects = p.getActivePotionEffects();
			return true;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean apply(Player p) {
		if (p == null)
			return false;
		
		try {
			p.setHealth(this.health);
			p.setFoodLevel(this.food);
			p.setLevel(this.level);
			p.setExp(this.exp);
			p.setFireTicks(this.fireTicks);
			p.setExhaustion(this.exhaustion);
			p.closeInventory();
			//p.setGameMode(this.gm);
			p.setAllowFlight(this.allowFly);
			p.setFlying(this.flying);
			p.getInventory().setContents(this.inv.toArray(new ItemStack[]{}));
			p.getInventory().setBoots(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setHelmet(null);
			p.getEquipment().setArmorContents(this.invEquip.toArray(new ItemStack[]{}));
			p.updateInventory();
			p.getEnderChest().setContents(this.enderchest.toArray(new ItemStack[]{}));
			for (PotionEffect effect : p.getActivePotionEffects())
				p.removePotionEffect(effect.getType());
			p.addPotionEffects(this.potionEffects);
			return true;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean save(ConfigurationSection c) {
		if (c == null)
			return false;

		c.set("health", Double.valueOf(this.health));
		c.set("food", Integer.valueOf(this.food));
		c.set("level", this.level);
		c.set("exp", Double.valueOf(this.exp));
		c.set("fireTicks", Integer.valueOf(this.fireTicks));
		c.set("exhaustion", Double.valueOf(this.exhaustion));
		c.set("gm", Integer.valueOf(this.gm.getValue()));
		c.set("allowFly", Boolean.valueOf(this.allowFly));
		c.set("flying", Boolean.valueOf(this.flying));

		ConfigurationSection invSect = c.createSection("inv");
		saveItemStackList(invSect, this.inv);

		ConfigurationSection invEquipSect = c.createSection("invEquipment");
		saveItemStackList(invEquipSect, this.invEquip);

		ConfigurationSection enderchestSect = c.createSection("enderchest");
		saveItemStackList(enderchestSect, this.enderchest);

		ConfigurationSection potionEffectsSect = c
				.createSection("potionEffects");
		savePotionEffectCollection(potionEffectsSect, this.potionEffects);

		return true;
	}

	public static DRCPlayerState load(ConfigurationSection c) {
		if (c == null) {
			return null;
		}
		try {
			double health = c.getDouble("health", 20.0D);
			int food = c.getInt("food", 20);
			int level = c.getInt("level", 0);
			float exp = (float) c.getDouble("exp", 0.0D);
			int fireTicks = c.getInt("fireTicks", 0);
			float exhaustion = (float) c.getDouble("exhaustion", 0.0D);
			GameMode gm = GameMode.getByValue(c.getInt("gm", 0));
			boolean allowFly = c.getBoolean("allowFly", false);
			boolean flying = c.getBoolean("flying", false);

			ConfigurationSection invSect = c.getConfigurationSection("inv");
			List<ItemStack> inv = loadItemStackList(invSect);

			ConfigurationSection invEquipSect = c
					.getConfigurationSection("invEquipment");
			List<ItemStack> invEquip = loadItemStackList(invEquipSect);

			ConfigurationSection enderchestSect = c
					.getConfigurationSection("enderchest");
			List<ItemStack> enderchest = loadItemStackList(enderchestSect);

			ConfigurationSection potionEffectsSect = c
					.getConfigurationSection("potionEffects");
			Collection<PotionEffect> potionEffects = loadPotionEffectCollection(potionEffectsSect);

			return new DRCPlayerState(health, food, level, exp, fireTicks, exhaustion,
					gm, allowFly, flying, inv, invEquip, enderchest,
					potionEffects);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private boolean saveItemStackList(ConfigurationSection c, List<ItemStack> items) {
		if ((c == null) || (items == null)) {
			return false;
		}
		try {
			int i = 0;
			for (ItemStack is : items) {
				if (is != null)
					c.set(String.valueOf(i), is);
				else
					c.set(String.valueOf(i), "null");
				i++;
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private static List<ItemStack> loadItemStackList(ConfigurationSection c) {
		if (c == null) {
			return null;
		}
		try {
			Set<String> keys = c.getKeys(false);

			List<ItemStack> itemStackList = new ArrayList<ItemStack>();

			for (String key : keys) {
				if (c.getString(key, "").equals("null"))
					itemStackList.add(null);
				else {
					itemStackList.add(c.getItemStack(key, null));
				}
			}

			return itemStackList;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private boolean savePotionEffectCollection(ConfigurationSection c,
			Collection<PotionEffect> potionEffects) {
		if ((c == null) || (potionEffects == null)) {
			return false;
		}
		try {
			int i = 0;
			for (PotionEffect pe : potionEffects) {
				if (pe != null) {
					ConfigurationSection sect = c.createSection(String
							.valueOf(i));
					sect.set("type", Integer.valueOf(pe.getType().getId()));
					sect.set("duration", Integer.valueOf(pe.getDuration()));
					sect.set("amplifier", Integer.valueOf(pe.getAmplifier()));
					sect.set("ambient", Boolean.valueOf(pe.isAmbient()));
					i++;
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private static Collection<PotionEffect> loadPotionEffectCollection(
			ConfigurationSection c) {
		if (c == null) {
			return null;
		}

		try {
			Set<String> keys = c.getKeys(false);

			Collection<PotionEffect> potionEffects = new HashSet<PotionEffect>();

			for (String key : keys) {
				ConfigurationSection sect = c.getConfigurationSection(String
						.valueOf(key));

				PotionEffectType type = PotionEffectType.getById(sect.getInt(
						"type", 0));
				int duration = sect.getInt("duration", 0);
				int amplifier = sect.getInt("amplifier", 0);
				boolean ambient = sect.getBoolean("ambient");

				PotionEffect pe = new PotionEffect(type, duration, amplifier,
						ambient);

				potionEffects.add(pe);
			}

			return potionEffects;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}