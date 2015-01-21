package com.ibank.Database;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class QueryResult {
	private HashMap<Integer, HashMap<String, Object>> keyvalues = new HashMap<Integer, HashMap<String, Object>>();
	private int readpointer = 0; //readpointer = pointer at current position
	private int pointer = 0; //pointer = next empty position , -1 : ""
	public boolean found = false;
	
	/**
	 * Initials the object
	 */
	public QueryResult() 
	{
		newEntry();
	}
	
	/**
	 * Adds an entry to the result
	 * @param key The key
	 * @param value The entry
	 */
	public void add(String key, Object value) 
	{
		keyvalues.get(readpointer).put(key, value);
		found = true;
	}
	
	/**
	 * Returns if the current set contains this key
	 */
	public boolean hasKey(String string) 
	{
		return keyvalues.size() > readpointer && keyvalues.get(readpointer).containsKey(string);
	}
	
	/**
	 * Gets an entry from the result
	 */
	public Object get(String key) 
	{
		return keyvalues.size()>readpointer ? keyvalues.get(readpointer).get(key) : null;
	}
	
	/**
	 * Gets an entry from the result as String
	 */
	public String getString(String key) 
	{
		return keyvalues.size()>readpointer ? String.valueOf(keyvalues.get(readpointer).get(key)) : null;
	}
	
	/**
	 * Gets an entry from the result as Double
	 * @param key String
	 */
	public double getDouble(String key) 
	{
		try
		{
            String raw = String.valueOf(keyvalues.get(readpointer));
            double val = Double.parseDouble(raw);
            if(keyvalues.size() > readpointer) {
                return val;
            }
		}
		catch(Exception e) 
		{ 
			System.out.println("[iBank] "+key+" Error while parsing Double!"+e);
		}

        return 0.00;
	}
	
	/**
	 * Gets an entry from the result as BigDecimal
	 * @param key String
	 */
	public BigDecimal getBigInteger(String key) 
	{
		try
		{
			return keyvalues.size()>readpointer ? new BigDecimal(String.valueOf(keyvalues.get(readpointer).get(key))) : null;
		}
		catch(Exception e) 
		{ 
			return null; 
		}
	}
	
    /**
     * Creates an new entry and counts up
     */
	public void newEntry() 
	{
		keyvalues.put(pointer, new HashMap<String, Object>());
		pointer++;
	}
	
	/**
	 * Go to the next entry
	 */
	public boolean nextEntry() 
	{
		if((readpointer + 1)<pointer) readpointer++; else return false;
		return true;
	}
	
	/**
	 * Gets the previous entry
	 */
	public boolean previousEntry() 
	{
		if((readpointer - 1) > 0) readpointer--; else return false;
		return true;
	}
	
	/**
	 * Resets the read pointer
	 */
	public void resetPointer() 
	{
		readpointer = 0;
	}
	
	public void print() 
	{
		for(Map.Entry<Integer, HashMap<String, Object>> s : keyvalues.entrySet()) 
		{
			System.out.println(s.getKey());
			for(Map.Entry <String,Object> ss : s.getValue().entrySet()) 
				System.out.println("==>"+ss.getKey() + "=" + ss.getValue());
		}
	}
	
	public int getInteger(String key) 
	{
		try
		{
			return keyvalues.size()>readpointer ? Integer.parseInt(String.valueOf(keyvalues.get(readpointer).get(key))) : 0;
		}
		catch(Exception e) 
		{ 
			System.out.println("[iBank] "+key+" Error while parsing Integer!"+e); return 0; 
		}
	}
	
	public long getLong(String key) 
	{
		try
		{
			return keyvalues.size()>readpointer ? Long.parseLong(String.valueOf(keyvalues.get(readpointer).get(key))) : 0;
		}
		catch(Exception e) 
		{ 
			System.out.println("[iBank] "+key+" Error while parsing Long!"+e); 
			return 0; 
		}
	}
}
