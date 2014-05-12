package org.albianj.service;

import org.albianj.kernel.Kernel;

/**
 * ���������������������service������������������������������������������������������������������
 * @author Seapeak
 *
 */
@Kernel
public abstract class FreeAlbianService implements IAlbianService
{

	private AlbianServiceLifetime state = AlbianServiceLifetime.Normal;

	public AlbianServiceLifetime getAlbianServiceState()
	{
		// TODO Auto-generated method stub
		return this.state;
	}

	public void beforeLoad() throws AlbianServiceException
	{
		// TODO Auto-generated method stub
		this.state = AlbianServiceLifetime.BeforeLoading;
	}

	public void loading() throws AlbianServiceException
	{
		// TODO Auto-generated method stub
		this.state = AlbianServiceLifetime.Loading;
	}

	public void afterLoading() throws AlbianServiceException
	{
		// TODO Auto-generated method stub
		this.state = AlbianServiceLifetime.Running;
	}

	public void beforeUnload() throws AlbianServiceException
	{
		// TODO Auto-generated method stub
		this.state = AlbianServiceLifetime.BeforeUnloading;

	}

	public void unload() throws AlbianServiceException
	{
		// TODO Auto-generated method stub
		this.state = AlbianServiceLifetime.Unloading;
	}

	public void afterUnload() throws AlbianServiceException
	{
		// TODO Auto-generated method stub
		this.state = AlbianServiceLifetime.Unloaded;
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

}
