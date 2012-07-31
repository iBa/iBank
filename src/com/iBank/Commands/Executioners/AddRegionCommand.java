package com.iBank.Commands.Executioners;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.Commands.API.Command;
import com.iBank.Commands.DataTypes.Argument;
import com.iBank.Commands.DataTypes.ArgumentCollection;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.Listeners.iBankListener;

/**
 * Adds an region
 *  /bank addregion <REGIONNAME>
 *  Can't be run from console
 */
public class AddRegionCommand extends Command
{
    public AddRegionCommand()
    {
        this.registerHandler("handle", "name");
    }
    public ArgumentCollection getArguments()
    {
        return new ArgumentCollection(new Argument(Argument.Type.REQUIRED, "name"));
    }
    
    public String getHelp()
    {
        return Configuration.StringEntry.AddRegionDescription.getValue();
    }
    
    public String getName()
    {
        return "addregion";
    }
    
    public List<String> getPermissions()
    {
        return Arrays.asList(new String[] { "iBank.regions" } );
    }
    
    public boolean runnableFromConsole()
    {
        return false;
    }
    
    /**
     * Called if name is given
     */
    public void handle()
    {
        Entry<Location, Location> raw = ((iBankListener)iBank.Listener).LastMarkedPoint.get(source.getName());
        if(raw == null || raw.getKey()==null || raw.getValue()==null){
            send("&r&"+Configuration.StringEntry.ErrorRegionSelect.getValue());
            return;
        }
        if(!Bank.hasRegion(this.getArgument("name"))) {
            if(!iBank.hasPermission(source, "iBank.regions") && !Configuration.Entry.AllowBuyRegion.getBoolean()) {
                send("&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
                return;
            }
            if(Configuration.Entry.AllowBuyRegion.getBoolean() && !iBank.hasPermission(source, "iBank.regions")) {
                if(!iBank.economy.has(((Player)source).getName(), Configuration.Entry.RegionsPrice.getDouble())) {
                    send("&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
                    return;
                }else{
                    //cashout
                    iBank.economy.withdrawPlayer(((Player)source).getName(), Configuration.Entry.RegionsPrice.getDouble());
                    send("[iBank] Balance - " + String.valueOf(Configuration.Entry.RegionsPrice.getBoolean()));
                }
            }
            Bank.createRegion(this.getArgument("name"), raw.getKey(), raw.getValue());
            send("&g&"+Configuration.StringEntry.SuccessAddRegion.getValue().replace("$name$", this.getArgument("name")));
        }else{
            send("&r&"+Configuration.StringEntry.ErrorAlreadyExists.getValue().replace("$name$", "Region "+this.getArgument("name")+" "));
            return;
        }
    }
}
