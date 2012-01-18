package com.iBank;
import java.math.BigDecimal;
import java.util.List;
import java.util.TimerTask;

import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Configuration;

public class BankInterest extends TimerTask {

	@Override
	public void run() {
		System.out.println("Interest loaded");
		int needed = Configuration.Entry.InterestOnline.getInteger();
		BigDecimal on =  new BigDecimal(Configuration.Entry.InterestOnPercentage.getDouble() / 100);
		BigDecimal off = new BigDecimal(Configuration.Entry.InterestOffPercentage.getDouble() / 100);
		
		//get all bank accounts
		
		List<String> it = Bank.getAccounts();
		if(it.size()==0) return;
		for(String i : it)
		{
			BankAccount item = Bank.getAccount(i);

			on = new BigDecimal(item.getOnlinePercentage() / 100);
			off = new BigDecimal(item.getOfflinePercentage() / 100);
			
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