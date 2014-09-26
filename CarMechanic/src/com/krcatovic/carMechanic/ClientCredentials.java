package com.krcatovic.carMechanic;

import java.util.List;

public class ClientCredentials
{
	private List<ClientCredential> clientCredentials;
  
	public List<ClientCredential> getClientCredentials()
	{
	    return this.clientCredentials;
	}
  
	public void setClientCredentials(List<ClientCredential> clientCredentials)
	{
		this.clientCredentials = clientCredentials;
	}
}