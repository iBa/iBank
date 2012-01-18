package com.iBank.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;
import com.iBank.utils.StringUtils;

/**
 *  /bank - Shows the balance
 * @author steffengy
 *
 */
public class BankRootCommand extends Handler{

	/**
	 *  Shows if in bank region if enabled
	 *  Shows a list of all accounts the player owns and has access to
	 */
	@Override
	public void handle(CommandSender sender, String[] arguments) {
		if(!(sender instanceof Player)) {
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.toString());
			return;
		}
		if(iBank.GetRegionAt(((Player)sender).getLocation()) != "") {
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
			return;
		}
		// Show list of accounts
		List<String> owner = Bank.getAccountsByOwner(((Player)sender).getName());
		List<String> user = Bank.getAccountsByUser(((Player)sender).getName());
		if(owner == null && user == null) {
			MessageManager.send(sender, "&r&" + Configuration.StringEntry.GeneralNoAccounts.toString());
			return;
		}
		MessageManager.send(sender, "&blue&Owner &y&User");
		owner = owner == null ? new ArrayList<String>() : owner;
		user = user == null ? new ArrayList<String>() : user;
		MessageManager.send(sender, "&blue&"+StringUtils.join(owner, "&w&,&blue&"), "");
		MessageManager.send(sender, "&y&"+StringUtils.join(user, "&w&,&y&"), "");
	}
	
}
