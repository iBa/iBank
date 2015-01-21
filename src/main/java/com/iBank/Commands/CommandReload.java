package com.ibank.Commands;

import org.bukkit.command.CommandSender;

import com.ibank.iBank;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;

/**
 *   /bank reload - Call reload function in iBank
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { }, 
		permission = "ibank.reload",
		root = "bank", 
		sub = "reload"
)
public class CommandReload extends Command 
{
	public void handle(CommandSender sender, String[] arguments) 
	{
		iBank.mainInstance.reloadConfig();
	}
	
	public String getHelp() 
	{
		return Configuration.StringEntry.ReloadDescription.getValue();
	}
}
