package com.iBank.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.iBank.iBank;

/**
 * The commands class for iBank
 * @author steffengy
 * 
 */
public class Commands {
	private static HashMap<String, List<String>> commands = new HashMap<String, List<String>>();
	private static HashMap<String, String[]> alias = new HashMap<String, String[]>();
	private static HashMap<String, HashMap<String, Handler>> handler = new HashMap<String, HashMap<String, Handler>>();
	private static HashMap<String, HashMap<String,String>> help = new HashMap<String, HashMap<String,String>>();
	private static HashMap<String, HashMap<String,String>> helpargs = new HashMap<String, HashMap<String,String>>();
	private static HashMap<String, HashMap<String,String>> permissions = new HashMap<String, HashMap<String,String>>();
	private static String[] last = new String[2];
	private static HashMap<String, String> tags = new HashMap<String,String>();
	private static iBank main = null;
	
	/**
	 * Adds a new root command (e.g. /bank)
	 * @param name
	 */
	public static void addRootCommand(String name) {
		commands.put(name, new ArrayList<String>());
		handler.put(name, new HashMap<String, Handler>());
		help.put(name, new HashMap<String,String>());
		helpargs.put(name, new HashMap<String,String>());
		permissions.put(name, new HashMap<String,String>());
	}
	/**
	 * Adds a new sub command (e.g. /bank help)
	 * @param root The rootcommand
	 * @param sub The subcommand
	 */
	public static void addSubCommand(String root,String sub) {
		commands.get(root).add(sub);
		last[0] = root;
		last[1] = sub;
	}
	/**
	 * Sets the permission needed to execute the command
	 * @param root The root command
	 * @param sub The subcommand
	 * @param permission The permission
	 */
	public static void setPermission(String root, String sub, String permission) {
		if(!permissions.containsKey(root)) return;
		if(permissions.get(root) == null)
		{
			//create new hashmap
			permissions.put(root, new HashMap<String, String>());
		}
		
		permissions.get(root).put(sub, permission);
		last[0] = root;
		last[1] = sub;
	}
	/**
	 * Sets the permission to the last changed command
	 * @param permission 
	 */
	public static void setPermission(String permission) {
		setPermission(last[0],last[1], permission);
	}
	/**
	 * Returns the permission needed to execute a command
	 * @param root The root command
	 * @param sub The sub command
	 * @return String the Permission
	 */
	public static String getPermission(String root, String sub) {
		if(!permissions.containsKey(root)) return null;
		try{
			return permissions.get(root).get(sub);
		}catch(Exception e) {
			return null;
		}
	}
	/**
	 * Returns if the command is accessable 
	 * @param player The player
	 * @param rootCMD The command
	 * @param subCmd The sub command
	 * @return
	 */
	public static boolean isCallable(Player player, String rootCMD,
			String subCmd) {
		try{
			// alias
			return iBank.permission.has(player, getPermission(rootCMD, subCmd));
		}catch(Exception e) {
			return false;
		}
	}
	/**
	 * Sets the help of a command
	 * @param root The Root command
	 * @param sub The sub command
	 * @param text The text displayed in help
	 */
	public static void setHelp(String root, String sub, String text) {
		if(!help.containsKey(root)) return;
		if(help.get(root) == null)
		{
			//create new hashmap
			help.put(root, new HashMap<String, String>());
		}
		
		help.get(root).put(sub, text);
		last[0] = root;
		last[1] = sub;
	}
	/**
	 * Adds a help entry to the last changed command
	 * @param text The help text
	 */
	public static void setHelp(String text)
	{
		setHelp(last[0], last[1], text);
	}
	/**
	 * Returns the text which should be shown in the help
	 * @param root The root command
	 * @param sub The sub command
	 * @return String the help text
	 */
	public static String getHelp(String root, String sub) {
		if(!help.containsKey(root)) return null;
		try{
			return help.get(root).get(sub);
		}catch(Exception e) {
			return null;
		}
	}
	/**
	 * Adds an arg list to the last changed command
	 * @param text The help text
	 */
	public static void setHelpArgs(String text)
	{
		setHelpArgs(last[0], last[1], text);
	}
	/**
	 * Sets the help of a command
	 * @param root The Root command
	 * @param sub The sub command
	 * @param text The text displayed in help
	 */
	public static void setHelpArgs(String root, String sub, String text) {
		if(!helpargs.containsKey(root)) return;
		if(helpargs.get(root) == null)
		{
			//create new hashmap
			helpargs.put(root, new HashMap<String, String>());
		}
		
		helpargs.get(root).put(sub, text);
		last[0] = root;
		last[1] = sub;
	}
	/**
	 * Gets the help args of a command
	 * @param root The Root command 
	 * @param sub The sub command
	 */
	public static String getHelpArgs(String root, String sub) {
		if(!helpargs.containsKey(root)) return null;
		try{
			return helpargs.get(root).get(sub);
		}catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Sets Handler to a command
	 * @param root The root command
	 * @param sub The sub command
	 * @param handle The handler
	 */
	public static void setHandler(String root,String sub, Handler handle) {
		if(!handler.containsKey(root)) return;
		if(handler.get(root) == null)
		{
			//create new hashmap
			handler.put(root, new HashMap<String, Handler>());
		}
		
		handler.get(root).put(sub, handle);
		last[0] = root;
		last[1] = sub;
	}
	/**
	 * Sets handler to the last accessed command
	 * @param handle
	 */
	public static void setHandler(Handler handle)
	{
		setHandler(last[0], last[1], handle);
	}
	/**
	 * Returns Handler for a command
	 * @param root The root command
	 * @param sub The sub command
	 * @return
	 */
	public static Handler getHandler(String root, String sub) {
		if(!handler.containsKey(root)) return null;
		try{
			Handler ret = handler.get(root).get(sub);
			ret.setSource(main);
			return ret;
		}catch(Exception e) {
			return null;
		}
	}
	/**
	 * Adds an alias
	 * @param string The name of the command
	 * @param string2 Its aliases
	 */
	public static void alias(String string, String... string2) {
		List<String> tmp = new ArrayList<String>();
		for(String arg : string2)
		{
			tmp.add(arg);
		}
		alias.put(string,tmp.toArray(new String[1]));
	}
	/**
	 * Returns if the given Command has a Link with this
	 * @param rootCMD The command
	 * @return
	 */
	public static boolean rootIsLinked(String rootCMD) {
		return commands.containsKey(rootCMD);
	}
	/**
	 * Returns if the Sub command is linked with this plugin
	 * @param root The root command
	 * @param sub The command
	 * @return
	 */
	public static boolean subIsLinked(String root, String sub) {
		return commands.containsKey(root) ? commands.get(root).contains(sub) : false;
	}
	/**
	 * Clones root,sub pair into root2,sub2 pair
	 */
	public static void clone(String root,String sub,String root2,String sub2) {
		handler.get(root2).put(sub2, handler.get(root).get(sub));
		help.get(root2).put(sub2, help.get(root).get(sub));
		permissions.get(root2).put(sub2, permissions.get(root).get(sub));
		
	}
	/**
	 * Returns a list of all commands
	 * @param root The rootname
	 * @return
	 */
	public static String[] getCommands(String root) {
		return commands.get(root).toArray(new String[1]);
	}
	/**
	 * Sets the tag of a command to the last root command
	 */
	public static void setTag(String string) {
		tags.put(last[0], string);
	}
	/**
	 * Returns the tag of a root command
	 * @param name the name
	 */
	public static String getTag(String name) {
		return tags.get(name);
	}
	public static void setVarSource(iBank iBank) {
		main = iBank;
	}
	
}
