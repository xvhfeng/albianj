package org.albianj.persistence.impl.context;

import org.albianj.persistence.object.IFilterCondition;
import org.albianj.persistence.object.IOrderByCondition;

public interface IReaderJobAdapter
{
	public IReaderJob buildReaderJob(Class<?> cls, String routingName,
			int start, int step, IFilterCondition[] wheres,
			IOrderByCondition[] orderbys);
}
