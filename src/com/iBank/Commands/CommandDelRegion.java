package com.iBank.Commands;

import org.bukkit.command.CommandSender;

import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;

/**
 *  /bank delregion <NAME>
 * @author steffengy
 *
 */
public class CommandDelRegion extends Handler {
	@Override
	public void handle(CommandSender sender, String[] arguments) {
		if(arguments.length == 1) {
			//delete region
			if(Bank.hasRegion(arguments[0])) {
				Bank.removeRegion(arguments[0]);
				MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessDelRegion.toString().replace("$name$", arguments[0]));
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", "Region "+arguments[0]+" "));
			}
			return;
		}
		MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
	}
}
