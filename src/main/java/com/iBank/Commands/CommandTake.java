package com.ibank.Commands;

import com.ibank.Event.iBankEvent;
import com.ibank.Event.iEvent;
import com.ibank.iBank;
import com.ibank.system.Bank;
import com.ibank.system.BankAccount;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;

/**
 *  /bank take <ACCOUNT> <MONEY> - Take money from account
 *  
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Account", "Money" },  
		permission = "ibank.manage",
		root = "bank", 
		sub = "take"
)
public class CommandTake extends Command {
	@Override
	public void handle(CommandSender sender, String[] arguments) 
	{
		if(arguments.length == 2) 
		{
			if(!Bank.hasAccount(arguments[0])) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
				return;
			}
			BigDecimal todp;
			BankAccount account = Bank.getAccount(arguments[0]);
			// the needed checks
			try
			{
				todp = new BigDecimal(arguments[1]);
			}
			catch(Exception e) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [AMOUNT]");
				return;
			}
			if(todp.compareTo(new BigDecimal(0.10)) < 0) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorInvalidAm.getValue());
				return;
			}
			// and save to account
			if(account.has(todp)) 
			{
				//iBank - call Event
				iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_TAKE, new Object[] { arguments[0], todp, true} );
				Bukkit.getPluginManager().callEvent(event);
				if(event.isCancelled()) return;
				//iBank - end
				account.subtractBalance(todp);
			}
			else
			{
				//iBank - call Event
				iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_TAKE, new Object[] { arguments[0], todp, false} );
				Bukkit.getPluginManager().callEvent(event);
				if(event.isCancelled()) {
					return;
				}
				//iBank - end
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
			}
			send(sender, "&g&"+Configuration.StringEntry.SuccessTake.getValue().replace("$name$", arguments[0]).replace("$amount$", iBank.format(todp)));
		}
		else
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	
	public String getHelp() 
	{
		return Configuration.StringEntry.TakeDescription.getValue();
	}
}
