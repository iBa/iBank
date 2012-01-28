package com.iBank.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;

/**
 *  /bank close <ACCOUNT> - Close the account
 * @author steffengy
 * @needs_owner true
 */
public class CommandClose extends Handler {
	public void handle(CommandSender sender, String[] arguments) { 	
		boolean console = false;
		if(!(sender instanceof Player)) console = true;
		if(!console) {
			if(iBank.GetRegionAt(((Player)sender).getLocation()) == "") {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
				return;
			}
		}
		if(arguments.length == 1) {
			// check account
			if(Bank.hasAccount(arguments[0])) {
				//@needs_owner true
				if(console || Bank.getAccount(arguments[0]).isOwner(((Player)sender).getName())) {
					// Simulate withdraw
					new CommandWithdraw().handle(sender, new String[] { arguments[0] } );
					// Close account
					Bank.removeAccount(arguments[0]);
					MessageManager.send(sender, "&g&" + Configuration.StringEntry.SuccessClose.toString().replace("$name$", arguments[0]));
				}else{
					MessageManager.send(sender, "&r&" + Configuration.StringEntry.ErrorNeedOwner.toString());
				}
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
			}
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
}	
