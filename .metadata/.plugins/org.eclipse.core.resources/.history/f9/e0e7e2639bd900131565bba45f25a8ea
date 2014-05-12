package org.albianj.persistence.impl.context;

import java.util.Map;


public class WriterJob implements IWriterJob
{
	private Map<String,IWriterTask> writerTasks = null;
	private WriterJobLifeTime writerJobLifeTime = WriterJobLifeTime.Normal;
	private ICompensateCallback compensateCallback = null;
	private Object compensateCallbackObject = null;
	private INotify notifyCallback = null;
	private String currentStorage = null;
	
	@Override
	public Map<String,IWriterTask> getWriterTasks()
	{
		// TODO Auto-generated method stub
		return this.writerTasks;
	}

	@Override
	public void setWriterTasks(Map<String,IWriterTask> writerTasks)
	{
		// TODO Auto-generated method stub
		this.writerTasks = writerTasks;
	}

	@Override
	public WriterJobLifeTime getWriterJobLifeTime()
	{
		// TODO Auto-generated method stub
		return this.writerJobLifeTime;
	}

	@Override
	public void setWriterJobLifeTime(WriterJobLifeTime writerJobLifeTime)
	{
		// TODO Auto-generated method stub
		this.writerJobLifeTime = writerJobLifeTime;
	}

	@Override
	public ICompensateCallback getCompensateCallback()
	{
		// TODO Auto-generated method stub
		return this.compensateCallback;
	}

	@Override
	public void setCompensateCallback(ICompensateCallback compensateCallback)
	{
		// TODO Auto-generated method stub
		this.compensateCallback = compensateCallback;
	}

	public void setCompensateCallbackObject(Object compensateCallbackObject)
	{
		this.compensateCallbackObject = compensateCallbackObject;
	}
	public Object getCompensateCallbackObject()
	{
		return this.compensateCallbackObject;
	}
	
	public INotify getNotifyCallback()
	{
		return this.notifyCallback;
	}
	
	public void setNotifyCallback(INotify notifyCallback)
	{
		this.notifyCallback = notifyCallback;
	}
	public String getCurrentStorage()
	{
		return this.currentStorage;
	}
	public void setCurrentStorage(String currentStorage)
	{
		this.currentStorage = currentStorage;
	}
}
