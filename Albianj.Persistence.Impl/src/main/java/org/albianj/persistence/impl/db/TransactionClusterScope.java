package org.albianj.persistence.impl.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.impl.context.IWriterJob;
import org.albianj.persistence.impl.context.IWriterTask;
import org.albianj.persistence.impl.context.WriterJobLifeTime;
import org.albianj.persistence.impl.storage.StorageService;
import org.albianj.persistence.object.IStorageAttribute;
import org.albianj.verify.Validate;

public class TransactionClusterScope extends FreeTransactionClusterScope
		implements ITransactionClusterScope
{
	protected void preExecute(IWriterJob writerJob) throws SQLException
	{
		writerJob.setWriterJobLifeTime(WriterJobLifeTime.Opening);
		Map<String, IWriterTask> tasks = writerJob.getWriterTasks();
		if (Validate.isNullOrEmpty(tasks)) { throw new RuntimeException(
				"The task for job is empty or null."); }

		for (Map.Entry<String, IWriterTask> task : tasks.entrySet())
		{
			writerJob.setCurrentStorage(task.getKey());
			IStorageAttribute storage = task.getValue().getStorage();
			if (null == storage) { throw new RuntimeException(
					"The storage for task is null."); }
			task.getValue().setConnection(
					StorageService.getConnection(storage.getName()));
			List<ICommand> cmds = task.getValue().getCommands();
			if (Validate.isNullOrEmpty(cmds)) { throw new RuntimeException(
					"The commands for task is empty or null."); }
			List<Statement> statements = new Vector<Statement>();
			for (ICommand cmd : cmds)
			{
				PreparedStatement prepareStatement = task.getValue()
						.getConnection().prepareStatement(cmd.getCommandText());
				Map<Integer, String> map = cmd.getParameterMapper();
				if (Validate.isNullOrEmpty(map))
				{
					continue;
				}
				else
				{
					for (int i = 1; i <= map.size(); i++)
					{
						String paraName = map.get(i);
						ISqlParameter para = cmd.getParameters().get(paraName);
						if (null == para.getValue())
						{
							prepareStatement.setNull(i, para.getSqlType());
						}
						else
						{
							prepareStatement.setObject(i, para.getValue(),
									para.getSqlType());
						}
					}
				}
				statements.add(prepareStatement);
			}
			task.getValue().setStatements(statements);
		}
	}

	protected void executeHandler(IWriterJob writerJob) throws SQLException
	{
		writerJob.setWriterJobLifeTime(WriterJobLifeTime.Running);
		Map<String, IWriterTask> tasks = writerJob.getWriterTasks();
		if (Validate.isNullOrEmpty(tasks)) { throw new RuntimeException(
				"The task is null or empty."); }
		for (Map.Entry<String, IWriterTask> task : tasks.entrySet())
		{
			writerJob.setCurrentStorage(task.getKey());
			List<Statement> statements = task.getValue().getStatements();
			for (Statement statement : statements)
			{
				((PreparedStatement) statement).executeUpdate();
			}
		}
	}

	protected void executed(IWriterJob writerJob) throws SQLException
	{
		writerJob.setWriterJobLifeTime(WriterJobLifeTime.Commiting);
		Map<String, IWriterTask> tasks = writerJob.getWriterTasks();
		if (Validate.isNullOrEmpty(tasks)) { throw new RuntimeException(
				"The task is null or empty."); }
		for (Map.Entry<String, IWriterTask> task : tasks.entrySet())
		{
			writerJob.setCurrentStorage(task.getKey());
			task.getValue().getConnection().commit();
		}
	}

	protected void exceptionHandler(IWriterJob writerJob) throws SQLException
	{
		boolean isThrow = false;
		Map<String, IWriterTask> tasks = writerJob.getWriterTasks();
		if (Validate.isNullOrEmpty(tasks)) { throw new RuntimeException(
				"The task is null or empty."); }
		for (Map.Entry<String, IWriterTask> task : tasks.entrySet())
		{
			try
			{
				task.getValue().getConnection().rollback();
			}
			catch (Exception e)
			{
				AlbianLoggerService.error(e,
						"rollback the trancation is error.");
				isThrow = true;
			}
		}
		if (isThrow) throw new SQLException("rollback the trancation is error");
	}

	protected void unLoadExecute(IWriterJob writerJob) throws SQLException
	{
		boolean isThrow = false;
		Map<String, IWriterTask> tasks = writerJob.getWriterTasks();
		if (Validate.isNullOrEmpty(tasks)) { throw new RuntimeException(
				"The task is null or empty."); }
		for (Map.Entry<String, IWriterTask> task : tasks.entrySet())
		{
			try
			{
				List<Statement> statements = task.getValue().getStatements();
				for (Statement statement : statements)
				{
					try
					{
						((PreparedStatement) statement).clearParameters();
						statement.close();
					}
					catch (Exception e)
					{
						isThrow = true;
						AlbianLoggerService.error(e,
								"clear the statement is error.");
					}
				}
				task.getValue().getConnection().close();
			}
			catch (Exception exc)
			{
				isThrow = true;
				AlbianLoggerService
						.error(exc, "close the connection is error.");
			}
		}
		if (isThrow) throw new SQLException(
				"there is error in the unload trancation scope.");
	}

}
