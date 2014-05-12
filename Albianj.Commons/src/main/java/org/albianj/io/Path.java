package org.albianj.io;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public final class Path
{

	public static ClassLoader getClassLoader()
	{

		return Path.class.getClassLoader();
	}

	public static ClassLoader getClassLoader(
			@SuppressWarnings("rawtypes") Class cla)
	{

		return cla.getClassLoader();// Path.class.getClassLoader();
	}

	public static String getAbsolutePathOfClassLoaderClassPath()
	{
		return getClassLoader().getResource("").toString();
	}

	public static String getAbsolutePathOfClassLoaderClassPath(
			@SuppressWarnings("rawtypes") Class cla)
	{
		return getClassLoader(cla).getResource("").toString();
	}

	public static String getExtendResourcePath(String relativePath) throws MalformedURLException, URISyntaxException
	{
		return getExtendResourcePath(Path.class, relativePath);
	}

	public static String getExtendResourcePath(
			@SuppressWarnings("rawtypes") Class cla, String relativePath)
			throws MalformedURLException, URISyntaxException
	{
		URL resourceAbsoluteURL = null;
		boolean isWindows = false;
		String system = System.getProperty("os.name");
		if (system.toLowerCase().contains("windows"))// start with '/'
		{
			isWindows = true;
		}
		String path = null;
		if (!relativePath.contains("../"))
		{
			resourceAbsoluteURL = getResource(cla, relativePath);
		}
		else
		{
			String classPathAbsolutePath = getAbsolutePathOfClassLoaderClassPath(cla);
			if (relativePath.substring(0, 1).equals("/"))
			{
				relativePath = relativePath.substring(1);
			}
			String wildcardString = relativePath.substring(0,
					relativePath.lastIndexOf("../") + 3);
			relativePath = relativePath.substring(relativePath
					.lastIndexOf("../") + 3);
			int containSum = containSum(wildcardString, "../");
			classPathAbsolutePath = cutLastString(classPathAbsolutePath, "/",
					containSum);
			String resourceAbsolutePath = classPathAbsolutePath + relativePath;
			resourceAbsoluteURL = new URL(resourceAbsolutePath);
			path = resourceAbsoluteURL.toURI().getPath();
		}

		if (isWindows && path.startsWith("/"))
		{
			path = path.substring(1);
		}
		return path;
	}

	private static int containSum(String source, String dest)
	{
		int containSum = 0;
		int destLength = dest.length();
		while (source.contains(dest))
		{
			containSum = containSum + 1;
			source = source.substring(destLength);

		}
		return containSum;
	}

	private static String cutLastString(String source, String dest, int num)
	{
		for (int i = 0; i < num; i++)
		{
			source = source.substring(0,
					source.lastIndexOf(dest, source.length() - 2) + 1);

		}
		return source;
	}

	public static URL getResource(String resource)
	{
		return getClassLoader().getResource(resource);
	}

	public static URL getResource(@SuppressWarnings("rawtypes") Class cla,
			String resource)
	{
		return getClassLoader(cla).getResource(resource);
	}
}
