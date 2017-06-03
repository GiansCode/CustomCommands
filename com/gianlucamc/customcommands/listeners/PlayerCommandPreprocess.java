package com.gianlucamc.customcommands.listeners;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.gianlucamc.customcommands.CustomCommands;
import com.gianlucamc.customcommands.objects.CustomCommand;
import com.google.common.collect.Maps;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlayerCommandPreprocess implements Listener {

	private CustomCommands plugin;
	private Map<UUID, Long> cooldown;

	public PlayerCommandPreprocess(CustomCommands plugin) {
		this.plugin = plugin;
		this.cooldown = Maps.newHashMap();
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String cmd = e.getMessage().replace("/", "");

		CustomCommand command = plugin.getCustomCommand(cmd);

		if (command == null) {
			return;
		}

		e.setCancelled(true);

		if (!(p.hasPermission("customcommands.use." + command.getCommand()))) {
			p.sendMessage(command.getNoPermissionMessage());
			return;
		}

		if (plugin.getSettingsManager().isCooldownEnabled()) {
			if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()) {
				long timeRemaining = cooldown.get(p.getUniqueId()) - System.currentTimeMillis();

				p.sendMessage(plugin.getMessageManager().getOnCooldown().replace("%time%", String.valueOf(timeRemaining / 1000)));
				return;
			}
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

		cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (plugin.getSettingsManager().getCooldown() * 1000));
	}
}