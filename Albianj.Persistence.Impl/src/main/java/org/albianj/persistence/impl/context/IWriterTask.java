package org.albianj.persistence.impl.context;

import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.albianj.persistence.impl.db.ICommand;
import org.albianj.persistence.object.IStorageAttribute;

public interface IWriterTask
{
	public IStorageAttribute getStorage();
	public void setStorage(IStorageAttribute storage);
	public List<ICommand> getCommands();
	public void setCommands(List<ICommand> commands);
	public Connection getConnection();
	public void setConnection(Connection connection);
	public List<Statement> getStatements();
	public void setStatements(List<Statement> statements);
}
