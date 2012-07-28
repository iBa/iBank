package com.iBank.Commands;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;
import com.iBank.system.Loan;
import com.iBank.system.MessageManager;

/**
 *  /bank loan <AMOUNT>
 *  Loan <AMOUNT> from Bank
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Amount" }, 
		permission = "iBank.loan",
		root = "bank", 
		sub = "loan"
)
public class CommandLoan implements Command {
	public void handle(CommandSender sender, String[] arguments) {
		handle(sender, arguments, false);
	}
	public void handle(CommandSender sender, String[] arguments, boolean check) {
		if(!(sender instanceof Player)) {
			MessageManager.send(sender, Configuration.StringEntry.ErrorNoPlayer.getValue());
			return;
		}
		if(!check && !iBank.canExecuteCommand(((Player)sender))) {
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
			return;
		}
		if(arguments.length == 1) {
			BigDecimal amount = null;
			String player = ((Player) sender).getName();
			try{
				amount = new BigDecimal(arguments[0]);
			}catch(Exception e) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [Amount]");
				return;
			}
			//check max count of loans
			if(Bank.getLoansByAccount(player).size() < Configuration.Entry.LoanMax.getInteger()) {
				//validate amount > max
				if(Configuration.Entry.LoanAmount.getBigDecimal().compareTo(amount) >= 0) {
                    /* Realistic mode check */
				    if(Configuration.Entry.RealisticMode.getBoolean())
				    {
				        // Check if enough money is on the bank-account
				        if(Configuration.Entry.RealisticInternal.getBoolean())
				        {
				            if(!Configuration.Entry.RealisticNegative.getBoolean())
				            {
				                MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnoughBank);
                                return;
				            }
				            com.iBank.system.BankAccount tmp = Bank.getAccount(Configuration.Entry.RealisticAccount.toString());
				            BigDecimal newAmount = tmp.getBalance().subtract(amount);
				            //Can be either equal to the max*-1 or it needs to be bigger
                            if(newAmount.compareTo(new BigDecimal((Configuration.Entry.RealisticMaxNeg.getDouble() * -1))) < 0)
                            {
                                MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnoughBank);
                                return;
                            }
                            tmp.subtractBalance(amount);
				        }
				        else
				        {
				            if(!iBank.economy.has(Configuration.Entry.RealisticAccount.toString(), amount.doubleValue()))
				            {
				                if(!Configuration.Entry.RealisticNegative.getBoolean())
				                {
				                    MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnoughBank);
				                    return;
				                }
				                BigDecimal newAmount = new BigDecimal(iBank.economy.getBalance(Configuration.Entry.RealisticAccount.toString())).subtract(amount);
				                //Can be either equal to the max*-1 or it needs to be bigger
				                if(newAmount.compareTo(new BigDecimal((Configuration.Entry.RealisticMaxNeg.getDouble() * -1))) < 0)
				                {
				                    MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnoughBank);
				                    return;
				                }
				            }
				            //All fine
				            iBank.economy.withdrawPlayer(Configuration.Entry.RealisticAccount.toString(), amount.doubleValue());
				        }
				    }
					//all validated (player and account)
					new Loan(player, Configuration.Entry.LoanInterest.getInteger(), Configuration.Entry.LoanInterestTime.getInteger() , (60 * Configuration.Entry.LoanTime.getInteger()) , amount, true);
					iBank.economy.depositPlayer(((Player)sender).getName(), amount.doubleValue());
					MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessLoan.getValue().replace("$amount$", amount.toString()));
				}else{
					//amount is bigger :(
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorLoanLimit.getValue().replace("$max$", iBank.format(Configuration.Entry.LoanAmount.getBigDecimal())));
				}
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorMaxLoan.getValue().replace("$max$", Configuration.Entry.LoanMax.getBigDecimal().toString()));
			}
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	public String getHelp() {
		return Configuration.StringEntry.LoanDescription.getValue();
	}
}
