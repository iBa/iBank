package com.ibank.Listeners;

import com.ibank.Commands.CommandBalance;
import com.ibank.Commands.CommandDeposit;
import com.ibank.Commands.CommandLoan;
import com.ibank.Commands.CommandLoanInfo;
import com.ibank.Commands.CommandTransfer;
import com.ibank.Commands.CommandWithdraw;
import com.ibank.iBank;
import com.ibank.system.CommandHandler;
import com.ibank.system.Configuration;
import com.ibank.system.MessageManager;
import com.ibank.utils.Mathematics;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The default Listener
 * @author steffengy
 */
public class iBankListener implements Listener 
{
	public HashMap<String, Map.Entry<Location, Location>> LastMarkedPoint = new HashMap<String, Map.Entry<Location,Location>>();
	public List<String> order = new ArrayList<String>( Arrays.asList(
			new String[] { "withdraw", "deposit", "transfer", "balance", "loaninfo", "loan" }) 
	);
	public HashMap<String, Location> lastpos = new HashMap<String, Location>();
	
	@EventHandler
	public void playerInteract(PlayerInteractEvent event)
    {
		   /* REGION_SELECTION RIGHT|LEFT */
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if(event.getPlayer().getItemInHand().getTypeId() == Configuration.Entry.SelectionTool.getInteger())
			{
				if(CommandHandler.isCallable(event.getPlayer(), "bank", "addregion"))
					if(event.getAction() == Action.RIGHT_CLICK_BLOCK) //1st point
					{
						if(!LastMarkedPoint.containsKey(event.getPlayer().getName())) 
							LastMarkedPoint.put(event.getPlayer().getName(), new AbstractMap.SimpleEntry<Location, Location>(event.getClickedBlock().getLocation(),null));
						else
						{
							Location second = LastMarkedPoint.get(event.getPlayer().getName()).getValue();
							LastMarkedPoint.remove(event.getPlayer().getName());
							LastMarkedPoint.put(event.getPlayer().getName(), new AbstractMap.SimpleEntry<Location, Location>(event.getClickedBlock().getLocation(), second));
						}
						MessageManager.send(event.getPlayer(), "Position 1 set!");
					}
					else //2nd point
					{
						if(!LastMarkedPoint.containsKey(event.getPlayer().getName())) 
							LastMarkedPoint.put(event.getPlayer().getName(), new AbstractMap.SimpleEntry<Location, Location>(null,event.getClickedBlock().getLocation()));
						else
						{
							LastMarkedPoint.get(event.getPlayer().getName()).setValue(event.getClickedBlock().getLocation());
						}
						MessageManager.send(event.getPlayer(), "Position 2 set!");
				   }
			} 
		}
		/* SIGN_SUPPORT RIGHT */
		if(event.getAction() == Action.LEFT_CLICK_BLOCK && Configuration.Entry.EnableSign.getBoolean()) 
		{
			 /* SIGN_CHECKS */
			 if(event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST || 
				event.getClickedBlock().getType() == Material.WALL_SIGN)
			 {
				 //check if the first line equals [iBank]
				 if(((Sign)event.getClickedBlock().getState()).getLine(0).equalsIgnoreCase("[ibank]")) 
				 {
					 iBank.login(event.getPlayer().getName());
					 lastpos.put(event.getPlayer().getName(), event.getPlayer().getLocation());
					 if(((Sign)event.getClickedBlock().getState()).getLine(1).length() > 1) {
						 iBank.loggedinto.put(event.getPlayer().getName(), ((Sign)event.getClickedBlock().getState()).getLine(1));
					 }else{
						 iBank.loggedinto.put(event.getPlayer().getName(), order.get(0));
					 }
					 MessageManager.send(event.getPlayer(), "&g&" + Configuration.StringEntry.SuccessLogin.getValue());
				 }
			 }
		 }
		 /* CHANGE_SIGN_TYPE */
		 if(event.getAction() == Action.RIGHT_CLICK_BLOCK && Configuration.Entry.EnableSign.getBoolean()) 
		 {
			 if(event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST || 
			    event.getClickedBlock().getType() == Material.WALL_SIGN) 
			 {
				 Sign state = (Sign) event.getClickedBlock().getState();
				 if(state.getLine(0).equalsIgnoreCase("[ibank]")) 
				 {
					 int newindex = 0;
					 try
					 {
						 newindex = order.indexOf(state.getLine(1)) + 1;
					 }
					 catch(Exception ignored) { }

					 newindex = newindex >= order.size() ? 0 : newindex;
					 
					 state.setLine(1, order.get(newindex));
					 iBank.loggedinto.put(event.getPlayer().getName() , order.get(newindex));
					 state.update(true);
				 }
			 }
		 }
    }
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void playerChat(AsyncPlayerChatEvent event)
	{
		//if logged in catch chat stuff
		if(iBank.connected.contains(event.getPlayer().getName())) 
		{
			String[] arguments;
			if(event.getMessage().contains(" ")) 
			{
				arguments = event.getMessage().split(" ");
			}
			else
			{
				arguments = new String[] { event.getMessage() };
			}
			//validate type
			String type = iBank.loggedinto.get(event.getPlayer().getName());
			CommandSender sender = event.getPlayer();
			
			if(type.equals("withdraw")) new CommandWithdraw().handle(sender, arguments, true);
			else if(type.equals("deposit")) new CommandDeposit().handle(sender, arguments, true);
			else if(type.equals("balance")) new CommandBalance().handle(sender, arguments, true);
			else if(type.equals("transfer")) new CommandTransfer().handle(sender, arguments, true);
			else if(type.equals("loaninfo")) new CommandLoanInfo().handle(sender, arguments, true);
			else if(type.equals("loan")) new CommandLoan().handle(sender, arguments, true);
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void signUpdate(SignChangeEvent event) 
	{
		/* CHECK_IBANK => CHECK_PERMISSION ! => CANCELLED ! => SUCCESS */
		if(!event.isCancelled()) 
		{
			if( event.getBlock().getType() == Material.SIGN || event.getBlock().getType() == Material.WALL_SIGN || 
					event.getBlock().getType() == Material.SIGN_POST) 
			{
				if(!event.getLine(0).equalsIgnoreCase("[ibank]")) return;
				if(!iBank.hasPermission(event.getPlayer() , "ibank.sign"))
				{
						MessageManager.send(event.getPlayer(), "&r&Permission denied!");
						event.setCancelled(true);
				}
				else
				{
					if(!order.contains(event.getLine(1))) event.setLine(1, order.get(0));
					MessageManager.send(event.getPlayer(), "&g&"+Configuration.StringEntry.SuccessSignCreate.getValue());
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void playerMoved(PlayerMoveEvent event) 
	{
		//force player not to move
		if(iBank.connected.contains(event.getPlayer().getName())) 
		{
			//Simplified: logout on movement where block difference was bigger than 1
			int[] diffs = Mathematics.diffLoc(lastpos.get(event.getPlayer().getName()), event.getPlayer().getLocation());
			if(diffs[0] > 0 || diffs[1] > 0 || diffs[2] > 0) 
			{
				iBank.logout(event.getPlayer().getName());
				MessageManager.send(event.getPlayer(), "&g&"+Configuration.StringEntry.SuccessSignLogout.getValue());
			}
		}
	}
	
	@EventHandler
	public void logout(PlayerQuitEvent event) 
	{
		//force disconnect from bank
		iBank.logout(event.getPlayer().getName());
	}
}
