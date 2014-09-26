package com.krcatovic.carMechanic;

public class ClientCredential
{
	public String firstname = "";
	public String lastname = "";
	public String username = "";
	public String password = "";
	public Integer rowId = Integer.valueOf(0);
	
	public ClientCredential() {}
	
	public ClientCredential(String firstname, String lastname, String username, String password)
	{
	  this.firstname = firstname;
	  this.lastname = lastname;
	  this.username = username;
	  this.password = password;
	}
	
	public String getUsername()
	{
	  return this.username;
	}
	
	public void setUsername(String username)
	{
	  this.username = username;
	}
	
	public String getPassword()
	{
	  return this.password;
	}
	
	public void setPassword(String password)
	{
	  this.password = password;
	}
	
	public String getFirstname()
	{
	  return this.firstname;
	}
	
	public void setFirstname(String firstname)
	{
	  this.firstname = firstname;
	}
	
	public String getLastname()
	{
	  return this.lastname;
	}
	
	public void setLastname(String lastname)
	{
	  this.lastname = lastname;
	}
	
	public Integer getRowId()
	{
	  return this.rowId;
	}
	
	public void setRowId(Integer rowId)
	{
	  this.rowId = rowId;
	}
}