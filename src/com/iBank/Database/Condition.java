package com.iBank.Database;

/**
 * The raw condition
 * Providing the conditions, needed by iBank
 * @author steffengy
 *
 */
public class Condition {
	public enum Operators {
		IDENTICAL("="),
		NOT_IDENTICAL("!="),
		BIGGER(">"), 
		LOWER("<");
		
		String chr;
		private Operators(String chars) {
			chr = chars;
		}
		public String getChar() {
			return chr;
		}
	}
	String field = null;
	Object value = null;
	Operators action = null;

}
