package org.albianj.persistence.impl.db;

import java.beans.PropertyDescriptor;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.impl.cached.BeanPropertyDescriptorMap;
import org.albianj.persistence.impl.context.IReaderJob;
import org.albianj.persistence.impl.storage.StorageService;
import org.albianj.persistence.object.IAlbianObject;
import org.albianj.persistence.object.IStorageAttribute;
import org.albianj.verify.Validate;

public class QueryScope extends FreeQueryScope implements IQueryScope
{
	protected void perExecute(IReaderJob job) throws SQLException
	{
		NameSqlParameter.parseSql(job.getCommand());
		IStorageAttribute storage = job.getStorage();
		job.setConnection(StorageService.getConnection(storage.getName()));
		ICommand cmd = job.getCommand();
		PreparedStatement statement = job.getConnection().prepareStatement(cmd.getCommandText());
		Map<Integer,String>  map = cmd.getParameterMapper();
		if(!Validate.isNullOrEmpty(map))
		{
			for(int i = 1; i<= map.size(); i++)
			{
				String paraName = map.get(i);
				ISqlParameter para = cmd.getParameters().get(paraName);
				if(null == para.getValue())
				{
					statement.setNull(i, para.getSqlType());
				}
				else
				{
					statement.setObject(i, para.getValue(), para.getSqlType());
				}
			}
		}
		job.setStatement(statement);
		return;
	}
	
	protected void executing(IReaderJob job) throws SQLException
	{
		ResultSet result = ((PreparedStatement) job.getStatement()).executeQuery();
		job.setResult(result);
	}
	
	protected <T extends IAlbianObject> List<T> executed(Class<T> cls,IReaderJob job) throws SQLException
	{
		return executed(cls,job.getResult());
	}
	
	protected  void unloadExecute(IReaderJob job) throws SQLException
	{ 
		try
		{
			job.getResult().close();
			job.setResult(null);
		}
		finally
		{
			try
			{
				((PreparedStatement) job.getStatement()).clearParameters();
				job.getStatement().close();
				job.setStatement(null);
			}
			finally
			{
				job.getConnection().close();
			}
		}
	}

	
	protected ResultSet executing(CommandType cmdType,Statement statement) throws SQLException
	{
		if(CommandType.Text == cmdType)
		{
			return ((PreparedStatement) statement).executeQuery();
		}
		return ((CallableStatement) statement).executeQuery();
	}
	protected <T extends IAlbianObject> List<T> executed(Class<T> cls,ResultSet result) throws SQLException
	{
		String className = cls.getName();
		 PropertyDescriptor[] propertyDesc = (PropertyDescriptor[]) BeanPropertyDescriptorMap.get(className);
		 List<T> list = new Vector<T>();
		 while(result.next())
		 {
			 try
			{
				T obj = (T) cls.newInstance();
				for(PropertyDescriptor desc : propertyDesc)
				{
					String name = desc.getName();
					if(name.equals("isAlbianNew")) 
					{
					desc.getWriteMethod().invoke(obj, false);
						
						continue;
					}
					Object v = result.getObject(name);
					desc.getWriteMethod().invoke(obj, v);
					obj.setOldAlbianObject(desc.getName(), v);
				}
				list.add(obj);
			}
			catch (Exception e)
			{
					AlbianLoggerService.error(e,"create the albian object for list is error.");
				throw new RuntimeException(e);
			}
		 }
		 return list;
	}
}