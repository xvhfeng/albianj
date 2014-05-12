package org.albianj.persistence.impl.storage;

import java.util.List;

import org.albianj.io.Path;
import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.impl.cached.StorageAttributeMap;
import org.albianj.persistence.object.DatabaseStyle;
import org.albianj.persistence.object.IStorageAttribute;
import org.albianj.service.FreeAlbianService;
import org.albianj.service.parser.IParser;
import org.albianj.verify.Validate;
import org.albianj.xml.XmlParser;
import org.dom4j.Document;
import org.dom4j.Element;

public abstract class FreeStorageParser extends FreeAlbianService implements
		IParser
{
	private final static String path = "../config/storage.xml";
	private final static String tagName = "Storages/Storage";

	@Override
	public void init()
	{
		Document doc = null;
		try
		{
			doc = XmlParser.load(Path.getExtendResourcePath(path));
		}
		catch(Exception e)
		{
			AlbianLoggerService.error(e, "There is error when parser the storage config file.");
		}
		if (null == doc) { throw new RuntimeException(
				"load persistence is error."); }
		@SuppressWarnings("rawtypes")
		List nodes = XmlParser.analyze(doc, tagName);
		if (Validate.isNullOrEmpty(nodes))
		{
			String msg = String.format("There is not %1$s nodes.", tagName);
			AlbianLoggerService.error(msg);
			throw new UnsupportedOperationException(msg);
		}
		parserStorages(nodes);
		return;
	}

	protected abstract void parserStorages(
			@SuppressWarnings("rawtypes") List nodes)
			throws StorageAttributeException;

	protected abstract IStorageAttribute parserStorage(Element node);

	public static String generateConnectionUrl(String storageName)
	{
		if (Validate.isNullOrEmptyOrAllSpace(storageName))
		{
			AlbianLoggerService
					.warn("the argument storageName is null or empty.");
			return null;
		}
		IStorageAttribute storageAttribute = (IStorageAttribute) StorageAttributeMap
				.get(storageName);
		return generateConnectionUrl(storageAttribute);
	}

	public static String generateConnectionUrl(
			IStorageAttribute storageAttribute)
	{
		if (null == storageAttribute)
		{
			AlbianLoggerService.warn("The argument storageAttribute is null.");
			return null;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:");
		// String url =
		// "jdbc:mysql://localhost/baseinfo?useUnicode=true&characterEncoding=8859_1";
		switch (storageAttribute.getDatabaseStyle())
		{
			case (DatabaseStyle.MySql):
			{
				sb.append("mysql://").append(storageAttribute.getServer())
						.append("/").append(storageAttribute.getDatabase());
				if (null != storageAttribute.getCharset())
				{
					sb.append("?useUnicode=true&characterEncoding=").append(
							storageAttribute.getCharset());
				}
			}
			case (DatabaseStyle.Oracle):
			{
				sb.append("oracle:thin:@").append(storageAttribute.getServer())
						.append(":").append(storageAttribute.getDatabase());
			}
			case (DatabaseStyle.SqlServer):
			{
				sb.append("microsoft:sqlserver://")
						.append(storageAttribute.getServer()).append(";")
						.append(storageAttribute.getDatabase());
			}
			default:
			{
				sb.append("mysql://").append(storageAttribute.getServer())
						.append("/").append(storageAttribute.getDatabase());
				if (null != storageAttribute.getCharset())
				{
					sb.append("?useUnicode=true&characterEncoding=").append(
							storageAttribute.getCharset());
				}
			}
		}
		return sb.toString();
	}
}
