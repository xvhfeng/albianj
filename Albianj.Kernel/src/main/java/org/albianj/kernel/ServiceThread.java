package org.albianj.kernel;

import org.albianj.logger.AlbianLoggerService;

public class ServiceThread extends Thread
{
	@Override
	public void run()
	{
		try
		{
			AlbianBootService.doStart();
		}
		catch (Exception e)
		{
				AlbianLoggerService.error(e,"start service is fail.");
				e.printStackTrace();
				
		}
	}
}
