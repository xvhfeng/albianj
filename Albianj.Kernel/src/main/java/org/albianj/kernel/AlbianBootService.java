package org.albianj.kernel;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.service.AlbianIdService;
import org.albianj.service.AlbianServiceException;
import org.albianj.service.IAlbianService;
import org.albianj.service.IAlbianServiceAttribute;
import org.albianj.service.parser.FreeServiceParser;
import org.albianj.service.parser.IServiceParser;
import org.albianj.service.parser.ServiceAttributeMap;
import org.albianj.service.parser.ServiceParser;

/**
 * albianj启动类。
 * 必须在使用albianj的任何功能前调用start。
 * 一般调用start此方法都是在进程启动时，并且一个进程只需调用一次即可
 * 
 * @author Seapeak
 *
 */
@Kernel
public final class AlbianBootService
{
	private static AlbianState state = AlbianState.Normal;
	private static Date startDateTime;
	private static String serialId;
	
	public static Date getStartDateTime()
	{
		return startDateTime;
	}
	public static String getSerialId()
	{
		return serialId;
	}
	
	
	
	public static AlbianState getLifeState()
	{
		return state;
	}
	
	/**
	 * 启动albianj
	 * 此方法会读取你根目录下的config文件夹下的配置文件，根据service.xml文件中配置的service解析各个服务，
	 * 并且把服务全部设置成单例，放入缓存。解析服务和启动服务不受相互依赖关系
	 * 
	 * @throws Exception
	 */
	public static void start() throws Exception
	{
		startDateTime = new Date();
		serialId = AlbianIdService.generate32UUID();
		
//		Thread thread = new ServiceThread(); 
//		thread.start();
		doStart();
	}
	
	static void doStart() throws AlbianServiceException
	{
		state = AlbianState.Initing;
		IServiceParser parser = new ServiceParser();
		parser.init();
		@SuppressWarnings("unchecked")
		LinkedHashMap<String,IAlbianServiceAttribute> map = (LinkedHashMap<String, IAlbianServiceAttribute>) ServiceAttributeMap.get(FreeServiceParser.ALBIANJSERVICEKEY);
		LinkedHashMap<String,IAlbianServiceAttribute> failMap = new LinkedHashMap<String,IAlbianServiceAttribute>();
		int lastFailSize = 0;
		int currentFailSize = 0;
		
		while(true)
		{
			lastFailSize = currentFailSize;
			currentFailSize = 0;
			for(Map.Entry<String,IAlbianServiceAttribute> entry : map.entrySet())
			{
				try
				{
					IAlbianServiceAttribute serviceAttr = entry.getValue(); 
					Class<?> cla = Class.forName(serviceAttr.getType());
					IAlbianService service =(IAlbianService) cla.newInstance();
					service.beforeLoad();
					service.loading();
					service.afterLoading();
					ServiceMap.insert(entry.getKey(), service);
				}
				catch(Exception exc)
				{
					currentFailSize++;
					failMap.put(entry.getKey(), entry.getValue());
				}
			}
			if(0 == currentFailSize) 
			{
				/*
				IControllerClientService controlService = 
						AlbianServiceRouter.getService(IControllerClientService.class, "ControllerClientService");
				if(null == controlService)
				{
					String msg = "The kernel no regedit to controls center.";
					AlbianLoggerService.error(msg);
					state = AlbianState.Unloaded;
					throw new AlbianServiceException(msg);
				}
				else
				{
					controlService.report();
				}
				*/
				state = AlbianState.Running;
				AlbianLoggerService.info("start service is success!");
				break;//all success
			}
			
			if(lastFailSize == currentFailSize)//create instance but all fail in this times,so throw exception
			{
				state = AlbianState.Unloading;
				/*
				IControllerClientService controlService = 
						AlbianServiceRouter.getService(IControllerClientService.class, "ControllerClientService");
				if(null == controlService)
				{
					String msg = "The kernel no regedit to controls center.";
					AlbianLoggerService.warn(msg);
				}
				else
				{
					controlService.logout();
				}
				*/
				
				String msg = "the service maybe cross reference.";
					AlbianLoggerService.error(msg);
					AlbianLoggerService.error(failMap.keySet().toString());
				ServiceMap.clear();
				state = AlbianState.Unloaded;
				throw new AlbianServiceException(msg);
			}
			else
			{
				map.clear();
				map.putAll(failMap);
				failMap.clear();	
			}		
		}
	}
	
	public static String requestHandlerContext()
	{
		if(AlbianState.Running != state)
		{
			return "Albian is not ready,Please wait a minute or contact administrators!";
		}
		return "";
	}
	
	public static void unload() throws Exception
	{
		Set<String> keys = ServiceMap.getKeys();
		for(String key : keys)
		{
			try
			{
				IAlbianService service = (IAlbianService) ServiceMap.get(key);
				service.beforeUnload();
				service.unload();
				service.afterUnload();
			}
			catch(Exception e)
			{
				AlbianLoggerService.error(e, "unload the service is error.");
				e.printStackTrace();
			}
		}
	}

}
