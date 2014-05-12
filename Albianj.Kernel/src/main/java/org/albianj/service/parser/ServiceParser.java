package org.albianj.service.parser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.service.AlbianServiceAttribute;
import org.albianj.service.IAlbianServiceAttribute;
import org.albianj.verify.Validate;
import org.albianj.xml.XmlParser;
import org.dom4j.Element;

public class ServiceParser extends FreeServiceParser
{

	private final static String ID_ATTRBUITE_NAME = "Id";
	private final static String TYPE_ATTRBUITE_NAME = "Type";

	@Override
	public void loading()
	{
		super.init();
		super.loading();
	}

	@Override
	protected Map<String, IAlbianServiceAttribute> parserServices(
			@SuppressWarnings("rawtypes") List nodes)
	{
		if (Validate.isNullOrEmpty(nodes))
		{
			String msg = "nodes is null or size is 0";
			AlbianLoggerService.error(msg);
			throw new IllegalArgumentException(msg);
		}
		Map<String, IAlbianServiceAttribute> map = new LinkedHashMap<String, IAlbianServiceAttribute>(
				nodes.size());
		for (Object node : nodes)
		{
			Element elt = XmlParser.toElement(node);
			IAlbianServiceAttribute serviceAttr = parserService(elt);
			if (null == serviceAttr) { throw new NullPointerException(
					"parser service node is error."); }
			map.put(serviceAttr.getId(), serviceAttr);
		}
		return 0 == map.size() ? null : map;
	}

	@Override
	protected IAlbianServiceAttribute parserService(Element elt)
	{
		if (null == elt)
		{
			String msg = "The node is null";
			AlbianLoggerService.error(msg);
			throw new IllegalArgumentException(msg);
		}
		IAlbianServiceAttribute serviceAttr = new AlbianServiceAttribute();
		String id = XmlParser.getAttributeValue(elt, ID_ATTRBUITE_NAME);
		if (Validate.isNullOrEmptyOrAllSpace(id))
		{
			AlbianLoggerService.error("The id is null or empty.");
			return null;
		}
		serviceAttr.setId(id);
		String type = XmlParser.getAttributeValue(elt, TYPE_ATTRBUITE_NAME);
		if (Validate.isNullOrEmptyOrAllSpace(type))
		{
			AlbianLoggerService.error("The", serviceAttr.getId(),
					"Type is null or empty.");
			return null;
		}
		serviceAttr.setType(type);
		return serviceAttr;
	}

}
