package org.albianj.persistence.impl.persistence;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.albianj.logger.AlbianLoggerService;

public class ReflectionHelper
{
	public static BeanInfo getBeanInfo(String className)
	{
		try
		{
			@SuppressWarnings("rawtypes")
			Class cls = Class.forName(className);
			BeanInfo info = Introspector.getBeanInfo(cls, Object.class);
			return info;
		}
		catch (ClassNotFoundException exc)
		{
				AlbianLoggerService.error(exc,"Reflection bean is error.");
			return null;
		}
		catch (IntrospectionException exc)
		{
				AlbianLoggerService.error(exc,"Reflection bean is error.Message:");
			return null;
		}
	}
	
	public static PropertyDescriptor[] getBeanPropertyDescriptors(String className)
	{
		BeanInfo beanInfo;
		try
		{
			beanInfo = getBeanInfo(className);
		}
		catch(Exception exc)
		{			
				AlbianLoggerService.error(exc,"Get bean info is error.");
			return null;
		}
		if(null == beanInfo)
		{
			AlbianLoggerService.error("Get bean info is error.");
			return null;
		}
		return beanInfo.getPropertyDescriptors();
	}
	
	public static String getClassSimpleName(String className)
	{
		try
		{
			@SuppressWarnings("rawtypes")
			Class cls = Class.forName(className);
			return cls.getSimpleName();
		}
		catch (ClassNotFoundException exc)
		{
			AlbianLoggerService.error(exc,"Reflection bean is error.");
			return null;
		}
	}

	public static String getClassName(Class<?> cls)
	{
		return cls.getName();
	}
	
	public static String getSimpleName(Class<?> cls)
	{
		return cls.getSimpleName();
	}
}
