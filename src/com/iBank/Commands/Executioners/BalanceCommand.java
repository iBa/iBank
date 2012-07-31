package com.iBank.Commands.Executioners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.Commands.API.Command;
import com.iBank.Commands.DataTypes.Argument;
import com.iBank.Commands.DataTypes.ArgumentCollection;
import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Configuration;

/**
 *  /bank balance [NAME]
 * @author steffengy
 * 
 */
public class BalanceCommand extends Command
{
    public BalanceCommand()
    {
        this.registerHandler("handle", "name");
    }
    
    public ArgumentCollection getArguments()
    {
        return new ArgumentCollection(
                new Argument(Argument.Type.REQUIRED, "name")
        );
    }

    public String getHelp()
    {
        return Configuration.StringEntry.BalanceDescription.getValue();
    }

    public String getName()
    {
        return "balance";
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
     * Handles this command
     */
    public void handle()
    {
        boolean console = false;
        if(!(source instanceof Player)) 
            console = true;
        if(Bank.hasAccount(this.getArgument("name"))) 
        {
            BankAccount acc = Bank.getAccount(this.getArgument("name"));
            if(console || 
                    (
                            acc.isOwner(((Player)source).getName()) 
                            || acc.isUser(((Player)source).getName())
                    ) 
                    || iBank.hasPermission(source, "iBank.balance")
            ) 
            {
                String formattedBalance = iBank.economy.format(acc.getBalance().doubleValue());
                if(!console) 
                {
                    if(!iBank.canExecuteCommand(((Player)source))) 
                    {
                        send("&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
                        return;
                    }
                }
                send("&dg&" + Configuration.StringEntry.BalanceShort.getValue() + " &gray&" + this.getArgument("name") + " &w&: " + formattedBalance);
            } else {
                send("&r&" + Configuration.StringEntry.ErrorNoAccess.getValue());
            }
        } else {
            send("&r&" + Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", this.getArgument("name")));
        }
    }
}
