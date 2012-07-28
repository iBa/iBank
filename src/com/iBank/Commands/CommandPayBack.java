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

/**
 *  /bank payback (ID) [AMOUNT]
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "(ID)", "Amount" },  
		permission = "iBank.loan",
		root = "bank", 
		sub = "payback"
)
public class CommandPayBack extends Command {
	public void handle(CommandSender sender, String[] arguments) {
		handle(sender, arguments, false);
	}
	public void handle(CommandSender sender, String[] arguments, boolean check) {
		if(!(sender instanceof Player)) {
			send(sender, Configuration.StringEntry.ErrorNoPlayer.getValue());
			return;
		}
		if(!check && !iBank.canExecuteCommand(((Player)sender))) {
			send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.getValue());
			return;
		}
		if(arguments.length == 2) {
			//arguments[0] -> int
			int arg = 0;
			try{
				arg = Integer.parseInt(arguments[0]);
			}catch(Exception e) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [ID]");
				return;
			}
			//arguments[1] -> BigDecimal
			BigDecimal todp = BigDecimal.ZERO;
			try{
				todp = new BigDecimal(arguments[1]);
			}catch(Exception e) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [AMOUNT]");
				return;
			}
			if(!iBank.economy.has(((Player)sender).getName(), todp.doubleValue())) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
				return;
			}
			//try to get this loan
			Loan loan = Bank.getLoanById(arg);
			if(loan == null) {
				//notfound
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name", String.valueOf(arg)));
			}else{
				//loan.getAmount() has to be bigger or equal than given (0 or -1)
				if(!(loan.getAmount().compareTo(todp)<=0)) {
					//throw error
					send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue() + "AMOUNT>LOAN");
					return;
				}
				loan.setAmount(loan.getAmount().subtract(todp));
			    iBank.economy.withdrawPlayer(((Player)sender).getName(), todp.doubleValue());
				//<= to prevent MAGIC exceptions
				if(loan.getAmount().compareTo(BigDecimal.ZERO)<=0) {
					loan.remove();
				}
				if(Configuration.Entry.RealisticMode.getBoolean())
				{
				    if(Configuration.Entry.RealisticInternal.getBoolean())
				    {
				        com.iBank.system.BankAccount tmp = Bank.getAccount(Configuration.Entry.RealisticAccount.getValue());
				        tmp.addBalance(todp);
				    }
				    else
				    {
				        iBank.economy.depositPlayer(Configuration.Entry.RealisticAccount.getValue(), todp.doubleValue());
				    }
				}
				send(sender, "&g&"+Configuration.StringEntry.SuccessPayback.getValue().replace("$amount$", iBank.format(todp)));
			}
		}else if(arguments.length == 1){
			//loop through all, calculate stuff, etc.
			//arguments[0] -> BigDecimal
			BigDecimal todp = BigDecimal.ZERO;
			try{
				todp = new BigDecimal(arguments[0]);
			}catch(Exception e) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [AMOUNT]");
				return;
			}
			if(!iBank.economy.has(((Player)sender).getName(), todp.doubleValue())) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.getValue());
				return;
			}
			BigDecimal paiedback = BigDecimal.ZERO;
			for(Loan loan : Bank.getLoansByAccount(((Player)sender).getName())) {
				//todp > loan => remove loan, todp -= loan
				if(todp.compareTo(loan.getAmount()) >= 0) {
					iBank.economy.withdrawPlayer(((Player)sender).getName(), loan.getAmount().doubleValue());
					paiedback = paiedback.add(loan.getAmount());
					todp.subtract(loan.getAmount());
					loan.remove();
				}
				//todp < loan => subtract as much as possible, loan -= todp
				if(todp.compareTo(loan.getAmount()) < 0) {
					loan.setAmount(loan.getAmount().subtract(todp));
					paiedback = paiedback.add(todp);
					iBank.economy.withdrawPlayer(((Player)sender).getName(), todp.doubleValue());
					//break because no money left
					break;
				}
			}
			if(Configuration.Entry.RealisticMode.getBoolean())
            {
                iBank.economy.depositPlayer(Configuration.Entry.RealisticAccount.getValue(), todp.doubleValue());
            }
			send(sender, "&g&"+Configuration.StringEntry.SuccessPayback.getValue().replace("$amount$", iBank.format(paiedback)));
		}else{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
	}
	public String getHelp() {
		return Configuration.StringEntry.PayBackDescription.getValue();
	}
}
