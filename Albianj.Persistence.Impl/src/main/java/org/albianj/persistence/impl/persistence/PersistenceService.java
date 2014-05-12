package org.albianj.persistence.impl.persistence;

import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.albianj.kernel.KernelSetting;
import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.impl.cached.AlbianObjectsMap;
import org.albianj.persistence.impl.cached.BeanPropertyDescriptorMap;
import org.albianj.persistence.impl.routing.RoutingService;
import org.albianj.persistence.impl.storage.StorageService;
import org.albianj.persistence.impl.toolkit.Convert;
import org.albianj.persistence.object.IAlbianObjectAttribute;
import org.albianj.persistence.object.ICacheAttribute;
import org.albianj.persistence.object.IMemberAttribute;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.persistence.object.impl.AlbianObjectAttribute;
import org.albianj.persistence.object.impl.CacheAttribute;
import org.albianj.persistence.object.impl.MemberAttribute;
import org.albianj.persistence.object.impl.RoutingAttribute;
import org.albianj.text.StringHelper;
import org.albianj.verify.Validate;
import org.albianj.xml.XmlParser;
import org.dom4j.Element;
import org.dom4j.Node;

public class PersistenceService extends FreePersistenceParser
{

	private static final String cacheTagName = "Cache";
	private static final String memberTagName = "Members/Member";
	public void loading()
	{
		super.init();
	}
	
	@Override
	protected void parserAlbianObjects(@SuppressWarnings("rawtypes") List nodes)
	{
		if(Validate.isNullOrEmpty(nodes))
		{
			throw new IllegalArgumentException("nodes");
		}
		String type = null;
		for (Object node : nodes)
		{
			IAlbianObjectAttribute albianObjectAttribute = null;
			try {
				albianObjectAttribute =  parserAlbianObject((Element) node);
			}catch(Exception e){
				String msg = String.format("parser the albian object next %1$s is error.",
						type);
					AlbianLoggerService.warn(msg);
				throw new PersistenceAttributeException(msg);
			}
			if (null == albianObjectAttribute)
			{
				String msg = String.format("parser the albian object next %1$s is error.",
						type);
					AlbianLoggerService.warn(msg);
				throw new PersistenceAttributeException(msg);
			}
			type = albianObjectAttribute.getType();
			AlbianObjectsMap.insert(type,albianObjectAttribute);
		}

	}

	@Override
	protected IAlbianObjectAttribute parserAlbianObject(Element node)
	{
		String type = XmlParser.getAttributeValue(node, "Type");
		if (Validate.isNullOrEmptyOrAllSpace(type))
		{
				AlbianLoggerService.error("The albianObject's type is empty or null.");
			return null;
		}

		Map<String, IMemberAttribute> map = reflexAlbianObjectMembers(type);

		Node cachedNode = node.selectSingleNode(cacheTagName);
		ICacheAttribute cached;
		if (null == cachedNode)
		{
			cached = new CacheAttribute();
			cached.setEnable(true);
			cached.setLifeTime(300);
		}
		else
		{
			cached = parserAlbianObjectCache(cachedNode);
		}

		IRoutingAttribute defaultRouting = new RoutingAttribute();
		defaultRouting.setName(RoutingService.DEFAULT_ROUTING_NAME);
		defaultRouting.setOwner("dbo");
		defaultRouting.setStorageName(StorageService.DEFAULT_STORAGE_NAME);
		defaultRouting.setTableName(ReflectionHelper.getClassSimpleName(type));

		@SuppressWarnings("rawtypes")
		List nodes = node.selectNodes(memberTagName);
		if(!Validate.isNullOrEmpty(nodes))
		{
			parserAlbianObjectMembers(type,nodes, map);
		}
		IAlbianObjectAttribute albianObjectAttribute = new AlbianObjectAttribute();
		albianObjectAttribute.setCache(cached);
		albianObjectAttribute.setMembers(map);
		albianObjectAttribute.setType(type);
		albianObjectAttribute.setDefaultRouting(defaultRouting);
		return albianObjectAttribute;
	}

	private static ICacheAttribute parserAlbianObjectCache(Node node)
	{
		String enable = XmlParser.getAttributeValue(node, "Enable");
		String lifeTime = XmlParser.getAttributeValue(node, "LifeTime");
		ICacheAttribute cache = new CacheAttribute();
		cache.setEnable(Validate.isNullOrEmpty(enable) ? true
				: new Boolean(enable));
		cache.setLifeTime(Validate.isNullOrEmpty(lifeTime) ? 300
				: new Integer(lifeTime));
		return cache;
	}

	private static void parserAlbianObjectMembers(String type,
			@SuppressWarnings("rawtypes") List nodes,
			Map<String, IMemberAttribute> map)
	{
		for (Object node : nodes)
		{
			parserAlbianObjectMember(type,(Element) node, map);
		}
	}

	private static void parserAlbianObjectMember(String type,Element elt,
			Map<String, IMemberAttribute> map)
	{
		String name = XmlParser.getAttributeValue(elt, "Name");
		if (Validate.isNullOrEmpty(name))
		{
			String msg = "AlbianObject name is null or empty.";
				AlbianLoggerService.error(msg);
			throw new PersistenceAttributeException(msg);
		}
		IMemberAttribute member = (IMemberAttribute) map.get(name);
		if(null == member){
			String msg = String.format("the field: %1$s is not found in the %2$s.",
					name,type
					);
			throw new PersistenceAttributeException(msg);
		}
		
		String fieldName = XmlParser.getAttributeValue(elt, "FieldName");
		String allowNull = XmlParser.getAttributeValue(elt, "AllowNull");
		String length = XmlParser.getAttributeValue(elt, "Length");
		String primaryKey = XmlParser.getAttributeValue(elt, "PrimaryKey");
		String dbType = XmlParser.getAttributeValue(elt, "DbType");
		String isSave = XmlParser.getAttributeValue(elt, "IsSave");
		if (!Validate.isNullOrEmpty(fieldName))
		{
			member.setSqlFieldName(fieldName);
		}
		if (!Validate.isNullOrEmpty(allowNull))
		{
			member.setAllowNull(new Boolean(allowNull));
		}
		if (!Validate.isNullOrEmpty(length))
		{
			member.setLength(new Integer(length));
		}
		if (!Validate.isNullOrEmpty(primaryKey))
		{
			member.setPrimaryKey(new Boolean(primaryKey));
		}
		if (!Validate.isNullOrEmpty(dbType))
		{
			member.setDatabaseType(Convert.toSqlType(dbType));
		}
		if (!Validate.isNullOrEmpty(isSave))
		{
			member.setIsSave(new Boolean(isSave));
		}
	}

	private static Map<String, IMemberAttribute> reflexAlbianObjectMembers(
			String type)
	{
		Map<String, IMemberAttribute> map = new LinkedHashMap<String, IMemberAttribute>();
		PropertyDescriptor[] propertyDesc = ReflectionHelper
				.getBeanPropertyDescriptors(type);
		BeanPropertyDescriptorMap.insert(type, propertyDesc);
		for (PropertyDescriptor p : propertyDesc)
		{
			IMemberAttribute member = reflexAlbianObjectMember(p);
			map.put(member.getName(), member);
		}
		return map;
	}

	private static IMemberAttribute reflexAlbianObjectMember(
			PropertyDescriptor propertyDescriptor)
	{
		IMemberAttribute member = new MemberAttribute();
		if ("id".equals(propertyDescriptor.getName()))
		{
			member.setAllowNull(false);
			member.setDatabaseType(Convert.toSqlType(propertyDescriptor
					.getPropertyType()));
			member.setSqlFieldName(propertyDescriptor.getName());
			member.setIsSave(true);
			member.setLength(32);
			member.setPrimaryKey(true);
			member.setName(propertyDescriptor.getName());
			return member;
		}
		if ("isAlbianNew".equals(propertyDescriptor.getName()))
		{
			member.setIsSave(false);
			member.setName(propertyDescriptor.getName());
			return member;
		}
		member.setAllowNull(true);
		member.setDatabaseType(Convert.toSqlType(propertyDescriptor
				.getPropertyType()));
		member.setSqlFieldName(propertyDescriptor.getName());
		member.setIsSave(true);
		member.setLength(200);
		member.setPrimaryKey(false);
		member.setName(propertyDescriptor.getName());
		return member;
	}
}
