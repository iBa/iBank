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
 *  /bank withdraw <NAME> <AMOUNT>
 * @author steffengy
 *
 */
public class CommandWithdraw extends Handler {
	public void handle(CommandSender sender, String[] arguments) { 	
		if(arguments.length == 2) {
			if(!(sender instanceof Player)) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.toString());
				return;
			}
			if(iBank.GetRegionAt(((Player)sender).getLocation()) != "") {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
				return;
			}
			if(Bank.hasAccount(arguments[0])) {
				BankAccount account = Bank.getAccount(arguments[0]);
				if(account.isOwner(((Player)sender).getName()) || account.isUser(((Player)sender).getName())) {
					BigDecimal todp = null;
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
					if(account.has(todp)) {
						account.subtractBalance(todp);
						iBank.economy.depositPlayer(((Player)sender).getName(), todp.doubleValue());
						MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessWithdraw.toString().replace("$name$", arguments[0]).replace("$amount$", iBank.format(todp)));
					}else{
						MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.toString());
					}
				}else{
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				}
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
			}
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
}
