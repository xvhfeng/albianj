package org.albianj.kernel;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.service.IAlbianService;

/**
 * 获取albianj控制的service。
 * 使用此类获取的service必须实现IAlbianSerivce或者继承FreeAlbianService，并且在service.xml中配置启动。
 * 
 * @author Seapeak
 *
 */
public class AlbianServiceRouter
{
	/**
	 * 获取相应的albianj控制的service。
	 * @param cla：获取service的接口
	 * @param id：在service.xml中配置的相应的id
	 * @param isThrowIfException： 获取service出错是否抛出异常
	 * 
	 * @important 获取的service都是单例模式，也就是说进程共同使用一个service，故service应该是无状态的。
	 */
	public static <T extends IAlbianService> T getService(Class<T> cla,String id,boolean isThrowIfException) throws IllegalArgumentException
	{
		if(null == id || id.isEmpty())
			throw new IllegalArgumentException("id");
		try
		{
			IAlbianService service =(IAlbianService) ServiceMap.get(id);		
			if(null == service) return null;
			return cla.cast(service);
		}
		catch(IllegalArgumentException exc)
		{
				AlbianLoggerService.error(exc,"Get service is error.");
			if(isThrowIfException)
				throw exc;
		}
		return null;
	}
	
	/**
	 * 获取相应的albianj控制的service，出错会返回null，不抛出异常
	 * @param cla：获取service的接口
	 * @param id：在service.xml中配置的相应的id
	 * 
	 * @important 获取的service都是单例模式，也就是说进程共同使用一个service，故service应该是无状态的。
	 */
	public static <T extends IAlbianService> T getService(Class<T> cla,String id)
	{
		try
		{
			return getService(cla, id,false);
		}
		catch(Exception exc)
		{
		}
		return null;
	}
}
