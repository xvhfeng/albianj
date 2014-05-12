package org.albianj.persistence.object.impl;

import java.util.Map;

import org.albianj.persistence.object.IAlbianObjectAttribute;
import org.albianj.persistence.object.ICacheAttribute;
import org.albianj.persistence.object.IMemberAttribute;
import org.albianj.persistence.object.IRoutingAttribute;

public class AlbianObjectAttribute implements IAlbianObjectAttribute
{

	private ICacheAttribute cache = null;
	private IRoutingAttribute defaultRouting = null;
	private  Map<String,IMemberAttribute> members = null;
	private String type = null; 
	
	public String getType()
	{
		return this.type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public ICacheAttribute getCache()
	{
		// TODO Auto-generated method stub
		return this.cache;
	}

	public void setCache(ICacheAttribute cache)
	{
		// TODO Auto-generated method stub
		this.cache = cache;
	}

	public IRoutingAttribute getDefaultRouting()
	{
		return this.defaultRouting;
	}
	
	public void setDefaultRouting(IRoutingAttribute defaultRouting)
	{
		this.defaultRouting = defaultRouting;
	}
	
	public Map<String,IMemberAttribute> getMembers()
	{
		// TODO Auto-generated method stub
		return this.members;
	}

	public void setMembers(Map<String,IMemberAttribute> members)
	{
		// TODO Auto-generated method stub
		this.members = members;
	}
}