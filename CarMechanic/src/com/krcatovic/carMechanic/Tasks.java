package com.krcatovic.carMechanic;

import java.util.List;

public class Tasks
{
	private List<Task> tasks;
	private String username = "";
	private String password = "";
	private String rememberCbox = "";
	private Integer rowIdClient = Integer.valueOf(0);
	
	public Integer getRowIdClient()
	{
	  return this.rowIdClient;
	}
	
	public void setRowIdClient(Integer rowIdClient)
	{
	  this.rowIdClient = rowIdClient;
	}
	
	public String getRememberCbox()
	{
	  return this.rememberCbox;
	}
	
	public void setRememberCbox(String rememberCbox)
	{
	  this.rememberCbox = rememberCbox;
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
	
	public List<Task> getTasks()
	{
	  return this.tasks;
	}
	
	public void setTasks(List<Task> tasks)
	{
	  this.tasks = tasks;
	}
}