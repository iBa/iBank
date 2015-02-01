package com.ibank.Commands;

import com.ibank.iBank;
import com.ibank.system.Bank;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;
import com.ibank.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 *  /bank - Shows the accounts of the executor
 * @author steffengy
 * Can't be run from console
 */
@CommandInfo(
		arguments = { "" }, 
		permission = "ibank.access",
		root = "bank", 
		sub = ""
)
public class BankRootCommand extends Command 
{

	/**
	 *  Shows if in bank region if enabled
	 *  Shows a list of all accounts the player owns and has access to
	 */
	@Override
	public void handle(CommandSender sender, String[] arguments) 
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
		// Show list of accounts
		List<String> ownerAccounts = Bank.getAccountsByOwner(((Player) sender).getUniqueId());
		List<String> userAccounts = Bank.getAccountsByUser(((Player) sender).getUniqueId());
		if(ownerAccounts.size() == 0 && userAccounts.size() == 0)
		{
			send(sender, "&r&" + Configuration.StringEntry.GeneralNoAccounts.getValue());
			return;
		}
		send(sender, "&blue&Owner &y&User");
        userAccounts = userAccounts == null ? new ArrayList<String>() : userAccounts;
		send(sender, "&blue&"+StringUtils.join(ownerAccounts, "&w&,&blue&"), "");
		send(sender, "&y&"+StringUtils.join(userAccounts, "&w&,&y&"), "");
	}
	
	public String getHelp() {
		return Configuration.StringEntry.BankDescription.getValue();
	}
}
