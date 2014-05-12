package org.albianj.persistence.impl.context;

import java.util.List;

import org.albianj.persistence.object.IAlbianObject;

public interface IWriterJobAdapter
{
	public IWriterJob buildCreation(IAlbianObject object);
	
	public IWriterJob buildCreation(List<IAlbianObject> objects);
	
	public IWriterJob buildModification(IAlbianObject object);
	
	public IWriterJob buildModification(List<IAlbianObject> objects);
	public IWriterJob buildRemoved(IAlbianObject object);
	
	public IWriterJob buildRemoved(List<IAlbianObject> objects);
	
	public IWriterJob buildSaving(IAlbianObject object);
	
	public IWriterJob buildSaving(List<IAlbianObject> objects);

}
