package com.iBank.Commands;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.Loan;
import com.iBank.system.MessageManager;

/**
 *  /bank payback (ID) [AMOUNT]
 * @author steffengy
 *
 */
public class CommandPayBack extends Handler {
	public void handle(CommandSender sender, String[] arguments) {
		if(!(sender instanceof Player)) {
			MessageManager.send(sender, Configuration.StringEntry.ErrorNoPlayer.toString());
			return;
		}
		if(arguments.length == 2) {
			//arguments[0] -> int
			int arg = 0;
			try{
				arg = Integer.parseInt(arguments[0]);
			}catch(Exception e) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" [ID]");
				return;
			}
			//arguments[1] -> BigDecimal
			BigDecimal todp = new BigDecimal("0.00");
			try{
				todp = new BigDecimal(arguments[1]);
			}catch(Exception e) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" [AMOUNT]");
				return;
			}
			//try to get this loan
			int i = 0;
			for(Loan loan : Bank.getLoansByAccount(((Player)sender).getName())) {
				if(i == arg) {
					//loan.getAmount() has to be bigger or equal than given (0 or -1)
					if(!(loan.getAmount().compareTo(todp)<=0)) {
						//throw error
						MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString() + "AMOUNT>LOAN");
						return;
					}
					loan.setAmount(loan.getAmount().subtract(todp));
				    iBank.economy.withdrawPlayer(((Player)sender).getName(), todp.doubleValue());
					//<= to prevent MAGIC exceptions
					if(loan.getAmount().compareTo(new BigDecimal("0.00"))<=0) {
						loan.remove();
					}
					break;
				}
				
				i++;
			}
		}else if(arguments.length == 1){
			
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
}
