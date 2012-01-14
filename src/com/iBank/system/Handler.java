package com.iBank.system;


import org.bukkit.command.CommandSender;

import com.iBank.iBank;

/**
 * The handler template
 * @author steffengy
 *
 */
public class Handler {
	public iBank main;
	public void handle(CommandSender sender, String[] arguments) { }
	public void setSource(iBank main) {
		this.main = main;
	}
}
