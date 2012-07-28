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
import com.iBank.utils.Mathematics;

/**
 *  /bank withdraw <NAME> <AMOUNT>
 * @author steffengy
 * Can't be run from console
 */
@CommandInfo(
		arguments = { "Name", "Amount" }, 
		permission = "iBank.access",
		root = "bank", 
		sub = "withdraw"
)
public class CommandWithdraw extends Command {
	public void handle(CommandSender sender, String[] arguments) {
		handle(sender, arguments, false);
	}
	
	public void handle(CommandSender sender, String[] arguments, boolean check) { 	
		if(!(sender instanceof Player)) {
			send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.getValue());
			return;
		}
		if(!check && !iBank.canExecuteCommand(((Player)sender))) {
			send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
			return;
		}
		
		if(arguments.length == 2) {
			if(Bank.hasAccount(arguments[0])) {
				BankAccount account = Bank.getAccount(arguments[0]);
				if(account.isOwner(((Player)sender).getName()) || account.isUser(((Player)sender).getName())) {
					BigDecimal todp = null;
					if((todp = Mathematics.parseString(arguments[1])) == null) {
						send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [AMOUNT]");
						return;
					}
					BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeWithdraw.getValue(), todp);
					
					if(account.has(todp.add(fee))) {
						if(fee.compareTo(BigDecimal.ZERO)>0) send(sender, "&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(fee)));
							//iBank - call Event
							iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_WITHDRAW, new Object[] { arguments[0], todp, fee, true } );
							Bukkit.getServer().getPluginManager().callEvent(event);
							if(event.isCancelled()) {
								return;
							}
							//iBank - end
							doWithdraw(sender, todp.add(fee), account);
					}else{
						send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
					}
				}else{
					send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				}
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
			}
		}else if(arguments.length == 1) {
			if(Bank.hasAccount(arguments[0])) {
				BankAccount account = Bank.getAccount(arguments[0]);
				if(account.isOwner(((Player)sender).getName()) || account.isUser(((Player)sender).getName())) {
					//Withdraw The MAX
					BigDecimal amount = account.getBalance();
					BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeWithdraw.getValue(), amount);
					if(fee.compareTo(BigDecimal.ZERO)>0) send(sender, "&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(fee)));
					amount = amount.subtract(fee);
					//iBank - call Event
					iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_WITHDRAW, new Object[] { arguments[0], amount, fee, true } );
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(event.isCancelled()) {
						return;
					}
					//iBank - end
					doWithdraw(sender, amount, account);
					account.setBalance(BigDecimal.ZERO, true);
				}else{
					send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				}
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
			}
		} else {
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	public void doWithdraw(CommandSender sender, BigDecimal todp, BankAccount account) {
			account.subtractBalance(todp);
			iBank.economy.depositPlayer(((Player)sender).getName(), todp.doubleValue());
			send(sender, "&g&"+Configuration.StringEntry.SuccessWithdraw.getValue().replace("$name$", account.getName()).replace("$amount$", iBank.format(todp)));
	}
	public String getHelp() {
		return Configuration.StringEntry.WithdrawDescription.getValue();
	}
}