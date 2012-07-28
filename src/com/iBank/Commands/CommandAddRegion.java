package com.iBank.Commands;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;
import com.iBank.Listeners.iBankListener;

@CommandInfo(
		arguments = { "Name" }, 
		permission = "iBank.access", 
		root = "bank", 
		sub = "addregion"
)

public class CommandAddRegion extends Command {

	/**
	 * Adds an region
	 *  /bank addregion REGIONNAME
	 *  Can't be run from console
	 */
	@Override
	public void handle(CommandSender sender, String[] arguments) {
		if(!(sender instanceof Player)) {
			send(sender, Configuration.StringEntry.ErrorNoPlayer.getValue());
			return;
		}
		
		if(arguments.length==1) {
			Entry<Location, Location> raw = ((iBankListener)iBank.Listener).LastMarkedPoint.get(sender.getName());
			if(raw == null || raw.getKey()==null || raw.getValue()==null){
				send(sender, "&r&"+Configuration.StringEntry.ErrorRegionSelect.getValue());
				return;
			}
			if(!Bank.hasRegion(arguments[0])) {
				if(!iBank.hasPermission(sender, "iBank.regions") && !Configuration.Entry.AllowBuyRegion.getBoolean()) {
					send(sender, "&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
					return;
				}
				if(Configuration.Entry.AllowBuyRegion.getBoolean() && !iBank.hasPermission(sender, "iBank.regions")) {
					if(!iBank.economy.has(((Player)sender).getName(), Configuration.Entry.RegionsPrice.getDouble())) {
						send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
						return;
					}else{
						//cashout
						iBank.economy.withdrawPlayer(((Player)sender).getName(), Configuration.Entry.RegionsPrice.getDouble());
						send(sender, "[iBank] Balance - " + String.valueOf(Configuration.Entry.RegionsPrice.getBoolean()));
					}
				}
				Bank.createRegion(arguments[0], raw.getKey(), raw.getValue());
				send(sender, "&g&"+Configuration.StringEntry.SuccessAddRegion.getValue().replace("$name$", arguments[0]));
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorAlreadyExists.getValue().replace("$name$", "Region "+arguments[0]+" "));
				return;
			}
		}else{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	public String getHelp() {
		return Configuration.StringEntry.AddRegionDescription.getValue();
	}
}
