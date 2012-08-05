package com.iBank.Commands.Executioners;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.iBank.iBank;
import com.iBank.Commands.API.Command;
import com.iBank.Commands.DataTypes.Argument;
import com.iBank.Commands.DataTypes.ArgumentCollection;
import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Configuration;

public class GiveCommand extends Command
{
    public GiveCommand()
    {
        this.registerHandler("handle", "account", "amount");
    }
    
    public ArgumentCollection getArguments()
    {
        return new ArgumentCollection(
                new Argument(Argument.Type.REQUIRED, "account"),
                new Argument(Argument.Type.REQUIRED, "amount")
        );
    }

    public String getHelp()
    {
        return Configuration.StringEntry.GiveDescription.getValue();
    }
    /**
     * Get the name of the command
     * @return String The name
     */
    public String getName()
    {
        return "give";
    }
    
    public List<String> getPermissions()
    {
        return Arrays.asList(new String[] { "iBank.manage" } );
    }

    public boolean runnableFromConsole()
    {
        return true;
    }
    
    public void handle()
    {
        if(Bank.hasAccount(this.getArgument("account"))) 
        {
            BigDecimal todp = null;
            BankAccount account = Bank.getAccount(this.getArgument("account"));
            // the needed checks
            try{
            todp = new BigDecimal(this.getArgument("amount"));
            }catch(Exception e) {
                send("&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [AMOUNT]");
                return;
            }
            if(todp.compareTo(new BigDecimal(0.10)) < 0) {
                send("&r&"+Configuration.StringEntry.ErrorInvalidAm.getValue());
                return;
            }
            // and save to account
            account.addBalance(todp);
            send( "&g&"+Configuration.StringEntry.SuccessGive.getValue().replace("$name$", this.getArgument("name")).replace("$amount$", iBank.format(todp)));
        }else{
            send("&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name$", this.getArgument("name")));
        }
    }
}
