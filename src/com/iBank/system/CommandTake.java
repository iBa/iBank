package com.iBank.system;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;

import com.iBank.iBank;

/**
 *  /bank take <ACCOUNT> <MONEY> - Take money from account
 *  
 * @author steffengy
 *
 */
public class CommandTake extends Handler {
	public void handle(CommandSender sender, String[] arguments) {
		if(arguments.length == 2) {
			if(Bank.hasAccount(arguments[0])) {
				BigDecimal todp = null;
				BankAccount account = Bank.getAccount(arguments[0]);
				// the needed checks
				try{
				todp = new BigDecimal(arguments[1]);
				}catch(Exception e) {
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" [AMOUNT]");
					return;
				}
				if(todp.compareTo(new BigDecimal(0.10)) < 0) {
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorInvalidAm.toString());
					return;
				}
				// and save to account
				if(account.has(todp)) {
					account.subtractBalance(todp);
				}else{
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.toString());
				}
				MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessTake.toString().replace("$name$", arguments[0]).replace("$amount$", iBank.format(todp)));
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
			}
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
}
