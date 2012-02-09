package com.iBank.system;

import org.bukkit.command.CommandSender;

/**
 * The command interface
 * @author steffengy
 *
 */
public interface Command {
	public void handle(CommandSender sender, String[] arguments);
}
