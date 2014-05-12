package org.albianj.persistence.impl.context;

import java.util.Map;
import java.util.Set;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.impl.cached.AlbianObjectsMap;
import org.albianj.persistence.impl.cached.RoutingMap;
import org.albianj.persistence.object.IAlbianObjectAttribute;
import org.albianj.persistence.object.IAlbianObjectHashMapping;
import org.albianj.persistence.object.IFilterCondition;
import org.albianj.persistence.object.IOrderByCondition;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.persistence.object.IRoutingsAttribute;
import org.albianj.verify.Validate;

public class ReaderJobAdapter extends FreeReaderJobAdapter implements
		IReaderJobAdapter
{
	protected IRoutingAttribute parserReaderRouting(Class<?> cls,
			String routingName, Map<String, IFilterCondition> hashWheres,
			Map<String, IOrderByCondition> hashOrderbys)
	{
		String className = cls.getName();
		IRoutingsAttribute routings = (IRoutingsAttribute) RoutingMap
				.get(className);
		IAlbianObjectAttribute albianObject = (IAlbianObjectAttribute) AlbianObjectsMap
				.get(className);
		if (null == routings
				|| Validate.isNullOrEmpty(routings.getReaderRoutings()))
		{
			AlbianLoggerService.warn(
					"the '%s' routings is null or empty.then use default.",
					className);
			return albianObject.getDefaultRouting();
		}
		if (!Validate.isNullOrEmptyOrAllSpace(routingName))
		{
			IRoutingAttribute routing = routings.getReaderRoutings().get(
					routingName);
			if (null == routing)
			{
				AlbianLoggerService
						.warn("The '%s' routing is not in reader routings,then use default routing.",
								routingName);
				return albianObject.getDefaultRouting();
			}
			if (!routing.getEnable())
			{
				AlbianLoggerService.warn(
						"The '%s' routing is not enable,then use default.",
						routingName);
				return albianObject.getDefaultRouting();
			}
			return routing;
		}

		if (1 == routings.getReaderRoutings().size())
		{
			AlbianLoggerService.warn(
					"The %s routing in configure is one,so only use it.",
					className);

			Set<String> keys = routings.getReaderRoutings().keySet();
			String key = (String) keys.toArray()[0];
			IRoutingAttribute routing = routings.getReaderRoutings().get(key);
			if (null == routing)
			{
				AlbianLoggerService
						.warn("The '%s' routing is not in reader routings,then use default routing.",
								routingName);
				return albianObject.getDefaultRouting();
			}
			if (!routing.getEnable())
			{
				AlbianLoggerService
						.warn("The '%s' routing is only one,but not enable,then use default.",
								className);
				return albianObject.getDefaultRouting();
			}
			return routing;
		}

		if (routings.getReaderHash())
		{
			IAlbianObjectHashMapping hashMapping = routings.getHashMapping();
			if (null != hashMapping)
			{
				IRoutingAttribute routing = hashMapping.mappingReaderRouting(
						routings.getReaderRoutings(), hashWheres, hashOrderbys);
				if (null == routing || !routing.getEnable())
				{
					AlbianLoggerService
							.warn("The '%s'hash routing is null or empty,,then use default.",
									className);
					return albianObject.getDefaultRouting();
				}
				return routing;
			}
			AlbianLoggerService
					.warn("There is not hash function for reader.then use default.");
			return albianObject.getDefaultRouting();
		}

		AlbianLoggerService
				.warn("Not match the all parser routings condition.then use default.");
		return albianObject.getDefaultRouting();
	}
}
