package com.iBank.Commands.Executioners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.Commands.API.Command;
import com.iBank.Commands.DataTypes.Argument;
import com.iBank.Commands.DataTypes.ArgumentCollection;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.utils.StringUtils;

/**
 *  /bank LIST (NAME)
 *  If name is given, all accounts of NAME will be shown
 *  else all accounts will be shown
 * @author steffengy
 *
 */
public class ListCommand extends Command
{
    public ListCommand()
    {
        this.registerHandler("handle", "name");
        this.registerHandler("handle");
    }
    
    public ArgumentCollection getArguments()
    {
        return new ArgumentCollection(
                new Argument(Argument.Type.REQUIRED, "name")
        );
    }

    public String getHelp()
    {
        return Configuration.StringEntry.ListDescription.getValue();
    }

    public String getName()
    {
        return "list";
    }
    
    public List<String> getPermissions()
    {
        return Arrays.asList(new String[] { "iBank.list" } );
    }

    public boolean runnableFromConsole()
    {
        return true;
    }
    
    /**
     * Handle this command
     */
    public void handle()
    {
        if((source instanceof Player) && !iBank.canExecuteCommand(((Player)source))) {
            send("&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
            return;
        }
        // Show list of accounts#
        List<String> owner;
        List<String> user;
        if(this.getArgument("name") != null) {
            owner = Bank.getAccountsByOwner(this.getArgument("name"));
            user = Bank.getAccountsByUser(this.getArgument("name"));
            send("&blue&Owner &y&User");
        }else{
            owner = Bank.getAccounts();
            user = new ArrayList<String>();
        }

        if(owner.size() == 0 && user.size() == 0) {
            send("&r&" + Configuration.StringEntry.GeneralNoAccounts.getValue());
            return;
        }
        owner = owner == null ? new ArrayList<String>() : owner;
        user = user == null ? new ArrayList<String>() : user;
        send("&blue&"+ StringUtils.join(owner, "&w&,&blue&"), "");
        send("&y&"+ StringUtils.join(user, "&w&,&y&"), "");
    }
}
