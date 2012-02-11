package com.iBank.Database;

import java.math.BigInteger;

/**
 * Simple SQL Generator
 * Generating the SQL Code
 * @author steffengy
 */
public class SQLBuilder {
	/**
	 * Generates a sql code
	 * @param fields
	 * @param table
	 * @param condition
	 */
	public static String select(String[] fields, String table, Condition... condition) {
		String sqlcode = "SELECT ";
		for(int c = 0;c < fields.length ; c++) {
			sqlcode += "`" + fields[c] + "`";
			if(fields.length-1 != c) sqlcode += ",";
		}
		sqlcode += " FROM "+table;
		sqlcode += parseConditions(condition);
		return sqlcode;
	}
	/**
	 * Parses the conditions to a String (WHERE .. )
	 * @param condition The Condition Array
	 * @return
	 */
	private static String parseConditions(Condition[] condition) {
		if(condition.length == 0) return "";
		String condstring = " WHERE ";
		String operator = "";
		int c = 0;
		for(Condition a : condition) {
			if(a instanceof AndCondition) {
				operator = ((AndCondition)a).action.getChar();
				if(a.value instanceof Integer || a.value instanceof Float || a.value instanceof Double)
					condstring+= "`" + a.field + "`" + operator + a.value;
				else
					condstring+= "`" + a.field + "`" + operator + "'" + a.value + "'";
				if(c==condition.length-2) condstring += " AND ";
				else if(c<condition.length-2) condstring += ", ";
				c++;
			}
		}
		return condstring;
	}
	/**
	 * Inserts into a table
	 * @param fields The fields
	 * @param table The table
	 * @param values The values
	 * @return
	 */
	public static String insert(String[] fields, String table, Object[] values) {
		String sqlcode = "INSERT INTO "+table+"(";
		int c = 0;
		if(fields.length != values.length) { System.out.println("[iBank] Insert in "+table+" failed! LENGTH_ERROR!"); return ""; }
		for(c = 0;c<fields.length;c++) {
			sqlcode += "`" + fields[c] + "`";
			if(c!=fields.length-1) sqlcode+=",";
		}
		sqlcode += ") VALUES (";
		for(c = 0;c<values.length;c++) {
			if(values[c] instanceof Integer||values[c] instanceof Double||values[c] instanceof Float||values[c] instanceof BigInteger) sqlcode += String.valueOf(values[c]);
			else sqlcode += "'"+String.valueOf(values[c])+"'";
			if(c!=values.length-1) sqlcode += ",";
		}
		return sqlcode + ")";
	}
	/**
	 * Updates a table
	 * @param fields the fields which shall be changed
	 * @param table The table where to change
	 * @param values The values "
	 * @param conds The conditions where to change
	 * @return SqlCode
	 */
	public static String update(String[] fields, String table, Object[] values, Condition... conds) {
		String sqlcode = "UPDATE " + table+" SET ";
		if(fields.length != values.length) { System.out.println("[iBank] Insert in "+table+" failed! LENGTH_ERROR!"); return ""; }
		//parse vals + keys
		for(int c = 0; c < fields.length ; c++) {
			if(values[c] instanceof Integer || values[c] instanceof Float || values[c] instanceof Double)
				sqlcode += "`" + fields[c] + "`" +"="+values[c];
			else
				sqlcode += "`" + fields[c] + "`" +"='"+values[c]+"'";
			if(c!=fields.length-1) sqlcode+=",";
		}
		return sqlcode + parseConditions(conds);
	}
	/**
	 * Deletes from a table
	 * @param table The table
	 * @param condition The conditions
	 * @return
	 */
	public static String delete(String table, Condition... condition) {
		return "DELETE FROM "+table+parseConditions(condition);
	}
	/**
	 * Very, Very Simple Alter
	 * @param table The table
	 * @param add Add or DROP?
	 * @param field The field
	 * @param type The type
	 * @return
	 */
	public static String alter(String table, boolean add, String field, String type) {
		String tt = add ? "ADD" : "DROP";
		return "ALTER TABLE `"+table+"` " + tt +  " `"+field+"` "+type;
	}
}
