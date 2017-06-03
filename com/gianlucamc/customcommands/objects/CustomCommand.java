package com.gianlucamc.customcommands.objects;

import java.util.List;

public class CustomCommand {

	private String command;
	private String noPermissionMessage;
	private List<String> actions;

	public CustomCommand(String command, String noPermissionMessage, List<String> actions) {
		this.command = command;
		this.noPermissionMessage = noPermissionMessage;
		this.actions = actions;
	}

	public String getCommand() {
		return command;
	}

	public String getNoPermissionMessage() {
		return noPermissionMessage;
	}

	public List<String> getActions() {
		return actions;
	}
}