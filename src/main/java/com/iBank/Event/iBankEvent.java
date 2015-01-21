package com.ibank.Event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.ibank.Event.iEvent.Types;

/**
 * Event representing all iBank Events
 * @author steffengy
 *
 */
public class iBankEvent extends Event implements Cancellable 
{
	private static final HandlerList handlers = new HandlerList();
 	private Types type;
    private Object data;
 	private boolean cancelled = false;
 	
    public iBankEvent(Types type, Object data)
    {
        this.type = type;
        this.data = data;
    }
 
    public HandlerList getHandlers() 
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList() 
    {
        return handlers;
    }
    /**
     * Returns the iEvent.Types of this event
     */
    public Types getiBankType() 
    {
    	return type;
    }

	@Override
	public boolean isCancelled() 
	{
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) 
	{
		cancelled = arg0;
	}

    public void setData(Object data)
    {
        this.data = data;
    }

    public Object getData()
    {
        return data;
    }
}
