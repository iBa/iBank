package com.iBank.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;

/**
 *  /bank - Shows the balance
 * @author steffengy
 *
 */
public class BankRootCommand extends Handler{

	/**
	 *  Shows if in bank region if enabled
	 *  Shows a list of all accounts the player owns and has access to
	 */
	@Override
	public void handle(CommandSender sender, String[] arguments) {
		if(!(sender instanceof Player)) {
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.toString());
			return;
		}
		if(!iBank.canExecuteCommand(((Player)sender).getLocation())) {
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
			return;
		}
	}
	
}
