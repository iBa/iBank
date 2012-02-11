package com.iBank.Event;
/**
 * Providing event enums/info/funcs
 * @author steffengy
 *
 */
public class iEvent {
	public static enum Types {
		/**
		 * Called when a account was created
		 */
		ACCOUNT_CREATE,
		/**
		 * Called when a account was deleted
		 */
		ACCOUNT_DELETE,
		/**
		 * Called when a account gets money deposited by a player
		 */
		ACCOUNT_DEPOSIT,
		/**
		 * Called when a accounts gets money withdrawed by a player
		 */
		ACCOUNT_WITHDRAW,
		/**
		 * Called when a user transfers money between 2 accounts
		 */
		ACCOUNT_TRANSFER,
		/**
		 * Called when a user gets money via give command
		 */
		ACCOUNT_GIVE,
		/**
		 * Called when a user loses money via take command
		 */
		ACCOUNT_TAKE,
		/**
		 * Called when a user wants to know the balance of his account
		 */
		ACCOUNT_BALANCE,
		/**
		 * Called when a region was created
		 */
		REGION_CREATE,
		/**
		 * Called when a region was deleted
		 */
		REGION_DELETE
	}
}
