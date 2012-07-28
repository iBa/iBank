package com.iBank.system;

import java.math.BigDecimal;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The configuration class for iBank
 * @author steffengy
 * 
 */
public class Configuration {
	public static enum Entry
	{
		Enabled("System.Enabled", true),
		EnableSign("System.SignSupport", false),
		Debug("System.Debug", false),
		BoundToRegion("System.BoundToRegion", true),
		StandardBalance("System.StandardBalance", 30),
		MaxAccountsPerUser("System.MaxAccountsPerUser", -1),
		
		InterestEnabled("System.Interest.Enabled", false),
		InterestOnPercentage("System.Interest.Percentages.Online", 1.00),
		InterestOffPercentage("System.Interest.Percentages.Offline", 1),
		InterestPeriod("System.Interest.Period", 10),
		InterestOnline("System.Interest.Online", 1),
		
		DatabaseType("System.Database.Type", "sqlite"),
		DatabaseUrl("System.Database.Url", "bank.db"),
		DatabaseName("System.Database.Database", "bank"),
		DatabaseUser("System.Database.User", "user"),
		DatabasePW("System.Database.Password", "pw"), 
		DatabaseRegionTable("System.Database.Tables.Region", "regions"),
		DatabaseAccountsTable("System.Database.Tables.Accounts", "accounts"),
		DatabaseLoanTable("System.Database.Tables.Loan", "loan"),
		
		SelectionTool("System.SelectionTool", 280),
	
		Loan("System.Loan.Enabled", false),
		LoanMax("System.Loan.Max", 1),
		LoanAmount("System.Loan.Amount", 10000),
		LoanTime("System.Loan.Time", 60),
		LoanForceInterest("System.Loan.forceInterest", true),
		LoanForceMoney("System.Loan.forceMoney", false),
		LoanInterestTime("System.Loan.DefaultInterestTime", 20),
		LoanInterest("System.Loan.DefaultInterest", 1),
		LoanForceBank("System.Loan.forceBank", false),
		
		AllowBuyRegion("System.AllowBuyRegions", false),
		RegionsPrice("System.RegionsPrice", 12000),
		
		FeeCreate("System.Fee.Create", "0.00%;1.00%;2.00%"),
		FeeDeposit("System.Fee.Deposit", "0.00%"),
		FeeWithdraw("System.Fee.Withdraw", "0.00%"),
		FeeTransfer("System.Fee.Transfer", "0.00%"),
		
		RealisticMode("System.RealisticMode.Enabled", false),
		RealisticNegative("System.RealisticMode.negative", true),
		RealisticMaxNeg("System.RealisticMode.maxNegative", 10000),
		RealisticInternal("System.RealisticMode.useiBank", true),
		RealisticAccount("System.RealisticMode.account", "bank");
		
		String key;
		Object value;
	
		/**
		 * Constructor of a Value
		 * @param Name The name of the entry
		 * @param value The value of the entry
		 */
		private Entry(String Name, Object value) {
			this.key = Name;
			this.value = value;
		}
		/**
		 * @return The key of the Entry
		 */
		public String getKey() {
			return key;
		}
		/**
		 * Gets the value as String
		 * @return String The value of the Entry
		 */
		public String getValue() {
			return (String) value;
		}
		
		/**
		 * Gets the value as Object
		 * @return Object
		 */
		public Object getObject()
		{
		    return value;
		}
		/**
		 * @return The value of the Entry as boolean
		 */
		public Boolean getBoolean() {
            return (Boolean) value;
        }
		/*
		 * @return The value of the Entry as Integer (or Double)
		 */
        public Integer getInteger() {
            if(value instanceof Double)
                return ((Double) value).intValue();

            return (Integer) value;
        }
        /**
         * @return The value of the Entry as Double
         */
        public Double getDouble() {
            if(value instanceof Integer)
                return (double) ((Integer) value).intValue();

            return (Double) value;
        }
        /**
         * @return The value of the Entry as BigDecimal
         */
        public BigDecimal getBigDecimal() {
            return new BigDecimal(String.valueOf(value));
        }
        /**
         * @return The value of the Entry as Long (or Integer)
         */
        public Long getLong() {
            if(value instanceof Integer)
                return ((Integer) value).longValue();

            return (Long) value;
        }
        /*
         * @return The value of the Entry as List
         */
        @SuppressWarnings("unchecked")
		public List<String> getStringList() {
        		return (List<String>) value;
        }
        /*
         * Sets the value of this Entry
         * @param value the value as object
         */
        public void setValue(Object value) {
        	this.value = value;
        }
        @Deprecated
        public String toString() {
        	return getValue();
        }
	}
	public static enum StringEntry
	{
		BankTag("Tags.Bank", "&g&[&w&Bank&g&]"),
		BalanceShort("Short.Balance", "Balance"),
		
		HelpDescription("Description.Help", "Displays the help"),
		BankDescription("Description.bank", "Shows a list of your accounts"),
		BalanceDescription("Description.balance", "Shows the accounts balance"),
		AddRegionDescription("Description.addregion", "Adds a region"),
		RegionDescription("Description.region", "Shows info about a region"),
		DelRegionDescription("Description.delregion", "Deletes a region"),
		OpenAccountDescription("Description.openaccount", "Opens a bankaccount"),
		DepositDescription("Description.deposit", "Deposit money to an account"),
		WithdrawDescription("Description.withdraw", "Withdraw from an account"),
		ListDescription("Description.list", "Shows a list of accounts"),
		AccountDescription("Description.account", "Provides managment stuff for accounts"),
		GiveDescription("Description.give", "Gives an amount to an account"),
		TakeDescription("Description.take", "Takes money from an account"),
		TransferDescription("Description.transfer", "Transfers money"),
		DeleteDescription("Description.delete", "Deletes an account"),
		LoanDescription("Description.loan", "Loan money from the bank"),
		LoanInfoDescription("Description.loaninfo", "Shows info about your/others loans"),
		CloseDescription("Description.close", "Close an account"),
		OwnersDescription("Description.owners", "Manage the owners of an account"),
		UsersDescription("Description.users", "Manage the users of an account"),
		PayBackDescription("Description.payback", "payback money you loaned"),
		ReloadDescription("Description.reload", "Reload the configs"),
		LoanEditDescription("Description.loanedit", "Edits a loan"),
		RenameDescription("Description.rename", "Renames an account"),
		
		SuccessAddRegion("Success.addregion", "Successfully, created the region $name$"),
		SuccessDelRegion("Success.delregion", "Successfully, deleted the region $name$"),
		SuccessAddAccount("Success.addaccount", "Successfully, opened the account $name$"),
		SuccessDelAccount("Success.delaccount", "Successfully, deleted the account $name$"),
		SuccessDeposit("Success.deposit", "Successfully, deposited $amount$ to $name$"),
		SuccessGive("Success.give", "Successfully, gave $amount$ to $name$"),
		SuccessTake("Success.take", "Successfully, took $amount$ from $name$"),
		SuccessWithdraw("Success.withdraw", "Successfully, withdrawed $amount$ to $name$"),
		SuccessRegion("Success.region", "Successfully, modified the region $name$"),
		SuccessAccount("Success.account", "Successfully, modfied the account $name$"),
		SuccessTransfer("Success.transfer", "Successfully, transfered $amount$ from $name$ to $name2$"),
		SuccessLoan("Success.loan", "Successfully, loaned $amount$"),
		SuccessClose("Success.close", "Successfully, closed $name$"),
		SuccessMod("Success.modified", "Successfully, modified $name$!"),
		SuccessLogin("Success.login", "Successfully, logged in to sign bank!"),
		SuccessSignCreate("Success.signcreate", "Successfully, created iBank sign!"),
		SuccessSignLogout("Success.logout", "Sucessfully, logged out from iBank sign bank!"),
		SuccessPayback("Success.payback", "Successfully, payed back $amount$!"),
		SuccessLoanEdit("Success.loanedit", "Successfully, modified the loan!"),
		SuccessRename("Success.rename", "Successfully, renamed $a$ to $b$"),
		
		ErrorAlreadyExists("Error.already_exists", "$name$ does already exists!"),
		ErrorRegionSelect("Error.region_select", "Please select a region first!"),
		ErrorWrongArguments("Error.wrong_arguments", "Wrong arguments given!"), 
		ErrorNoPlayer("Error.no_player", "Sorry, you need to be a player to execute this!"),
		ErrorNotExist("Error.not_exist", "$name$ does not exist!"),
		ErrorNotRegion("Error.not_region", "You need to be in a bank region!"),
		ErrorNotEnough("Error.not_enough", "You dont have enough money!"),
		ErrorNoAccess("Error.no_access", "You dont have access to this account!"),
		ErrorInvalidAm("Error.invalid_amount", "The given amount has to be bigger than 0.10"),
		ErrorMaxLoan("Error.max_loan", "You can only have $max$ loans!"),
		ErrorLoanLimit("Error.loan_limit", "You cant loan more than $max$"),
		ErrorNeedOwner("Error.need_owner", "You need to be an owner of this account!"),
		ErrorAlready("Error.already", "$name$ is already one of the $type$ of this account!"),
		ErrorNot("Error.not", "$name$ is not one of the $type$ of this account!"),
		ErrorMaxAcc("Error.max_acc", "You can only have $max$ accounts!"),
		ErrorNotEnoughBank("Error.not_enough_bank", "The bank does not have enough money!"),
		
		GeneralInfo("General.Info", "Info about $type$ $name$:"), 
		GeneralNoAccounts("General.no_accounts", "No accounts found!"),
		GeneralUntil("General.until", "until"),
		GeneralPer("General.per", "per"),
		GeneralMin("General.minutes", "minutes"),
		GeneralOwners("General.owners", "Owners"),
		GeneralUsers("General.users", "Users"),
		GeneralInterval("General.interval", "Interval"),
		PaidFee("General.paid_fee", "You paid $amount$ in fee!");
		
		String key;
		String value;
	
		/**
		 * Constructor of a Value
		 * @param Name The name of the entry
		 * @param value The value of the entry
		 */
		private StringEntry(String Name, String value) {
			this.key = Name;
			this.value = value;
		}
		/**
		 * @return The key of the Entry
		 */
		public String getKey() {
			return key;
		}
		/**
		 * @return The value of the Entry
		 */
		public String getValue() {
			return value;
		}
        /*
         * Sets the value of this Entry
         * @param value the value
         */
        public void setValue(String value) {
        	this.value = value;
        }
        @Override
        @Deprecated
        public String toString()
        {
            return "[DEPRECATED-PLEASE_REPORT_THIS!!] " + this.value;
        }
	}
    /**
     * Sets the config system up
     * @param config The configuration got from the YamlFile 
     */
    public static void init(YamlConfiguration config)
    {
    	for(Entry s : Entry.values())
    		if(!s.getKey().isEmpty())
    			if(config.get(s.getKey()) != null)
    				s.setValue(config.get(s.getKey()));
    }
    /**
     * Sets the lang system up
     * @param config The configuration got from the YamlFile 
     */
    public static void stringinit(YamlConfiguration config)
    {
    	for(StringEntry s : StringEntry.values())
    		if(!s.getKey().isEmpty())
    			if(config.get(s.getKey()) != null)
    				s.setValue(config.getString(s.getKey()));
    }
}
