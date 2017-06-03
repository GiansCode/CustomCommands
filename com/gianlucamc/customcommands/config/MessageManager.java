package com.gianlucamc.customcommands.config;

import java.util.List;

public class MessageManager {

	private String notAPlayer;
	private String noPermission;
	private String noCommandFound;
	private String incorrectArguments;
	private String onCooldown;
	private String reloadSuccess;
	private List<String> help;

	public MessageManager(String notAPlayer, String noPermission, String noCommandFound, String incorrectArguments, String onCooldown, String reloadSuccess, List<String> help) {
		this.notAPlayer = notAPlayer;
		this.noPermission = noPermission;
		this.noCommandFound = noCommandFound;
		this.incorrectArguments = incorrectArguments;
		this.onCooldown = onCooldown;
		this.reloadSuccess = reloadSuccess;
		this.help = help;
	}

	public String getNotAPlayer() {
		return notAPlayer;
	}

	public String getNoPermission() {
		return noPermission;
	}

	public String getNoCommandFound() {
		return noCommandFound;
	}

	public String getIncorrectArguments() {
		return incorrectArguments;
	}

	public String getOnCooldown() {
		return onCooldown;
	}

	public String getReloadSuccess() {
		return reloadSuccess;
	}

	public List<String> getHelp() {
		return help;
	}
}