package com.iBank.Commands;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.Listeners.playerListener;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;

public class CommandAddRegion extends Handler {

	/**
	 * Adds an region
	 *  /bank addregion REGIONNAME
	 *  
	 */
	@Override
	public void handle(CommandSender sender, String[] arguments) {
		if(!(sender instanceof Player)) {
			MessageManager.send(sender, Configuration.StringEntry.ErrorNoPlayer.toString());
			return;
		}
		
		if(arguments.length==1) {
			Entry<Location, Location> raw = ((playerListener)main.playerListener).LastMarkedPoint.get(sender.getName());
			if(raw == null || raw.getKey()==null||raw.getValue()==null){
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorRegionSelect.toString());
				return;
			}
			if(!Bank.hasRegion(arguments[0])) {
				Bank.createRegion(arguments[0], raw.getKey(), raw.getValue());
				MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessAddRegion.toString().replace("$name$", arguments[0]));
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorAlreadyExists.toString().replace("$name$", "Region "+arguments[0]+" "));
				return;
			}
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}

}
