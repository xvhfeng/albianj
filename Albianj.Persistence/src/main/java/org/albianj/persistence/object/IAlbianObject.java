package org.albianj.persistence.object;
import java.io.Serializable;


public interface IAlbianObject extends Serializable
{	
	

	public String getId();
	public void setId(String id);
	/**
	 * albianj kernnel方法，在不重新实现albianj机制的情况下不要去操作
	 */
	public boolean getIsAlbianNew();
	
	  /**
	   * albianj kernel方法，在不重新实现albianj机制的情况下不要去操作
	   */
	public void setIsAlbianNew(boolean isNew);
	
	public void setOldAlbianObject(String key,Object v);
	
	public Object getOldAlbianObject(String key);
}
