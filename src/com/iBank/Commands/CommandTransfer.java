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
 *  /bank transfer <SRC> <DEST> <AMOUNT>
 * @author steffengy
 *
 */
public class CommandTransfer extends Handler {
	public void handle(CommandSender sender, String[] arguments) {
		boolean console = false;
		if(!(sender instanceof Player)) console = true;
		if(arguments.length == 3) {
			BigDecimal money = null;
			try{
				money = new BigDecimal(arguments[1]);
			}catch(Exception e) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" [AMOUNT]");
				return;
			}
			if(money.compareTo(new BigDecimal(0.10)) < 0) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorInvalidAm.toString());
				return;
			}
			if(!Bank.hasAccount(arguments[0])) {	
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
				return;
			}
			if(!Bank.hasAccount(arguments[1])) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[1]));
				return;
			}
			BankAccount src = Bank.getAccount(arguments[0]);
			BankAccount dest = Bank.getAccount(arguments[1]);
			if(!console && (!src.isUser(((Player)sender).getName()) || !src.isOwner(((Player)sender).getName()))) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				return;
			}
			BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeTransfer.toString(), money);
			if(!src.has(money.add(fee))) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.toString());
				return;
			}
			
			src.subtractBalance(money.add(fee));
			dest.addBalance(money);
			MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessTransfer.toString().replace("$name$", arguments[0]).replace("$name2$", arguments[1]).replace("$amount$", iBank.format(money)));
			if(fee.compareTo(new BigDecimal("0.00"))>0) MessageManager.send(sender, "&g&"+Configuration.StringEntry.PaidFee.toString().replace("$amount$", iBank.format(fee)));
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
}
