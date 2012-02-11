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
import com.iBank.system.MessageManager;

/**
 *  /bank transfer <SRC> <DEST> <AMOUNT>
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "SRC", "DEST", "AMOUNT" },  
		permission = "iBank.access",
		root = "bank", 
		sub = "transfer"
)
public class CommandTransfer implements Command {
	public void handle(CommandSender sender, String[] arguments) {
		handle(sender, arguments, false);
	}
	public void handle(CommandSender sender, String[] arguments, boolean check) {
		boolean console = false;
		if(!(sender instanceof Player)) console = true;
		if(arguments.length == 3) {
			if(!check && !iBank.canExecuteCommand(((Player)sender))) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
				return;
			}
			BigDecimal money = null;
			try{
				money = new BigDecimal(arguments[2]);
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
			if(!console && !src.isUser(((Player)sender).getName()) && !src.isOwner(((Player)sender).getName())) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				return;
			}
			BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeTransfer.toString(), money);
			if(!src.has(money.add(fee))) {
				//iBank - call Event
				iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_TRANSFER, new Object[] { arguments[0], arguments[1], money, BigDecimal.ZERO, false } );
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(event.isCancelled()) {
					return;
				}
				//iBank - end
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.toString());
				return;
			}
			//iBank - call Event
			iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_TRANSFER, new Object[] { arguments[0], arguments[1], money, fee, false } );
			Bukkit.getServer().getPluginManager().callEvent(event);
			if(event.isCancelled()) {
				return;
			}
			//iBank - end
			src.subtractBalance(money.add(fee));
			dest.addBalance(money);
			MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessTransfer.toString().replace("$name$", arguments[0]).replace("$name2$", arguments[1]).replace("$amount$", iBank.format(money)));
			if(fee.compareTo(new BigDecimal("0.00"))>0) MessageManager.send(sender, "&g&"+Configuration.StringEntry.PaidFee.toString().replace("$amount$", iBank.format(fee)));
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
	public String getHelp() {
		return Configuration.StringEntry.TransferDescription.getValue();
	}
}
