package org.albianj.persistence.impl.service;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.albianj.persistence.impl.cached.AlbianObjectsMap;
import org.albianj.persistence.impl.context.ICompensateCallback;
import org.albianj.persistence.impl.context.INotify;
import org.albianj.persistence.impl.context.IReaderJob;
import org.albianj.persistence.impl.context.IReaderJobAdapter;
import org.albianj.persistence.impl.context.IWriterJob;
import org.albianj.persistence.impl.context.IWriterJobAdapter;
import org.albianj.persistence.impl.context.ReaderJobAdapter;
import org.albianj.persistence.impl.context.WriterJobAdapter;
import org.albianj.persistence.impl.db.CommandType;
import org.albianj.persistence.impl.db.IQueryScope;
import org.albianj.persistence.impl.db.ITransactionClusterScope;
import org.albianj.persistence.impl.db.QueryScope;
import org.albianj.persistence.impl.db.TransactionClusterScope;
import org.albianj.persistence.impl.dbcached.CacheOperator;
import org.albianj.persistence.object.IAlbianObject;
import org.albianj.persistence.object.IAlbianObjectAttribute;
import org.albianj.persistence.object.IFilterCondition;
import org.albianj.persistence.object.IOrderByCondition;
import org.albianj.persistence.object.LogicalOperation;
import org.albianj.persistence.object.RelationalOperator;
import org.albianj.verify.Validate;

/**
 * 数据库持久化类
 * 所有的数据操作都使用此类
 * 此类必须和persistence.xml,routing.xml,storage.xml配合使用
 * 使用此类的数据对象必须实现IAlbianObject或者继承FreeAlbianObject
 * 使用此类的数据对象必须在数据库中存在单主键ID（或者自定义名称，需在persistence。xml注明），
 * 		类型为char，长度为32，使用AlbianIdService创建。
 * 
 * Remark:
 * 		此类其实一共有6个方法。其中各个方法的总体情况如下：
 * 		create：往数据库中插入对象数据
 * 		modify：修改数据库中的数据对象
 * 		remove：删除数据库中的数据对象
 * 		save：  当数据库中存在对象事更改对象，不存在时插入对象
 * 		find**：首先从缓存中查找对象，没有才去数据库中load对象
 * 		load**：直接从数据库中load对象
 * 
 * 		所有参数为List<IAlbianObect>的操作方法（查询不行）其实都可以执行不同类型的数据对象
 * 			比如一个User对象，一个LogInfo对象，可以合并在一个list中进行保存操作
 */
public class AlbianPersistenceService
{
	/**
	 * 保存数据对象到数据库
	 * object: 需要保存的数据对象
	 * return： 是否保存成功
	 */
	public static boolean create(IAlbianObject object)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildCreation(object);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * 
	 */
	public static boolean create(IAlbianObject object,INotify notifyCallback)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildCreation(object);
		job.setNotifyCallback(notifyCallback);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 */
	public static boolean create(IAlbianObject object,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildCreation(object);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 往数据库中新增对象数据
	 * @param 需要增加的数据对象
	 * @param 如果启用缓存，此参数是缓存更新接口
	 * @param 如果有事务操作，这是事务补偿机制
	 * @param 补偿操作的对象
	 * @return 新增数据是否成功
	 */
	public static boolean create(IAlbianObject object,INotify notifyCallback,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildCreation(object);
		job.setNotifyCallback(notifyCallback);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}

	/**
	 * S
	 * @param objects
	 * @return
	 */
	public static boolean create(List<IAlbianObject> objects)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildCreation(objects);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param objects
	 * @param notifyCallback
	 * @return
	 */
	public static boolean create(List<IAlbianObject> objects,INotify notifyCallback)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildCreation(objects);
		job.setNotifyCallback(notifyCallback);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param objects
	 * @param compensateCallback
	 * @param compensateCallbackObject
	 * @return
	 */
	public static boolean create(List<IAlbianObject> objects,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildCreation(objects);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 往数据库中新增一批数据对象，数据对象可以是不同类型的对象，只要都实现IAlianObject就可以
	 * @param 新增的一批数据
	 * @param 如果启用缓存，此参数是缓存更新接口
	 * @param 如果有事务操作，这是事务补偿机制
	 * @param 补偿操作的对象
	 * @return 新增数据是否成功
	 */
	public static boolean create(List<IAlbianObject> objects,INotify notifyCallback,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildCreation(objects);
		job.setNotifyCallback(notifyCallback);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	

	/**
	 * 
	 * @param object
	 * @return
	 */
	public static boolean modify(IAlbianObject object)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildModification(object);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param object
	 * @param notifyCallback
	 * @return
	 */
	public static boolean modify(IAlbianObject object,INotify notifyCallback)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildModification(object);
		job.setNotifyCallback(notifyCallback);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param object
	 * @param compensateCallback
	 * @param compensateCallbackObject
	 * @return
	 */
	public static boolean modify(IAlbianObject object,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildModification(object);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 更新数据库中的对象数据
	 * @param 需要更新的数据对象
	 * @param 如果启用缓存，此参数是缓存更新接口
	 * @param 如果有事务操作，这是事务补偿机制
	 * @param 补偿操作的对象
	 * @return 更新数据是否成功
	 */
	public static boolean modify(IAlbianObject object,INotify notifyCallback,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildModification(object);
		job.setNotifyCallback(notifyCallback);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}

	/**
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean modify(List<IAlbianObject> objects)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildModification(objects);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param objects
	 * @param notifyCallback
	 * @return
	 */
	public static boolean modify(List<IAlbianObject> objects,INotify notifyCallback)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildModification(objects);
		job.setNotifyCallback(notifyCallback);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param objects
	 * @param compensateCallback
	 * @param compensateCallbackObject
	 * @return
	 */
	public static boolean modify(List<IAlbianObject> objects,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildModification(objects);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 更新数据库中的一批数据对象，数据对象可以是不同类型的对象，只要都实现IAlianObject就可以
	 * @param 更新的一批数据
	 * @param 如果启用缓存，此参数是缓存更新接口
	 * @param 如果有事务操作，这是事务补偿机制
	 * @param 补偿操作的对象
	 * @return 更新数据是否成功
	 */
	public static boolean modify(List<IAlbianObject> objects,INotify notifyCallback,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildModification(objects);
		job.setNotifyCallback(notifyCallback);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public static boolean remove(IAlbianObject object)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildRemoved(object);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param object
	 * @param notifyCallback
	 * @return
	 */
	public static boolean remove(IAlbianObject object,INotify notifyCallback)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildRemoved(object);
		job.setNotifyCallback(notifyCallback);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param object
	 * @param compensateCallback
	 * @param compensateCallbackObject
	 * @return
	 */
	public static boolean remove(IAlbianObject object,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildRemoved(object);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 删除数据库中的某个数据对象
	 * @param 需要删除的数据对象
	 * @param 如果启用缓存，此参数是缓存更新接口
	 * @param 如果有事务操作，这是事务补偿机制
	 * @param 补偿操作的对象
	 * @return 删除数据是否成功
	 */
	public static boolean remove(IAlbianObject object,INotify notifyCallback,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildRemoved(object);
		job.setNotifyCallback(notifyCallback);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean remove(List<IAlbianObject> objects)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildRemoved(objects);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param objects
	 * @param notifyCallback
	 * @return
	 */
	public static boolean remove(List<IAlbianObject> objects,INotify notifyCallback)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildRemoved(objects);
		job.setNotifyCallback(notifyCallback);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param objects
	 * @param compensateCallback
	 * @param compensateCallbackObject
	 * @return
	 */
	public static boolean remove(List<IAlbianObject> objects,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildRemoved(objects);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 删除数据库中的一批数据对象，数据对象可以是不同类型的对象，只要都实现IAlianObject就可以
	 * @param 需要删除的一批数据
	 * @param 如果启用缓存，此参数是缓存更新接口
	 * @param 如果有事务操作，这是事务补偿机制
	 * @param 补偿操作的对象
	 * @return 删除数据是否成功
	 */
	public static boolean remove(List<IAlbianObject> objects,INotify notifyCallback,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildRemoved(objects);
		job.setNotifyCallback(notifyCallback);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	/**
	 * 
	 * @param object
	 * @return
	 */
	public static boolean save(IAlbianObject object)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildSaving(object);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param object
	 * @param notifyCallback
	 * @return
	 */
	public static boolean save(IAlbianObject object,INotify notifyCallback)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildSaving(object);
		job.setNotifyCallback(notifyCallback);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param object
	 * @param compensateCallback
	 * @param compensateCallbackObject
	 * @return
	 */
	public static boolean save(IAlbianObject object,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildSaving(object);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 往数据库中保存某个数据对象
	 * @param 需要保存的某数据对象
	 * @param 如果启用缓存，此参数是缓存更新接口
	 * @param 如果有事务操作，这是事务补偿机制
	 * @param 补偿操作的对象
	 * @return 保存数据是否成功
	 */
	public static boolean save(IAlbianObject object,INotify notifyCallback,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildSaving(object);
		job.setNotifyCallback(notifyCallback);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean save(List<IAlbianObject> objects)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildSaving(objects);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param objects
	 * @param notifyCallback
	 * @return
	 */
	public static boolean save(List<IAlbianObject> objects,INotify notifyCallback)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildSaving(objects);
		job.setNotifyCallback(notifyCallback);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 
	 * @param objects
	 * @param compensateCallback
	 * @param compensateCallbackObject
	 * @return
	 */
	public static boolean save(List<IAlbianObject> objects,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildSaving(objects);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	/**
	 * 往数据库中保存一批数据对象，数据对象可以是不同类型的对象，只要都实现IAlianObject就可以
	 * @param 需要保存的一批数据
	 * @param 如果启用缓存，此参数是缓存更新接口
	 * @param 如果有事务操作，这是事务补偿机制
	 * @param 补偿操作的对象
	 * @return 保存数据是否成功
	 */
	public static boolean save(List<IAlbianObject> objects,INotify notifyCallback,ICompensateCallback compensateCallback,Object compensateCallbackObject)
	{
		IWriterJobAdapter ja = new WriterJobAdapter();
		IWriterJob job = ja.buildSaving(objects);
		job.setNotifyCallback(notifyCallback);
		job.setCompensateCallback(compensateCallback);
		job.setCompensateCallbackObject(compensateCallbackObject);
		ITransactionClusterScope tcs = new TransactionClusterScope();
		return tcs.execute(job);
	}
	
	

	/**
	 * 
	 */
	public static <T extends IAlbianObject> T findObject(Class<T> cls,String routingName, IFilterCondition[] wheres)
	{
	    return doFindObject(cls,routingName, wheres);
	}
	
	/**
	 * 
	 * @param cls
	 * @param id
	 * @return
	 */
	public static <T extends IAlbianObject> T findObject(Class<T> cls,String id)
	{
		IFilterCondition[] wheres = new IFilterCondition[1];
		wheres[0].setFieldClass(String.class);
		wheres[0].setFieldName("id");
		wheres[0].setLogicalOperation(LogicalOperation.Equal);
		wheres[0].setRelationalOperator(RelationalOperator.And);
		wheres[0].setValue(id);
		
		return doFindObject(cls, null, wheres);
	}
	
	/**
	 * 
	 * @param cls
	 * @param routingName
	 * @param id
	 * @return
	 */
	public static  <T extends IAlbianObject> T findObject(Class<T> cls,String routingName, String id)
	{
		IFilterCondition[] wheres = new IFilterCondition[1];
		wheres[0].setFieldClass(String.class);
		wheres[0].setFieldName("id");
		wheres[0].setLogicalOperation(LogicalOperation.Equal);
		wheres[0].setRelationalOperator(RelationalOperator.And);
		wheres[0].setValue(id);
		
		return doFindObject(cls, routingName, wheres);
	}
	
	/**
	 * 
	 * @param cls
	 * @param wheres
	 * @return
	 */
	public static <T extends IAlbianObject> T findObject(Class<T> cls,IFilterCondition[] wheres)
	{
	    return doFindObject(cls, null, wheres);
	}
	
	/**
	 * 
	 * @param cls
	 * @param cacheKey
	 * @param cmdType
	 * @param statement
	 * @return
	 */
	public static <T extends IAlbianObject> T findObject(Class<T> cls,String cacheKey,CommandType cmdType,Statement statement)
	{
		return doFindObject(cls, cacheKey, cmdType, statement);
	}
	
	/**
	 * 
	 * @param cls
	 * @param start
	 * @param step
	 * @param wheres
	 * @param orderbys
	 * @return
	 */
	public static <T extends IAlbianObject> List<T> findObjects(Class<T> cls,int start,int step, IFilterCondition[] wheres, IOrderByCondition[] orderbys)
	{
		return doFindObjects(cls, null, start, step, wheres, orderbys);
	}
	
	/**
	 * 
	 * @param cls
	 * @param wheres
	 * @return
	 */
	public static <T extends IAlbianObject> List<T> findObjects(Class<T> cls,IFilterCondition[] wheres)
	{
	    return doFindObjects(cls, null, 0, 30, wheres, null);
	}
	
	
	public static <T extends IAlbianObject> List<T> findObjects(Class<T> cls, IFilterCondition[] wheres, IOrderByCondition[] orderbys)
	{
	    return doFindObjects(cls, null, 0, 30, wheres, orderbys);
	}
	public static  <T extends IAlbianObject> List<T> findObjects(Class<T> cls,IOrderByCondition[] orderbys)
	{
	    return doFindObjects(cls, null, 0, 30, null, orderbys);
	}
	
	/**
	 * 
	 * @param cls
	 * @param routingName
	 * @param wheres
	 * @param orderbys
	 * @return
	 */
	public static  <T extends IAlbianObject> List<T> findObjects(Class<T> cls,String routingName, IFilterCondition[] wheres, IOrderByCondition[] orderbys)
	{
	    return doFindObjects(cls, routingName, 0, 30, wheres, orderbys);
	}
	public static  <T extends IAlbianObject> List<T> findObjects(Class<T> cls,String routingName, IOrderByCondition[] orderbys)
	{
		return doFindObjects(cls, routingName, 0, 30, null, orderbys);
	}
	public static <T extends IAlbianObject> List<T> findObjects(Class<T> cls,String routingName, IFilterCondition[] wheres)
	{
		return doFindObjects(cls, routingName, 0, 30, wheres, null);
	}
	
	/**
	 * 先从缓存中加载数据，缓存中没有再调用load**从数据库中加载一批数据
	 * @param cls：加载数据的类型
	 * @param routingName：根据哪个routing加载数据,此name必须在routing.xml中配置
	 * @param start：数据分页开始的行数
	 * @param step：需要取的数据行数
	 * @param wheres 需要加载数据的条件
	 * @param orderbys 数据的排序规则
	 * @return
	 */
	public static  <T extends IAlbianObject> List<T> findObjects(Class<T> cls,String routingName, int start,int step, IFilterCondition[] wheres,
	                                      IOrderByCondition[] orderbys)
	{
		return doFindObjects(cls, routingName, start, step, wheres, orderbys);
	}
	public static  <T extends IAlbianObject> List<T> findObjects(Class<T> cls,String routingName, int start,int step, IFilterCondition[] wheres)
	{
		return doFindObjects(cls, routingName, start, step, wheres, null);
	}
	public static  <T extends IAlbianObject> List<T> findObjects(Class<T> cls,String routingName, int start,int step, IOrderByCondition[] orderbys)
	{
		return doFindObjects(cls, routingName, start, step, null, orderbys);
	}
	public static  <T extends IAlbianObject> List<T> findObjects(Class<T> cls,String cacheKey,CommandType cmdType,Statement statement)
	{
		return doFindObjects(cls, cacheKey, cmdType, statement);
	}
	
	public static  <T extends IAlbianObject> T loadObject(Class<T> cls,String routingName, IFilterCondition[] wheres)
	{
		return doLoadObject(cls, routingName, wheres);
	}
	public static  <T extends IAlbianObject> T loadObject(Class<T> cls,String id)
	{
		IFilterCondition[] wheres = new IFilterCondition[1];
		wheres[0].setFieldClass(String.class);
		wheres[0].setFieldName("id");
		wheres[0].setLogicalOperation(LogicalOperation.Equal);
		wheres[0].setRelationalOperator(RelationalOperator.And);
		wheres[0].setValue(id);
		
		return doLoadObject(cls, null, wheres);
	}
	public static  <T extends IAlbianObject> T loadObject(Class<T> cls,String routingName, String id)
	{
		IFilterCondition[] wheres = new IFilterCondition[1];
		wheres[0].setFieldClass(String.class);
		wheres[0].setFieldName("id");
		wheres[0].setLogicalOperation(LogicalOperation.Equal);
		wheres[0].setRelationalOperator(RelationalOperator.And);
		wheres[0].setValue(id);
		
		return doLoadObject(cls, routingName, wheres);
	}
	public static  <T extends IAlbianObject> T loadObject(Class<T> cls,IFilterCondition[] wheres)
	{
		return doLoadObject(cls, null, wheres);
	}
	public static  <T extends IAlbianObject> T loadObject(Class<T> cls,CommandType cmdType, Statement statement)
	{
		return doLoadObject(cls, cmdType, statement);
	}
	
	public static  <T extends IAlbianObject> List<T> loadObjects(Class<T> cls,int start,int step, IFilterCondition[] wheres, IOrderByCondition[] orderbys)
	{
		return doLoadObjects(cls, null, start, step, wheres, orderbys);
	}
	public static  <T extends IAlbianObject> List<T> loadObjects(Class<T> cls,IFilterCondition[] wheres)
	{
		return doLoadObjects(cls, null, 0, 30, wheres, null);
	}
	public static  <T extends IAlbianObject> List<T> loadObjects(Class<T> cls,IFilterCondition[] wheres, IOrderByCondition[] orderbys)
	{
		return doLoadObjects(cls, null, 0, 30, null, orderbys);
	}
	public static  <T extends IAlbianObject> List<T> loadObjects(Class<T> cls,IOrderByCondition[] orderbys)
	{
		return doLoadObjects(cls, null, 0, 30, null, orderbys);
	}
	public static  <T extends IAlbianObject> List<T> loadObjects(Class<T> cls,String routingName, IFilterCondition[] wheres, IOrderByCondition[] orderbys)
	{
		return doLoadObjects(cls, routingName, 0, 30, wheres, orderbys);
	}
	public static  <T extends IAlbianObject> List<T> loadObjects(Class<T> cls,String routingName, IOrderByCondition[] orderbys)
	{
		return doLoadObjects(cls, routingName, 0, 30, null, orderbys);
	}
	public static  <T extends IAlbianObject> List<T> loadObjects(Class<T> cls,String routingName, IFilterCondition[] wheres)
	{
		return doLoadObjects(cls, routingName, 0, 30, wheres, null);
	}
	/**
	 * 直接从数据库中加载一批数据
	 * @param cls：加载数据的类型
	 * @param routingName：根据哪个routing加载数据,此name必须在routing.xml中配置
	 * @param start：数据分页开始的行数
	 * @param step：需要取的数据行数
	 * @param wheres 需要加载数据的条件
	 * @param orderbys 数据的排序规则
	 * @return 数据库中新鲜的数据
	 */
	public static  <T extends IAlbianObject> List<T> loadObjects(Class<T> cls,String routingName, int start,int step,IFilterCondition[] wheres, IOrderByCondition[] orderbys)
	{
	return  doLoadObjects(cls, routingName, start, step, wheres, orderbys);
	}
	public static  <T extends IAlbianObject> List<T> loadObjects(Class<T> cls,String routingName, int start,int step,IFilterCondition[] wheres)
	{
		return doLoadObjects(cls, routingName, start, step, wheres, null);
	}
	public static  <T extends IAlbianObject> List<T> loadObjects(Class<T> cls,CommandType cmdType,Statement statement)
	{
		return doLoadObjects(cls, cmdType, statement);
	}
	
	@SuppressWarnings("unchecked")
	protected static  <T extends IAlbianObject> T doFindObject(Class<T> cls,String routingName, IFilterCondition[] wheres)
	{
		Object obj = CacheOperator.getObjectFromRemoterCache(cls,0,0, wheres, null);
		if(null != obj) return (T) obj;
		T newObj = doLoadObject(cls, routingName, wheres);
		if(null == newObj) return null;
		IAlbianObjectAttribute albianObject = (IAlbianObjectAttribute) AlbianObjectsMap
				.get(cls.getName());
		CacheOperator.storeObjectToRemoterCache(cls, 0,0,wheres, null, newObj, null == albianObject.getCache() ? 300 : albianObject.getCache().getLifeTime());
		return newObj;
	}
	
	@SuppressWarnings("unchecked")
	protected static  <T extends IAlbianObject> T doFindObject(Class<T> cls,String cacheKey,CommandType cmdType,Statement statement)
	{
		Object obj = CacheOperator.getObjectFromRemoterCache(cacheKey);
		if(null != obj) return (T) obj;
		T newObj = doLoadObject(cls, cmdType, statement);
		if(null == newObj) return null;
		IAlbianObjectAttribute albianObject = (IAlbianObjectAttribute) AlbianObjectsMap
				.get(cls.getName());
		CacheOperator.storeObjectToRemoterCache(cacheKey, newObj, null == albianObject.getCache() ? 300 : albianObject.getCache().getLifeTime());
		return newObj;
	}
	
	@SuppressWarnings("unchecked")
	protected static  <T extends IAlbianObject> List<T> doFindObjects(Class<T> cls,String routingName, int start,int step, IFilterCondition[] wheres,
	                                         IOrderByCondition[] orderbys)
	{
		Object obj = CacheOperator.getObjectFromLocalCache(cls, start,step,wheres, orderbys);
		if(null != obj) return (List<T>) obj;
		List<T> newObj = doLoadObjects(cls, routingName,start,step, wheres,orderbys);
		if(null == newObj) return null;
		IAlbianObjectAttribute albianObject = (IAlbianObjectAttribute) AlbianObjectsMap
				.get(cls.getName());
		CacheOperator.storeObjectToRemoterCache(cls, start,step,wheres, null, newObj, null == albianObject.getCache() ? 300 : albianObject.getCache().getLifeTime());
		return newObj;
	}
	@SuppressWarnings("unchecked")
	protected static  <T extends IAlbianObject> List<T> doFindObjects(Class<T> cls,String cacheKey,CommandType cmdType,Statement statement)
	{
		Object obj = CacheOperator.getObjectFromLocalCache(cacheKey);
		if(null != obj) return (List<T>) obj;
		List<T> newObj = doLoadObjects(cls, cmdType, statement);
		if(null == newObj) return null;
		IAlbianObjectAttribute albianObject = (IAlbianObjectAttribute) AlbianObjectsMap
				.get(cls.getName());
		CacheOperator.storeObjectToRemoterCache(cacheKey, newObj, null == albianObject.getCache() ? 300 : albianObject.getCache().getLifeTime());
		return newObj;
	}
	
	protected static  <T extends IAlbianObject> T doLoadObject(Class<T> cls,String routingName,  IFilterCondition[] wheres)
	{
		List<T> list = doLoadObjects(cls,routingName,0,0,wheres,null);
		if(Validate.isNullOrEmpty(list)) return null;
		return list.get(0);
	}
	protected static  <T extends IAlbianObject> T doLoadObject(Class<T> cls,CommandType cmdType,Statement statement)
	{
		List<T> list = doLoadObjects(cls,cmdType,statement);
		if(Validate.isNullOrEmpty(list)) return null;
		return list.get(0);
	}
	
	protected static  <T extends IAlbianObject> List<T> doLoadObjects(Class<T> cls,String routingName, int start,int step, IFilterCondition[] wheres, IOrderByCondition[] orderbys)
	{
		IReaderJobAdapter ad = new ReaderJobAdapter();
		IReaderJob job = ad.buildReaderJob(cls, routingName, start, step, wheres, orderbys);
		IQueryScope scope = new QueryScope();
		List<T> list = null;
		try
		{
			list = scope.execute(cls, job);
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		return list;
	}
	protected static  <T extends IAlbianObject> List<T> doLoadObjects(Class<T> cls,CommandType cmdType,Statement statement)
	{
		IQueryScope scope = new QueryScope();
		List<T> list = null;
		try
		{
			list = scope.execute(cls, cmdType,statement);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		return list;
	}
}
