package com.iBank.Commands.DataTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class HandlerCollection 
{
    private HashMap<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();
    /**
     * Register a command
     * @param method The method name
     * @param cmds The commands
     */
    public void register(String method, String... cmds)
    {
        data.put(method, new ArrayList<String>(Arrays.asList(cmds)));
    }
    
    /**
     * Gets a handler by a collection of given arguments
     * @return String
     */
    public String getHandler(ArrayList<String> list) 
    {
        for(Entry<String, ArrayList<String>> entry : data.entrySet())
        {
            if(entry.getValue().equals(list))
                return entry.getKey();
        }
        return null;
    }
}
