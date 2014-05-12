package org.albianj.persistence.impl.context;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.albianj.persistence.impl.db.ICommand;
import org.albianj.persistence.object.IStorageAttribute;

public class WriterTask implements IWriterTask
{
	private IStorageAttribute storage = null;
	private List<ICommand> commands = null;
	private Connection connection = null;
	private List<Statement> statements = null;
	
	public IStorageAttribute getStorage()
	{
		// TODO Auto-generated method stub
		return this.storage;
	}

	public void setStorage(IStorageAttribute storage)
	{
		// TODO Auto-generated method stub
		this.storage = storage;
	}

	public List<ICommand> getCommands()
	{
		// TODO Auto-generated method stub
		return this.commands;
	}

	public void setCommands(List<ICommand> commands)
	{
		// TODO Auto-generated method stub
		this.commands = commands;
	}

	public Connection getConnection()
	{
		// TODO Auto-generated method stub
		return this.connection;
	}

	public void setConnection(Connection connection)
	{
		// TODO Auto-generated method stub
		this.connection = connection;
	}

	public List<Statement> getStatements()
	{
		// TODO Auto-generated method stub
		return this.statements;
	}

	public void setStatements(List<Statement> statements)
	{
		// TODO Auto-generated method stub
		this.statements = statements;
	}

}
