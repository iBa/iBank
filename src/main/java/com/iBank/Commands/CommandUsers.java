package com.ibank.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ibank.system.Bank;
import com.ibank.system.BankAccount;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;

@CommandInfo(
		arguments = { "Name", "Acc-Key", "Value" }, 
		permission = "ibank.access",
		root = "bank", 
		sub = "users"
)
public class CommandUsers extends Command 
{
	@Override
	public void handle(CommandSender sender, String[] arguments) 
	{ 
		boolean console = !(sender instanceof Player);
		
		if(arguments.length == 0) 
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
			return;
		}
		if(!Bank.hasAccount(arguments[0])) 
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
			return;
		}
		if(arguments.length == 1) 
		{
			send(sender, "&w&"+Configuration.StringEntry.GeneralInfo.getValue().replace("$type$","Account").replace("$name$", arguments[0]));
			String users = Bank.getAccount(arguments[0]).getUsers().toString();
			send(sender, "&w&"+Configuration.StringEntry.GeneralUsers.getValue()+" : "+users);
		}
		else if(arguments.length == 3) 
		{
			BankAccount tmp = Bank.getAccount(arguments[0]);
			
			if(!console && !tmp.isOwner(((Player)sender).getName())) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				return;
			}
				
			if(arguments[1].equalsIgnoreCase("a") || arguments[1].equalsIgnoreCase("add")) 
			{
					if(!tmp.isUser(arguments[2])) 
					{ 
						if(Bukkit.getOfflinePlayer(arguments[2]) != null) 
							tmp.addUser(arguments[2]);
						else
							send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[2]));
					} 
					else 
					{
						send(sender, "&r&" + Configuration.StringEntry.ErrorAlready.getValue().replace("$name$", arguments[0]).replace("$type$", Configuration.StringEntry.GeneralUsers.getValue()));
						return;
					}
					send(sender, "&g&"+Configuration.StringEntry.SuccessMod.getValue().replace("$name$", arguments[0]));	
			}
			else if(arguments[1].equalsIgnoreCase("d") || arguments[1].equalsIgnoreCase("del")) 
			{
				if(tmp.isUser(arguments[2])) 
				{
					if(Bukkit.getOfflinePlayer(arguments[2]) != null) 
						tmp.removeUser(arguments[2]);
					else
						send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[2]));
				} 
				else 
				{
					send(sender, "&r&" + Configuration.StringEntry.ErrorNot.getValue().replace("$name$", arguments[2]).replace("$type$", Configuration.StringEntry.GeneralUsers.getValue()));
					return;
				}
				send(sender, "&g&"+Configuration.StringEntry.SuccessMod.getValue().replace("$name$", arguments[0]));
			}
			else
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
		else
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	
	public String getHelp() 
	{
		return Configuration.StringEntry.UsersDescription.getValue();
	}
}
