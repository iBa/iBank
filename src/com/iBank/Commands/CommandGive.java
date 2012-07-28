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
 *  /bank give <ACCOUNT> <AMOUNT>
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Account", "Amount" }, 
		permission = "iBank.manage",
		root = "bank", 
		sub = "give"
)
public class CommandGive extends Command {
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
				//iBank - call Event
				iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_GIVE, new Object[] { arguments[0], todp} );
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(event.isCancelled()) {
					return;
				}
				//iBank - end
				// and save to account
				account.addBalance(todp);
				send(sender, "&g&"+Configuration.StringEntry.SuccessGive.getValue().replace("$name$", arguments[0]).replace("$amount$", iBank.format(todp)));
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
			}
		}else{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	public String getHelp() {
		return Configuration.StringEntry.GiveDescription.getValue();
	}
}
