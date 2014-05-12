package org.albianj.persistence.impl.db;

import java.util.Map;

import org.albianj.persistence.object.IAlbianObject;
import org.albianj.persistence.object.IAlbianObjectAttribute;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.persistence.object.IRoutingsAttribute;

public class SaveCommandAdapter implements IUpdateCommand
{

	private IUpdateCommand create;
	private IUpdateCommand modify;
	public SaveCommandAdapter(IUpdateCommand create,IUpdateCommand modify)
	{
		this.create = create;
		this.modify = modify;
	}
	
	public SaveCommandAdapter()
	{
		if(null == this.create)
			this.create = new CreateCommandAdapter();
		if(null == this.modify)
			this.modify = new ModifyCommandAdapter();
	}
	
	public ICommand builder(IAlbianObject object, IRoutingsAttribute routings,
			IAlbianObjectAttribute albianObject, Map<String, Object> mapValue,
			IRoutingAttribute routing)
	{
		if(object.getIsAlbianNew())
		{
			return create.builder(object, routings, albianObject, mapValue, routing);
		}
		else
		{
			return modify.builder(object, routings, albianObject, mapValue, routing);
		}
	}

}
