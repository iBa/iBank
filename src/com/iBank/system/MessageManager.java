package com.iBank.system;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * For handling all sending stuff
 * @author steffengy
 *
 */
public class MessageManager {
	/**
	 * Sends a message to the player
	 * @param sender The destination player 
	 * @param message The message
	 */
	public static void send(CommandSender sender, String message, String Tag) {
		// parse message
		message = parse(message);
		sender.sendMessage(parse(Tag) + " " + message);
	}
	public static void send(CommandSender sender, String message) {
		send(sender, message, Configuration.StringEntry.BankTag.getValue());
	}
	public static String parse(String message) {
		message = message.replace("&g&",ChatColor.GREEN.toString()).replace("&b&",ChatColor.BLACK.toString()).replace("&w&", ChatColor.WHITE.toString());
		message = message.replace("&gray&", ChatColor.GRAY.toString()).replaceAll("&gold&",ChatColor.GOLD.toString()).replace("&y&", ChatColor.YELLOW.toString());
		message = message.replace("&r&", ChatColor.RED.toString()).replace("&blue&", ChatColor.BLUE.toString()).replace("&dg&", ChatColor.DARK_GREEN.toString());
		return message;
	}
}
