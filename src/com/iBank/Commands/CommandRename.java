package com.iBank.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;
import com.iBank.system.MessageManager;

/**
 *  /bank rename A B - Rename account A to b
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Name", "New Name"},  
		permission = "iBank.access",
		root = "bank", 
		sub = "rename"
)
public class CommandRename implements Command {

	@Override
	public void handle(CommandSender sender, String[] arguments) {
		boolean hasPerm = !(sender instanceof Player) || iBank.hasPermission(((Player)sender), "iBank.manage");
		if(arguments.length == 2) {
			if(Bank.hasAccount(arguments[0])) {
				BankAccount account = Bank.getAccount(arguments[0]);
				//Permission check / etc
				if(!hasPerm && !account.isOwner(((Player)sender).getName())) {
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
					return;
				}
				//Already there?
				if(Bank.hasAccount(arguments[1])) {
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorAlreadyExists.toString().replace("$name$", "Account "+arguments[1]+" "));
					return;
				}
				//Rename simply
				account.Update("name", arguments[1]);
				MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessRename.toString().replace("$a$", arguments[0]).replace("$b$", arguments[1]));
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
			}
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}

	@Override
	public String getHelp() {
		return Configuration.StringEntry.RenameDescription.getValue();
	}
	
}
