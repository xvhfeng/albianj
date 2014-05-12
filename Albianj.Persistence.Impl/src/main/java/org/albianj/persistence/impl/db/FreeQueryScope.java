package org.albianj.persistence.impl.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.albianj.persistence.impl.context.IReaderJob;
import org.albianj.persistence.object.IAlbianObject;

public abstract class FreeQueryScope implements IQueryScope
{

	public <T extends IAlbianObject> List<T> execute(Class<T> cls,IReaderJob job) throws SQLException
	{
		try
		{
			perExecute(job);		
			executing(job);
			List<T> list = executed(cls, job);
			return list;
		}
		finally
		{
			unloadExecute(job);
		}
	}

	public <T extends IAlbianObject> List<T> execute(Class<T> cls,CommandType cmdType,Statement statement) throws SQLException
	{
		ResultSet result = null;
		List<T> list = null;
		try
		{
			result = executing(cmdType,statement);
			list = executed(cls,result);
		}
		finally
		{
			if(null != result)
				result.close();
		}
		return list;
	}
	protected abstract void perExecute(IReaderJob job) throws SQLException;
	protected abstract void executing(IReaderJob job) throws SQLException;
	protected abstract <T extends IAlbianObject> List<T> executed(Class<T> cls,IReaderJob job) throws SQLException;
	protected abstract void unloadExecute(IReaderJob job) throws SQLException;
	
	protected abstract ResultSet executing(CommandType cmdType,Statement statement) throws SQLException;
	protected abstract <T extends IAlbianObject> List<T> executed(Class<T> cls,ResultSet result) throws SQLException;
}
