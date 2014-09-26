package com.krcatovic.carMechanic;

import java.util.List;

public class Clients
{
	private List<Client> clients;
	private String username = "";
	private String password = "";
	private String rememberCbox = "";
	
	public String getRememberCbox()
	{
	  return this.rememberCbox;
	}
	
	public void setRememberCbox(String rememberCbox)
	{
	  this.rememberCbox = rememberCbox;
	}
	
	public List<Client> getClients()
	{
	  return this.clients;
	}
	
	public void setClients(List<Client> clients)
	{
	  this.clients = clients;
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
}