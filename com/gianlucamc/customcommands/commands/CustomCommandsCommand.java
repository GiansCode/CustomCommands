package com.gianlucamc.customcommands.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gianlucamc.customcommands.CustomCommands;
import com.gianlucamc.customcommands.objects.CustomCommand;

import me.clip.placeholderapi.PlaceholderAPI;

public class CustomCommandsCommand implements CommandExecutor {

	private CustomCommands plugin;

	public CustomCommandsCommand(CustomCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			if (!(sender.hasPermission("customcommands.commands.help"))) {
				sender.sendMessage(plugin.getMessageManager().getNoPermission());
				return true;
			}

			for (String helpMessage : plugin.getMessageManager().getHelp()) {
				helpMessage = ChatColor.translateAlternateColorCodes('&', helpMessage);
				helpMessage = helpMessage.replace("%version%", plugin.getDescription().getVersion());

				sender.sendMessage(helpMessage);
			}
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				if (!(sender.hasPermission("customcommands.commands.list"))) {
					sender.sendMessage(plugin.getMessageManager().getNoPermission());
					return true;
				}

				sender.sendMessage("Created Commands:");

				for (CustomCommand command : plugin.getCustomCommands()) {
					String id = command.getCommand();

					sender.sendMessage("- " + id);
				}

				sender.sendMessage(" ");
				return true;
			}

			if (args[0].equalsIgnoreCase("reload")) {
				if (!(sender.hasPermission("customcommands.commands.reload"))) {
					sender.sendMessage(plugin.getMessageManager().getNoPermission());
					return true;
				}

				plugin.saveDefaultConfig();
				plugin.reloadConfig();
				plugin.loadConfig();

				sender.sendMessage(plugin.getMessageManager().getReloadSuccess());
				return true;
			}
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("execute")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(plugin.getMessageManager().getNotAPlayer());
					return true;
				}

				Player p = (Player) sender;

				if (!(p.hasPermission("customcommands.commands.execute"))) {
					p.sendMessage(plugin.getMessageManager().getNoPermission());
					return true;
				}

				CustomCommand command = plugin.getCustomCommand(args[1]);

				if (command == null) {
					p.sendMessage(plugin.getMessageManager().getNoCommandFound().replace("%command%", args[1]));
					return true;
				}

				for (String action : command.getActions()) {
					action = ChatColor.translateAlternateColorCodes('&', action);
					action = PlaceholderAPI.setPlaceholders(p, action);

					if (action.startsWith("[console]")) {
						action = action.replace("[console] ", "");

						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), action);
					}

					if (action.startsWith("[player]")) {
						action = action.replace("[player] ", "");

						p.performCommand(action);
					}

					if (action.startsWith("[message]")) {
						action = action.replace("[message] ", "");

						p.sendMessage(action);
					}
				}
			}

			if (args.length > 2) {
				sender.sendMessage(plugin.getMessageManager().getIncorrectArguments());
				return true;
			}
		}
		return true;
	}
}