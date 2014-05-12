package org.albianj.persistence.object.impl;

import java.util.List;
import java.util.Map;

import org.albianj.persistence.object.IAlbianObject;
import org.albianj.persistence.object.IAlbianObjectHashMapping;
import org.albianj.persistence.object.IFilterCondition;
import org.albianj.persistence.object.IOrderByCondition;
import org.albianj.persistence.object.IRoutingAttribute;

public abstract class FreeAlbianObjectHashMapping implements
		IAlbianObjectHashMapping
{

	
	/**
	 * @param ������������writer routings
	 * @param ��������������
	 * @return ��������������routings
	 */
	public List<IRoutingAttribute> mappingWriterRouting(Map<String,IRoutingAttribute> routings,IAlbianObject obj)
	{
		return null;
	}
	/**
	 * @param ����������reader routings
	 * @param ��������
	 * @param ��������
	 * @return ������������routing
	 */
	public IRoutingAttribute mappingReaderRouting(Map<String,IRoutingAttribute> routings,Map<String,IFilterCondition> wheres,
			Map<String,IOrderByCondition> orderbys)
	{
		return null;
	}
	/**
	 * @param ��������������routing
	 * @param ��������������
	 * @return ����������������������
	 */
	public String mappingWriterTable(IRoutingAttribute routing,IAlbianObject obj)
	{
		return null;
	}

	/**
	 * @param ��������������reader routing
	 * @param ��������
	 * @param ��������
	 * @return ��������������������
	 */
	public String mappingReaderTable(IRoutingAttribute routing,Map<String,IFilterCondition> wheres,
			Map<String,IOrderByCondition> orderbys)
	{
		return null;
	}
}
