package com.iBank;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
import com.iBank.Commands.CommandLoanEdit;
import com.iBank.Commands.CommandLoanInfo;
import com.iBank.Commands.CommandManager;
import com.iBank.Commands.CommandOpenAccount;
import com.iBank.Commands.CommandOwners;
import com.iBank.Commands.CommandPayBack;
import com.iBank.Commands.CommandRegion;
import com.iBank.Commands.CommandReload;
import com.iBank.Commands.CommandRename;
import com.iBank.Commands.CommandTake;
import com.iBank.Commands.CommandTransfer;
import com.iBank.Commands.CommandUsers;
import com.iBank.Commands.CommandWithdraw;
import com.iBank.Database.DataSource;
import com.iBank.Database.DataSource.Drivers;
import com.iBank.Listeners.iBankListener;
import com.iBank.system.Bank;
import com.iBank.system.CommandHandler;
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
	public static iBank mainInstance = null;
    private static YamlConfiguration StringConfig = null;
    private static YamlConfiguration Config = null;
    private static File ConfigFile = null;
    private static File StringFile = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    public static PluginDescriptionFile description = null;
    public static Listener Listener = new iBankListener();
    private static Permission permission = null;
    public static Economy economy = null;
    public static DataSource data = new DataSource();
    private Timer Loan = null;
    private Timer Interest = null;
    public static List<String> connected = new ArrayList<String>();
    public static HashMap<String, String> loggedinto = new HashMap<String, String>();
    
	@Override
	public void onEnable() {
		//dirty hack :(
		mainInstance = this;
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
		    return;
		}
		setupPermissions();
		
	    //register commands
		CommandHandler.register(new BankRootCommand());
	    CommandHandler.register(new CommandHelp("bank"));
	    CommandHandler.register(new CommandAddRegion());
	    CommandHandler.register(new CommandDelRegion());
	    CommandHandler.register(new CommandRegion());
	    CommandHandler.register(new CommandOpenAccount());
	    CommandHandler.register(new CommandBalance());
	    CommandHandler.register(new CommandList());
	    CommandHandler.register(new CommandDeposit());
	    CommandHandler.register(new CommandWithdraw());
	    CommandHandler.register(new CommandTransfer());
	    CommandHandler.register(new CommandManager());
	    CommandHandler.register(new CommandOwners());
	    CommandHandler.register(new CommandUsers());
	    CommandHandler.register(new CommandGive());
	    CommandHandler.register(new CommandTake());
	    CommandHandler.register(new CommandDelete());
	    CommandHandler.register(new CommandClose());
	    CommandHandler.register(new CommandRename());
	    CommandHandler.register(new CommandReload());
	    //register loan help
	      if(Configuration.Entry.Loan.getBoolean()) {
	    	  CommandHandler.register(new CommandLoan());
	    	  CommandHandler.register(new CommandLoanInfo());
	    	  CommandHandler.register(new CommandLoanEdit());
	    	  CommandHandler.register(new CommandPayBack());
	      }
		description = this.getDescription();  
		  
		//DB
			if(Configuration.Entry.DatabaseType.getValue().equalsIgnoreCase("sqlite") || Configuration.Entry.DatabaseType.getValue().equalsIgnoreCase("mysql")) {
				if(Configuration.Entry.DatabaseUrl.getValue() != null) {
					// connect
					Drivers driver = DataSource.Drivers.SQLite;
					if(Configuration.Entry.DatabaseType.getValue().equalsIgnoreCase("mysql")) {
						driver = DataSource.Drivers.MYSQL;
					}
					
					if(!DataSource.setup(driver, Configuration.Entry.DatabaseUrl.getValue(), this)) {
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
				if(!DataSource.setup(DataSource.Drivers.SQLite, Configuration.Entry.DatabaseUrl.getValue(), this)) {
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
					 long time = 60L * 1000L;
					 Interest = new Timer();
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
					Loan = new Timer();
					Loan.scheduleAtFixedRate(new BankLoan(), time, time);
				}
			}).start();
		}
		if(Configuration.Entry.RealisticMode.getBoolean())
		{
		    System.out.println("[iBank] starting with enabled realistic mode!");
		    // Check if negative amounts are supported, if configured
		    if(Configuration.Entry.RealisticNegative.getBoolean())
		    {
		        if(Configuration.Entry.RealisticInternal.getBoolean())
		        {
		            if(!Bank.hasAccount(Configuration.Entry.RealisticAccount.getValue()))
		            {
		                System.out.println("[iBank][RealisticMode] Internal account created!");
		                Bank.createAccount(Configuration.Entry.RealisticAccount.getValue(), "");
		            }
		            /* Negative amount test xD */
		            com.iBank.system.BankAccount test = Bank.getAccount(Configuration.Entry.RealisticAccount.getValue());
		            BigDecimal tmp = test.getBalance().add(new BigDecimal(100));
		            test.subtractBalance(tmp);
		            if(test.getBalance().compareTo(BigDecimal.ZERO) < 0) 
		            {
		                System.out.println("[iBank][RealisticMode] Negative amounts supported! (new Balance:" + test.getBalance().toString() + ")");
		                test.addBalance(tmp);
		            }
		            else
		            {
		                System.out.println("[iBank][RealisticMode] Negative amounts not supported! (new Balance:" + test.getBalance().toString() + ")");
		            }
		        }
		        else
		        {
		            double amount = economy.getBalance(Configuration.Entry.RealisticAccount.getValue()) + 10000;
		            economy.withdrawPlayer(Configuration.Entry.RealisticAccount.getValue(), amount);
		            if(economy.getBalance(Configuration.Entry.RealisticAccount.getValue()) < 0)
		            {
		                System.out.println("[iBank][RealisticMode] Negative amounts supported! ");
		                economy.depositPlayer(Configuration.Entry.RealisticAccount.getValue(), amount);
		            }
		            else
		            {
		                System.out.println("[iBank][RealisticMode] Negative amounts not supported!");
		                Configuration.Entry.RealisticNegative.setValue(false);
		            }
		        }
		    }
		}
		else
		{
		    System.out.println("[iBank] starting without realistic mode!");
		}
		System.out.println("[iBank] Version " + description.getVersion() + " loaded successfully!");
	}
	
	@Override
	public void onDisable() {
		DataSource.shutdown();
		//Kill timers
		if(Interest != null) {
                Interest.cancel();
                Interest.purge();
                Interest = null;
		}
		if(Loan != null) {
				Loan.cancel();
				Loan.purge();
				Loan = null;
		}
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
    	return CommandHandler.handle(sender, cmd.getName(), args);
    }
    
    /**
     * Checks if a player can execute this command
     * !NO PERMISSION CHECK!
     * @param l The location of the player
     * @param p The player
     * @return boolean
     */
    public static boolean canExecuteCommand(Player p) {
    	if(!Configuration.Entry.BoundToRegion.getBoolean()) return true;
    	if(hasPermission(p, "iBank.global")) return true;
    	Location l = p.getLocation();
    	return regionAt(l) == null ? false : true;
    }
    /**
     * Gets a region at given location 
     * @param l The location
     * @return Name of region or null
     */
    public static String regionAt(Location l) {
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
    	return null;
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
		BigDecimal val = BigDecimal.ZERO;
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
		BigDecimal tmp = BigDecimal.ZERO;
		if(part.endsWith("%")) {
			tmp = tmp.add(due.multiply(new BigDecimal(Double.parseDouble(part.replace("%","")) / 100)));
		}else{
			tmp = tmp.add(new BigDecimal(Double.parseDouble(part)));
		}
		return tmp;
	}
	/**
	 * Checks if an user has a permission
	 * @param user The Player
	 * @param permission The permission
	 * @return boolean
	 */
	public static boolean hasPermission(Player user, String permission) {
		if(iBank.permission.isEnabled()) {
			return iBank.permission.has(user, permission);
		}else{
			return (user).hasPermission(permission);
		}
	}
	/**
	 * Checks if a command sender has a permission
	 * @param user CommandSender 
	 * @param permission Permission
	 * @return boolean
	 */
	public static boolean hasPermission(CommandSender user, String permission) {
		if(!(user instanceof Player)) return true;
		return hasPermission((Player)user, permission);
	}
	/**
	 * Disables all commands of a player, and connects him with 
	 * the directbank
	 * @param player
	 * @param what What to login?
	 */
	public static void login(String player) {
		connected.add(player);
	}
	/**
	 * Debinds a player from directbank
	 * @param player
	 */
	public static void logout(String player) {
		if(connected.contains(player)) connected.remove(player);
	}
}
