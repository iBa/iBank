package com.iBank.system;

import java.math.BigInteger;

import com.iBank.Database.DataSource;

/**
 * This class represents a loan
 * @author steffengy
 *
 */
public class Loan {
	private BigInteger amount;
	private String user;
	private double interest;
	private double interval;
	private long time;
	
	/**
	 * Creates a new loan
	 * @param username The username
	 * @param interest The interest (f.e. 1 would be handled as 1 %)
	 * @param interval The interval, his interest-product grows
	 * @param time How much time (in seconds) the "loan-taker" has to pay-back
	 * @param amount The amount which he received
	 * @param boolean If given, loan will be created
	 */
	public Loan(String username, double interest, double interval,long time, BigInteger amount, boolean create) {
		this(username, interest, interval, time, amount);
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000L + time);
		DataSource.insertEntry(Configuration.Entry.DatabaseLoanTable.toString(), new String[] { "user", "amount", "percentage", "until", "interval" } , new Object[] { username, amount, interest, timestamp, interval }); 
	}
	
	public Loan(String username, double interest, double interval, long time, BigInteger amount) {
		this.user = username;
		this.amount = amount;
		this.interest = interest;
		this.interval = interval;
		this.time = time;
	}
	
	
	
}
