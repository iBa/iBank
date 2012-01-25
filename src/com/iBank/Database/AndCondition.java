package com.iBank.Database;

/**
 * Providing easy AND conditions
 * @author steffengy
 *
 */
public class AndCondition extends Condition {
	
	/**
	 * Representing a AND Condition
	 * @param field The name of the field
	 * @param value The value
	 * @param action What needs to match.
	 */
	public AndCondition(String field, Object value, Operators action) {
		this.field = field;
		this.value = value;
		this.action = action;
	}
}
