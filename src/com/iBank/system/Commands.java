package com.iBank.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The commands class for iBank
 * @author steffengy
 * 
 */
public class Commands {
	private static HashMap<String, List<String>> commands = new HashMap<String, List<String>>();
	private static HashMap<String, HashMap<String,String>> help = new HashMap<String, HashMap<String,String>>();
	private static String[] last = new String[2];
	private static HashMap<String, String> tags = new HashMap<String,String>();
	
	/**
	 * Adds a new root command (e.g. /bank)
	 * @param name
	 */
	public static void addRootCommand(String name) {
		commands.put(name, new ArrayList<String>());
		help.put(name, new HashMap<String,String>());
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
}
