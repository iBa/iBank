package com.iBank.Commands;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;
import com.iBank.system.MessageManager;

/**
 *  /bank give <ACCOUNT> <AMOUNT>
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Account", "Amount" }, 
		help = "", 
		permission = "iBank.manage",
		root = "bank", 
		sub = "give"
)
public class CommandGive implements Command {
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
				account.addBalance(todp);
				MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessGive.toString().replace("$name$", arguments[0]).replace("$amount$", iBank.format(todp)));
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
			}
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
}
