package com.iBank.Commands.API;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.iBank.Commands.DataTypes.ArgumentCollection;
import com.iBank.Commands.DataTypes.HandlerCollection;

/**
 * Representing a command
 * @author steffen
 *
 */
public class Command 
{
    public static final String NO_ARGUMENTS =  "";
    private HandlerCollection handlers = new HandlerCollection();
    private ArgumentCollection args = new ArgumentCollection();
    private CommandSender source;
    
    /**
     * @return ArgumentCollection all Arguments this accepts
     */
    public ArgumentCollection getArguments()
    {
        return new ArgumentCollection();
    }
    /**
     * Get the help entry for the command
     * @return String The help entry
     */
    public String getHelp()
    {
        return "[NO HELP ENTRY SPECIFIED]";
    }
    /**
     * Get the name of the command
     * @return String The name
     */
    public String getName()
    {
        return "[INTERNAL_CLASS]";
    }
    
    /**
     * Return the possible permissions to get access to this command
     * @return List<String>
     */
    public List<String> getPermissions()
    {
        return Arrays.asList(new String[] { "iBank.access" } );
    }
    /**
     * Handles this command
     * @param source the source of the command
     * @param arguments The given arguments
     * @return Handler
     */
    public void handle(CommandSender source, ArgumentCollection arguments)
    {
        // Make them accessible
        args = arguments;
        this.source = source;
        // Get given arguments and match them
        String handler = handlers.getHandler(args.getList());
        if(handler == null)
        {
            source.sendMessage("[iBank] Wrong arguments!");
            return;
        }
        //TODO Fire handler
    }
    
    /**
     * Registers a handler
     * @param method The method name
     * @param cmds The needed commands
     */
    public void registerHandler(String method, String... commands)
    {
        this.handlers.register(method, commands);
    }
    
    /**
     * Gets the value from an argument
     * @param name The argument name
     */
    public String getArgument(String name)
    {
        return this.args.getValue(name);
    }
}
