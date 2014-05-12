package org.albianj.persistence.object.impl;

import java.util.Map;

import org.albianj.persistence.object.IAlbianObjectHashMapping;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.persistence.object.IRoutingsAttribute;

public class RoutingsAttribute implements IRoutingsAttribute
{

	private boolean writerHash = false;
	private boolean readerHash = false;
	private Map<String,IRoutingAttribute> writerRoutings = null;
	private Map<String,IRoutingAttribute> readerRoutings = null;
	private IAlbianObjectHashMapping hashMapping = null; 
	private String type = null;
	
	public String getType()
	{
		return this.type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public boolean getWriterHash()
	{
		// TODO Auto-generated method stub
		return this.writerHash;
	}

	public void setWriterHash(boolean writerHash)
	{
		// TODO Auto-generated method stub
		this.writerHash = writerHash;
	}

	public boolean getReaderHash()
	{
		// TODO Auto-generated method stub
		return this.readerHash;
	}

	public void setReaderHash(boolean readerHash)
	{
		// TODO Auto-generated method stub
		this.readerHash = readerHash;
	}

	public Map<String,IRoutingAttribute> getWriterRoutings()
	{
		// TODO Auto-generated method stub
		return this.writerRoutings;
	}

	public void setWriterRoutings(Map<String,IRoutingAttribute> writerRoutings)
	{
		// TODO Auto-generated method stub
		this.writerRoutings = writerRoutings;
	}

	public Map<String,IRoutingAttribute> getReaderRoutings()
	{
		// TODO Auto-generated method stub
		return this.readerRoutings;
	}

	public void setReaderRoutings(Map<String,IRoutingAttribute> readerRoutings)
	{
		// TODO Auto-generated method stub
		this.readerRoutings = readerRoutings;
	}

	public IAlbianObjectHashMapping getHashMapping()
	{
		return this.hashMapping;
	}
	public void setHashMapping(IAlbianObjectHashMapping hashMapping)
	{
		this.hashMapping = hashMapping;
	}
}
