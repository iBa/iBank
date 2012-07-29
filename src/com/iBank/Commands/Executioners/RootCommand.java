package com.iBank.Commands.Executioners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.Commands.API.Command;
import com.iBank.Commands.DataTypes.ArgumentCollection;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.utils.StringUtils;

/**
 * /bank - Shows the accounts of the executor
 * @author steffengy
 * Can't be run from console
 */
public class RootCommand extends Command 
{
    public RootCommand()
    {
        this.registerHandler("handle");
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
        if(!iBank.canExecuteCommand(((Player)source))) {
            send("&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
            return;
        }
        // Show list of accounts
        List<String> owner = Bank.getAccountsByOwner(((Player)source).getName());
        List<String> user = Bank.getAccountsByUser(((Player)source).getName());
        if(owner.size() == 0 && user.size() == 0) {
            send("&r&" + Configuration.StringEntry.GeneralNoAccounts.getValue());
            return;
        }
        send("&blue&Owner &y&User");
        owner = owner == null ? new ArrayList<String>() : owner;
        user = user == null ? new ArrayList<String>() : user;
        send("&blue&"+StringUtils.join(owner, "&w&,&blue&"), "");
        send("&y&"+StringUtils.join(user, "&w&,&y&"), "");
    }

    public String getName()
    {
        return "";
    }
    
    public List<String> getPermissions()
    {
        return Arrays.asList(new String[] { "iBank.access" } );
    }
    
    public boolean runnableFromConsole()
    {
        return false;
    }
}
