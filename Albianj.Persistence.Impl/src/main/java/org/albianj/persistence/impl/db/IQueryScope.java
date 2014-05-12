package org.albianj.persistence.impl.db;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.albianj.persistence.impl.context.IReaderJob;
import org.albianj.persistence.object.IAlbianObject;

public interface IQueryScope
{
	public <T extends IAlbianObject> List<T> execute(Class<T> cls,IReaderJob job) throws SQLException;
	public <T extends IAlbianObject> List<T> execute(Class<T> cls, CommandType cmdType,Statement statement) throws SQLException;
}
