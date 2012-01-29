package com.iBank;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.iBank.Commands.BankRootCommand;
import com.iBank.Commands.CommandAddRegion;
import com.iBank.Commands.CommandBalance;
import com.iBank.Commands.CommandClose;
import com.iBank.Commands.CommandDelRegion;
import com.iBank.Commands.CommandDelete;
import com.iBank.Commands.CommandDeposit;
import com.iBank.Commands.CommandGive;
import com.iBank.Commands.CommandHelp;
import com.iBank.Commands.CommandList;
import com.iBank.Commands.CommandLoan;
import com.iBank.Commands.CommandLoanInfo;
import com.iBank.Commands.CommandManager;
import com.iBank.Commands.CommandOpenAccount;
import com.iBank.Commands.CommandOwners;
import com.iBank.Commands.CommandRegion;
import com.iBank.Commands.CommandTransfer;
import com.iBank.Commands.CommandWithdraw;
import com.iBank.Database.DataSource;
import com.iBank.Database.DataSource.Drivers;
import com.iBank.Listeners.playerListener;
import com.iBank.system.Bank;
import com.iBank.system.CommandHandler;
import com.iBank.system.CommandTake;
import com.iBank.system.Commands;
import com.iBank.system.Configuration;
import com.iBank.system.Region;
import com.iBank.utils.Mathematics;
import com.iBank.utils.StreamUtils;

/**
 * IBank
 * @author steffengy
 * @copyright Copyright steffengy (C) 2012
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class iBank extends JavaPlugin {
    private YamlConfiguration StringConfig = null;
    private YamlConfiguration Config = null;
    private File ConfigFile = null;
    private File StringFile = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    public static PluginDescriptionFile description = null;
    public Listener Listener = new playerListener();
    public static Permission permission = null;
    public static Economy economy = null;
    public static String CodeName = "Gilbert";
    public static DataSource data = new DataSource();
    
	@Override
	public void onEnable() {
		if(!(getDataFolder().exists())) getDataFolder().mkdir();
		// Load configuration + strings
		reloadConfig();
		loadStrings();
		Configuration.init(Config);
		Configuration.stringinit(StringConfig);
		
		if(!Configuration.Entry.Enabled.getBoolean()) 
		{
			System.out.println("[iBank] Disabled as configured in configuration");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		// Load permissions + economy system via Vault
		if(!setupEconomy())
		{
			log.log(Level.SEVERE, String.format("[%s] - Disabled due to no Vault/economy dependency found!", getDescription().getName()));
		    getServer().getPluginManager().disablePlugin(this);
		}
		setupPermissions();
		
		//Register Commands
		Commands.setVarSource(this);
	    Commands.addRootCommand("bank");
	    Commands.setTag("&g&[&w&Bank&g&] ");
	    
		  Commands.addSubCommand("bank", "help");
		  Commands.setPermission("iBank.access");
		  Commands.setHelp(Configuration.StringEntry.HelpDescription.getValue());
		  Commands.setHandler(new CommandHelp("bank"));
		  
		  Commands.addSubCommand("bank", "");
	      Commands.setPermission("iBank.access");
	      Commands.setHelp(Configuration.StringEntry.BankDescription.getValue());
	      Commands.setHandler(new BankRootCommand());
			
	      Commands.addSubCommand("bank", "addregion");
	      Commands.setPermission("iBank.regions");
	      Commands.setHelp(Configuration.StringEntry.AddRegionDescription.getValue());
	      Commands.setHandler(new CommandAddRegion());
	      Commands.setHelpArgs("[Name]");
	      
	      Commands.addSubCommand("bank", "delregion");
	      Commands.setPermission("iBank.regions");
	      Commands.setHelp(Configuration.StringEntry.DelRegionDescription.getValue());
	      Commands.setHandler(new CommandDelRegion());
	      Commands.setHelpArgs("[Name]");
	      
	      Commands.addSubCommand("bank", "region");
	      Commands.setPermission("iBank.regions");
	      Commands.setHelp(Configuration.StringEntry.RegionDescription.getValue());
	      Commands.setHandler(new CommandRegion());
	      Commands.setHelpArgs("[Name]");	
	      
	      Commands.addSubCommand("bank", "open");
	      Commands.setPermission("iBank.access");
	      Commands.setHelp(Configuration.StringEntry.OpenAccountDescription.getValue());
	      Commands.setHandler(new CommandOpenAccount());
	      Commands.setHelpArgs("[Name]");
	      
	      Commands.addSubCommand("bank", "balance");
	      Commands.setPermission("iBank.access");
	      Commands.setHelp(Configuration.StringEntry.BalanceDescription.getValue());
	      Commands.setHandler(new CommandBalance());
	      Commands.setHelpArgs("[Name]");
	      
	      Commands.addSubCommand("bank", "list");
	      Commands.setPermission("iBank.list");
	      Commands.setHelp(Configuration.StringEntry.ListDescription.getValue());
	      Commands.setHandler(new CommandList());
	      Commands.setHelpArgs("(Player)");
	      
	      Commands.addSubCommand("bank", "deposit");
	      Commands.setPermission("iBank.access");
	      Commands.setHelp(Configuration.StringEntry.DepositDescription.getValue());
	      Commands.setHandler(new CommandDeposit());
	      Commands.setHelpArgs("[Name] [Amount]");
	      
	      Commands.addSubCommand("bank", "withdraw");
	      Commands.setPermission("iBank.access");
	      Commands.setHelp(Configuration.StringEntry.WithdrawDescription.getValue());
	      Commands.setHandler(new CommandWithdraw());
	      Commands.setHelpArgs("[Name] [Amount]");
	      
	      Commands.addSubCommand("bank", "transfer");
	      Commands.setPermission("iBank.access");
	      Commands.setHelp(Configuration.StringEntry.TransferDescription.getValue());
	      Commands.setHandler(new CommandTransfer());
	      Commands.setHelpArgs("[Src] [Dest] [Amount]");
	      
	      Commands.addSubCommand("bank", "account");
	      Commands.setPermission("iBank.manage");
	      Commands.setHelp(Configuration.StringEntry.AccountDescription.getValue());
	      Commands.setHandler(new CommandManager());
	      Commands.setHelpArgs("[Name]");
	      
	      Commands.addSubCommand("bank", "owners");
	      Commands.setPermission("iBank.access");
	      Commands.setHelp(Configuration.StringEntry.OwnersDescription.getValue());
	      Commands.setHandler(new CommandOwners());
	      Commands.setHelpArgs("[Name]");
	      
	      Commands.addSubCommand("bank", "give");
	      Commands.setPermission("iBank.manage");
	      Commands.setHelp(Configuration.StringEntry.GiveDescription.getValue());
	      Commands.setHandler(new CommandGive());
	      Commands.setHelpArgs("[Player] [Amount]");
	      
	      Commands.addSubCommand("bank", "take");
	      Commands.setPermission("iBank.manage");
	      Commands.setHelp(Configuration.StringEntry.TakeDescription.getValue());
	      Commands.setHandler(new CommandTake());
	      Commands.setHelpArgs("[Player] [Amount]");
	      
	      Commands.addSubCommand("bank", "delete");
	      Commands.setPermission("iBank.manage");
	      Commands.setHelp(Configuration.StringEntry.DeleteDescription.getValue());
	      Commands.setHandler(new CommandDelete());
	      Commands.setHelpArgs("[Name]");
	      
	      Commands.addSubCommand("bank", "close");
	      Commands.setPermission("iBank.access");
	      Commands.setHelp(Configuration.StringEntry.CloseDescription.getValue());
	      Commands.setHandler(new CommandClose());
	      Commands.setHelpArgs("[Name]");
	      
	      if(Configuration.Entry.Loan.getBoolean()) {
	      
	    	  Commands.addSubCommand("bank", "loan");
	    	  Commands.setPermission("iBank.loan");
	    	  Commands.setHelp(Configuration.StringEntry.LoanDescription.getValue());
	    	  Commands.setHandler(new CommandLoan());
	    	  Commands.setHelpArgs("[Amount]");
	      
	    	  Commands.addSubCommand("bank", "loaninfo");
	    	  Commands.setPermission("iBank.loan");
	    	  Commands.setHelp(Configuration.StringEntry.LoanInfoDescription.getValue());
	    	  Commands.setHandler(new CommandLoanInfo());
	    	  Commands.setHelpArgs("(:Site|Player)");
	      }
	      
		description = this.getDescription();  
		  
		//DB
			if(Configuration.Entry.DatabaseType.getValue().toString().equalsIgnoreCase("sqlite") || Configuration.Entry.DatabaseType.getValue().toString().equalsIgnoreCase("mysql")) {
				if(Configuration.Entry.DatabaseUrl.getValue().toString() != null) {
					// connect
					Drivers driver = DataSource.Drivers.SQLite;
					if(Configuration.Entry.DatabaseType.getValue().toString().equalsIgnoreCase("mysql")) {
						driver = DataSource.Drivers.MYSQL;
					}
					
					if(!DataSource.setup(driver, Configuration.Entry.DatabaseUrl.getValue().toString(), this)) {
						System.out.println("[iBank] Database connection failed! Shuting down iBank...");
						getServer().getPluginManager().disablePlugin(this);
						return;
					}
				}else{
					System.out.println("[iBank] Database connection failed! No File specified!");
				}
			}else{
				if(Configuration.Entry.DatabaseUrl.getValue().toString() != null) {
				// connect
				if(!DataSource.setup(DataSource.Drivers.SQLite, Configuration.Entry.DatabaseUrl.getValue().toString(), this)) {
					System.out.println("[iBank] Database connection failed! Shuting down iBank...");
					getServer().getPluginManager().disablePlugin(this);
					return;
				}
				}else{
					getServer().getPluginManager().disablePlugin(this);
					return;
				}
			}
	    
		//Register events
		getServer().getPluginManager().registerEvents(Listener, this);
		
		//start interest syncer
		if(Configuration.Entry.InterestEnabled.getBoolean()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					 long time = Configuration.Entry.InterestPeriod.getLong() * 60L * 1000L;
					 Timer Interest = new Timer();
	                 Interest.scheduleAtFixedRate(new BankInterest(), time, time);
				}
			}).start();
		}
		//start loan syncer
		if(Configuration.Entry.Loan.getBoolean()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					long time = 60L * 1000L;
					Timer Loan = new Timer();
					Loan.scheduleAtFixedRate(new BankLoan(), time, time);
				}
			}).start();
		}
		
		System.out.println("[iBank] Version "+description.getVersion()+" "+CodeName+" loaded successfully!");
	}
	
	@Override
	public void onDisable() {
		DataSource.shutdown();
		System.out.println("[iBank] unloaded");
	}

	/**
	 * Reloads the config
	 */
	public void reloadConfig() 
	{
	    if(ConfigFile == null) {
	    	ConfigFile = new File(getDataFolder(), "config.yml");
	    }
	    if(ConfigFile.exists()) {
	    	Config = YamlConfiguration.loadConfiguration(ConfigFile);
	    }else{
	    	if(StreamUtils.copy(getResource("config.yml"), ConfigFile)) {
	    		Config = YamlConfiguration.loadConfiguration(ConfigFile);
	    	}else{
	    		System.out.println("[iBank] OOPS! Failed loading config!");
	    	}
	    }
	}
	/**
	 * Loads the strings file
	 */
	private void loadStrings() 
	{
	    if(StringFile == null) {
	    	StringFile = new File(getDataFolder(), "strings.yml");
	    }
	    StringConfig = YamlConfiguration.loadConfiguration(StringFile);
	    //Get default config
	    InputStream defConfigStream = getResource("strings.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        StringConfig.setDefaults(defConfig);
	    }
	}
	/**
	 * Vault Function to setup the permissions
	 */
	private Boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	/**0
	 * Vault function to setup Economy
	 */
    private Boolean setupEconomy()
    {
    	if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { 
    	return CommandHandler.parse(cmd.getName(), args, sender);	
    }
    
    /**
     * Checks if a player can execute this command
     * !NO PERMISSION CHECK!
     * @param l The location of the player
     * @return String name of region
     */
    public static String GetRegionAt(Location l) {
    	if(!Configuration.Entry.BoundToRegion.getBoolean()) return " ";
    	double x = l.getX();
    	double y = l.getY();
    	double z = l.getZ();
    	Region tmp;
    	for(String i : Bank.getRegions()) {
    		tmp = Bank.getRegion(i);
    		if(l.getWorld() == tmp.getFirstLocation().getWorld()) {
    			if(Mathematics.isInBox(x,y,z,tmp.getFirstLocation().getX(),tmp.getFirstLocation().getY(),tmp.getFirstLocation().getZ(),tmp.getSecondLocation().getX(),tmp.getSecondLocation().getY(),tmp.getSecondLocation().getZ())) {
    				return i;
    			}
    		       
    		}
    	}
    	return "";
    }
    /**
     * Formats an big Integer
     * @param todp The amount
     * @return String
     * (probalby wrong formated due to BigInteger/double limits
     */
    public static String format(BigDecimal todp) {
         return economy.format(todp.doubleValue());
    }
    /**
     * Calculates the fee by a given fee and a config-string
     * @param fee The to apply fee
     * @param due The value, which shall be applied to 
     * @return BigDecimal The fee value
     */
	public static BigDecimal parseFee(String fee, BigDecimal due) {
		BigDecimal val = new BigDecimal("0.00");
		//check if percentage or static amount
		if(fee.contains("+")) {
			String[] plus = fee.split("+");
			val = val.add(parseFeeString(plus[0], due));
			val = val.add(parseFeeString(plus[1], due));
		}else
		if(fee.contains("-")) {
			String[] minus = fee.split("-");
			val = val.subtract(parseFeeString(minus[0], due));
			val = val.subtract(parseFeeString(minus[1], due));
		}else{
			val = parseFeeString(fee, due);
		}
		return val;
	}
	/**
	 * Parses a single part of a feestring
	 * @param part The part
	 * @param due The due
	 * @return
	 */
	public static BigDecimal parseFeeString(String part, BigDecimal due) {
		BigDecimal tmp = new BigDecimal("0.00");
		if(part.endsWith("%")) {
			tmp = tmp.add(due.multiply(new BigDecimal(Double.parseDouble(part.replace("%","")) / 100)));
		}else{
			tmp = tmp.add(new BigDecimal(Double.parseDouble(part)));
		}
		return tmp;
	}
}
