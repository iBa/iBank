package com.iBank.Commands;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;
import com.iBank.system.MessageManager;
import com.iBank.utils.Mathematics;

/**
 *  /bank withdraw <NAME> <AMOUNT>
 * @author steffengy
 * Can't be run from console
 */
@CommandInfo(
		arguments = { "Name", "Amount" }, 
		help = "", 
		permission = "iBank.access",
		root = "bank", 
		sub = "withdraw"
)
public class CommandWithdraw implements Command {
	public void handle(CommandSender sender, String[] arguments) {
		handle(sender, arguments, false);
	}
	
	public void handle(CommandSender sender, String[] arguments, boolean check) { 	
		if(!(sender instanceof Player)) {
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.toString());
			return;
		}
		if(!check && !iBank.canExecuteCommand(((Player)sender))) {
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
			return;
		}
		
		if(arguments.length == 2) {
			if(Bank.hasAccount(arguments[0])) {
				BankAccount account = Bank.getAccount(arguments[0]);
				if(account.isOwner(((Player)sender).getName()) || account.isUser(((Player)sender).getName())) {
					BigDecimal todp = null;
					if((todp = Mathematics.parseString(arguments[1])) == null) {
						MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" [AMOUNT]");
						return;
					}
					BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeWithdraw.toString(), todp);
					
					if(account.has(todp.add(fee))) {
						if(fee.compareTo(new BigDecimal("0.00"))>0) MessageManager.send(sender, "&g&"+Configuration.StringEntry.PaidFee.toString().replace("$amount$", iBank.format(fee)));
							doWithdraw(sender, todp.add(fee), account);
					}else{
						MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.toString());
					}
				}else{
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				}
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
			}
		}else if(arguments.length == 1) {
			if(Bank.hasAccount(arguments[0])) {
				BankAccount account = Bank.getAccount(arguments[0]);
				if(account.isOwner(((Player)sender).getName()) || account.isUser(((Player)sender).getName())) {
					//Withdraw The MAX
					BigDecimal amount = account.getBalance();
					BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeWithdraw.toString(), amount);
					if(fee.compareTo(new BigDecimal("0.00"))>0) MessageManager.send(sender, "&g&"+Configuration.StringEntry.PaidFee.toString().replace("$amount$", iBank.format(fee)));
					amount = amount.subtract(fee);
					doWithdraw(sender, amount, account);
					account.setBalance(new BigDecimal("0.00"), true);
				}else{
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				}
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
			}
		} else {
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
	public void doWithdraw(CommandSender sender, BigDecimal todp, BankAccount account) {
			account.subtractBalance(todp);
			iBank.economy.depositPlayer(((Player)sender).getName(), todp.doubleValue());
			MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessWithdraw.toString().replace("$name$", account.getName()).replace("$amount$", iBank.format(todp)));
	}
	public String getHelp() {
		return Configuration.StringEntry.WithdrawDescription.getValue();
	}
}