package com.iBank.Commands.Executioners;

import java.util.Arrays;
import java.util.List;

import com.iBank.Commands.API.Command;
import com.iBank.Commands.DataTypes.Argument;
import com.iBank.Commands.DataTypes.ArgumentCollection;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;


/**
 *  /bank delregion <NAME>
 * @author steffengy
 *
 */
public class DelRegionCommand extends Command
{
    public DelRegionCommand()
    {
        this.registerHandler("handle", "name");
    }
    
    public ArgumentCollection getArguments()
    {
        return new ArgumentCollection(new Argument(Argument.Type.REQUIRED, "name"));
    }
    
    public String getHelp()
    {
        return Configuration.StringEntry.DelRegionDescription.getValue();
    }
    
    public String getName()
    {
        return "delregion";
    }
    
    public List<String> getPermissions()
    {
        return Arrays.asList(new String[] { "iBank.regions" } );
    }
    
    public boolean runnableFromConsole()
    {
        return true;
    }
    
    /**
     * Called if name is given
     */
    public void handle()
    {
      //delete region
        if(Bank.hasRegion(this.getArgument("name"))) 
        {
            Bank.removeRegion(this.getArgument("name"));
            send("&g&"+Configuration.StringEntry.SuccessDelRegion.getValue().replace("$name$", this.getArgument("name")));
        }
        else
        {
            send("&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", "Region "+ this.getArgument("name") +" "));
        }
        return;
    }
}
