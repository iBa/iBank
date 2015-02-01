package com.ibank;

import com.ibank.system.Bank;
import com.ibank.system.BankAccount;
import com.ibank.system.Configuration;
import com.ibank.system.Loan;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.TimerTask;

public class BankLoan extends TimerTask 
{

	@Override
	public void run() 
	{
		if(Configuration.Entry.Debug.getBoolean()) System.out.println("Loan loaded");

		for(Loan a : Bank.getLoans()) 
		{
			//moved below, to prevent exceptions , we not want.
			a.setMinutesDone(a.getMinutesDone() + 1);
			//If interval is reached, +money
			//<= to fix probably not calculated possibilitys which are MAGIC
			if(a.getInterval() <= a.getMinutesDone()) 
			{
				a.setAmount(a.getAmount().multiply(new BigDecimal((1+(a.getInterest() / 100)))));
				a.setMinutesDone(0);
			}
			//Is time reached?
			if(a.getLeftTime() <= 0) 
			{
				//Now he will get some punishment...
				if(Configuration.Entry.LoanForceInterest.getBoolean()) a.setAmount(a.getAmount().multiply(new BigDecimal((1+(a.getInterest() / 100)))));
				if(Configuration.Entry.LoanForceMoney.getBoolean()) 
				{
					if(iBank.economy.has(Bukkit.getPlayer(a.getUser()), a.getAmount().doubleValue()))
					{
						iBank.economy.withdrawPlayer(Bukkit.getPlayer(a.getUser()), a.getAmount().doubleValue());
						a.remove(); //close
					}
					else
					{
						a.setAmount(a.getAmount().subtract(new BigDecimal(iBank.economy.getBalance(Bukkit.getPlayer(a.getUser())))));
						iBank.economy.withdrawPlayer(Bukkit.getPlayer(a.getUser()), iBank.economy.getBalance(Bukkit.getPlayer(a.getUser())));
					}
				}
				if(Configuration.Entry.LoanForceBank.getBoolean()) 
				{
					BankAccount tmpaccount;
					for(String raw : Bank.getAccountsByOwner(a.getUser())) 
					{
						tmpaccount = Bank.getAccount(raw);
						if(tmpaccount.getBalance().compareTo(a.getAmount()) > 0) 
						{
							//bank account has more
							tmpaccount.subtractBalance(a.getAmount());
							a.setAmount(new BigDecimal("0"));
							a.remove();
						}
						else
						{
							//bank account has not that much
							a.setAmount(a.getAmount().subtract(tmpaccount.getBalance()));
							tmpaccount.setBalance(new BigDecimal("0"), true);
						}
					}
				}
			}
		}
		if(Configuration.Entry.Debug.getBoolean()) System.out.println("Loan end!");
	}

}
