package com.ibank.Commands;

import com.ibank.iBank;
import com.ibank.system.Bank;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;
import com.ibank.system.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;

/**
 * [1] /bank open <ACCOUNTNAME>
 * @author steffengy
 * [1] Can't be run from console
 */

//todo Add parameter for other owners
@CommandInfo(
		arguments = { "Name" }, 
		permission = "ibank.access",
		root = "bank", 
		sub = "open"
)
public class CommandOpenAccount extends Command 
{
	public void handle(CommandSender sender, String[] arguments) 
	{ 
		if(arguments.length == 1) 
		{
			if(!(sender instanceof Player)) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.getValue());
				return;
			}
			if(!iBank.canExecuteCommand(((Player)sender))) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
				return;
			}
			String region = iBank.regionAt(((Player)sender).getLocation());
			region = region == null ?  " " : region;
			
			if(Bank.hasAccount(arguments[0])) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorAlreadyExists.getValue().replace("$name$", "Account "+arguments[0]+" "));
				return;
			}
			// fee stuff
			String fee = Configuration.Entry.FeeCreate.getValue();
			BigDecimal extra;
			if(!fee.contains(";")) 
				extra = iBank.parseFee(fee, new BigDecimal(iBank.economy.getBalance((Player)sender)));
			else
			{
				String[] costs = fee.split(";");
				int account = Bank.getAccountsByOwner(((Player) sender).getUniqueId()).size();
			
				if(costs.length > account) 
					extra = iBank.parseFee(costs[account], new BigDecimal(iBank.economy.getBalance((Player)sender)));
				else
					extra = iBank.parseFee(costs[costs.length - 1], new BigDecimal(iBank.economy.getBalance((Player)sender)));
			}
			List<String> tmp = Bank.getAccountsByOwner(((Player) sender).getUniqueId());
			//skip if max is higher/equal to precision
			if(Configuration.Entry.MaxAccountsPerUser.getInteger() != -1 && tmp.size() >= Configuration.Entry.MaxAccountsPerUser.getInteger()) 
			{
				send(sender, "&r&" + Configuration.StringEntry.ErrorMaxAcc.getValue().replace("$max$", Configuration.Entry.MaxAccountsPerUser.getValue()));
				return;
			}
			if(!iBank.economy.has((Player)sender, extra.doubleValue()))
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
				return;
			}
			iBank.economy.withdrawPlayer((Player)sender, extra.doubleValue());
			Bank.createAccount(arguments[0], ((Player) sender).getUniqueId());
			// check for custom percentages
			if(!region.equals(" "))
			{
				Region reg = Bank.getRegion(region);
				if(!reg.onDefault) Bank.getAccount(arguments[0]).setOnPercentage(reg.getOnPercentage(), true);
				if(!reg.offDefault) Bank.getAccount(arguments[0]).setOffPercentage(reg.getOffPercentage(), true);
			}
			send(sender, "&g&"+Configuration.StringEntry.SuccessAddAccount.getValue().replace("$name$", "Account "+arguments[0]+" "));
			if(extra.compareTo(BigDecimal.ZERO)>0) send(sender, "&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(extra)));
		
		}
		else
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	
	public String getHelp() 
	{
		return Configuration.StringEntry.OpenAccountDescription.getValue();
	}
}
