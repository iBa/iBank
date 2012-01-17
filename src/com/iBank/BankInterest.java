package com.iBank;
import java.math.BigDecimal;
import java.util.TimerTask;

import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Configuration;

public class BankInterest extends TimerTask {

	//@todo Look for custom percentage in account
	@Override
	public void run() {
		int needed = Configuration.Entry.InterestOnline.getInteger();
		BigDecimal on =  new BigDecimal(Configuration.Entry.InterestOnPercentage.getDouble() / 100);
		BigDecimal off = new BigDecimal(Configuration.Entry.InterestOffPercentage.getDouble() / 100);
		
		//get all bank accounts
		for(String i : Bank.getAccounts())
		{
			BankAccount item = Bank.getAccount(i);
			if(item.getOnlines(needed + 1).length >= needed)
			{
				BigDecimal add = item.getBalance().multiply(on);
				item.addBalance(add);
			}else{
				BigDecimal add = item.getBalance().multiply(off);
				item.addBalance(add);
			}
		}
	}

}