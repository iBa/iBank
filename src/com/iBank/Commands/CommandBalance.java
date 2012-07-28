package com.iBank.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.Event.iBankEvent;
import com.iBank.Event.iEvent;
import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;

/**
 *  /bank balance [NAME]
 * @author steffengy
 * 
 */
@CommandInfo(
		arguments = { "Name" }, 
		permission = "iBank.access",
		root = "bank", 
		sub = "balance"
)
public class CommandBalance extends Command {
	public void handle(CommandSender sender, String[] arguments) {
		handle(sender, arguments, false);
	}
	public void handle(CommandSender sender, String[] arguments, boolean check) {
	boolean console = false;
	if(!(sender instanceof Player)) console = true;
	if(arguments.length == 1) {
		// has account
		if(Bank.hasAccount(arguments[0])) {
			//iBank - call Event
			iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_BALANCE, new Object[] { arguments[0], false} );
			Bukkit.getServer().getPluginManager().callEvent(event);
			if(event.isCancelled()) {
				return;
			}
			//iBank - end
			BankAccount acc = Bank.getAccount(arguments[0]);
			if(console || (acc.isOwner(((Player)sender).getName()) || acc.isUser(((Player)sender).getName())) || iBank.hasPermission(sender, "iBank.balance")) {
				String formattedBalance = iBank.economy.format(acc.getBalance().doubleValue());
				if(!console && !check) {
					if(!iBank.canExecuteCommand(((Player)sender))) {
						//iBank - call Event
						event = new iBankEvent(iEvent.Types.ACCOUNT_BALANCE, new Object[] { arguments[0], true} );
						Bukkit.getServer().getPluginManager().callEvent(event);
						if(event.isCancelled()) {
							return;
						}
						//iBank - end
						send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
						return;
					}
				}
				send(sender, "&dg&"+Configuration.StringEntry.BalanceShort.getValue()+" &gray&"+arguments[0]+" &w&: "+formattedBalance);
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
			}
		}else{
			send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
		}
	}
   }
	public String getHelp() {
		return Configuration.StringEntry.BalanceDescription.getValue();
	}
}
