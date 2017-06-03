package com.gianlucamc.customcommands;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gianlucamc.customcommands.commands.CustomCommandsCommand;
import com.gianlucamc.customcommands.config.MessageManager;
import com.gianlucamc.customcommands.config.SettingsManager;
import com.gianlucamc.customcommands.listeners.PlayerCommandPreprocess;
import com.gianlucamc.customcommands.objects.CustomCommand;
import com.google.common.collect.Lists;

public class CustomCommands extends JavaPlugin implements Listener {

	private MessageManager messageManager;
	private SettingsManager settingsManager;

	private List<CustomCommand> customCommands;

	@Override
	public void onEnable() {
		if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
			Bukkit.getServer().getLogger().severe("[CustomCommands] PlaceholderAPI not found, disabling the plugin...");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		customCommands = Lists.newArrayList();

		saveDefaultConfig();
		loadConfig();

		getCommand("customcommands").setExecutor(new CustomCommandsCommand(this));
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerCommandPreprocess(this), this);
	}

	@Override
	public void onDisable() {
		customCommands = null;

		saveDefaultConfig();
	}

	public void loadConfig() {
		getCustomCommands().clear();

		messageManager = new MessageManager(
				ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.notAPlayer")),
				ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.noPermission")),
				ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.noCommandFound")),
				ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.incorrectArguments")),
				ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.onCooldown")),
				ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.reloadSuccess")),
				getConfig().getStringList("messages.help")
				);

		settingsManager = new SettingsManager(
				getConfig().getBoolean("settings.cooldownEnabled"),
				getConfig().getInt("settings.cooldown"));

		ConfigurationSection section = getConfig().getConfigurationSection("commands");

		if (section != null) {
			Set<String> commands = section.getKeys(false);

			if (commands != null && !commands.isEmpty()) {
				for (String command : commands) {
					String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("commands." + command + ".noPermissionMessage"));
					List<String> actions = getConfig().getStringList("commands." + command + ".actions");

					getCustomCommands().add(new CustomCommand(command, noPermissionMessage, actions));
				}
			}

			Bukkit.getServer().getLogger().info("[CustomCommands] Created " + getCustomCommands().size() + " Custom Commands!");
		}
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public List<CustomCommand> getCustomCommands() {
		return customCommands;
	}

	public CustomCommand getCustomCommand(String cmd) {
		for (CustomCommand customCommand : getCustomCommands()) {
			if (customCommand.getCommand().equalsIgnoreCase(cmd)) {
				return customCommand;
			}
		}

		return null;
	}
}
