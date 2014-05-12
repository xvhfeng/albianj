package org.albianj.persistence.impl.routing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.impl.cached.RoutingMap;
import org.albianj.persistence.object.IAlbianObjectHashMapping;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.persistence.object.IRoutingsAttribute;
import org.albianj.persistence.object.impl.RoutingAttribute;
import org.albianj.persistence.object.impl.RoutingsAttribute;
import org.albianj.verify.Validate;
import org.albianj.xml.XmlParser;
import org.dom4j.Element;
import org.dom4j.Node;

public class RoutingService extends FreeRoutingParser
{

	public static final String DEFAULT_ROUTING_NAME = "!@#$%Albianj_Default_Routing%$#@!";

	public void loading()
	{
		super.init();
	}

	protected Map<String, IRoutingAttribute> parserRoutings(
			@SuppressWarnings("rawtypes") List nodes)
	{
		for (Object node : nodes)
		{
			IRoutingsAttribute routingsAttribute = getRoutingsAttribute((Element) node);
			if (null == routingsAttribute) return null;
			RoutingMap
					.insert(routingsAttribute.getType(), routingsAttribute);
		}
		return null;
	}

	private static IRoutingsAttribute getRoutingsAttribute(Element elt)
	{
		String type = XmlParser.getAttributeValue(elt, "Type");
		IRoutingsAttribute routing = new RoutingsAttribute();
		if (Validate.isNullOrEmptyOrAllSpace(type))
		{
			AlbianLoggerService
					.error("The albianObject's type is empty or null.");
			return null;
		}

		routing.setType(type);
		String hashMapping = XmlParser.getAttributeValue(elt, "HashMapping");
		if (!Validate.isNullOrEmptyOrAllSpace(hashMapping))
		{
			try
			{
				Class<?> cls = Class.forName(hashMapping);
				routing.setHashMapping((IAlbianObjectHashMapping) cls
						.newInstance());

			}
			catch (ClassNotFoundException e)
			{
				AlbianLoggerService
						.error(e, "fail in find class for %s.", type);
				return null;
			}
			catch (InstantiationException e)
			{
				AlbianLoggerService.error(e,
						"init the hash mapping for the %s is error.", type);
				return null;
			}
			catch (IllegalAccessException e)
			{
				AlbianLoggerService.error(e,
						"There is no access for %s with init the instance.",
						type);
				return null;
			}

		}

		Node writer = elt.selectSingleNode("WriterRoutings");
		if (null != writer)
		{
			String hash = XmlParser.getAttributeValue(writer, "Hash");
			if (!Validate.isNullOrEmptyOrAllSpace(hash))
			{
				routing.setWriterHash(new Boolean(hash));
			}
		}
		Node reader = elt.selectSingleNode("ReaderRoutings");
		if (null != reader)
		{
			String hash = XmlParser.getAttributeValue(reader, "Hash");
			if (!Validate.isNullOrEmptyOrAllSpace(hash))
			{
				routing.setReaderHash(new Boolean(hash));
			}
		}

		List<?> writers = elt.selectNodes("WriterRoutings/WriterRouting");
		if (!Validate.isNullOrEmpty(writers))
		{
			Map<String, IRoutingAttribute> map = parserRouting(writers);
			if (null != map) routing.setWriterRoutings(map);
		}

		List<?> readers = elt.selectNodes("ReaderRoutings/ReaderRouting");
		if (!Validate.isNullOrEmpty(readers))
		{
			Map<String, IRoutingAttribute> map = parserRouting(readers);
			if (null != map) routing.setReaderRoutings(map);
		}
		return routing;
	}

	private static Map<String, IRoutingAttribute> parserRouting(
			@SuppressWarnings("rawtypes") List nodes)
	{
		Map<String, IRoutingAttribute> map = new HashMap<String, IRoutingAttribute>();
		for (Object node : nodes)
		{
			IRoutingAttribute routingAttribute = getroutingAttribute((Element) node);
			if (null == routingAttribute) return null;
			map.put(routingAttribute.getName(), routingAttribute);
		}
		return map;
	}

	private static IRoutingAttribute getroutingAttribute(Element elt)
	{
		String name = XmlParser.getAttributeValue(elt, "Name");
		if (Validate.isNullOrEmptyOrAllSpace(name))
		{
				AlbianLoggerService
					.error("this routing attribute is null or empty.");
			return null;
		}

		String storageName = XmlParser.getAttributeValue(elt, "StorageName");
		if (Validate.isNullOrEmptyOrAllSpace(storageName))
		{
			AlbianLoggerService.error("this storage name for the %s routing attribute is null or empty.",name);
			return null;
		}
		IRoutingAttribute routing = new RoutingAttribute();
		routing.setName(name);
		routing.setStorageName(storageName);
		String tableName = XmlParser.getAttributeValue(elt, "TableName");
		if (!Validate.isNullOrEmptyOrAllSpace(tableName))
		{
			routing.setTableName(tableName);
		}
		String enable = XmlParser.getAttributeValue(elt, "Enable");
		if (!Validate.isNullOrEmptyOrAllSpace(enable))
		{
			routing.setEnable(new Boolean(enable));
		}
		String owner = XmlParser.getAttributeValue(elt, "Owner");
		if (!Validate.isNullOrEmptyOrAllSpace(owner))
		{
			routing.setOwner(owner);
		}

		return routing;

	}

}
