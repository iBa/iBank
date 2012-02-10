package com.iBank.utils;

import java.math.BigDecimal;

import org.bukkit.Location;

/**
 * Math Utils needed by iBank
 * @author steffengy
 *
 */
public class Mathematics {
	/**
     * Returns whether the given coords match
     * @return Boolean
     * @params x,y,z The cordinates to-check
     * @params x,y,z The first point
     * @params x,y,z The second point
     * 9 Arguments
     */					  	//       x        y         z        x1       y1       z1       x2        x3      x4
    public static boolean isInBox(double d,double e, double f, double g,double h,double i,double j,double k,double l)
    {
    	double x1 = g>j?j:g; //x1=lower
    	double y1 = h>k?k:h;
    	double z1 = i>l?l:i;
    	double x2 = g<j?j:g; //x2=higher
    	double y2 = h<k?k:h;
    	double z2 = i<l?l:i;
    	if(d>=x1&&d<=x2&&e>=y1&&e<=y2&&f>=z1&&f<=z2)
    	{
    		return true;
    	}
    	return false;
    }
    
    /**
     * String -> BigDecimal
     * @param str
     * @return
     */
    public static BigDecimal parseString(String str) {
    	BigDecimal todp = null;
    	try{    			
    		todp = new BigDecimal(str);
    		}catch(Exception e) {

    			return todp;
    		}
    	return todp;
    }
    /**
     * Diffs location a with b
     * @param a 
     * @param b
     * @return int, whole difference
     */
    public static int[] diffLoc(Location a, Location b) {
    	int[] diff = new int[] { 0,0,0 };
    	if(a.getX() > b.getX()) 
    		diff[0] = a.getBlockX() - b.getBlockX(); 
    	else 
    		diff[0] = b.getBlockX() - a.getBlockX();
    	if(a.getY() > b.getY()) 
    		diff[1] += a.getBlockY() - b.getBlockY();
    	else 
    		diff[1] += b.getBlockY() - a.getBlockY();
    	if(a.getZ() > b.getZ()) 
    		diff[2] += a.getBlockZ() - b.getBlockZ();
    	else
    		diff[2] += b.getBlockZ() - a.getBlockZ();
    	return diff;
    }
}
