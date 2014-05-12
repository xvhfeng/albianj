package org.albianj.service.parser;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesParser
{
	public static Properties load(String filePath) throws Exception
	{
		Properties props;
		File file = new File(filePath);
		FileInputStream inStream = new FileInputStream(file);
		try
		{
			props = new Properties();
			props.load(inStream);
			return props;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			inStream.close();
		}
	}

	public static  String getValue(Properties props, String key)
	{
		return props.getProperty(key);
	}
	public static  String getValue(Properties props, String key, String defaultValue)
	{
		return props.getProperty(key, defaultValue);
	}

}
