package com.iBank.Commands;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;

import com.iBank.system.Bank;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;
import com.iBank.system.Configuration;
import com.iBank.system.Loan;

/**
 *  /bank loanedit <ID> (KEY) (VALUE)
 *  Either shows info about a loan or modifies it
 *  Keys: [interval,percentage,until,amount]
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { "Name", "(Key)", "(Val)" },  
		permission = "iBank.loanedit",
		root = "bank", 
		sub = "loanedit"
)
public class CommandLoanEdit extends Command {
	public void handle(CommandSender sender, String[] arguments) {
		if(arguments.length == 3) {
			int id = 0;
			try{
				id = Integer.parseInt(arguments[0]);
			}catch(Exception e) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [Id]");
				return;
			}
			Loan loan = null;
			if((loan = Bank.getLoanById(id)) == null) {
				send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.getValue().replace("$name", String.valueOf(id)));
				return;
			}
			// Validated
			/**
			 * interval
			 * [2] as Integer
			 */
			if(arguments[1].equalsIgnoreCase("interval")) {
				int param = 0;
				try{
					param = Integer.parseInt(arguments[2]);
				}catch(Exception e) {
					send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [Value]");
					return;
				}
				if(!(param>0)) param = Configuration.Entry.LoanInterestTime.getInteger();
				loan.setInterval(param);
			}
			/**
			 * percentage
			 * [2] as double
			 */
			else if(arguments[1].equalsIgnoreCase("percentage")) {
				double param = 0.00;
				try{
					param = Double.parseDouble(arguments[2]);
				}catch(Exception e) {
					send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [Value]");
					return;
				}
				loan.setInterest(param);
			}
			/**
			 * amount
			 * [2] as BigDecimal
			 */
			else if(arguments[1].equalsIgnoreCase("amount")) {
				BigDecimal param = BigDecimal.ZERO;
				String mode = "normal";
				if(arguments[2].startsWith("+") || arguments[2].startsWith("-")) {
					mode = ""+arguments[2].charAt(0);
					arguments[2] = arguments[2].substring(1);
				}
				try{
					param = new BigDecimal(arguments[2]);
				}catch(Exception e) {
					send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [Value]");
					return;
				}
				if(mode == "normal") 
					loan.setAmount(param);
				else if(mode == "+") 
					loan.setAmount(loan.getAmount().add(param));
				else if(mode == "-") {
					loan.setAmount(loan.getAmount().subtract(param));
					// loan > param
					if(loan.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
						loan.remove();
					}
				}
			}
			/**
			 * until
			 * [2] as String
			 * first char = (+ if to extend duration) , (- if to lower duration)
			 * following stuff = 1y2d3m => 1 year, 2 days, 3 months
			 * or 1ydM => 1 year, 1 day , 1 month
			 */
			else if(arguments[1].equalsIgnoreCase("until")) {
				String param = arguments[2];
				if(!(param.length() > 2)) {
					send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [Value]");
					return;
				}
				int leftMinutes = 0;
				boolean flush = false;
				String cache = "";
				for(int pointer = 1; pointer < param.length(); pointer++) {
					char tmp = param.charAt(pointer);
					if(tmp >= '0' && tmp <= '9') {
						if(flush) {
							cache = "";
							flush = false;
						}
						cache += tmp; 
					}else{
						if(((String)""+tmp).equalsIgnoreCase("d")) {
							leftMinutes += Integer.parseInt(cache) * 24 * 60;
							flush = true;
						}else if(((String)""+tmp).equals("m")) {
							leftMinutes += Integer.parseInt(cache);
							flush = true;
						}else if(((String)""+tmp).equals("M")) {
							leftMinutes += Integer.parseInt(cache) * 24 * 60 * 30;
							flush = true;
						}else if(((String)""+tmp).equalsIgnoreCase("h")) {
							leftMinutes += Integer.parseInt(cache) * 60;
							flush = true;
						}else{
							send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [Value]");
							return;
						}
					}
				}
				if(((String)""+param.charAt(0)).equals("+")) {
					loan.setLeftTime(loan.getLeftMinutes() + leftMinutes);
				}else{
					loan.setLeftTime(loan.getLeftMinutes() - leftMinutes > 0 ? loan.getLeftMinutes() - leftMinutes  : 0);
				}
			}else{
				send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue()+" [Value]");
				return;
			}
			//success
			send(sender, "&g&"+Configuration.StringEntry.SuccessLoanEdit.getValue());
		}else{
			send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.getValue());
		}
 	}
	public String getHelp() {
		return Configuration.StringEntry.LoanEditDescription.getValue();
	}
}
