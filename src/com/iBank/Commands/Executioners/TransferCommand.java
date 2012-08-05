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
 *  /bank transfer <SRC> <DEST> <AMOUNT>
 * @author steffengy
 *
 */
public class TransferCommand extends Command
{
    public TransferCommand()
    {
        this.registerHandler("handle", "src", "dest", "amount");
    }
    
    public ArgumentCollection getArguments()
    {
        return new ArgumentCollection(
                new Argument(Argument.Type.REQUIRED, "src"),
                new Argument(Argument.Type.REQUIRED, "dest"),
                new Argument(Argument.Type.REQUIRED, "amount")
        );
    }
    
    public String getHelp()
    {
        return Configuration.StringEntry.TransferDescription.getValue();
    }

    public String getName()
    {
        return "transfer";
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
     * Handles this with given src,dest and amount
     */
    public void handle()
    {
        boolean console = false;
        if(!(source instanceof Player))
            console = true;
        if(!console && !iBank.canExecuteCommand(((Player)source))) 
        {
            send("&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
            return;
        }
        BigDecimal money = null;
        try
        {
            money = new BigDecimal(this.getArgument("amount"));
        } catch(Exception e) {
            send("&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [AMOUNT]");
            return;
        }
        if(money.compareTo(new BigDecimal(0.10)) < 0) 
        {
            send("&r&"+Configuration.StringEntry.ErrorInvalidAm.getValue());
            return;
        }
        if(!Bank.hasAccount(this.getArgument("src"))) 
        {    
            send("&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", this.getArgument("src")));
            return;
        }
        if(!Bank.hasAccount(this.getArgument("dest"))) 
        {
            send("&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", this.getArgument("dest")));
            return;
        }
        BankAccount src = Bank.getAccount(this.getArgument("src"));
        BankAccount dest = Bank.getAccount(this.getArgument("dest"));
        if(!console && !src.isUser(((Player)source).getName()) && !src.isOwner(((Player)source).getName())) 
        {
            send("&r&"+Configuration.StringEntry.ErrorNoAccess.getValue());
            return;
        }
        BigDecimal fee = iBank.parseFee(Configuration.Entry.FeeTransfer.getValue(), money);
        if(!src.has(money.add(fee))) 
        {
            send("&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
            return;
        }
        src.subtractBalance(money.add(fee));
        dest.addBalance(money);
        send("&g&"+Configuration.StringEntry.SuccessTransfer.getValue().replace("$name$", this.getArgument("src")).replace("$name2$", this.getArgument("dest")).replace("$amount$", iBank.format(money)));
        if(fee.compareTo(BigDecimal.ZERO)>0)
            send("&g&"+Configuration.StringEntry.PaidFee.getValue().replace("$amount$", iBank.format(fee)));
    }
}
