package com.iBank.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;

/**
 *  /bank balance [NAME]
 * @author steffengy
 * 
 */
public class CommandBalance extends Handler {
	public void handle(CommandSender sender, String[] arguments) {
	boolean console = false;
	if(!(sender instanceof Player)) console = true;
	if(arguments.length == 1) {
		// has account
		if(Bank.hasAccount(arguments[0])) {
			BankAccount acc = Bank.getAccount(arguments[0]);
			if(console || (acc.isOwner(((Player)sender).getName()) || acc.isUser(((Player)sender).getName())) || iBank.hasPermission(sender, "iBank.balance")) {
				String formattedBalance = iBank.economy.format(acc.getBalance().doubleValue());
				if(!console) {
					if(!iBank.canExecuteCommand(((Player)sender))) {
						MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
						return;
					}
				}
				MessageManager.send(sender, "&dg&"+Configuration.StringEntry.BalanceShort.getValue()+" &gray&"+arguments[0]+" &w&: "+formattedBalance);
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
			}
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
		}
	}
	}
}
