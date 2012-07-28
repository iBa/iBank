package com.iBank.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;

/**
 *  /bank owners <ACCOUNT> - Show the owners of the account
 *  /bank owners <ACCOUNT> a(dd) <NAME> - Add owner <NAME> to account
 *  /bank owners <ACCOUNT> d(el) <NAME> - Remove owner <NAME> (as owner) from account
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Name", "Acc-Key", "Value" }, 
		permission = "iBank.access",
		root = "bank", 
		sub = "owners"
)
public class CommandOwners extends Command {
	public void handle(CommandSender sender, String[] arguments) { 
		boolean console = false;
		if(!(sender instanceof Player)) {
			console = true;
		}
		if(arguments.length == 0) {
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
			return;
		}
		
		if(Bank.hasAccount(arguments[0])) {
		if(arguments.length == 1) {
				send(sender, "&w&"+Configuration.StringEntry.GeneralInfo.getValue().replace("$type$","Account").replace("$name$", arguments[0]));
				String owners = Bank.getAccount(arguments[0]).getOwners().toString();
				send(sender, "&w&"+Configuration.StringEntry.GeneralOwners.getValue()+" : "+owners);
			}else if(arguments.length == 3) {
				BankAccount tmp = Bank.getAccount(arguments[0]);
				
				if(!console && !tmp.isOwner(((Player)sender).getName())) {
					send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
					return;
				}
				
				if(arguments[1].equalsIgnoreCase("a") || arguments[1].equalsIgnoreCase("add")) {
						if(!tmp.isOwner(arguments[2])) { 
							if(Bukkit.getOfflinePlayer(arguments[2]) != null) {
								tmp.addOwner(arguments[2]);
							}else{
								send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[2]));
							}
						} else {
							send(sender, "&r&" + Configuration.StringEntry.ErrorAlready.getValue().replace("$name$", arguments[0]).replace("$type$", Configuration.StringEntry.GeneralOwners.getValue()));
							return;
						}
							send(sender, "&g&"+Configuration.StringEntry.SuccessMod.getValue().replace("$name$", arguments[0]));
					
				}else if(arguments[1].equalsIgnoreCase("d") || arguments[1].equalsIgnoreCase("del")) {
					if(tmp.isOwner(arguments[2])) {
						if(Bukkit.getOfflinePlayer(arguments[2]) != null) {
							tmp.removeOwner(arguments[2]);
						}else{
							send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[2]));
						}
					} else {
						send(sender, "&r&" + Configuration.StringEntry.ErrorNot.getValue().replace("$name$", arguments[2]).replace("$type$", Configuration.StringEntry.GeneralOwners.getValue()));
						return;
					}
					send(sender, "&g&"+Configuration.StringEntry.SuccessMod.getValue().replace("$name$", arguments[0]));
				}else{
					send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
				}
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
			}
		}else{
			send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
		}
	}
	public String getHelp() {
		return Configuration.StringEntry.OwnersDescription.getValue();
	}
}
