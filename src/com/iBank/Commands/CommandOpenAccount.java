package com.iBank.Commands;

import java.math.BigDecimal;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;
import com.iBank.system.Region;

/**
 * [1] /bank open <ACCOUNTNAME>
 * @author steffengy
 * [1] Can't be run from console
 * @todo Add parameter for other owners
 */
@CommandInfo(
		arguments = { "Name" }, 
		permission = "iBank.access", 
		root = "bank", 
		sub = "open"
)
public class CommandOpenAccount extends Command {
	public void handle(CommandSender sender, String[] arguments) { 
		if(arguments.length == 1) {
			if(!(sender instanceof Player)) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.getValue());
				return;
			}
			if(!iBank.canExecuteCommand(((Player)sender))) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
				return;
			}
			String region = iBank.regionAt(((Player)sender).getLocation());
			region = region == null ?  " " : region;
			
			if(!Bank.hasAccount(arguments[0])) {
				// fee stuff
				String fee = Configuration.Entry.FeeCreate.getValue();
				BigDecimal extra = BigDecimal.ZERO;
				if(!fee.contains(";")) {
					extra = iBank.parseFee(fee, new BigDecimal(iBank.economy.getBalance(((Player)sender).getName())));
				}else{
					String[] costs = fee.split(";");
					int account = Bank.getAccountsByOwner(((Player)sender).getName()).size();
				
					if(costs.length > account) {
						extra = iBank.parseFee(costs[account], new BigDecimal(iBank.economy.getBalance(((Player)sender).getName())));
					}else{
						extra = iBank.parseFee(costs[costs.length - 1], new BigDecimal(iBank.economy.getBalance(((Player)sender).getName())));
					}
				}
				List<String> tmp = Bank.getAccountsByOwner(((Player)sender).getName());
				//skip if max is higher/equal to precision
				if(Configuration.Entry.MaxAccountsPerUser.getInteger() != -1 && tmp.size() >= Configuration.Entry.MaxAccountsPerUser.getInteger()) {
					send(sender, "&r&" + Configuration.StringEntry.ErrorMaxAcc.getValue().replace("$max$", Configuration.Entry.MaxAccountsPerUser.getValue()));
					return;
				}
				if(!iBank.economy.has(((Player)sender).getName(), extra.doubleValue())) {
					send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
					return;
				}
				iBank.economy.withdrawPlayer(((Player)sender).getName(), extra.doubleValue());
				Bank.createAccount(arguments[0], ((Player)sender).getName());
				// check for custom percentages
				if(region != " ") {
					Region reg = Bank.getRegion(region);
					if(!reg.onDefault) Bank.getAccount(arguments[0]).setOnPercentage(reg.getOnPercentage(), true);
					if(!reg.offDefault) Bank.getAccount(arguments[0]).setOffPercentage(reg.getOffPercentage(), true);
				}
				send(sender, "&g&"+Configuration.StringEntry.SuccessAddAccount.getValue().replace("$name$", "Account "+arguments[0]+" "));
				if(extra.compareTo(BigDecimal.ZERO)>0) send(sender, "&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(extra)));
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorAlreadyExists.getValue().replace("$name$", "Account "+arguments[0]+" "));
			}
		}else{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	public String getHelp() {
		return Configuration.StringEntry.OpenAccountDescription.getValue();
	}
}
