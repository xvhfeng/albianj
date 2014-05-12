package org.albianj.persistence.impl.context;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.impl.cached.AlbianObjectsMap;
import org.albianj.persistence.impl.cached.BeanPropertyDescriptorMap;
import org.albianj.persistence.impl.cached.RoutingMap;
import org.albianj.persistence.impl.cached.StorageAttributeMap;
import org.albianj.persistence.impl.db.ICommand;
import org.albianj.persistence.impl.db.IUpdateCommand;
import org.albianj.persistence.object.IAlbianObject;
import org.albianj.persistence.object.IAlbianObjectAttribute;
import org.albianj.persistence.object.IAlbianObjectHashMapping;
import org.albianj.persistence.object.IMemberAttribute;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.persistence.object.IRoutingsAttribute;
import org.albianj.persistence.object.IStorageAttribute;
import org.albianj.verify.Validate;

public class WriterJobAdapter extends FreeWriterJobAdapter
{
	protected void buildWriterJob(IAlbianObject object, IWriterJob writerJob,
			IUpdateCommand update)
	{
		String className = object.getClass().getName();
		IRoutingsAttribute routings = (IRoutingsAttribute) RoutingMap
				.get(className);
		IAlbianObjectAttribute albianObject = (IAlbianObjectAttribute) AlbianObjectsMap
				.get(className);
		PropertyDescriptor[] propertyDesc = (PropertyDescriptor[]) BeanPropertyDescriptorMap
				.get(className);
		Map<String, Object> mapValue = buildSqlParameter(object, albianObject,
				propertyDesc);

		List<IRoutingAttribute> useRoutings = parserRoutings(object, routings,
				albianObject);

		for (IRoutingAttribute routing : useRoutings)
		{
			ICommand cmd = update.builder(object, routings, albianObject,
					mapValue, routing);

			if (Validate.isNull(writerJob.getWriterTasks()))
			{
				Map<String, IWriterTask> tasks = new LinkedHashMap<String, IWriterTask>();
				IWriterTask task = new WriterTask();
				List<ICommand> cmds = new Vector<ICommand>();
				cmds.add(cmd);
				task.setCommands(cmds);
				IStorageAttribute storage = (IStorageAttribute) StorageAttributeMap
						.get(routing.getStorageName());
				task.setStorage(storage);
				tasks.put(routing.getStorageName(), task);
				writerJob.setWriterTasks(tasks);
			}
			else
			{
				if (writerJob.getWriterTasks().containsKey(
						routing.getStorageName()))
				{
					writerJob.getWriterTasks().get(routing.getStorageName())
							.getCommands().add(cmd);
				}
				else
				{
					IWriterTask task = new WriterTask();
					List<ICommand> cmds = new Vector<ICommand>();
					cmds.add(cmd);
					task.setCommands(cmds);
					IStorageAttribute storage = (IStorageAttribute) StorageAttributeMap
							.get(routing.getStorageName());
					task.setStorage(storage);
					writerJob.getWriterTasks().put(routing.getStorageName(),
							task);
				}
			}
		}
	}

	protected Map<String, Object> buildSqlParameter(IAlbianObject object,
			IAlbianObjectAttribute albianObject,
			PropertyDescriptor[] propertyDesc)
	{
		Map<String, Object> mapValue = new HashMap<String, Object>();
		for (PropertyDescriptor p : propertyDesc)
		{
			try
			{
				String name = p.getName();
				if ("string".equalsIgnoreCase(p.getPropertyType()
						.getSimpleName()))
				{
					Object oValue = p.getReadMethod().invoke(object);
					if (null == oValue)
					{
						mapValue.put(name, null);
					}
					else
					{
						String value = oValue.toString();
						IMemberAttribute member = albianObject.getMembers()
								.get(name);
						if (member.getLength() < value.length())// sub the
																// property
																// value for
																// database
																// length
						{
							mapValue.put(p.getName(),
									value.substring(0, member.getLength()));
						}
						else
						{
							mapValue.put(name, value);
						}
					}
				}
				else
				{
					mapValue.put(name, p.getReadMethod().invoke(object));
				}

			}
			catch (Exception e)
			{
				AlbianLoggerService.error(e,"invoke bean read method is error.");
				throw new RuntimeException("invoke bean read method is error");
			}
		}
		return mapValue;
	}

	protected List<IRoutingAttribute> parserRoutings(IAlbianObject object,
			IRoutingsAttribute routings, IAlbianObjectAttribute albianObject)
	{
		List<IRoutingAttribute> useRoutings = new Vector<IRoutingAttribute>();
		if (null == routings)// no routing form configure file,then use default
		{
			useRoutings.add(albianObject.getDefaultRouting());
		}
		else
		// there are routings from configure file
		{
			if (Validate.isNullOrEmpty(routings.getWriterRoutings()))// writer
																		// routings
																		// is
																		// null
																		// or
																		// empty,teh
																		// use
																		// default
			{
				useRoutings.add(albianObject.getDefaultRouting());
			}
			else
			// there are writer routings form configure file
			{
				if (routings.getWriterHash())// use hash mapping for writer
												// operation
				{
					IAlbianObjectHashMapping hashMapping = routings
							.getHashMapping();
					if (null == hashMapping)// there is no hash mapping function
											// from configure file,then use
											// default
					{
						useRoutings.add(albianObject.getDefaultRouting());
					}
					else
					// there us hash mapping function from configure file
					{
						List<IRoutingAttribute> writerRoutings = hashMapping
								.mappingWriterRouting(
										routings.getWriterRoutings(), object);
						if (Validate.isNullOrEmpty(writerRoutings))// there is
																	// no
																	// routings
																	// by hash
																	// mapping
																	// function
						{
							useRoutings.add(albianObject.getDefaultRouting());
						}
						else
						{
							for (IRoutingAttribute writerRouting : writerRoutings)
							{
								if (writerRouting.getEnable())
								{
									useRoutings.add(writerRouting);
								}
							}
							if (Validate.isNullOrEmpty(useRoutings))
							{
								useRoutings.add(albianObject
										.getDefaultRouting());
							}
						}
					}
				}
			}
		}
		return useRoutings;
	}
}
