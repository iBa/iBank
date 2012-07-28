package com.iBank.Commands;

import java.math.BigDecimal;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.iBank.iBank;
import com.iBank.Event.iBankEvent;
import com.iBank.Event.iEvent;
import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;

/**
 *  /bank take <ACCOUNT> <MONEY> - Take money from account
 *  
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Account", "Money" },  
		permission = "iBank.manage",
		root = "bank", 
		sub = "take"
)
public class CommandTake extends Command {
	public void handle(CommandSender sender, String[] arguments) {
		if(arguments.length == 2) {
			if(Bank.hasAccount(arguments[0])) {
				BigDecimal todp = null;
				BankAccount account = Bank.getAccount(arguments[0]);
				// the needed checks
				try{
				todp = new BigDecimal(arguments[1]);
				}catch(Exception e) {
					send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [AMOUNT]");
					return;
				}
				if(todp.compareTo(new BigDecimal(0.10)) < 0) {
					send(sender, "&r&"+Configuration.StringEntry.ErrorInvalidAm.getValue());
					return;
				}
				// and save to account
				if(account.has(todp)) {
					//iBank - call Event
					iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_TAKE, new Object[] { arguments[0], todp, true} );
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(event.isCancelled()) {
						return;
					}
					//iBank - end
					account.subtractBalance(todp);
				}else{
					//iBank - call Event
					iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_TAKE, new Object[] { arguments[0], todp, false} );
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(event.isCancelled()) {
						return;
					}
					//iBank - end
					send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
				}
				send(sender, "&g&"+Configuration.StringEntry.SuccessTake.getValue().replace("$name$", arguments[0]).replace("$amount$", iBank.format(todp)));
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
			}
		}else{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	public String getHelp() {
		return Configuration.StringEntry.TakeDescription.getValue();
	}
}
