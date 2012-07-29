package com.iBank.Commands.Executioners;

import java.util.Arrays;
import java.util.List;

import com.iBank.Commands.API.Command;
import com.iBank.Commands.DataTypes.ArgumentCollection;
import com.iBank.system.Configuration;

/**
 * /bank - Shows the accounts of the executor
 * @author steffengy
 * Can't be run from console
 */
public class RootCommand extends Command 
{
    public RootCommand()
    {
        this.registerHandler(Command.NO_ARGUMENTS, "handle");
    }
    public ArgumentCollection getArguments()
    {
        return new ArgumentCollection();
    }

    public String getHelp()
    {
        return Configuration.StringEntry.BankDescription.getValue();
    }
    
    /**
     * The method which will handle all incoming requests
     */
    public void handle()
    {
        System.out.println("Received bank!");
    }

    public String getName()
    {
        return "";
    }
    
    public List<String> getPermissions()
    {
        return Arrays.asList(new String[] { "iBank.access" } );
    }
}
