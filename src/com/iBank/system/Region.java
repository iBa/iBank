package com.iBank.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.iBank.Database.AndCondition;
import com.iBank.Database.Condition.Operators;
import com.iBank.Database.DataSource;
import com.iBank.utils.StringUtils;

/**
 * This class represents a region
 * @author steffengy
 *
 */
public class Region {
	private String name;
	private Location first;
	private Location second;
	private List<String> Owners = new ArrayList<String>();
	private double on = Configuration.Entry.InterestOnPercentage.getDouble();
	public boolean onDefault = true;
	private double off = Configuration.Entry.InterestOffPercentage.getDouble();
	public boolean offDefault = true;
	
	/**
	 * "Creates a region"
	 * @param name Name of the region
	 * @param first First location of the region
	 * @param second Second location of the region
	 */
	
	public Region(String name,Location first, Location second) {
		this.name = name;
		this.first = first;
		this.second = second;
	}
	/**
	 * Set owners
	 * @param str String[]
	 */
	public void Owners(String str) {
		if(str.contains(","))
			Owners = new ArrayList<String>(Arrays.asList(str.split(",")));
		else {
			Owners = new ArrayList<String>();
			Owners.add(str);
		}
	}
	/**
	 * Adds an owner
	 * @param owner The name of the owner
	 */
	public void addOwner(String owner) {
		Owners.add(owner);
		DataSource.update(Configuration.Entry.DatabaseRegionTable.getValue(), new String[]{"owners"}, new Object[]{StringUtils.join(Owners,",")}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	/**
	 * Removes an owner
	 * @param owner The name of the owner
	 */
	public void removeOwner(String owner) {
		Owners.remove(owner);
		DataSource.update(Configuration.Entry.DatabaseRegionTable.getValue(), new String[]{"owners"}, new Object[]{StringUtils.join(Owners,",")}, new AndCondition("name", name, Operators.IDENTICAL));
	}
	/**
	 * Returns a list with the owners
	 * @return List<String>
	 */
	public List<String> getOwners() {
		return Owners;
	}
	/**
	 * Creates a region
	 * @param name The name
	 * @param first The location as String: world;x;y;z
	 * @param second The location as String world;x;y;z
	 */
	public Region(String name,String first, String second) {
		this.name = name;
		String[] rawFirst = first.split(";");
		String[] rawSeco = second.split(";");
		this.first = new Location(Bukkit.getWorld(rawFirst[0]), Double.parseDouble(rawFirst[1]),Double.parseDouble(rawFirst[2]),Double.parseDouble(rawFirst[3]));
		this.second = new Location(Bukkit.getWorld(rawSeco[0]), Double.parseDouble(rawSeco[1]),Double.parseDouble(rawSeco[2]),Double.parseDouble(rawSeco[3]));
	}
	/**
	 * Sets the online percentage of a region
	 * @param Double The online percentage
	 * @param boolean Save it?
	 */
	public void setOnPercentage(double on,boolean write) {
		this.on = on;
		//Write to db
	    if(write) DataSource.update(Configuration.Entry.DatabaseRegionTable.getValue(), new String[]{"onper"}, new Object[]{String.valueOf(on)}, new AndCondition("name", name, Operators.IDENTICAL));
	    onDefault = false;
	}
	/**
	 * Sets the offline percentage of a region
	 * @param Double The offline percentage
	 * @param Boolean save it?
	 */
	public void setOffPercentage(double off,boolean write) {
		this.off = off;
		//Write to db
	    if(write) DataSource.update(Configuration.Entry.DatabaseRegionTable.getValue(), new String[]{"offper"}, new Object[]{String.valueOf(off)}, new AndCondition("name", name, Operators.IDENTICAL));
	    offDefault = false;
	}
	/**
	 * Returns the online percentage in the region
	 * @return Double
	 */
	public double getOnPercentage() {
		return this.on;
	}
	/**
	 * Returns the offline percentage in the region
	 * @return Double
	 */
	public double getOffPercentage() {
		return this.off;
	}
	
	/**
	 * Get the first location
	 * @return Location 
	 */
	public Location getFirstLocation() {
		return this.first;
	}
	/**
	 * Get the second location
	 * @return Location 
	 */
	public Location getSecondLocation() {
		return this.second;
	}
	
	/**
	 * Save this region under _THE_ Name
	 * This can overwrite EXISTING regions!!
	 */
	public void save() {
		if(!Bank.hasRegion(name)) {
			Bank.createRegion(name, this.first, this.second);
		}
	}
}
