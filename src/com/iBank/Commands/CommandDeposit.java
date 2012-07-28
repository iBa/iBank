package com.iBank.Commands;

import java.math.BigDecimal;

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
 *  /bank deposit <NAME> <AMOUNT>
 * @author steffengy
 * Can't be run from console
 */
@CommandInfo(
		arguments = { "Name", "Amount" }, 
		permission = "iBank.access",
		root = "bank", 
		sub = "deposit"
)
public class CommandDeposit extends Command {
	public void handle(CommandSender sender, String[] arguments) {
		handle(sender, arguments, false);
	}
	public void handle(CommandSender sender, String[] arguments, boolean check) { 	
		if(arguments.length == 2) {
			if(!(sender instanceof Player)) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.getValue());
				return;
			}
			if(!check && !iBank.canExecuteCommand(((Player)sender))) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
				return;
			}
			if(Bank.hasAccount(arguments[0])) {
				BigDecimal todp = null;
				BankAccount account = Bank.getAccount(arguments[0]);
				if(!account.isOwner(((Player)sender).getName()) && !account.isUser(((Player)sender).getName())) {
					send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
					return;
				}
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
				// check if current player has that amount
				//double needed = 0.00;
				BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeDeposit.getValue(), todp);
				if(iBank.economy.has(((Player)sender).getName(), todp.doubleValue() + fee.doubleValue())) {
					//iBank - call Event
					iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_DEPOSIT, new Object[] { arguments[0], todp, fee, true } );
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(event.isCancelled()) {
						return;
					}
					//iBank - end
					iBank.economy.withdrawPlayer(((Player)sender).getName(), todp.doubleValue() + fee.doubleValue());
					account.addBalance(todp);
					send(sender, "&g&"+Configuration.StringEntry.SuccessDeposit.getValue().replace("$name$", arguments[0]).replace("$amount$", iBank.format(todp)));
					if(fee.compareTo(BigDecimal.ZERO)>0) send(sender, "&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(fee)));
				}else{
					//iBank - call Event
					iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_DEPOSIT, new Object[] { arguments[0], todp, fee, false } );
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(event.isCancelled()) {
						return;
					}
					//iBank - end
					send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
					return;
				}
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
			}
			
		}else{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	public String getHelp() {
		return Configuration.StringEntry.DepositDescription.getValue();
	}
}
