package com.iBank.system;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * All what has todo with parsing a command
 * @author steffengy
 *
 */
public class CommandHandler {

	/**
	 * Parses a command
	 * @param message The message
	 * @param player The player
	 */
	public static boolean parse(String rootCMD, String[] args, CommandSender sender) {
		if(Commands.rootIsLinked(rootCMD)) {
			//Get subcommand 
			String subCMD = "";
			if(args.length > 0) subCMD = args[0]==null ? "" : args[0].toLowerCase();
			 //remove first arg from args
			List<String> argtmp = new ArrayList<String>(Arrays.asList(args));
			if(argtmp.size()>0) argtmp.remove(0);
			args = argtmp.toArray(new String[0]);
			if(!Commands.subIsLinked(rootCMD, subCMD)) return false;
			if(!(sender instanceof Player)) {
				Commands.getHandler(rootCMD, subCMD).handle(sender, args);
			}else{
				if(Commands.isCallable((Player)sender , rootCMD, subCMD))
			       Commands.getHandler(rootCMD, subCMD).handle(sender, args);
			    else
			       MessageManager.send(sender, "&r& Permission denied!");
			}
			return true;
		}else{
			return false;
		}
	}

}
