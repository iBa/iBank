package com.ibank.Commands;

import com.ibank.iBank;
import com.ibank.system.Bank;
import com.ibank.system.Command;
import com.ibank.system.CommandInfo;
import com.ibank.system.Configuration;
import com.ibank.system.Loan;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

/**
 *  /bank loan <AMOUNT>
 *  Loan <AMOUNT> from Bank
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Amount" }, 
		permission = "ibank.loan",
		root = "bank", 
		sub = "loan"
)
public class CommandLoan extends Command 
{
	@Override
	public void handle(CommandSender sender, String[] arguments)
	{
		handle(sender, arguments, false);
	}
	
	public void handle(CommandSender sender, String[] arguments, boolean check) 
	{
		if(!(sender instanceof Player)) 
		{
			send(sender, Configuration.StringEntry.ErrorNoPlayer.getValue());
			return;
		}
		if(!check && !iBank.canExecuteCommand(((Player)sender))) 
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
			return;
		}
		if(arguments.length == 1) 
		{
			BigDecimal amount;
			UUID player = ((Player) sender).getUniqueId();
			try{
				amount = new BigDecimal(arguments[0]);
			}
			catch(Exception e) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [Amount]");
				return;
			}
			//check max count of loans
			if(Bank.getLoansByAccount(player).size() >= Configuration.Entry.LoanMax.getInteger()) 
			{
				send(sender, "&r&"+Configuration.StringEntry.ErrorMaxLoan.getValue().replace("$max$", Configuration.Entry.LoanMax.getBigDecimal().toString()));
				return;
			}
			//validate amount > max
			if(Configuration.Entry.LoanAmount.getBigDecimal().compareTo(amount) < 0) 
			{
				//amount is bigger :(
				send(sender, "&r&"+Configuration.StringEntry.ErrorLoanLimit.getValue().replace("$max$", iBank.format(Configuration.Entry.LoanAmount.getBigDecimal())));
				return;
			}
        	/* Realistic mode check */
			if(Configuration.Entry.RealisticMode.getBoolean())
		    {
		        // Check if enough money is on the bank-account
		        if(Configuration.Entry.RealisticInternal.getBoolean())
		        {
		            if(!Configuration.Entry.RealisticNegative.getBoolean())
		            {
		                send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnoughBank.getValue());
                        return;
		            }
		            com.ibank.system.BankAccount tmp = Bank.getAccount(Configuration.Entry.RealisticAccount.getValue());
		            BigDecimal newAmount = tmp.getBalance().subtract(amount);
		            //Can be either equal to the max*-1 or it needs to be bigger
                    if(newAmount.compareTo(new BigDecimal((Configuration.Entry.RealisticMaxNeg.getDouble() * -1))) < 0)
                    {
                        send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnoughBank.getValue());
                        return;
                    }
                    tmp.subtractBalance(amount);
		        }
	        	else
		        {
		            if(!iBank.economy.has(Bukkit.getOfflinePlayer(Configuration.Entry.RealisticAccount.getValue()), amount.doubleValue()))
		            {
		                if(!Configuration.Entry.RealisticNegative.getBoolean())
		                {
		                    send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnoughBank.getValue());
		                    return;
		                }
		                BigDecimal newAmount = new BigDecimal(iBank.economy.getBalance(Bukkit.getOfflinePlayer(Configuration.Entry.RealisticAccount.getValue()))).subtract(amount);
		                //Can be either equal to the max*-1 or it needs to be bigger
		                if(newAmount.compareTo(new BigDecimal((Configuration.Entry.RealisticMaxNeg.getDouble() * -1))) < 0)
		                {
		                    send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnoughBank.getValue());
		                    return;
		                }
		            }
		            //All fine
		            iBank.economy.withdrawPlayer(Bukkit.getOfflinePlayer(Configuration.Entry.RealisticAccount.getValue()), amount.doubleValue());
		        }
		    }
			//all validated (player and account)
			new Loan(player, Configuration.Entry.LoanInterest.getInteger(), Configuration.Entry.LoanInterestTime.getInteger() , (60 * Configuration.Entry.LoanTime.getInteger()) , amount);
			iBank.economy.depositPlayer((Player)sender, amount.doubleValue());
			send(sender, "&g&"+Configuration.StringEntry.SuccessLoan.getValue().replace("$amount$", amount.toString()));
		}
		else
		{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	
	public String getHelp() 
	{
		return Configuration.StringEntry.LoanDescription.getValue();
	}
}
