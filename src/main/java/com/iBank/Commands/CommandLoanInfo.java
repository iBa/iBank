package com.ibank.Commands;

import static com.ibank.Util.getUniqueId;

import com.ibank.iBank;
import com.ibank.system.Bank;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;
import com.ibank.system.Loan;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

/**
 *  /bank loaninfo <PLAYER> - loan info of others
 *  /bank loaninfo Display your loan info
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Player" }, 
		permission = "ibank.access",
		root = "bank", 
		sub = "loaninfo"
)
public class CommandLoanInfo extends Command 
{
	@Override
	public void handle(CommandSender sender, String[] arguments) 
	{
		handle(sender, arguments, false);
	}
	
	public void handle(CommandSender sender, String[] arguments, boolean check) 
	{
		if(!check && !iBank.canExecuteCommand(((Player)sender))) 
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
			return;
		}
		if(arguments.length == 0) 
		{
			if(!(sender instanceof Player)) 
			{
				send(sender, Configuration.StringEntry.ErrorNoPlayer.getValue());
				return;
			}
			showLoanInfo(((Player) sender).getUniqueId(), null, sender, 0);
		}
		else if(arguments.length == 1 || arguments.length == 2) 
		{
			char tmp;
			if((tmp = arguments[0].charAt(0)) >= '0' && tmp <= '9') 
			{
				showLoanInfo(((Player)sender).getUniqueId(), null, sender, Integer.parseInt(arguments[0]));
				return;
			}
			boolean allowed = (!(sender instanceof Player)) || iBank.hasPermission(sender, "ibank.loaninfo");
			if(!allowed)
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				return;
			}
			int site = arguments.length > 1 ? Integer.parseInt(arguments[1]) +1 : 0;
			showLoanInfo(getUniqueId(arguments[0]), arguments[0], sender, site);
		}
		else
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	/**
	 * Shows the loan info to destination
	 * @param user The to-info user
	 * @param raw Raw name, may be null
	 * @param destination The destination user
	 */
	public void showLoanInfo(UUID user, String raw, CommandSender destination,int site)
	{
		List<Loan> allLoans;
		if(raw == null) {
			raw = Bukkit.getOfflinePlayer(user).getName();
		}
		if(raw.equalsIgnoreCase("all!"))
		{ 
			allLoans = Bank.getLoans();
		}
		else allLoans = Bank.getLoansByAccount(user);
		int sites = (int)Math.ceil(((double)allLoans.size() / 10));
		site = site > sites ? sites : site;
		
		send(destination, "&y&" + Configuration.StringEntry.GeneralInfo.getValue().replace("$type$", "Player").replace("$name$", raw));
		int i = 0;
		for(Loan loan : allLoans) 
		{
			if(i < (site * 10)) { i++; continue; }
			if(i > (site * 10)) break;
			String[] lang = new String[] { 
					Configuration.StringEntry.GeneralUntil.getValue(), Configuration.StringEntry.GeneralPer.getValue(), Configuration.StringEntry.GeneralMin.getValue() 
			};
			String date = new SimpleDateFormat("dd.MMM.yy HH:mm:ss").format(new Date(System.currentTimeMillis()+(loan.getLeftTime() * 1000)));
			String minutes = String.valueOf(loan.getInterval());
			send(destination, (i+1)+"."+" id:("+loan.getId()+") "+iBank.format(loan.getAmount())+" "+lang[0]+" "+date+" "+String.valueOf(loan.getInterest()) + "% " + lang[1] + " " + minutes+ " " +lang[2], "");
			++i;
		}
	}
	
	public String getHelp() 
	{
		return Configuration.StringEntry.LoanInfoDescription.getValue();
	}
}
