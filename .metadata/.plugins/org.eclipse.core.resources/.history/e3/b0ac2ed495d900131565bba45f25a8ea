package org.albianj.service;

import org.albianj.verify.Validate;

public class AlbianServiceAttribute implements IAlbianServiceAttribute
{

	private String id = "";
	private String type = "";

	@Override
	public String getId()
	{
		return this.id;
	}

	@Override
	public void setId(String id) throws IllegalArgumentException
	{
		if (Validate.isNullOrEmptyOrAllSpace(id)) 
		{ 
			throw new IllegalArgumentException("id"); 
		}
		this.id = id;
	}

	@Override
	public String getType()
	{
		return this.type;
	}

	@Override
	public void setType(String type) throws IllegalArgumentException
	{
		if (Validate.isNullOrEmptyOrAllSpace(type)) 
		{ 
			throw new IllegalArgumentException("type"); 
		}
		this.type = type;

	}

}
