package com.iBank.Commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;
import com.iBank.system.Region;

/**
 *  /bank region <NAME>
 *  /bank region <NAME> online 12  - 12 percentage online
 *  /bank region <NAME> offline 12 - 12 percentage offline 
 *  (IF created account in that region on+offline) 
 *  Displays info about a region
 * @author steffengy
 *
 */
public class CommandRegion extends Handler {
	@Override
	public void handle(CommandSender sender, String[] arguments) {
		if(arguments.length == 1) {
			//Display info
			if(Bank.hasRegion(arguments[0])) {
				Region tmp = Bank.getRegion(arguments[0]);
				MessageManager.send(sender, "&w&"+Configuration.StringEntry.GeneralInfo.toString().replace("$type$","Region").replace("$name$", arguments[0]));
				String onlineP = tmp.onDefault ? " Default " : String.valueOf(tmp.getOnPercentage()) + "%";
				String offlinP = tmp.offDefault ? " Default ": String.valueOf(tmp.getOffPercentage()) + "%";
				MessageManager.send(sender, "&w&Online %: &gray&" + onlineP, "");
				MessageManager.send(sender, "&w&Offline %: &gray&" + offlinP, "");
				Location loc = tmp.getFirstLocation();
				Location loc2 = tmp.getSecondLocation();
				MessageManager.send(sender, "&w&Location 1:&gray&"+loc.toString(), "");
				MessageManager.send(sender, "&w&Location 2:&gray&"+loc2.toString(), "");
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", "Region "+arguments[0]+" "));
				return;
			}
		}
	}
}
