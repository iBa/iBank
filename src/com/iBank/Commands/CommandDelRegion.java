package com.iBank.Commands;

import org.bukkit.command.CommandSender;

import com.iBank.system.Bank;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;

/**
 *  /bank delregion <NAME>
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Name" },  
		permission = "iBank.regions", 
		root = "bank", 
		sub = "delregion"
)
public class CommandDelRegion extends Command {
	@Override
	public void handle(CommandSender sender, String[] arguments) {
		if(arguments.length == 1) {
			//delete region
			if(Bank.hasRegion(arguments[0])) {
				Bank.removeRegion(arguments[0]);
				send(sender, "&g&"+Configuration.StringEntry.SuccessDelRegion.getValue().replace("$name$", arguments[0]));
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", "Region "+arguments[0]+" "));
			}
			return;
		}
		send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
	}
	public String getHelp() {
		return Configuration.StringEntry.DelRegionDescription.getValue();
	}
}
