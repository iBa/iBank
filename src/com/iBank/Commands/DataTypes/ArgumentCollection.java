package com.iBank.Commands.DataTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Containing all arguments this command accepts
 * @author steffen
 *
 */
public class ArgumentCollection 
{
    private HashMap<String, Argument> arguments = new HashMap<String, Argument>();
    private HashMap<Argument, String> data = new HashMap<Argument, String>();
    private int required = 0;

    
    /**
     * Adds an argument to this
     * @param arg The Argument
     */
    public void addArgument(Argument arg)
    {
        arguments.put(arg.getName(), arg);
        if(arg.getType() == Argument.Type.REQUIRED) 
            required++;
    }
    
    /**
     * Writes a value to our value-store
     * @param arg The argument
     * @param value The value
     */
    public void addValue(Argument arg, String value)
    {
        if(arguments.containsKey(arg.getName()))
            data.put(arg, value);
        else
            System.out.println("[iBank] Error cant write data for non-existing argument!");
    }
    
    /**
     * Gets a value from an argument
     * @param arg The argument
     * @return
     */
    public String getValue(Argument arg)
    {
        if(data.containsKey(arg))
        {
            return data.get(arg);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Inserts values into this [PARSE METHOD]
     * @param data A list of given arguments
     * @return this
     */
    public ArgumentCollection insert(ArrayList<String> args)
    {
        // Match params
        int arg = 0;
        int c = 0;
        for(int i = 0; arg < args.size(); i++) {
            if(arguments.size() > i && arguments.get(i).getType() == Argument.Type.REQUIRED)
            {
                //Its a needed one so just add it
                data.put(arguments.get(i), args.get(arg));
                arg++;
            }
            else
            {
                if(arguments.size() > i && arguments.size() >= (required + 1 + c)) 
                {
                    data.put(arguments.get(i), args.get(arg));
                    c++;
                    arg++;
                }
            }
        }
        return this;
    }
    
    /**
     * Gets a value from an argument
     * @param arg [STRING] The argument
     * @return
     */
    public String getValue(String arg)
    {
        if(arguments.containsKey(arg))
        {
            Argument argument = arguments.get(arg);
            return getValue(argument);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * The constructor
     * @param args The arguments
     */
    public ArgumentCollection(Argument... args)
    {
        for(Argument arg : args)
        {
            addArgument(arg);
        }
    }

    /**
     * Gets a list containing all available keys
     * @return ArrayList<String>
     */
    public ArrayList<String> getList() {
        ArrayList<String> ret = new ArrayList<String>();
        for(Entry<Argument, String> s : data.entrySet())
        {
            ret.add(s.getKey().getName());
        }
        return ret;
    }
}
