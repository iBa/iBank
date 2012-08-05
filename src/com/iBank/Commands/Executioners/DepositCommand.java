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
import com.iBank.system.BankAccount;
import com.iBank.system.Configuration;

/**
 * /bank deposit <NAME> <AMOUNT>
 * @author steffengy
 * Can't be run from console
 */
public class DepositCommand extends Command
{
    public DepositCommand()
    {
        this.registerHandler("handle", "name", "amount");
    }
    
    public ArgumentCollection getArguments()
    {
        return new ArgumentCollection(
                new Argument(Argument.Type.REQUIRED, "name"),
                new Argument(Argument.Type.REQUIRED, "amount")
        );
    }

    public String getHelp()
    {
        return Configuration.StringEntry.DepositDescription.getValue();
    }
    
    public String getName()
    {
        return "deposit";
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
     * Returns whether this method is runnable as console user
     * @return boolean
     */
    public boolean runnableFromConsole()
    {
        return false;
    }
    
    /**
     * Called if name and amount are given
     */
    public void handle()
    {
        if(!iBank.canExecuteCommand(((Player)source))) 
        {
            send("&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
            return;
        }
        if(Bank.hasAccount(this.getArgument("name"))) 
        {
            BigDecimal todp = null;
            BankAccount account = Bank.getAccount(this.getArgument("name"));
            if(!account.isOwner(((Player)source).getName()) && !account.isUser(((Player)source).getName())) 
            {
                send("&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
                return;
            }
            try
            {
                todp = new BigDecimal(this.getArgument("amount"));
            } catch(Exception e) {
                send("&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [AMOUNT]");
                return;
            }
            if(todp.compareTo(new BigDecimal(0.10)) < 0) 
            {
                send("&r&"+Configuration.StringEntry.ErrorInvalidAm.getValue());
                return;
            }
            // check if current player has that amount
            //double needed = 0.00;
            BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeDeposit.getValue(), todp);
            if(iBank.economy.has(((Player)source).getName(), todp.doubleValue() + fee.doubleValue())) {
                iBank.economy.withdrawPlayer(((Player)source).getName(), todp.doubleValue() + fee.doubleValue());
                account.addBalance(todp);
                send("&g&"+Configuration.StringEntry.SuccessDeposit.getValue().replace("$name$", 
                        this.getArgument("name")).replace("$amount$", iBank.format(todp))
                );
                if(fee.compareTo(BigDecimal.ZERO)>0) 
                    send("&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(fee)));
            }else{
                send("&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
                return;
            }
        }else{
            send("&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", this.getArgument("name")));
        }
    }
}
