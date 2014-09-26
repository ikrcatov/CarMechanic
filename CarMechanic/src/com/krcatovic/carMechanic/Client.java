package com.krcatovic.carMechanic;

public class Client
{
	public String firstname = "";
	public String lastname = "";
	public String phone = "";
	public Integer rowId = Integer.valueOf(0);
	
	public Client() {}
	
	public Client(int i, String firstname, String lastname, String phone)
	{
	  this.firstname = firstname;
	  this.lastname = lastname;
	  this.phone = phone;
	  this.rowId = Integer.valueOf(i);
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
	
	public String getPhone()
	{
	  return this.phone;
	}
	
	public void setPhone(String phone)
	{
	  this.phone = phone;
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