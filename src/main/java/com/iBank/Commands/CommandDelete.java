package com.ibank.Commands;

import org.bukkit.command.CommandSender;

import com.ibank.system.Bank;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;

/**
 *  /bank delete <NAME>
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Name" },
		permission = "ibank.manage",
		root = "bank", 
		sub = "delete"
)
public class CommandDelete extends Command 
{
	public void handle(CommandSender sender, String[] arguments) 
	{ 
		if(arguments.length == 1) 
		{
			if(!Bank.hasAccount(arguments[0])) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
				return;
			}
			Bank.removeAccount(arguments[0]);
			send(sender, "&g&"+Configuration.StringEntry.SuccessDelAccount.getValue().replace("$name$", arguments[0]));
		}
		else
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	
	public String getHelp() 
	{
		return Configuration.StringEntry.DeleteDescription.getValue();
	}
}
