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
		help = "", 
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
			MessageManager.send(sender, Configuration.StringEntry.ErrorNoPlayer.toString());
			return;
		}
		if(!check && !iBank.canExecuteCommand(((Player)sender))) {
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
			return;
		}
		if(arguments.length == 1) {
			BigDecimal amount = null;
			String player = ((Player) sender).getName();
			try{
				amount = new BigDecimal(arguments[0]);
			}catch(Exception e) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" [Amount]");
				return;
			}
			//check max count of loans
			if(Bank.getLoansByAccount(player).size() < Configuration.Entry.LoanMax.getInteger()) {
				//validate amount > max
				if(Configuration.Entry.LoanAmount.getBigDecimal().compareTo(amount) >= 0) {
					//all validated (player and account)
					new Loan(player, Configuration.Entry.LoanInterest.getInteger(), Configuration.Entry.LoanInterestTime.getInteger() , (60 * Configuration.Entry.LoanTime.getInteger()) , amount, true);
					iBank.economy.depositPlayer(((Player)sender).getName(), amount.doubleValue());
					MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessLoan.toString().replace("$amount$", amount.toString()));
				}else{
					//amount is bigger :(
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorLoanLimit.toString().replace("$max$", iBank.format(Configuration.Entry.LoanAmount.getBigDecimal())));
				}
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorMaxLoan.toString().replace("$max$", Configuration.Entry.LoanMax.getBigDecimal().toString()));
			}
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
}
