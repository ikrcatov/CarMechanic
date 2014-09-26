package com.krcatovic.carMechanic;

public class Task
{
	public String taskName = "";
	public String taskDescription = "";
	public String taskStatus = "";
	public Integer rowId = Integer.valueOf(0);
	
	public Task(int i, String taskName, String taskDescription, String taskStatus)
	{
	  this.taskName = taskName;
	  this.taskDescription = taskDescription;
	  this.taskStatus = taskStatus;
	  this.rowId = Integer.valueOf(i);
	}
	
	public String getTaskName()
	{
	  return this.taskName;
	}
	
	public void setTaskName(String taskName)
	{
	  this.taskName = taskName;
	}
	
	public String getTaskDescription()
	{
	  return this.taskDescription;
	}
	
	public void setTaskDescription(String taskDescription)
	{
	  this.taskDescription = taskDescription;
	}
	
	public String getTaskStatus()
	{
	  return this.taskStatus;
	}
	
	public void setTaskStatus(String taskStatus)
	{
	  this.taskStatus = taskStatus;
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