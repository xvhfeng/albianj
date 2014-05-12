package org.albianj.persistence.impl.context;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

import org.albianj.persistence.impl.db.CreateCommandAdapter;
import org.albianj.persistence.impl.db.IUpdateCommand;
import org.albianj.persistence.impl.db.ModifyCommandAdapter;
import org.albianj.persistence.impl.db.RemoveCommandAdapter;
import org.albianj.persistence.object.IAlbianObject;
import org.albianj.persistence.object.IAlbianObjectAttribute;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.persistence.object.IRoutingsAttribute;

public abstract class FreeWriterJobAdapter implements IWriterJobAdapter
{
	public IWriterJob buildCreation(IAlbianObject object)
	{
		IWriterJob job = new WriterJob();
		IUpdateCommand cca = new CreateCommandAdapter();
		buildWriterJob(object,job,cca);
		return job;
	}
	
	public IWriterJob buildCreation(List<IAlbianObject> objects)
	{
		IWriterJob job = new WriterJob();
		IUpdateCommand cca = new CreateCommandAdapter();
		for(IAlbianObject object : objects)
		{
			buildWriterJob(object,job,cca);
		}
		return job;
	}
	
	public IWriterJob buildModification(IAlbianObject object)
	{
		IWriterJob job = new WriterJob();
		IUpdateCommand mca = new ModifyCommandAdapter();
		buildWriterJob(object,job,mca);
		return job;
	}
	
	public IWriterJob buildModification(List<IAlbianObject> objects)
	{
		IWriterJob job = new WriterJob();
		IUpdateCommand mca = new ModifyCommandAdapter();
		for(IAlbianObject object : objects)
		{
			buildWriterJob(object,job,mca);
		}
		return job;
	}
	
	public IWriterJob buildRemoved(IAlbianObject object)
	{
		IWriterJob job = new WriterJob();
		IUpdateCommand rca = new RemoveCommandAdapter();
		buildWriterJob(object,job,rca);
		return job;
	}
	
	public IWriterJob buildRemoved(List<IAlbianObject> objects)
	{
		IWriterJob job = new WriterJob();
		IUpdateCommand rca = new RemoveCommandAdapter();
		for(IAlbianObject object : objects)
		{
			buildWriterJob(object,job,rca);
		}
		return job;
	}
	
	public IWriterJob buildSaving(IAlbianObject object)
	{
		IWriterJob job = new WriterJob();
		IUpdateCommand iuc;
		if(object.getIsAlbianNew())
		{
			iuc= new CreateCommandAdapter();
		}
		else
		{
			iuc= new ModifyCommandAdapter();
		}
		 
		buildWriterJob(object,job,iuc);
		return job;
	}
	
	public IWriterJob buildSaving(List<IAlbianObject> objects)
	{
		IWriterJob job = new WriterJob();
		IUpdateCommand cca = new CreateCommandAdapter();
		IUpdateCommand mca = new ModifyCommandAdapter();
		for(IAlbianObject object : objects)
		{
			if(object.getIsAlbianNew())
			{
				buildWriterJob(object,job,cca);
			}
			else
			{
				buildWriterJob(object,job,mca);
			}
		}
		return job;
	}
	
	
	protected abstract void buildWriterJob(IAlbianObject object,IWriterJob writerJob,IUpdateCommand update);

	protected abstract Map<String, Object> buildSqlParameter(IAlbianObject object,
			IAlbianObjectAttribute albianObject,
			PropertyDescriptor[] propertyDesc);
	
	protected abstract List<IRoutingAttribute> parserRoutings(IAlbianObject object,
			IRoutingsAttribute routings, IAlbianObjectAttribute albianObject);
}
