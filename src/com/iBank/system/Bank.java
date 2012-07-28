package com.iBank.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.iBank.Database.AndCondition;
import com.iBank.Database.Condition;
import com.iBank.Database.DataSource;
import com.iBank.Database.QueryResult;
import com.iBank.Database.Condition.Operators;
import com.iBank.Event.iBankEvent;
import com.iBank.Event.iEvent;

/**
 * The backend for all background code
 * f.e. getting accounts => API
 * @author steffengy
 *
 */
public class Bank {
	/**
	 * Gets an region from Database
	 * @param name the Name of the region
	 * @return the Region
	 */
	public static Region getRegion(String name) {
		// fetch region data
		QueryResult data = DataSource.query(new String[]{"loc1","loc2","onper","offper","owners"}, Configuration.Entry.DatabaseRegionTable.getValue(), new AndCondition("name", name, Condition.Operators.IDENTICAL));
		if(!data.found) return null;
		Region ret = new Region(name, data.getString("loc1"), data.getString("loc2"));
		if(data.getString("onper").length()>0) {
			ret.setOnPercentage(Double.parseDouble(data.getString("onper")), false);
		}
		if(data.getString("offper").length()>0) {
			ret.setOffPercentage(Double.parseDouble(data.getString("offper")), false);
		}
		if(data.getString("owners").length()>0) {
			ret.Owners(data.getString("owners"));
		}
		return ret;
	}
	/**
	 * Returns a list of all regions
	 * @return List<String>
	 */
	public static List<String> getRegions() {
		QueryResult data = DataSource.query(new String[]{"name"}, Configuration.Entry.DatabaseRegionTable.getValue());
		List<String> ret = new ArrayList<String>();
		if(!data.found) return ret;
		boolean c = true;
		while(c == true) {
			ret.add(data.getString("name"));
			c = data.nextEntry();
		}
		return ret;
	}
	/**
	 * Returns if the region was found
	 * @param name The name of the region
	 * @return boolean
	 */
	public static boolean hasRegion(String name) {
		QueryResult data = DataSource.query(new String[]{"loc1","loc2","onper","offper"}, Configuration.Entry.DatabaseRegionTable.getValue(), new AndCondition("name", name, Condition.Operators.IDENTICAL));
		return data.found;
	}
	/**
	 * Creates a region
	 * @param name Name of the region
	 * @param first First location
	 * @param second Second location
	 */
	public static void createRegion(String name, Location first, Location second) {
		//iBank - call Event
		iBankEvent event = new iBankEvent(iEvent.Types.REGION_CREATE, new Object[] { name, first, second } );
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			return;
		}
		//iBank - end
		DataSource.insertEntry(Configuration.Entry.DatabaseRegionTable.getValue(), new String[]{"name","loc1","loc2","onper","offper", "owners"}, new String[]{name, Bank.formLocation(first), Bank.formLocation(second), "", "", ""});
	}
	
	/**
	 * Removes a region 
	 * @param name Name of the region 
	 */
	public static void removeRegion(String name) {
		//iBank - call Event
				iBankEvent event = new iBankEvent(iEvent.Types.REGION_DELETE, name );
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(event.isCancelled()) {
					return;
				}
		//iBank - end
		DataSource.deleteEntry(Configuration.Entry.DatabaseRegionTable.getValue(), new AndCondition("name", name, Condition.Operators.IDENTICAL));
	}
	
	/**
	 * Converts a location to an iBank Location String
	 * @param loc The location
	 * @return
	 */
	private static String formLocation(Location loc) {
		return loc.getWorld().getName()+";"+loc.getX()+";"+loc.getY()+";"+loc.getZ();
	}
	
	/**
	 * Gets an Bankaccount by Name
	 * @param name The name
	 * @return
	 */
	public static BankAccount getAccount(String name) {
		QueryResult data = DataSource.query(new String[]{"balance", "owners", "users", "onper", "offper", "interval", "mD"}, Configuration.Entry.DatabaseAccountsTable.getValue(), new AndCondition("name", name, Condition.Operators.IDENTICAL));
		if(!data.found) return null;
		BankAccount obj = new BankAccount(name, data.getBigInteger("balance"));
		obj.Users(data.getString("users"));
		obj.Owners(data.getString("owners"));
		if(data.hasKey("onper") && data.getString("onper").length() > 0) obj.setOnPercentage(data.getDouble("onper"), false);
		if(data.hasKey("offper") && data.getString("offper").length()>0) obj.setOffPercentage(data.getDouble("offper"), false);
		if(data.hasKey("interval")) obj.setInterval(data.getInteger("interval"), false);
		if(data.hasKey("mD")) obj.setMinutesDone(data.getInteger("mD"), false);
		return obj;
	}
	/**
	 * Returns if this account exists
	 * @param name The accountname
	 * @return boolean
	 */
	public static boolean hasAccount(String name) {
		QueryResult data = DataSource.query(new String[]{"balance"}, Configuration.Entry.DatabaseAccountsTable.getValue(), new AndCondition("name", name, Condition.Operators.IDENTICAL));
		return data.found;
	}
	/**
	 * Gets a list of bankaccounts
	 * @return List<String>
	 */
	public static List<String> getAccounts() {
		QueryResult data = DataSource.query(new String[]{"name"}, Configuration.Entry.DatabaseAccountsTable.getValue());
		List<String> ret = new ArrayList<String>();
		if(!data.found) return ret;
		boolean c = true;
		while(c == true) {
			ret.add(data.getString("name"));
			c = data.nextEntry();
		}
		return ret;
	}
	/**
	 * Gets accounts where user has the user role
	 * @param user The username
	 * @return List<String>
	 */
	public static List<String> getAccountsByUser(String user) {
		QueryResult data = DataSource.query(new String[]{"name", "users"}, Configuration.Entry.DatabaseAccountsTable.getValue());
		List<String> ret = new ArrayList<String>();
		if(!data.found) return ret;
		boolean c = true;
		String str = "";
		List<String> users = null;
		while(c == true) {
			str = data.getString("users");
			if(str.contains(","))
				users = new ArrayList<String>(Arrays.asList(str.split(",")));
			else {
				users = new ArrayList<String>();
				users.add(str);
			}
			if(users.contains(user)) ret.add(data.getString("name"));
			c = data.nextEntry();
		}
		return ret;
	}
	/**
	 * Gets accounts where user has the owner role
	 * @param user The username
	 * @return List<String>
	 */
	public static List<String> getAccountsByOwner(String user) {
		QueryResult data = DataSource.query(new String[]{"name", "owners"}, Configuration.Entry.DatabaseAccountsTable.getValue());
		List<String> ret = new ArrayList<String>();
		if(!data.found) return ret;
		boolean c = true;
		String str = "";
		List<String> users = null;
		while(c == true) {
			str = data.getString("owners");
			if(str.contains(","))
				users = new ArrayList<String>(Arrays.asList(str.split(",")));
			else {
				users = new ArrayList<String>();
				users.add(str);
			}
			if(users.contains(user)) ret.add(data.getString("name"));
			c = data.nextEntry();
		}
		return ret;
	}
	/**
	 * Generates an user 
	 * @param name The name of the account
	 * @param name2 The name of the owner
	 */
	public static void createAccount(String name, String name2) {
		//iBank - Call event
		iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_CREATE, new String[] { name, name2 });
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			return;
		}
		//iBank - end
		DataSource.insertEntry(Configuration.Entry.DatabaseAccountsTable.getValue(), new String[]{"name","balance","owners","users","onper","offper"}, new Object[] {name, Configuration.Entry.StandardBalance.getInteger(), name2, "", "", ""});
	}
	/**
	 * Removes an account
	 * @param name The name of the account
	 */
	public static void removeAccount(String name) {
		//iBank - call Event
				iBankEvent event = new iBankEvent(iEvent.Types.ACCOUNT_DELETE, name );
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(event.isCancelled()) {
					return;
				}
		//iBank - end
		DataSource.deleteEntry(Configuration.Entry.DatabaseAccountsTable.getValue(), new AndCondition("name", name, Condition.Operators.IDENTICAL)); 
	}
	
	/**
	 * Returns a list with all loans
	 * @return List<Loan>
	 */
	public static List<Loan> getLoans() {
		QueryResult data = DataSource.query(new String[]{"id", "user", "amount", "percentage", "interval", "until", "mD"},  Configuration.Entry.DatabaseLoanTable.getValue());
		List<Loan> ret = new ArrayList<Loan>();
		if(!data.found) return ret;
		boolean c = true;
		double interest = 0.00;
		while(c == true) {
			if(data.hasKey("interest")) interest = data.getDouble("interest");
			else interest = Configuration.Entry.LoanInterest.getDouble();
			ret.add(new Loan(data.getString("user"), interest , data.getInteger("interval"), data.getLong("until"), data.getBigInteger("amount"), data.getInteger("mD"), data.getInteger("id"))); 
			c = data.nextEntry();
		}
		return ret;
	}
	/**
	 * Gets a list with all loans of the user
	 * @param user The user
	 * @return
	 */
	public static List<Loan> getLoansByAccount(String user) {
		QueryResult data = DataSource.query(new String[]{"id", "user", "amount", "percentage", "interval", "until", "mD"},  Configuration.Entry.DatabaseLoanTable.getValue(), new AndCondition("user", user, Operators.IDENTICAL));
		List<Loan> ret = new ArrayList<Loan>();
		if(!data.found) return ret;
		boolean c = true;
		double interest = 0.00;
		while(c == true) {
			if(data.hasKey("interest")) interest = data.getDouble("interest");
			else interest = Configuration.Entry.LoanInterest.getDouble();
			ret.add(new Loan(data.getString("user"), interest, data.getInteger("interval"), data.getLong("until"), data.getBigInteger("amount"), data.getInteger("mD"), data.getInteger("id"))); 
			c = data.nextEntry();
		}
		return ret;
	}
	/**
	 * Gets the Loan with the id
	 * @param id int id
	 * @return Loan
	 */
	public static Loan getLoanById(int id) {
		QueryResult data = DataSource.query(new String[]{"id", "user", "amount", "percentage", "interval", "until", "mD"},  Configuration.Entry.DatabaseLoanTable.getValue(), new AndCondition("id", id, Operators.IDENTICAL));
		if(!data.found) return null;
		Loan ret = null;
		double interest = 0.00;
			if(data.hasKey("interest")) interest = data.getDouble("interest");
			else interest = Configuration.Entry.LoanInterest.getDouble();
			ret = new Loan(data.getString("user"), interest, data.getInteger("interval"), data.getLong("until"), data.getBigInteger("amount"), data.getInteger("mD"), data.getInteger("id")); 
		return ret;
	}
	
}
