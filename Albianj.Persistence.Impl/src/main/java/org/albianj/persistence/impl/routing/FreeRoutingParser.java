package org.albianj.persistence.impl.routing;

import java.util.List;
import java.util.Map;

import org.albianj.io.Path;
import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.service.FreeAlbianService;
import org.albianj.service.parser.IParser;
import org.albianj.xml.XmlParser;
import org.dom4j.Document;

//<AlbianObjects>
//<AlbianObject Type="AppTest.Model.Imp.BizOffer">
//	<WriterRoutings Hash="true">
//		<WriteRouting Name="IdRouting" StorageName="2thStorage"
//			TableName="BizOfferById" Owner="dbo"></WriteRouting>
//	</WriterRoutings>
//	<ReaderRoutings Hash="true">
//		<ReaderRouting Name="CreateTimeRouting" StorageName="3thStorage"
//			TableName="BizOfferByCreateTime" Owner="dbo"></ReaderRouting>
//	</ReaderRoutings>
//</AlbianObject>
//</AlbianObjects>

public abstract class FreeRoutingParser extends FreeAlbianService implements
		IParser
{
	private final static String path = "../config/routing.xml";
	private final static String tagName = "AlbianObjects/AlbianObject";

	@Override
	public void init()
	{
		Document doc = null;
		try
		{
			doc = XmlParser.load(Path.getExtendResourcePath(path));
		}
		catch (Exception e)
		{
			AlbianLoggerService.error(e,
					"There is error when parser the routing config file");
		}
		if (null == doc) { throw new RuntimeException(
				"load persistence is error."); }
		@SuppressWarnings("rawtypes")
		List nodes = XmlParser.analyze(doc, tagName);
		if (null == nodes || 0 == nodes.size())
		{
			String msg = String.format("There is not %1$s nodes.", tagName);
			AlbianLoggerService.error(msg);
			throw new UnsupportedOperationException(msg);
		}
		parserRoutings(nodes);
		return;

	}

	protected abstract Map<String, IRoutingAttribute> parserRoutings(
			@SuppressWarnings("rawtypes") List nodes);
	// protected abstract Map<String,IRoutingAttribute> parserRouting(Element
	// elt);
}
