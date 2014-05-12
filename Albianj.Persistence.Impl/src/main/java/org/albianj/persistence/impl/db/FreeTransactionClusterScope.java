package org.albianj.persistence.impl.db;

import java.sql.SQLException;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.impl.context.ICompensateCallback;
import org.albianj.persistence.impl.context.IWriterJob;
import org.albianj.persistence.impl.context.WriterJobLifeTime;

public abstract class FreeTransactionClusterScope implements ITransactionClusterScope
{

	public boolean execute(IWriterJob writerJob)
	{
		boolean isSuccess = true;
		try
		{
			writerJob.setWriterJobLifeTime(WriterJobLifeTime.NoStarted);
			this.preExecute(writerJob);
			writerJob.setWriterJobLifeTime(WriterJobLifeTime.Opened);
			this.executeHandler(writerJob);
			writerJob.setWriterJobLifeTime(WriterJobLifeTime.Runned);
			this.executed(writerJob);
			writerJob.setWriterJobLifeTime(WriterJobLifeTime.Commited);
		}
		catch(Exception e)
		{
			isSuccess = false;
				AlbianLoggerService.error(e,"Execute the query is error.");
			if(null != writerJob.getNotifyCallback())
			{
				try
				{
					StringBuilder sbMsg = new StringBuilder();
					sbMsg.append("Execute job is error.Job lifetime is:")
					.append(writerJob.getWriterJobLifeTime()).append(",exception msg:")
					.append(e.getMessage()).append(",Current task:")
					.append(writerJob.getCurrentStorage());
					writerJob.getNotifyCallback().notice(sbMsg.toString());
				}
				catch(Exception exc)
				{
						AlbianLoggerService.error(e,"the job error notice is error.");
				}
			}
			
			try
			{
				writerJob.setWriterJobLifeTime(WriterJobLifeTime.Rollbacking);
				this.exceptionHandler(writerJob);
				writerJob.setWriterJobLifeTime(WriterJobLifeTime.Rollbacked);
			}
			catch(Exception exc)
			{
				AlbianLoggerService.error(exc,"rollback the query is error.");
			}
		}
		finally
		{
			try
			{
				ICompensateCallback callback = writerJob.getCompensateCallback();
				if(null != callback)
				{
					callback.Compensate(writerJob.getCompensateCallbackObject());
				}
			}
			catch(Exception e)
			{
					AlbianLoggerService.error(e,"execute the compensate callback is error.");
			}
			finally
			{
				try
				{
					unLoadExecute(writerJob);
				}
				catch(Exception exc)
				{
						AlbianLoggerService.error(exc,"unload the job is error.");
				}
			}
			writerJob.setCurrentStorage(null);
		}
		
		return isSuccess;		
	}

	protected abstract void preExecute(IWriterJob writerJob) throws SQLException;
	protected abstract void executeHandler(IWriterJob writerJob) throws SQLException;
	protected abstract void executed(IWriterJob writerJob) throws SQLException;
	protected abstract void exceptionHandler(IWriterJob writerJob) throws SQLException;
	protected abstract void unLoadExecute(IWriterJob writerJob) throws SQLException;
}
