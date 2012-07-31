package com.iBank.Commands.Executioners;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.Commands.API.Command;
import com.iBank.Commands.DataTypes.Argument;
import com.iBank.Commands.DataTypes.ArgumentCollection;
import com.iBank.system.Bank;
import com.iBank.system.Configuration;
import com.iBank.system.Region;

/**
 * [1] /bank open <ACCOUNTNAME> @todo: <OWNERS>
 * @author steffengy
 * [1] Can't be run from console
 * @todo Add parameter for other owners
 */
public class OpenAccountCommand extends Command
{
    public OpenAccountCommand()
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
        return Configuration.StringEntry.OpenAccountDescription.getValue();
    }

    public String getName()
    {
        return "open";
    }
    
    public List<String> getPermissions()
    {
        return Arrays.asList(new String[] { "iBank.access" } );
    }
    
    public boolean runnableFromConsole()
    {
        return false;
    }
    
    /**
     * Handles this command
     */
    public void handle()
    {
        if(!iBank.canExecuteCommand(((Player)source))) 
        {
            send("&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
            return;
        }
        String region = iBank.regionAt(((Player)source).getLocation());
        region = region == null ?  " " : region;

        if(!Bank.hasAccount(this.getArgument("name"))) 
        {
            // fee stuff
            String fee = Configuration.Entry.FeeCreate.getValue();
            BigDecimal extra = BigDecimal.ZERO;
            if(!fee.contains(";")) 
            {
                extra = iBank.parseFee(fee, new BigDecimal(iBank.economy.getBalance(((Player)source).getName())));
            }
            else
            {
                String[] costs = fee.split(";");
                int account = Bank.getAccountsByOwner(((Player)source).getName()).size();

                if(costs.length > account) 
                {
                    extra = iBank.parseFee(costs[account], new BigDecimal(iBank.economy.getBalance(((Player)source).getName())));
                }
                else
                {
                    extra = iBank.parseFee(costs[costs.length - 1], new BigDecimal(iBank.economy.getBalance(((Player)source).getName())));
                }
            }
            List<String> tmp = Bank.getAccountsByOwner(((Player)source).getName());
            //skip if max is higher/equal to precision
            if(Configuration.Entry.MaxAccountsPerUser.getInteger() != -1 && tmp.size() >= Configuration.Entry.MaxAccountsPerUser.getInteger()) 
            {
                send("&r&" + Configuration.StringEntry.ErrorMaxAcc.getValue().replace("$max$", Configuration.Entry.MaxAccountsPerUser.getValue()));
                return;
            }
            if(!iBank.economy.has(((Player)source).getName(), extra.doubleValue())) 
            {
                send("&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
                return;
            }
            iBank.economy.withdrawPlayer(((Player)source).getName(), extra.doubleValue());
            Bank.createAccount(this.getArgument("name"), ((Player)source).getName());
            // check for custom percentages
            if(region != " ") 
            {
                Region reg = Bank.getRegion(region);
                if(!reg.onDefault) 
                    Bank.getAccount(this.getArgument("name")).setOnPercentage(reg.getOnPercentage(), true);
                if(!reg.offDefault) 
                    Bank.getAccount(this.getArgument("name")).setOffPercentage(reg.getOffPercentage(), true);
            }
            send( "&g&"+Configuration.StringEntry.SuccessAddAccount.getValue().replace("$name$", "Account " + this.getArgument("name") + " "));
            if(extra.compareTo(BigDecimal.ZERO)>0) 
                send("&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(extra)));
        }
        else
        {
            send("&r&"+Configuration.StringEntry.ErrorAlreadyExists.getValue().replace("$name$", "Account " + this.getArgument("name") + " "));
        }
    }
}
