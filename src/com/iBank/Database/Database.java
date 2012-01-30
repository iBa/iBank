package com.iBank.Database;

import java.sql.ResultSet;
import java.util.List;

/**
 * Interface
 * @author steffengy
 *
 */
public interface Database {
	public boolean success();
	public ResultSet query(String query);
	public boolean execute(String query);
	public int insert(String query);
	public boolean existsTable(String name);
	public List<String> listFields(String table);
	public void close();
}
