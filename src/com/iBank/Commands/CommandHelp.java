package com.iBank.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Commands;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;

/**
 * /bank or /bank help
 * @author steffengy
 *
 */
public class CommandHelp extends Handler {
	protected String root = "";
	/**
	 * Constructor
	 * @param root The root name of the command
	 */
	public CommandHelp(String root) {
		this.root = root;
	}
	
	@Override
	public void handle(CommandSender sender, String[] arguments) {
		//Display possible help for this command
		if(!(sender instanceof Player)) {
			MessageManager.send(sender, "iBank "+iBank.description.getVersion()+" ("+iBank.CodeName+")");
			String args = "";
			for(String name : Commands.getCommands("bank"))
			{
					args = Commands.getHelpArgs(root, name) != null ? Commands.getHelpArgs(root, name) : "";
					MessageManager.send(sender, " /"+root+" "+name+" &gray&"+args+" &gold&-&y& "+Commands.getHelp(root, name));
			}
			return;
		}
		
		int sites = 1 + (int) Math.ceil(Commands.getCommands("bank").length / 16);
		int curSite = 0;
		try{
			curSite = arguments.length == 0 ? 0 : Integer.parseInt(arguments[0]) -1;
		}catch(Exception e) { }
		MessageManager.send(sender, "iBank "+iBank.description.getVersion()+" ("+iBank.CodeName+") ("+(curSite+1)+"/"+sites+")", "");
		String args = "";
		int counter = 0;
		//from = site * 16 
		//to = site * 16 + 15
		for(String name : Commands.getCommands("bank"))
		{
			if(Commands.isCallable((Player)sender, root , name))
			{
				if((curSite * 16) > counter) { counter++; continue; }
				if(curSite * 16 + 15 < counter) break;
				
				args = Commands.getHelpArgs(root, name) != null ? Commands.getHelpArgs(root, name) : "";
				MessageManager.send(sender, " /"+root+" "+name+" &gray&"+args+" &gold&-&y& "+Commands.getHelp(root, name), "");
				counter++; 
			}
		}
		
	}
}
