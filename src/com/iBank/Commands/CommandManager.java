package com.iBank.Commands;

import org.bukkit.command.CommandSender;

import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;
import com.iBank.system.MessageManager;

/**
 *  /bank account <NAME> - Shows info about this account
 *  /bank account <NAME> on 10 - Sets the online percentage of this account to zero
 *  /bank account <NAME> off 10 - Sets the offline percentage of this account to zero 
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Name" }, 
		help = "", 
		permission = "iBank.manage",
		root = "bank", 
		sub = "account"
)
public class CommandManager implements Command {
	public void handle(CommandSender sender, String[] arguments) {
		// General info about account
		if(arguments.length == 1) {
			if(Bank.hasAccount(arguments[0])) {
				BankAccount tmp = Bank.getAccount(arguments[0]);
				MessageManager.send(sender, "&w&"+Configuration.StringEntry.GeneralInfo.toString().replace("$type$","Account").replace("$name$", arguments[0]));
				String onlineP = tmp.onDefault ? " Default " : String.valueOf(tmp.getOnlinePercentage()) + "%";
				String offlinP = tmp.offDefault ? " Default ": String.valueOf(tmp.getOfflinePercentage()) + "%";

				String intString = !tmp.intervalDefault ? String.valueOf(tmp.getInterval()) : "Default";
				MessageManager.send(sender, "&w&" + Configuration.StringEntry.GeneralInterval.toString() + ": &gray&" + intString, "");
				MessageManager.send(sender, "&w&Online %: &gray&" + onlineP, "");
				MessageManager.send(sender, "&w&Offline %: &gray&" + offlinP, "");
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", "Account "+arguments[0]+" "));
				return;
			}
		// modify percentages and settings
		// todo add possibility to add interval
		}else if(arguments.length == 3) {
			if(Bank.hasAccount(arguments[0])) {
				if(arguments[1].equalsIgnoreCase("online") || arguments[1].equalsIgnoreCase("on")) {
					Double percentage = 0.00;
					try{
						percentage = Double.parseDouble(arguments[2]);
					}catch(Exception e) {
						MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" "+arguments[2]);
						return;
					}
					Bank.getAccount(arguments[0]).setOnPercentage(percentage, true);
				}else if(arguments[1].equalsIgnoreCase("offline") || arguments[1].equalsIgnoreCase("off")) {
					Double percentage = 0.00;
					try{
						percentage = Double.parseDouble(arguments[2]);
					}catch(Exception e) {
						MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" "+arguments[2]);
						return;
					}
					Bank.getAccount(arguments[0]).setOffPercentage(percentage, true);
				}else if(arguments[1].equalsIgnoreCase("interval")) {				
					int data  = 0;
					try{
						data = Integer.parseInt(arguments[2]);
					}catch(Exception e) {
						MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" "+arguments[2]);
						return;
					}
					Bank.getAccount(arguments[0]).setInterval(data, true);
				}else{
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" "+arguments[1]);
					return;
				}
				MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessAccount.toString().replace("$name$", arguments[0]));
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", "Account "+arguments[0]+" "));
				return;
			}
		} else {
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
	
	public String getHelp() {
		return Configuration.StringEntry.AccountDescription.getValue();
	}
}
