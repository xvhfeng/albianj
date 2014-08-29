package org.albianj.persistence.object.impl;

import java.util.HashMap;

import org.albianj.persistence.object.IAlbianObject;

public abstract class FreeAlbianObject implements IAlbianObject
{
	private static final long serialVersionUID = 1608573290358087720L;
 
	private String id = "";
	private boolean isAlbianNew = true;
	protected transient HashMap<String,Object> dic = null;
	
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
	
	public void setOldAlbianObject(String key,Object v){
		if(null == dic){
			dic = new HashMap<String,Object>();
		}
		dic.put(key, v);
	}
	
	public Object getOldAlbianObject(String key){
		if(null == dic){
			return null;
		}
		return dic.get(key);
	}
	
	
}
