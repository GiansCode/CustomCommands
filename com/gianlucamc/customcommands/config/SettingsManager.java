package com.gianlucamc.customcommands.config;

public class SettingsManager {

	private boolean cooldownEnabled;
	private int cooldown;

	public SettingsManager(boolean cooldownEnabled, int cooldown) {
		this.cooldownEnabled = cooldownEnabled;
		this.cooldown = cooldown;
	}

	public boolean isCooldownEnabled() {
		return cooldownEnabled;
	}

	public int getCooldown() {
		return cooldown;
	}
}