package com.iBank.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;
import com.iBank.utils.StringUtils;

/**
 *  /bank LIST (NAME)
 *  If name is given, all accounts of NAME will be shown
 *  else all accounts will be shown
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "(Name)" }, 
		permission = "iBank.list", 
		root = "bank", 
		sub = "list"
)
public class CommandList extends Command {
	
	public void handle(CommandSender sender, String[] arguments) {
		if((sender instanceof Player) && !iBank.canExecuteCommand(((Player)sender))) {
			send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
			return;
		}
		// Show list of accounts#
		List<String> owner;
		List<String> user;
		if(arguments.length > 0 && arguments[0] != null) {
			owner = Bank.getAccountsByOwner(arguments[0]);
			user = Bank.getAccountsByUser(arguments[0]);
			send(sender, "&blue&Owner &y&User");
		}else{
			owner = Bank.getAccounts();
			user = new ArrayList<String>();
		}
		
		if(owner.size() == 0 && user.size() == 0) {
			send(sender, "&r&" + Configuration.StringEntry.GeneralNoAccounts.getValue());
			return;
		}
		owner = owner == null ? new ArrayList<String>() : owner;
		user = user == null ? new ArrayList<String>() : user;
		send(sender, "&blue&"+StringUtils.join(owner, "&w&,&blue&"), "");
		send(sender, "&y&"+StringUtils.join(user, "&w&,&y&"), "");
	}
	public String getHelp() {
		return Configuration.StringEntry.ListDescription.getValue();
	}
}
