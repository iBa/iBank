package com.iBank.Commands;

import org.bukkit.command.CommandSender;

import com.iBank.iBank;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;

/**
 *   /bank reload - Call reload function in iBank
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { }, 
		permission = "iBank.reload",
		root = "bank", 
		sub = "reload"
)
public class CommandReload extends Command {
	public void handle(CommandSender sender, String[] arguments) {
		iBank.mainInstance.reloadConfig();
	}
	public String getHelp() {
		return Configuration.StringEntry.ReloadDescription.getValue();
	}
}
