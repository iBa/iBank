package com.iBank.Event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.iBank.Event.iEvent.Types;

/**
 * Event representing all iBank Events
 * @author steffengy
 *
 */
public class iBankEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	 	private Types data;
	 	private boolean cancelled = false;
	 	
	    public iBankEvent(Types type, Object data) {
	        data = type;
	    }
	 
	    public HandlerList getHandlers() {
	        return handlers;
	    }
	 
	    public static HandlerList getHandlerList() {
	        return handlers;
	    }
	    /**
	     * Returns the iEvent.Types of this event
	     * @return
	     */
	    public Types getiBankType() {
	    	return data;
	    }

		@Override
		public boolean isCancelled() {
			return cancelled;
		}

		@Override
		public void setCancelled(boolean arg0) {
			cancelled = arg0;
		}
}
