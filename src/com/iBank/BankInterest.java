package com.iBank;
import java.math.BigInteger;
import java.util.TimerTask;

import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Configuration;

public class BankInterest extends TimerTask {

	@Override
	public void run() {
		int needed = Configuration.Entry.InterestOnline.getInteger();
		String on = Configuration.Entry.InterestOnPercentage.toString();
		String off = Configuration.Entry.InterestOffPercentage.toString();
		
		//get all bank accounts
		for(String i : Bank.getAccounts())
		{
			BankAccount item = Bank.getAccount(i);
			if(item.getOnlines(needed + 1).length == needed)
			{
				BigInteger add = item.getBalance().divide(new BigInteger("100")).multiply(new BigInteger(on));
				item.addBalance(add);
			}else{
				BigInteger add = item.getBalance().divide(new BigInteger("100")).multiply(new BigInteger(off));
				item.addBalance(add);
			}
		}
	}

}