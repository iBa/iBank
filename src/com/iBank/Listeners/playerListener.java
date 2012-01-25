package com.iBank.Listeners;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.iBank.system.Commands;
import com.iBank.system.Configuration;
import com.iBank.system.MessageManager;

/**
 * The default player Listener
 * @author steffengy
 */
public class playerListener implements Listener {
	public HashMap<String, Map.Entry<Location, Location>> LastMarkedPoint = new HashMap<String, Map.Entry<Location,Location>>();
	
	@EventHandler
	public void playerInteract(PlayerInteractEvent event)
    {
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
    	}
    }
