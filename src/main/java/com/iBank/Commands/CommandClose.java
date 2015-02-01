package com.ibank.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ibank.iBank;
import com.ibank.system.Bank;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;

/**
 *  /bank close <ACCOUNT> - Close the account
 * @author steffengy
 */
@CommandInfo(
		arguments = { "Name" }, 
		permission = "ibank.access",
		root = "bank", 
		sub = "close"
)
public class CommandClose extends Command 
{
	public void handle(CommandSender sender, String[] arguments) 
	{ 	
		boolean console = !(sender instanceof Player);

		if(!console && !iBank.canExecuteCommand(((Player)sender))) 
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
			return;
		}
		if(arguments.length == 1) 
		{
			// check account
			if(!Bank.hasAccount(arguments[0]))
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
				return;
			}
			//@needs_owner true
			if(!console && !Bank.getAccount(arguments[0]).isOwner(((Player)sender).getUniqueId()))
			{
				send(sender, "&r&" + Configuration.StringEntry.ErrorNeedOwner.getValue());
				return;
			}
			// Simulate withdraw
			new CommandWithdraw().handle(sender, new String[] { arguments[0] } );
			// Close account
			Bank.removeAccount(arguments[0]);
			send(sender, "&g&" + Configuration.StringEntry.SuccessClose.getValue().replace("$name$", arguments[0]));
		}
		else
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	
	public String getHelp() {
		return Configuration.StringEntry.CloseDescription.getValue();
	}
}	
