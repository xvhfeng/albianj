package org.albianj.service.parser;

import java.util.List;
import java.util.Map;

import org.albianj.io.Path;
import org.albianj.logger.AlbianLoggerService;
import org.albianj.service.AlbianServiceException;
import org.albianj.service.FreeAlbianService;
import org.albianj.service.IAlbianServiceAttribute;
import org.albianj.verify.Validate;
import org.albianj.xml.XmlParser;
import org.dom4j.Document;
import org.dom4j.Element;

public abstract class FreeServiceParser extends FreeAlbianService implements
		IServiceParser
{

	private final static String path = "../config/service.xml";
	private final static String tagName = "Services/Service";

	public final static String ALBIANJSERVICEKEY = "@$#&ALBIANJ_ALL_SERVICE&#$@";

	public void init()
	{
		
		Document doc = null;
		try
		{
			doc = XmlParser.load(Path.getExtendResourcePath(path));
		}
		catch(Exception e)
		{
			AlbianLoggerService.error(e, "There is error when parser the service config file.");
		}
		if(null == doc)
		{
			throw new AlbianServiceException("load service.xml is error.");
		}
		@SuppressWarnings("rawtypes")
		List nodes = XmlParser.analyze(doc, tagName);
		
		if (Validate.isNullOrEmpty(nodes))
		{
			String msg = String.format("There is not %1$s nodes.", tagName);
			AlbianLoggerService.error(msg);
			throw new NullPointerException(msg);
		}
		Map<String, IAlbianServiceAttribute> map = parserServices(tagName,nodes);
		if (null == map)
		{
			AlbianLoggerService.error("The albian services is empty.");
			return;
		}
		ServiceAttributeMap.insert(ALBIANJSERVICEKEY, map);
		return;
	}

	protected abstract Map<String, IAlbianServiceAttribute> parserServices(String tarName,
			@SuppressWarnings("rawtypes") List nodes)
			throws NullPointerException, AlbianServiceException;

	protected abstract IAlbianServiceAttribute parserService(String name,Element node)
			throws NullPointerException, AlbianServiceException;
}
