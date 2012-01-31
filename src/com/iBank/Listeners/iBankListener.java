package com.iBank.Listeners;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.iBank.iBank;
import com.iBank.system.Commands;
import com.iBank.system.Configuration;
import com.iBank.system.MessageManager;

/**
 * The default Listener
 * @author steffengy
 */
public class iBankListener implements Listener {
	public HashMap<String, Map.Entry<Location, Location>> LastMarkedPoint = new HashMap<String, Map.Entry<Location,Location>>();
	public List<String> order = new ArrayList<String>( Arrays.asList(new String[] { "withdraw", "deposit", "transfer", "balance" }) );
	
	@EventHandler
	public void playerInteract(PlayerInteractEvent event)
    {
		   /* REGION_SELECTION RIGHT|LEFT */
    		   if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)
    	    	{
    	    	   if(event.getPlayer().getItemInHand().getTypeId() == Configuration.Entry.SelectionTool.getInteger())
    	    	   {
    	    		   if(!Commands.isCallable(event.getPlayer(), "bank", "addregion")) return;
    	    		   if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
    	    			   //1st
    	    			   if(!LastMarkedPoint.containsKey(event.getPlayer().getName())) {
    	    				   LastMarkedPoint.put(event.getPlayer().getName(), new AbstractMap.SimpleEntry<Location, Location>(event.getClickedBlock().getLocation(),null));
    	    			   }else{
    	    				   Location second = LastMarkedPoint.get(event.getPlayer().getName()).getValue();
    	    				   LastMarkedPoint.remove(LastMarkedPoint.remove(event.getPlayer().getName()));
    	    				   LastMarkedPoint.put(event.getPlayer().getName(), new AbstractMap.SimpleEntry<Location, Location>(event.getClickedBlock().getLocation(), second));
    	    			   }
    	    			   MessageManager.send(event.getPlayer(), "Position 1 set!");
    	    		   }else{
    	    			   //2nd
    	    			   if(!LastMarkedPoint.containsKey(event.getPlayer().getName())) {
    	    				   LastMarkedPoint.put(event.getPlayer().getName(), new AbstractMap.SimpleEntry<Location, Location>(null,event.getClickedBlock().getLocation()));
    	    			   }else{
    	    				   LastMarkedPoint.get(event.getPlayer().getName()).setValue(event.getClickedBlock().getLocation());
    	    			   }
    	    			   MessageManager.send(event.getPlayer(), "Position 2 set!");
    	    		   }
    	    	   } 
    	    	}
    		 /* SIGN_SUPPORT RIGHT */
    		 if(event.getAction() == Action.RIGHT_CLICK_BLOCK && Configuration.Entry.EnableSign.getBoolean()) {
    			 /* SIGN_CHECKS */
    			 if(event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
    				 //check if the first line equals [iBank]
    				 if(((CraftSign)event.getClickedBlock()).getLine(0).equalsIgnoreCase("[ibank]")) {
    					 //simulate sign edit or something like this
    					 iBank.login(event.getPlayer().getName(), ((CraftSign)event.getClickedBlock()).getLine(1));
    					 MessageManager.send(event.getPlayer(), "&g%" + Configuration.StringEntry.SuccessLogin.toString());
    				 }
    			 }
    		 }
    		 /* CHANGE_SIGN_TYPE */
    		 if(event.getAction() == Action.RIGHT_CLICK_BLOCK && Configuration.Entry.EnableSign.getBoolean()) {
    			 if(event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
    				 if(((CraftSign)event.getClickedBlock()).getLine(0).equalsIgnoreCase("[ibank]")) {
    					 int newindex = 0;
    					 try{
    						 newindex = order.indexOf(((CraftSign)event.getClickedBlock()).getLine(0)) + 1;
    					 }catch(Exception e) { }
    					 newindex = newindex >= order.size() ? 0 : newindex;
    					 ((CraftSign)event.getClickedBlock()).setLine(1, order.get(newindex));
    					 ((CraftSign)event.getClickedBlock()).update();
    				 }
    			 }
    		 }
    	}
		@EventHandler (priority = EventPriority.HIGHEST)
		public void playerChat(PlayerChatEvent event) {
			//if logged in catch chat stuff
			
		}
		
		@EventHandler (priority = EventPriority.HIGHEST)
		public void blockPlace(BlockPlaceEvent event) {
			/* CHECK_IBANK => CHECK_PERMISSION ! => CANCELLED ! => SUCCESS */
			
		}
    }
