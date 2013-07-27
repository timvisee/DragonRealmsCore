package com.timvisee.dragonrealmscore.manager;

public enum DRCEconomyManagerEconomySystemType {
	NONE("None"),
	SIMPLE_ECONOMY("Simple Economy"),
	BOSECONOMY("BOSEconomy"),
	VAULT("Vault");
	
	public String name;
	
	DRCEconomyManagerEconomySystemType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
