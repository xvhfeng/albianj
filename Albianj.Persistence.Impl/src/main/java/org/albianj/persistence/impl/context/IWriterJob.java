package org.albianj.persistence.impl.context;

import java.util.Map;

public interface IWriterJob
{
	public Map<String,IWriterTask> getWriterTasks();
	public void setWriterTasks(Map<String,IWriterTask> writerTasks);
	public WriterJobLifeTime getWriterJobLifeTime();
	public void setWriterJobLifeTime(WriterJobLifeTime writerJobLifeTime);
	public String getCurrentStorage();
	public void setCurrentStorage(String currentStorage);
	public INotify getNotifyCallback();
	public void setNotifyCallback(INotify notifyCallback);
	public ICompensateCallback getCompensateCallback();
	public void setCompensateCallback(ICompensateCallback compensateCallback);
	public void setCompensateCallbackObject(Object compensateCallbackObject);
	public Object getCompensateCallbackObject();
}
