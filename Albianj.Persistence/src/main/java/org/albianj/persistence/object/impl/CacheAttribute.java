package org.albianj.persistence.object.impl;

import org.albianj.persistence.object.ICacheAttribute;

public class CacheAttribute implements ICacheAttribute
{
	private boolean enable = true;
	private int lifeTime = 300;

	public boolean getEnable()
	{
		// TODO Auto-generated method stub
		return this.enable;
	}

	public void setEnable(boolean enable)
	{
		// TODO Auto-generated method stub
		this.enable = enable;
	}

	public int getLifeTime()
	{
		// TODO Auto-generated method stub
		return this.lifeTime;
	}

	public void setLifeTime(int lifeTime)
	{
		// TODO Auto-generated method stub
		this.lifeTime = lifeTime;
	}

}
