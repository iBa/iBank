package com.ibank.system;

import com.ibank.Database.AndCondition;
import com.ibank.Database.Condition.Operators;
import com.ibank.Database.DataSource;
import com.ibank.utils.StringUtils;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This object represents a BankAccount
 * @author steffengy
 *
 */
public class BankAccount 
{
	private String name;
	private BigDecimal balance = null;
	private double on = Configuration.Entry.InterestOnPercentage.getDouble();
	public boolean onDefault = true;
	private double off = Configuration.Entry.InterestOffPercentage.getDouble();
	public boolean offDefault = true;
	private List<UUID> owners = new ArrayList<UUID>();
	private List<UUID> users = new ArrayList<UUID>();
	private int interval = Configuration.Entry.InterestPeriod.getInteger();
	public boolean intervalDefault = true;
	public int mD = 0;
	
	public BankAccount(String name, BigDecimal bigInteger) 
	{
		this.balance = bigInteger;
		this.name = name;
	}
	
	/**
	 * Inits all owners as string
	 */
	public void initOwners(String rawString)
	{
        owners = new ArrayList<UUID>();
        String rawStrings[] = rawString.split(",");
        for(String s : rawStrings) {
            if(s == null ||s.trim().isEmpty()) {
                continue;
            }
            owners.add(UUID.fromString(s));
        }
	}
	
	/**
	 * Inits all users as string
	 */
	public void initUsers(String rawString)
	{
        users = new ArrayList<UUID>();
        String rawStrings[] = rawString.split(",");
        for(String s : rawStrings) {
            if(s == null ||s.trim().isEmpty()) {
                continue;
            }
            users.add(UUID.fromString(s));
        }
	}
	
	/**
	 * Sets the online percentage
	 * @param onper The online percentage as double
	 * @param write If to write
	 */
	public void setOnPercentage(double onper, boolean write) 
	{
		this.on = onper;
		//Write to db
	    if(write) DataSource.update(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{"onper"}, new Object[]{String.valueOf(on)}, new AndCondition("name", name, Operators.IDENTICAL));
	    onDefault = false;
	}
	
	/**
	 * Sets the offline percentage
	 * @param offper The offline percentage as double
	 * @param write If to write
	 */
	public void setOffPercentage(double offper, boolean write) 
	{
		this.off = offper;
		//Write to db
	    if(write) DataSource.update(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{"offper"}, new Object[]{String.valueOf(off)}, new AndCondition("name", name, Operators.IDENTICAL));
	    offDefault = false;
	}
	
	/**
	 * Sets the balance
	 * @param newbalance The new balance as BigInteger
	 * @param write The boolean
	 */
	public void setBalance(BigDecimal newbalance, boolean write) 
	{
		// round
		newbalance = newbalance.setScale(2, BigDecimal.ROUND_DOWN);
		//save
		this.balance = newbalance;
		//Write to DB
		if(write) DataSource.update(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{"balance"}, new Object[]{newbalance}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	
	/**
	 * Adds a balance to this account
	 * @param todp BigInteger
	 */
	public void addBalance(BigDecimal todp) 
	{
		setBalance(this.balance.add(todp), true);
	}
	
	/**
	 * Returns the balance
	 * @return BigInteger
	 */
	public BigDecimal getBalance() 
	{
		return balance;
	}
	
	/**
	 * Returns if the account has that amount
	 * @param amount The amount
	 * @return Boolean
	 */
	public boolean has(BigDecimal amount) 
	{
		return balance.compareTo(amount) >= 0;
	}
	
	/**
	 * Subtracts a value from this account
	 * @param balance BigInteger
	 */
	public void subtractBalance(BigDecimal balance) 
	{
		BigDecimal newval = this.balance.subtract(balance);
		setBalance(newval, true);
	}
	
	/**
	 * Returns the online percentage
	 * @return double
	 */
	public double getOnlinePercentage() 
	{
		return this.on;
	}
	
	/**
	 * Returns the offline percentage
	 * @return double
	 */
	public double getOfflinePercentage() 
	{
		return this.off;
	}
	
	/**
	 * Adds an owner
	 * @param user The user
	 */
	public void addOwner(UUID user)
	{
		owners.add(user);
		DataSource.update(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{"owners"}, new Object[]{StringUtils.join(owners,",")}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	
	/**
	 * Remove an owner
	 * @param user The owner as string
	 */
	public void removeOwner(UUID user)
	{
		owners.remove(user);
		DataSource.update(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{"owners"}, new Object[]{StringUtils.join(owners,",")}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	
	/**
	 * Adds an user
	 * @param user The user
	 */
	public void addUser(UUID user)
	{
		users.add(user);
		DataSource.update(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{"users"}, new Object[]{StringUtils.join(users,",")}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	
	/**
	 * Remove an user
	 * @param user The user as string
	 */
	public void removeUser(UUID user)
	{
		users.remove(user);
		DataSource.update(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{"users"}, new Object[]{StringUtils.join(users,",")}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	
	/**
	 * Returns all Owners
	 * @return List<String>
	 */
	public List<UUID> getOwners()
	{
		return owners;
	}
	
	/**
	 * Returns all users
	 * @return List<String>
	 */
	public List<UUID> getUsers()
	{
		return users;
	}
	
	/**
	 * Returns if the user is an owner of this account
	 * @param user The user
	 * @return boolean
	 */
	public boolean isOwner(UUID user)
	{
		return owners.contains(user);
	}
	
	/**
	 * Returns if the user is an user of this account
	 */
	public boolean isUser(UUID user)
	{
		return users.contains(user);
	}
	
	/**
	 * Returns a string array with all users online
	 * @param limit The max count of users who shall got
	 * @return String[] Contains the usernames
	 */
	public UUID[] getOnlines(int limit)
	{
		List<UUID> b = new ArrayList<UUID>();
		int c = 0;
		for(UUID p : owners)
		{
			if(Bukkit.getPlayer(p) != null)
				if(limit == -1 || c < limit) 
				{
				    if(!b.contains(p)) b.add(p); 
					c++;
				}
				else break;	
		}
		for(UUID p : users)
		{
			if(Bukkit.getPlayer(p) != null)
				if(limit == -1 || c < limit) 
				{
					if(!b.contains(p)) b.add(p);
					c++;
				}
				else break;
		}
		return b.toArray(new UUID[b.size()]);
	}
	
	/**
	 * Returns a string array with all users online without limit
	 * @return String[] Contains the usernames
	 */
	public UUID[] getOnlines()
	{
		return getOnlines(-1);
	}

	/**
	 * Returns the name of the account
	 * @return String
	 */
	public String getName() 
	{
		return name;
	}
	
	/**
	 * Get the interval
	 * @return int
	 */
	public int getInterval() 
	{
		return this.interval;
	}
	
	/**
	 * Set the interval
	 * @param interval The interval
	 * @param write if to write in Storage
 	 */
	public void setInterval(int interval,boolean write) 
	{
		this.interval = interval;
		intervalDefault = false;
		if(write) DataSource.update(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{"interval"}, new Object[]{interval}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	
	/**
	 * Sets how much "loops" this account already "did"
	 * @param integer The integer describing the amount
	 * @param write If to write to db
	 */
	public void setMinutesDone(int integer, boolean write) 
	{
		this.mD = integer;
		if(write) DataSource.update(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{"mD"}, new Object[]{integer}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	
	/**
	 * Returns how much "loops" this account already "did"
	 * @return Integer
	 */
	public int getMinutesDone() 
	{
		return this.mD;
	}
	
	/**
	 * Updates an field in this account to value2
	 */
	public void Update(String key, String value) 
	{
		DataSource.update(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{ key }, new Object[]{ value }, new AndCondition("name", name, Operators.IDENTICAL));
	}
}
