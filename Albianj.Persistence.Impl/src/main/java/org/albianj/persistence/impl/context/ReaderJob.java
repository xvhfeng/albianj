package org.albianj.persistence.impl.context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.albianj.persistence.impl.db.ICommand;
import org.albianj.persistence.object.IStorageAttribute;

public class ReaderJob implements IReaderJob
{
	private IStorageAttribute storage = null;
	private ICommand command = null;
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet result = null;
	public IStorageAttribute getStorage()
	{
		return storage;
	}
	public void setStorage(IStorageAttribute storage)
	{
		this.storage = storage;
	}
	public ICommand getCommand()
	{
		return command;
	}
	public void setCommand(ICommand command)
	{
		this.command = command;
	}
	public Connection getConnection()
	{
		return connection;
	}
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
	public Statement getStatement()
	{
		return statement;
	}
	public void setStatement(Statement statement)
	{
		this.statement = statement;
	}
	
	public ResultSet getResult()
	{
		return this.result;
	}
	public void setResult(ResultSet result)
	{
		this.result = result;
	}
}
