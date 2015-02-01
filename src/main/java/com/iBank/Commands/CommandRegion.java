package com.ibank.Commands;

import static com.ibank.Util.getUniqueId;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ibank.iBank;
import com.ibank.system.Bank;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;
import com.ibank.system.Region;

/**
 *  /bank region <NAME>
 *  /bank region <NAME> online 12  - 12 percentage online
 *  /bank region <NAME> offline 12 - 12 percentage offline 
 *  /bank region <NAME> add <USERNAME> - Add <USERNAME> as Owner
 *  /bank region <NAME> del <USERNAME> - Remove <USERNAME> as Owner
 *  (IF created account in that region on+offline) 
 *  Displays info about a region
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Name" }, 
		permission = "ibank.access",
		root = "bank", 
		sub = "region")
public class CommandRegion extends Command 
{
	@Override
	public void handle(CommandSender sender, String[] arguments) 
	{
		if(arguments.length == 1) 
		{
			//Display info
			if(Bank.hasRegion(arguments[0])) {
				Region tmp = Bank.getRegion(arguments[0]);
				send(sender, "&w&"+Configuration.StringEntry.GeneralInfo.getValue().replace("$type$","Region").replace("$name$", arguments[0]));
				String onlineP = tmp.onDefault ? " Default " : String.valueOf(tmp.getOnPercentage()) + "%";
				String offlinP = tmp.offDefault ? " Default ": String.valueOf(tmp.getOffPercentage()) + "%";
				send(sender, "&w&Online %: &gray&" + onlineP, "");
				send(sender, "&w&Offline %: &gray&" + offlinP, "");
				send(sender, "&w&"+Configuration.StringEntry.GeneralOwners.getValue()+": &gray&" + tmp.getOwners().toString(), "");
				Location loc = tmp.getFirstLocation();
				Location loc2 = tmp.getSecondLocation();
				send(sender, "&w&Location 1:&gray&"+loc.toString(), "");
				send(sender, "&w&Location 2:&gray&"+loc2.toString(), "");
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", "Region "+arguments[0]+" "));
			}
		} 
		else if(arguments.length == 3) 
		{ 
			if(!Bank.hasRegion(arguments[0])) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", "Region "+arguments[0]+" "));
				return;
			}
			if(sender instanceof Player && !iBank.hasPermission(sender, "ibank.regions") && !Bank.getRegion(arguments[0]).getOwners().contains(((Player) sender).getUniqueId()))
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
				return;
			}
			if(arguments[1].equalsIgnoreCase("online") || arguments[1].equalsIgnoreCase("on")) 
			{
				Double percentage;
				try
				{
					percentage = Double.parseDouble(arguments[2]);
				}
				catch(Exception e) 
				{
					send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" "+arguments[2]);
					return;
				}
				Bank.getRegion(arguments[0]).setOnPercentage(percentage, true);
			}
			else if(arguments[1].equalsIgnoreCase("offline") || arguments[1].equalsIgnoreCase("off")) 
			{
				Double percentage;
				try
				{
					percentage = Double.parseDouble(arguments[2]);
				}
				catch(Exception e) 
				{
					send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" "+arguments[2]);
					return;
				}
				Bank.getRegion(arguments[0]).setOffPercentage(percentage, true);
			}
			else if(arguments[1].equalsIgnoreCase("add")) 
				Bank.getRegion(arguments[0]).addOwner(getUniqueId(arguments[2]));
			else if(arguments[1].equalsIgnoreCase("del")) 
				if(Bank.getRegion(arguments[0]).getOwners().contains(getUniqueId(arguments[2]))) Bank.getRegion(arguments[0]).removeOwner(getUniqueId(arguments[2]));
			else
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" "+arguments[1]);
				return;
			}
			send(sender, "&g&"+Configuration.StringEntry.SuccessRegion.getValue().replace("$name$", arguments[0]));
		} 
		else 
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
	}
	
	public String getHelp() 
	{
		return Configuration.StringEntry.RegionDescription.getValue();
	}
}
