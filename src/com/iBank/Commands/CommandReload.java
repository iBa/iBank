package com.iBank.Commands;

import org.bukkit.command.CommandSender;

import com.iBank.system.Handler;

/**
 *   /bank reload - Call reload function in iBank
 * @author steffengy
 *
 */
public class CommandReload extends Handler {
	public void handle(CommandSender sender, String[] arguments) {
		main.reloadConfig();
	}
}
