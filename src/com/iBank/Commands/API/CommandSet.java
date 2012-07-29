package com.iBank.Commands.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;

/**
 * Internal storage for commands
 * @author steffen
 *
 */
public class CommandSet
{
    private HashMap<String, Object> commands = new HashMap<String, Object>();
    
    /**
     * Adds a single command to the store
     * @param cmd The command
     */
    public void addCommand(Object cmd)
    {
        if(cmd instanceof Command)
            commands.put(((Command)cmd).getName(), cmd);
        else
            System.out.println("An internal error occured! Invalid argument not instanceof Command!");
    }
    
    /**
     * Adds a link to a command set to the store
     * @param cmd The commandset
     */
    public void addCommand(String name, CommandSet cmd)
    {
        commands.put(name, cmd);
    }
    
    /**
     * Executes the commands and matches the right one
     * @param sender The sender of the command
     * @param arguments All arguments as a List<String>
     */
    public boolean handle(CommandSender sender, List<String> arguments)
    {
        if(arguments.size() == 0)
        {
            //Execute root command
            if(commands.containsKey(Command.NO_ARGUMENTS))
            {
                Object toHandle = commands.get(Command.NO_ARGUMENTS);
                ((Command) toHandle).handle(sender, ((Command) toHandle).getArguments().insert((ArrayList<String>)arguments));
                return true;
            }
        }
        else if(commands.containsKey(arguments.get(0)))
        {
            Object toHandle = commands.get(arguments.get(0));
            if(toHandle instanceof Command)
            {
                //Execute it
                ((Command) toHandle).handle(sender, ((Command) toHandle).getArguments().insert((ArrayList<String>)arguments));
                return true;
            }
            else if(toHandle instanceof CommandSet)
            {
                arguments.remove(0);
                return ((CommandSet) toHandle).handle(sender,  arguments);
            }
        }
        return false;
    }
}
