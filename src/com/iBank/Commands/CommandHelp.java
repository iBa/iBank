package com.iBank.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Command;
import com.iBank.system.CommandHandler;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;

/**
 * /bank or /bank help
 * @author steffengy
 *
 */
@CommandInfo(arguments = { "" }, 
  permission = "iBank.access", 
  root = "bank", 
  sub = "help"
)
public class CommandHelp extends Command {
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
			send(sender, "iBank "+iBank.description.getVersion());
			String args = "";
			for(String name : CommandHandler.getCommands("bank"))
			{
					args = CommandHandler.getArgInfo(root, name);
					send(sender, " /"+root+" "+name+" &gray&"+args+" &gold&-&y& "+CommandHandler.getHelp(root, name));
			}
			return;
		}
		
		int sites = 1 + (int) Math.ceil(CommandHandler.getCommands("bank").size() / 9);
		int curSite = 0;
		try{
			curSite = arguments.length == 0 ? 0 : Integer.parseInt(arguments[0]) -1;
		}catch(Exception e) { }
		send(sender, "iBank "+iBank.description.getVersion()+" ("+(curSite+1)+"/"+sites+")", "");
		String args = "";
		int counter = 0;
		//from = site * 12 
		//to = site * 12 + 12
		for(String name : CommandHandler.getCommands("bank"))
		{
			if(CommandHandler.isCallable((Player)sender, root , name))
			{
				if((curSite * 12) > counter) { counter++; continue; }
				if(curSite * 12 + 12 < counter) break;
				
				args = CommandHandler.getArgInfo(root, name) != null ? CommandHandler.getArgInfo(root, name) : "";
				send(sender, " /"+root+" "+name+" &gray&"+args+" &gold&-&y& "+CommandHandler.getHelp(root, name), "");
				counter++; 
			}
		}
		
	}
	public String getHelp() {
		return Configuration.StringEntry.HelpDescription.getValue();
	}
}
