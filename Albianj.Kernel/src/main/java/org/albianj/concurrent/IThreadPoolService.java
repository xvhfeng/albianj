package org.albianj.concurrent;

import org.albianj.service.IAlbianService;

public interface IThreadPoolService extends IAlbianService
{
	public void execute(Runnable event);
}
