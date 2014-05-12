package org.albianj.persistence.impl.db;

import java.util.Map;

import org.albianj.persistence.object.IAlbianObject;
import org.albianj.persistence.object.IAlbianObjectAttribute;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.persistence.object.IRoutingsAttribute;

public interface IUpdateCommand
{
	public ICommand builder(IAlbianObject object,
			IRoutingsAttribute routings, IAlbianObjectAttribute albianObject,
			Map<String, Object> mapValue, IRoutingAttribute routing);
}
