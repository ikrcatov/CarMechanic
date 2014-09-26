package com.krcatovic.carMechanic;

public class Helper
{
	public static Boolean isStringNullEmpty(String variable)
	{
		if (variable != null)
		{
		    if (!variable.equals(""))
			{
		    	if (!variable.equals("undefined")) 
		    	{
		    		return Boolean.valueOf(false);
		    	}
			    
		    	return Boolean.valueOf(true);
			}
		    return Boolean.valueOf(true);
		}
	  return Boolean.valueOf(true);
	}
}