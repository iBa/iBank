package com.iBank.Commands;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;

/**
 *  /bank deposit <NAME> <AMOUNT>
 * @author steffengy
 * Can't be run from console
 */
public class CommandDeposit extends Handler {
	public void handle(CommandSender sender, String[] arguments) {
		handle(sender, arguments, false);
	}
	public void handle(CommandSender sender, String[] arguments, boolean check) { 	
		if(arguments.length == 2) {
			if(!(sender instanceof Player)) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.toString());
				return;
			}
			if(!check && !iBank.canExecuteCommand(((Player)sender))) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
				return;
			}
			if(Bank.hasAccount(arguments[0])) {
				BigDecimal todp = null;
				BankAccount account = Bank.getAccount(arguments[0]);
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
				// check if current player has that amount
				//double needed = 0.00;
				BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeDeposit.toString(), todp);
				if(iBank.economy.has(((Player)sender).getName(), todp.doubleValue() + fee.doubleValue())) {
					iBank.economy.withdrawPlayer(((Player)sender).getName(), todp.doubleValue() + fee.doubleValue());
					account.addBalance(todp);
					MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessDeposit.toString().replace("$name$", arguments[0]).replace("$amount$", iBank.format(todp)));
					if(fee.compareTo(new BigDecimal("0.00"))>0) MessageManager.send(sender, "&g&"+Configuration.StringEntry.PaidFee.toString().replace("$amount$", iBank.format(fee)));
				}else{
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.toString());
					return;
				}
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
			}
			
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
}
