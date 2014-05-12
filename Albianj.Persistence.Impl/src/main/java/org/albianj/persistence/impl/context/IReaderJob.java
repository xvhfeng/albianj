package org.albianj.persistence.impl.context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.albianj.persistence.impl.db.ICommand;
import org.albianj.persistence.object.IStorageAttribute;

public interface IReaderJob
{
	public IStorageAttribute getStorage();
	public void setStorage(IStorageAttribute storage);
	public ICommand getCommand();
	public void setCommand(ICommand command);
	public Connection getConnection();
	public void setConnection(Connection connection);
	public Statement getStatement();
	public void setStatement(Statement statement);
	public ResultSet getResult();
	public void setResult(ResultSet result);
}
