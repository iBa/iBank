package com.iBank.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;

import com.iBank.Database.AndCondition;
import com.iBank.Database.DataSource;
import com.iBank.Database.Condition.Operators;
import com.iBank.utils.StringUtils;

/**
 * This object represents a BankAccount
 * @author steffengy
 *
 */
public class BankAccount {
	private String name;
	private BigDecimal balance = null;
	private double on = Configuration.Entry.InterestOnPercentage.getDouble();
	public boolean onDefault = true;
	private double off = Configuration.Entry.InterestOffPercentage.getDouble();
	public boolean offDefault = true;
	private List<String> owners = new ArrayList<String>();
	private List<String> users = new ArrayList<String>();
	
	public BankAccount(String name, BigDecimal bigInteger) {
		this.balance = bigInteger;
		this.name = name;
	}
	/**
	 * Inits all owners as string
	 * @param str
	 */
	public void Owners(String str) {
		if(str.contains(","))
			owners = new ArrayList<String>(Arrays.asList(str.split(",")));
		else {
			owners = new ArrayList<String>();
			owners.add(str);
		}
		
	}
	/**
	 * Inits all users as string
	 * @param str
	 */
	public void Users(String str) {
		if(str.contains(",")) 
			users = new ArrayList<String>(Arrays.asList(str.split(",")));
		else {
			users = new ArrayList<String>();
			users.add(str);
		}
	}
	/**
	 * Sets the online percentage
	 * @param onper The online percentage as double
	 * @param write If to write
	 */
	public void setOnPercentage(double onper, boolean write) {
		this.on = onper;
		//Write to db
	    if(write) DataSource.update(Configuration.Entry.DatabaseAccountsTable.toString(), new String[]{"onper"}, new Object[]{String.valueOf(on)}, new AndCondition("name", name, Operators.IDENTICAL));
	    onDefault = false;
	}
	/**
	 * Sets the offline percentage
	 * @param offper The offline percentage as double
	 * @param write If to write
	 */
	public void setOffPercentage(double offper, boolean write) {
		this.off = offper;
		//Write to db
	    if(write) DataSource.update(Configuration.Entry.DatabaseAccountsTable.toString(), new String[]{"offper"}, new Object[]{String.valueOf(off)}, new AndCondition("name", name, Operators.IDENTICAL));
	    offDefault = false;
	}
	/**
	 * Sets the balance
	 * @param newbalance The new balance as BigInteger
	 * @param write The boolean
	 */
	public void setBalance(BigDecimal newbalance, boolean write) {
		// round
		newbalance = newbalance.setScale(2, BigDecimal.ROUND_DOWN);
		//save
		this.balance = newbalance;
		//Write to DB
		if(write) DataSource.update(Configuration.Entry.DatabaseAccountsTable.toString(), new String[]{"balance"}, new Object[]{newbalance}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	/**
	 * Adds a balance to this account
	 * @param todp BigInteger
	 */
	public void addBalance(BigDecimal todp) {
		setBalance(this.balance.add(todp), true);
	}
	/**
	 * Returns the balance
	 * @return BigInteger
	 */
	public BigDecimal getBalance() {
		return balance;
	}
	/**
	 * Returns if the account has that amount
	 * @param amount The amount
	 * @return Boolean
	 */
	public boolean has(BigDecimal amount) {
		return balance.compareTo(balance) >= 0;
	}
	/**
	 * Subtracts a value from this account
	 * @param balance BigInteger
	 */
	public void subtractBalance(BigDecimal balance) {
		BigDecimal newval = this.balance.subtract(balance);
		setBalance(newval.compareTo(BigDecimal.ZERO)>0?newval:BigDecimal.ZERO, true);
	}
	/**
	 * Returns the online percentage
	 * @return double
	 */
	public double getOnlinePercentage() {
		return this.on;
	}
	/**
	 * Returns the offline percentage
	 * @return double
	 */
	public double getOfflinePercentage() {
		return this.off;
	}
	/**
	 * Adds an owner
	 * @param user The user
	 */
	public void addOwner(String user) {
		owners.add(user);
		DataSource.update(Configuration.Entry.DatabaseAccountsTable.toString(), new String[]{"owners"}, new Object[]{StringUtils.join(owners,",")}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	/**
	 * Adds an user
	 * @param user The user
	 */
	public void addUser(String user) {
		users.add(user);
		DataSource.update(Configuration.Entry.DatabaseAccountsTable.toString(), new String[]{"users"}, new Object[]{StringUtils.join(users,",")}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	/**
	 * Returns all Owners
	 * @return List<String>
	 */
	public List<String> getOwners() {
		return owners;
	}
	/**
	 * Returns all users
	 * @return List<String>
	 */
	public List<String> getUsers() {
		return users;
	}
	/**
	 * Returns if the user is an owner of this account
	 * @param user The user
	 * @return boolean
	 */
	public boolean isOwner(String user) {
		return owners.contains(user);
	}
	/**
	 * Returns if the user is an user of this account
	 * @param user 
	 * @return
	 */
	public boolean isUser(String user) {
		return users.contains(user);
	}
	/**
	 * Returns a string array with all users online
	 * @param int limit The max count of users who shall got
	 * @return String[] Contains the usernames
	 */
	public String[] getOnlines(int limit) {
		List<String> b = new ArrayList<String>();
		int c = 0;
		for(String p : owners) {
			if(Bukkit.getServer().getPlayer(p) != null) 
				if(limit == -1 || c > limit) {
					b.add(p); 
					c++;
				}else{
					break;
				}
		}
		for(String p : users) {
			if(Bukkit.getServer().getPlayer(p) != null)
				if(limit == -1 || c > limit) {
					b.add(p);
					c++;
				}else{
					break;
				}
		}
		return b.toArray(new String[1]);
	}
	/**
	 * Returns a string array with all users online without limit
	 * @return String[] Contains the usernames
	 */
	public String[] getOnlines() {
		return getOnlines(-1);
	}
}
