package com.iBank.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class providing access to MYSQL
 * @author steffengy
 *
 */
public class Mysql {
	private boolean success = false;
	private Connection connection = null;
	/**
	 * Constructor
	 * @param host The host (e.g. localhost , localhost:1234)
	 * @param user The user
	 * @param password The password
	 * @param database The databasename
	 */
	public Mysql(String host, String user, String password, String database) {
		try{
			connection = DriverManager.getConnection("jdbc:mysql://localhost/"+database+"?" + "user="+user+"&password="+password);
			success = true;
		}catch(Exception e) {
			System.out.println("[iBank] Establishing the Mysql Connection failed! "+ e);
		}
	}
	/**
	 * @return boolean if the init failed
	 */
	public boolean success() {
		return success;
	}
	/**
	 * Executs a query and returns a ResultSet
	 * @param query
	 * @return ResultSet
	 */
	public ResultSet query(String query) {
		try{
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);  
		return resultSet;
		}catch(Exception e) {
			System.out.println("[iBank] Error in query "+query+" "+e);
		}
		return null;
	}
	/**
	 * Executs a command
	 * @param query The command
	 */
	public boolean execute(String query) {
		try{
		Statement statement = connection.createStatement();
		return statement.execute(query);
		}catch(Exception e) {
			System.out.println("[iBank] Error in execution "+query+" "+e);
			return false;
		}
	}
	/**
	 * Returns the id
	 * @param query The command
	 * @return
	 */
	public int insert(String query) {
		try{
			Statement statement = connection.createStatement();
			statement.execute(query);
			return statement.getGeneratedKeys().getInt(1);
			}catch(Exception e) {
				System.out.println("[iBank] Error in insert "+query+" "+e);
				return -1;
			}
	}
	/**
	 * Returns if the table exists
	 */
	public boolean existsTable(String name) {
		try{
		ResultSet tables = connection.getMetaData().getTables(null, null, name, null);
		return tables.next();
		}catch(Exception e) {
			return false;
		}
	}
	/**
	 * Cloes the connection
	 */
	public void close() {
		if(connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("[iBank] Couldn't close MYSQL Connection "+e);
			}
	}
}
