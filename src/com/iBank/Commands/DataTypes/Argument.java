package com.iBank.Commands.DataTypes;

/**
 * A single argument in a collection
 * @author steffen
 *
 */
public class Argument 
{
    /**
     * All argument types
     * @author steffen
     *
     */
    enum Type
    {
        REQUIRED,
        OPTIONAL
    }
    /**
     * The current type
     */
    private Argument.Type type;
    /**
     * The current name
     */
    private String name;
    /**
     * Constructor
     * @param type The type of the argument
     * @param name The name of the argument
     */
    public Argument(Argument.Type type, String name)
    {
        this.type = type;
        this.name = name;
    }
    /**
     * Gets the type of this argument
     * @return Argument.Type
     */
    public Type getType()
    {
        return this.type;
    }
    /**
     * Gets the name of this argument
     * @return String The name
     */
    public String getName()
    {
        return this.name;
    }
}
