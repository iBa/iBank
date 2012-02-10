package com.iBank.system;

import org.bukkit.command.CommandSender;

/**
 * The command interface
 * @author steffengy
 *
 */
public interface Command {
	/**
	 * Handle params as this command
	 * @param sender
	 * @param arguments
	 */
	public void handle(CommandSender sender, String[] arguments);
	/**
	 * Return the line 2 display in help
	 * @return String
	 */
	public String getHelp();
}
