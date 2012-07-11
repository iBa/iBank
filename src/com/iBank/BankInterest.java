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
		if(Configuration.Entry.Debug.getBoolean()) System.out.println("Interest loaded");
		int needed = Configuration.Entry.InterestOnline.getInteger();
		BigDecimal on =  new BigDecimal(Configuration.Entry.InterestOnPercentage.getDouble() / 100);
		BigDecimal off = new BigDecimal(Configuration.Entry.InterestOffPercentage.getDouble() / 100);
		
		//get all bank accounts
		
		List<String> it = Bank.getAccounts();
		if(it.size()==0) return;
		
		int debugOnline = 0;
		int debugOffline = 0;
		 
		//loop through accounts
		for(String i : it)
		{
			BankAccount item = Bank.getAccount(i);
			// >= to prevent cases which are MAGIC
			// -1 to balance indifference in runtime
			if(item.getMinutesDone() >= item.getInterval() - 1) {
			
				on = new BigDecimal(item.getOnlinePercentage() / 100);
				off = new BigDecimal(item.getOfflinePercentage() / 100);
				if(item.getOnlines(needed + 1).length >= needed)
				{
					debugOnline++;
					BigDecimal add = item.getBalance().multiply(on);
					item.addBalance(add);
				}else{
					debugOffline++;
					BigDecimal add = item.getBalance().multiply(off);
					item.addBalance(add);
				}
				//now we can simply set mD to 0
				item.setMinutesDone(0, true);
			}else{
				//++mD
				item.setMinutesDone(item.getMinutesDone() + 1, true);
			}
		}
		if(Configuration.Entry.Debug.getBoolean()) System.out.println("Interest done, Online: "+debugOnline+", Offline:"+debugOffline);
	}

}