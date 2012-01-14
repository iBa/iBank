package com.iBank.system;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private BigInteger balance = null;
	private double on = Configuration.Entry.InterestOnPercentage.getDouble();
	private boolean onDefault = true;
	private double off = Configuration.Entry.InterestOffPercentage.getDouble();
	private boolean offDefault = true;
	private List<String> owners = new ArrayList<String>();
	private List<String> users = new ArrayList<String>();
	
	public BankAccount(String name, BigInteger balance) {
		this.balance = balance;
	}
	/**
	 * Inits all owners as string
	 * @param str
	 */
	public void Owners(String str) {
		owners = new ArrayList<String>(Arrays.asList(str.split(",")));
	}
	/**
	 * Inits all users as string
	 * @param str
	 */
	public void Users(String str) {
		users = new ArrayList<String>(Arrays.asList(str.split(",")));
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
	public void setBalance(BigInteger newbalance, boolean write) {
		this.balance = newbalance;
		//Write to DB
		if(write) DataSource.update(Configuration.Entry.DatabaseAccountsTable.toString(), new String[]{"balance"}, new Object[]{newbalance}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	/**
	 * Adds a balance to this account
	 * @param balance BigInteger
	 */
	public void addBalance(BigInteger balance) {
		setBalance(this.balance.add(balance), true);
	}
	/**
	 * Returns the balance
	 * @return BigInteger
	 */
	public BigInteger getBalance() {
		return balance;
	}
	/**
	 * Subtracts a value from this account
	 * @param balance BigInteger
	 */
	public void substractBalance(BigInteger balance) {
		BigInteger newval = this.balance.subtract(balance);
		setBalance(newval.compareTo(BigInteger.ZERO)>0?newval:BigInteger.ZERO, true);
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
}
