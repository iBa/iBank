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
import com.iBank.utils.Mathematics;

/**
 *  /bank withdraw <NAME> <AMOUNT>
 * @author steffengy
 * Can't be run from console
 */
public class WithdrawCommand extends Command
{
    public WithdrawCommand()
    {
        this.registerHandler("handle2", "name", "amount");
        this.registerHandler("handle1", "name");
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
        return Configuration.StringEntry.WithdrawDescription.getValue();
    }
    /**
     * Get the name of the command
     * @return String The name
     */
    public String getName()
    {
        return "withdraw";
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
    public void handle2()
    {
        if(!iBank.canExecuteCommand(((Player)source))) 
        {
            send("&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
            return;
        }
        if(Bank.hasAccount(this.getArgument("name"))) 
        {
            BankAccount account = Bank.getAccount(this.getArgument("name"));
            if(account.isOwner(((Player)source).getName()) || account.isUser(((Player)source).getName())) 
            {
                BigDecimal todp = null;
                if((todp = Mathematics.parseString(this.getArgument("amount"))) == null) 
                {
                    send("&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [AMOUNT]");
                    return;
                }
                BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeWithdraw.getValue(), todp);

                if(account.has(todp.add(fee))) 
                {
                    if(fee.compareTo(BigDecimal.ZERO)>0) 
                        send("&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(fee)));
                        doWithdraw(todp.add(fee), account);
                } else {
                    send("&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
                }
            } else {
                send("&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
            }
        } else {
            send("&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", this.getArgument("name")));
        }
    }
    
    /**
     * Called if only name is given (withdraw anything)
     */
    public void handle1()
    {
        if(!iBank.canExecuteCommand(((Player)source))) 
        {
            send("&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
            return;
        }
        if(Bank.hasAccount(this.getArgument("name"))) 
        {
            BankAccount account = Bank.getAccount(this.getArgument("name"));
            if(account.isOwner(((Player)source).getName()) || account.isUser(((Player)source).getName())) 
            {
                //Withdraw The MAX
                BigDecimal amount = account.getBalance();
                BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeWithdraw.getValue(), amount);
                if(fee.compareTo(BigDecimal.ZERO)>0) 
                    send("&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(fee)));
                amount = amount.subtract(fee);
                doWithdraw(amount, account);
                account.setBalance(BigDecimal.ZERO, true);
            } else {
                send("&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
            }
        }
    }
    
    public void doWithdraw(BigDecimal todp, BankAccount account) 
    {
        account.subtractBalance(todp);
        iBank.economy.depositPlayer(((Player)source).getName(), todp.doubleValue());
        send("&g&"+Configuration.StringEntry.SuccessWithdraw.getValue().replace("$name$", account.getName()).replace("$amount$", iBank.format(todp)));
    }
}
