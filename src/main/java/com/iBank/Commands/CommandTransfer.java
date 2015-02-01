package com.ibank.Commands;

import java.math.BigDecimal;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ibank.iBank;
import com.ibank.Event.iBankEvent;
import com.ibank.Event.iEvent;
import com.ibank.system.Bank;
import com.ibank.system.BankAccount;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;

/**
 *  /bank transfer <SRC> <DEST> <AMOUNT>
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "SRC", "DEST", "AMOUNT" },  
		permission = "ibank.access",
		root = "bank", 
		sub = "transfer"
)
public class CommandTransfer extends Command 
{
	@Override
	public void handle(CommandSender sender, String[] arguments) 
	{
		handle(sender, arguments, false);
	}
	
	public void handle(CommandSender sender, String[] arguments, boolean check) 
	{
		boolean console = !(sender instanceof Player);

		if(arguments.length == 3) 
		{
			if(!check && !iBank.canExecuteCommand(((Player)sender))) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
				return;
			}
			BigDecimal money = null;
			try
			{
				money = new BigDecimal(arguments[2]);
			}
			catch(Exception e) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [AMOUNT]");
				return;
			}
			if(money.compareTo(new BigDecimal(0.10)) < 0) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorInvalidAm.getValue());
				return;
			}
			if(!Bank.hasAccount(arguments[0])) 
			{	
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[0]));
				return;
			}
			if(!Bank.hasAccount(arguments[1]))
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", arguments[1]));
				return;
			}
			BankAccount src = Bank.getAccount(arguments[0]);
			BankAccount dest = Bank.getAccount(arguments[1]);
			if(src.getName().equalsIgnoreCase(dest.getName())) dest = src;
			if(!console && !src.isUser(((Player)sender).getName()) && !src.isOwner(((Player)sender).getName())) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				return;
			}
			BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeTransfer.getValue(), money);
			if(!src.has(money.add(fee))) 
			{
				//iBank - call Event
				iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_TRANSFER, new Object[] { arguments[0], arguments[1], money, BigDecimal.ZERO, false } );
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(event.isCancelled()) {
					return;
				}
				//iBank - end
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
				return;
			}
			//iBank - call Event
			iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_TRANSFER, new Object[] { arguments[0], arguments[1], money, fee, false } );
			Bukkit.getServer().getPluginManager().callEvent(event);
			if(event.isCancelled()) return;
			//iBank - end
			src.subtractBalance(money.add(fee));
			dest.addBalance(money);
			send(sender, "&g&"+Configuration.StringEntry.SuccessTransfer.getValue().replace("$name$", arguments[0]).replace("$name2$", arguments[1]).replace("$amount$", iBank.format(money)));
			if(fee.compareTo(BigDecimal.ZERO)>0) send(sender, "&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(fee)));
		}
		else
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	
	public String getHelp()
	{
		return Configuration.StringEntry.TransferDescription.getValue();
	}
}
