package com.ibank.system;

import org.bukkit.command.CommandSender;

/**
 * The command interface
 * @author steffengy
 *
 */
public class Command 
{
	/**
	 * Handle params as this command
	 */
	public void handle(CommandSender sender, String[] arguments) { }
	/**
	 * Return the line 2 display in help
	 * @return String
	 */
	public String getHelp() 
	{ 
		return "[NO HELP AVAILABLE - PLEASE REPORT THIS]"; 
	}
	
	/*
	 * Wrapper to first MessageManager send
	 */
	public void send(CommandSender sender, String message)
	{
		MessageManager.send(sender, message);
	}
	
	/**
	 * Wrapper to second MessageManager.send
	 */
	public void send(CommandSender sender, String message, String Tag)
	{
		MessageManager.send(sender, message, Tag);
	}
}
