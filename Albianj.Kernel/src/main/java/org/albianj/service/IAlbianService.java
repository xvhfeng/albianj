package org.albianj.service;

import org.albianj.kernel.Kernel;

/**
 * 所有albianj的service的接口，推荐直接继承FreeAlbianService而不是实现这个接口
 * @author Seapeak
 *
 */
@Kernel
public interface IAlbianService
{

	public AlbianServiceLifetime getAlbianServiceState();

	public void beforeLoad() throws RuntimeException;

	public void loading() throws RuntimeException;

	public void afterLoading() throws RuntimeException;

	public void beforeUnload() throws RuntimeException;

	public void unload() throws RuntimeException;

	public void afterUnload() throws RuntimeException;
}
