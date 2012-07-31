package com.iBank.Commands.Executioners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.Commands.API.Command;
import com.iBank.Commands.DataTypes.Argument;
import com.iBank.Commands.DataTypes.ArgumentCollection;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.system.Region;

/**
 *  /bank region <NAME>
 *  /bank region <NAME> online 12  - 12 percentage online
 *  /bank region <NAME> offline 12 - 12 percentage offline 
 *  /bank region <NAME> add <USERNAME> - Add <USERNAME> as Owner
 *  /bank region <NAME> del <USERNAME> - Remove <USERNAME> as Owner
 *  (IF created account in that region on+offline) 
 *  Displays info about a region
 * @author steffengy
 *
 */
public class RegionCommand extends Command
{
    public RegionCommand()
    {
        this.registerHandler("handleInfo", "name");
        this.registerHandler("updateInfo", "name", "key", "value");
    }
    public ArgumentCollection getArguments()
    {
        return new ArgumentCollection(
                new Argument(Argument.Type.REQUIRED, "name"),
                new Argument(Argument.Type.OPTIONAL, "key"),
                new Argument(Argument.Type.OPTIONAL, "value")
        );
    }
    
    public String getHelp()
    {
        return Configuration.StringEntry.RegionDescription.getValue();
    }
    
    public String getName()
    {
        return "region";
    }
    
    public List<String> getPermissions()
    {
        return Arrays.asList(new String[] { "iBank.access" } );
    }
    
    public boolean runnableFromConsole()
    {
        return true;
    }
    
    /**
     * Handles /bank region <NAME> and displays the info
     */
    public void handleInfo()
    {
        if(Bank.hasRegion(this.getArgument("name"))) 
        {
            Region tmp = Bank.getRegion(this.getArgument("name"));
            send("&w&"+Configuration.StringEntry.GeneralInfo.getValue().replace("$type$","Region").replace("$name$", this.getArgument("name")));
            String onlineP = tmp.onDefault ? " Default " : String.valueOf(tmp.getOnPercentage()) + "%";
            String offlinP = tmp.offDefault ? " Default ": String.valueOf(tmp.getOffPercentage()) + "%";
            send("&w&Online %: &gray&" + onlineP, "");
            send("&w&Offline %: &gray&" + offlinP, "");
            send("&w&"+Configuration.StringEntry.GeneralOwners.getValue()+": &gray&" + tmp.getOwners().toString(), "");
            Location loc = tmp.getFirstLocation();
            Location loc2 = tmp.getSecondLocation();
            send("&w&Location 1:&gray&"+loc.toString(), "");
            send("&w&Location 2:&gray&"+loc2.toString(), "");
        }
        else
        {
            send("&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", "Region "+ this.getArgument("name") +" "));
        }
    }
    
    /**
     * Handles /bank <NAME> <KEY> <VALUE> and saves the change
     * /bank region <NAME> online 12  - 12 percentage online
     * /bank region <NAME> offline 12 - 12 percentage offline 
     * /bank region <NAME> add <USERNAME> - Add <USERNAME> as Owner
     * /bank region <NAME> del <USERNAME> - Remove <USERNAME> as Owner
     */
    public void updateInfo()
    {
        if(Bank.hasRegion(this.getArgument("name"))) 
        {
            if(source instanceof Player && !iBank.hasPermission(source, "iBank.regions") 
                    && !Bank.getRegion(this.getArgument("name")).getOwners().contains(((Player)source).getName())
            ) 
            {
                send("&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
                return;
            }
            if(this.getArgument("key").equalsIgnoreCase("online") || this.getArgument("key").equalsIgnoreCase("on")) {
                Double percentage = 0.00;
                try{
                    percentage = Double.parseDouble(this.getArgument("value"));
                }catch(Exception e) {
                    send("&r&" + Configuration.StringEntry.ErrorWrongArguments.getValue() + " " + this.getArgument("value"));
                    return;
                }
                Bank.getRegion(this.getArgument("name")).setOnPercentage(percentage, true);
            }else if(this.getArgument("key").equalsIgnoreCase("offline") 
                   || this.getArgument("key").equalsIgnoreCase("off")
            ) 
            {
                Double percentage = 0.00;
                try{
                    percentage = Double.parseDouble(this.getArgument("value"));
                }catch(Exception e) {
                    send("&r&" + Configuration.StringEntry.ErrorWrongArguments.getValue() + " " + this.getArgument("value"));
                    return;
                }
                Bank.getRegion(this.getArgument("name")).setOffPercentage(percentage, true);
            }else if(this.getArgument("key").equalsIgnoreCase("add")) {
                Bank.getRegion(this.getArgument("name")).addOwner(this.getArgument("value"));
            }else if(this.getArgument("key").equalsIgnoreCase("del")) {
                if(Bank.getRegion(this.getArgument("name")).getOwners().contains(this.getArgument("value")))
                    Bank.getRegion(this.getArgument("name")).removeOwner(this.getArgument("value"));
            }else{
                send("&r&" + Configuration.StringEntry.ErrorWrongArguments.getValue() + " " + this.getArgument("key"));
                return;
            }
            send("&g&"+Configuration.StringEntry.SuccessRegion.getValue().replace("$name$", this.getArgument("name")));
        }else{
            send("&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", "Region " + this.getArgument("name") +" "));
            return;
        }
    }
}
