package org.albianj.persistence.object.impl;

import org.albianj.persistence.object.IAlbianObject;

public abstract class FreeAlbianObject implements IAlbianObject
{
	private static final long serialVersionUID = 1608573290358087720L;
 
	private String id = "";
	private boolean isAlbianNew = true;
	
	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * albianj kernnel���������������������������albianj���������������������������������
	 */
	public boolean getIsAlbianNew()
	{
		return this.isAlbianNew;
	}

	/**
	 * albianj kernnel���������������������������albianj���������������������������������
	 */
	public void setIsAlbianNew(boolean isAlbianNew)
	{
		this.isAlbianNew = isAlbianNew;
	}
}
