package com.iBank.Commands;

import org.bukkit.command.CommandSender;

import com.iBank.system.Bank;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;

/**
 *  /bank delete <NAME>
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Name" },  
		permission = "iBank.manage",
		root = "bank", 
		sub = "delete"
)
public class CommandDelete extends Command {
	public void handle(CommandSender sender, String[] arguments) { 
		if(arguments.length == 1) {
			if(Bank.hasAccount(arguments[0])) {
				Bank.removeAccount(arguments[0]);
				send(sender, "&g&"+Configuration.StringEntry.SuccessDelAccount.getValue().replace("$name$", arguments[0]));
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
			}
		}else{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	public String getHelp() {
		return Configuration.StringEntry.DeleteDescription.getValue();
	}
}
