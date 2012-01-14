package com.iBank.system;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.iBank.Database.AndCondition;
import com.iBank.Database.Condition;
import com.iBank.Database.DataSource;
import com.iBank.Database.QueryResult;

/**
 * The backend for (nearly) all code
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
		QueryResult data = DataSource.query(new String[]{"loc1","loc2","onper","offper"}, Configuration.Entry.DatabaseRegionTable.toString(), new AndCondition("name", name, Condition.Operators.IDENTICAL));
		if(!data.found) return null;
		Region ret = new Region(name, data.getString("loc1"), data.getString("loc2"));
		if(data.getString("onper").length()>0) {
			ret.setOnPercentage(Double.parseDouble(data.getString("onper")), false);
		}
		if(data.getString("offper").length()>0) {
			ret.setOffPercentage(Double.parseDouble(data.getString("offper")), false);
		}
		return ret;
	}
	/**
	 * Returns a list of all regions
	 * @return List<String>
	 */
	public static List<String> getRegions() {
		QueryResult data = DataSource.query(new String[]{"name"}, Configuration.Entry.DatabaseRegionTable.toString());
		if(!data.found) return null;
		List<String> ret = new ArrayList<String>();
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
		QueryResult data = DataSource.query(new String[]{"loc1","loc2","onper","offper"}, Configuration.Entry.DatabaseRegionTable.toString(), new AndCondition("name", name, Condition.Operators.IDENTICAL));
		return data.found;
	}
	/**
	 * Creates a region
	 * @param name Name of the region
	 * @param first First location
	 * @param second Second location
	 */
	public static void createRegion(String name, Location first, Location second) {
		DataSource.insertEntry(Configuration.Entry.DatabaseRegionTable.toString(), new String[]{"name","loc1","loc2","onper","offper"}, new String[]{name, Bank.formLocation(first), Bank.formLocation(second), "", ""});
	}
	
	/**
	 * Removes a region 
	 * @param name Name of the region 
	 */
	public static void removeRegion(String name) {
		DataSource.deleteEntry(Configuration.Entry.DatabaseRegionTable.toString(), new AndCondition("name", name, Condition.Operators.IDENTICAL));
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
		QueryResult data = DataSource.query(new String[]{"balance", "owners", "users", "onper", "offper"}, Configuration.Entry.DatabaseAccountsTable.toString(), new AndCondition("name", name, Condition.Operators.IDENTICAL));
		if(!data.found) return null;
		BankAccount obj = new BankAccount(name, data.getBigInteger("balance"));
		obj.Users(data.getString("users"));
		obj.Owners(data.getString("owners"));
		if(data.hasKey("onper")) obj.setOnPercentage(data.getDouble("onper"), false);
		if(data.hasKey("offper")) obj.setOffPercentage(data.getDouble("offper"), false);
		return obj;
	}
	/**
	 * Gets a list of bankaccounts
	 * @return List<String>
	 */
	public List<String> getAccounts() {
		QueryResult data = DataSource.query(new String[]{"name"}, Configuration.Entry.DatabaseAccountsTable.toString());
		if(!data.found) return null;
		List<String> ret = new ArrayList<String>();
		boolean c = true;
		while(c == true) {
			ret.add(data.getString("name"));
			c = data.nextEntry();
		}
		return ret;
	}
}
