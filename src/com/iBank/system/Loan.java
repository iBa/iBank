package com.iBank.system;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import com.iBank.Database.AndCondition;
import com.iBank.Database.DataSource;
import com.iBank.Database.Condition.Operators;

/**
 * This class represents a loan
 * @author steffengy
 *
 */
public class Loan {
	private BigDecimal amount;
	private int id = -1; //For saving
	private String user;
	private double interest;
	private int interval;
	private long time;
	private int minutesDone;
	
	/**
	 * Creates a new loan
	 * @param username The username
	 * @param interest The interest (f.e. 1 would be handled as 1 %)
	 * @param interval The interval, his interest-product grows
	 * @param time How much time (in seconds) the "loan-taker" has to pay-back
	 * @param amount The amount which he received
	 * @param boolean If given, loan will be created
	 */
	public Loan(String username, double interest, int interval,int time, BigDecimal amount, boolean create) {
		this(username, interest, interval, time, amount, 0, -1);
		int tmp = (int)(System.currentTimeMillis() / 1000L) + time;
		this.id = DataSource.insertEntry(Configuration.Entry.DatabaseLoanTable.getValue(), new String[] { "user", "amount", "percentage", "until", "interval", "mD" } , new Object[] { username, amount, interest, tmp, interval, 0 }, true); 
	}
	
	public Loan(String username, double interest, int interval, long time, BigDecimal amount,int mD, int id) {
		this.user = username;
		this.amount = amount;
		this.interest = interest;
		this.interval = interval;
		this.time = time;
		this.id = id;
		this.minutesDone = mD;
	}
	
	/**
	 * Returns how much seconds are left
	 */
	public long getLeftTime() {
		return time - System.currentTimeMillis()/1000L; 
	}
	/**
	 * Returns how much minutes are left
	 * @return int
	 */
	public int getLeftMinutes() {
		return (int) TimeUnit.SECONDS.toMinutes(getLeftTime()); 
	}
	/**
	 * Sets the left time
	 * @param seconds The minutes
	 * @return
	 */
	public void setLeftTime(int minutes) {
		if(id == -1) {
			throwIdError("SET_LEFT_TIME");
			return;
		}
		int minTMP = (int)(System.currentTimeMillis() / 100L) + (minutes * 60);
		DataSource.update(Configuration.Entry.DatabaseLoanTable.getValue(), new String[] { "until" }, new Object[] { minTMP } , new AndCondition("id", this.id, Operators.IDENTICAL)); 
		this.time = minTMP;
	}
	/**
	 * Return the interval in minutes
	 * @return int
	 */
	public int getInterval() {
		return interval;
	}
	/**
	 * Sets the interval of this loan
	 * @param seconds
	 */
	public void setInterval(int minutes) {
		if(id == -1) {
			throwIdError("SET_INTERVAL");
			return;
		}
		DataSource.update(Configuration.Entry.DatabaseLoanTable.getValue(), new String[] { "interval" }, new Object[] { minutes } , new AndCondition("id", this.id, Operators.IDENTICAL)); 
		this.interval = minutes;
	}
	/**
	 * returns the current interest
	 * @return double
	 */
	public double getInterest() {
		return interest;
	}
	/**
	 * sets the current interest
	 * @param interest double
	 */
	public void setInterest(double interest) {
		if(id == -1) {
			throwIdError("SET_INTEREST");
			return;
		}
		DataSource.update(Configuration.Entry.DatabaseLoanTable.getValue(), new String[] { "interest" }, new Object[] { interest } , new AndCondition("id", this.id, Operators.IDENTICAL)); 
		this.interest = interest;
	}
	/**
	 * Gets the amount of the loan
	 * @return BigDecimal
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * Sets the amount
	 * @param amount BigDecimal
	 */
	public void setAmount(BigDecimal amount) {
		if(id == -1) {
			throwIdError("SET_AMOUNT");
			return;
		}
		DataSource.update(Configuration.Entry.DatabaseLoanTable.getValue(), new String[] { "amount" }, new Object[] { amount } , new AndCondition("id", this.id, Operators.IDENTICAL)); 
		this.amount = amount;
	}
	/**
	 * Returns the username of the loan
	 * @return String
	 */
	public String getUser() {
		return user;
	}
	/**
	 * Returns how much minutes already went over
	 * @return int
	 */
	public int getMinutesDone() {
		return minutesDone;
	}
	/**
	 * Sets how much minutes are over
	 * @param count int
	 */
	public void setMinutesDone(int count) {
		if(id == -1) {
			throwIdError("SET_MINUTES_DONE");
			return;
		}
		DataSource.update(Configuration.Entry.DatabaseLoanTable.getValue(), new String[] { "mD" }, new Object[] { count } , new AndCondition("id", this.id, Operators.IDENTICAL)); 
		this.minutesDone = count;
	}
	/**
	 * Throws an id Error to console!
	 */
	private void throwIdError(String cause) {
		System.out.println("[iBank] Invalid Id Error in "+cause);
	}
	/**
	 * Deletes the loan from the database
	 */
	public void remove() {
		DataSource.deleteEntry(Configuration.Entry.DatabaseLoanTable.getValue(), new AndCondition("id", id, Operators.IDENTICAL));
	}
	/**
	 * Return the id
	 * @return int id
	 */
	public int getId() {
		return id;
	}
}
