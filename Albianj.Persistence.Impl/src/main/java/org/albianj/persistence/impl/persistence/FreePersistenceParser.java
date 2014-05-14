package org.albianj.persistence.impl.persistence;

import java.util.List;

import org.albianj.io.Path;
import org.albianj.kernel.KernelSetting;
import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.object.IAlbianObjectAttribute;
import org.albianj.service.FreeAlbianService;
import org.albianj.service.parser.IParser;
import org.albianj.verify.Validate;
import org.albianj.xml.XmlParser;
import org.dom4j.Document;
import org.dom4j.Element;

public abstract class FreePersistenceParser extends FreeAlbianService implements IParser
{

	private final static String file = "persistence.xml";
	private final static String tagName = "AlbianObjects/AlbianObject";
	
	public void init()
	{
		Document doc = null;
		try
		{
			doc = XmlParser.load(Path.getExtendResourcePath(KernelSetting.getAlbianConfigFilePath() + file));
		}
		catch(Exception e)
		{
			AlbianLoggerService.warn(e, "There is error when parser the persistence config file.");
		}
		if(null == doc)
		{
			throw new PersistenceAttributeException("load persistence is error.");
		}
		@SuppressWarnings("rawtypes")
		List nodes = XmlParser.analyze(doc, tagName);
		if (Validate.isNullOrEmpty(nodes))
		{
			String msg = String.format("There is not %1$s nodes.", tagName);
				AlbianLoggerService.error(msg);
			throw new UnsupportedOperationException(msg);
		}
		parserAlbianObjects(nodes);
		return;
	}
	
	protected abstract  void parserAlbianObjects(@SuppressWarnings("rawtypes") List nodes);
	protected abstract IAlbianObjectAttribute parserAlbianObject(Element node);

}
