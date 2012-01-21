package com.iBank.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;
import com.iBank.system.Region;

/**
 * [1] /bank open <ACCOUNTNAME>
 * @author steffengy
 * [1] Can't be run from console
 * @todo Add parameter for other owners
 */
public class CommandOpenAccount extends Handler {
	public void handle(CommandSender sender, String[] arguments) { 
		if(arguments.length == 1) {
			if(!(sender instanceof Player)) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.toString());
				return;
			}
			String region = "";
			if((region = iBank.GetRegionAt(((Player)sender).getLocation())) == "") {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
				return;
			}
			if(!Bank.hasAccount(arguments[0])) {
				Bank.createAccount(arguments[0], ((Player)sender).getName());
				// check for custom percentages
				if(region != " ") {
					Region reg = Bank.getRegion(region);
					if(!reg.onDefault) Bank.getAccount(arguments[0]).setOnPercentage(reg.getOnPercentage(), true);
					if(!reg.offDefault) Bank.getAccount(arguments[0]).setOffPercentage(reg.getOffPercentage(), true);
				}
				MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessAddAccount.toString().replace("$name$", "Account "+arguments[0]+" "));
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorAlreadyExists.toString().replace("$name$", "Account "+arguments[0]+" "));
			}
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
}
