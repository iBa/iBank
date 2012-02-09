package com.iBank.system;
/**
 * Interface for Annotation
 * @author steffengy
 *
 */
public @interface CommandInfo {
	String permission();
	String root();
	String sub();
	String help();
	String[] arguments();
}
