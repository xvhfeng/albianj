package org.albianj.service.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import org.albianj.kernel.KernelSetting;

public class PropertiesParser
{
	public static Properties load(String filePath) throws Exception
	{
		Properties props;
		File file = null;
		InputStream inStream = null;
		if(KernelSetting.getAlbianConfigFilePath().startsWith("http://") ||
				KernelSetting.getAlbianConfigFilePath().startsWith("https://")){
			URL url = new URL(filePath);
			inStream = url.openStream();
		}else {
			file = new File(filePath);
			inStream = new FileInputStream(file);
		}
		
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
			if(null != inStream){
				inStream.close();	
			}
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
