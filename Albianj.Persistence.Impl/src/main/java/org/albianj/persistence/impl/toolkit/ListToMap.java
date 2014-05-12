package org.albianj.persistence.impl.toolkit;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.albianj.persistence.object.IFilterCondition;
import org.albianj.persistence.object.IOrderByCondition;

public class ListToMap
{
	public static Map<String,IFilterCondition> convertFilterConditions(IFilterCondition[] filters)
	{
		if(null == filters)
		{
			return null;
		}
		int len = filters.length;
		if(0 == len)
		{
			return new LinkedHashMap<String, IFilterCondition>(0);
		}
		 Map<String,IFilterCondition> map =new LinkedHashMap<String, IFilterCondition>(len);
		 for(IFilterCondition filter : filters)
		 {
			 map.put(filter.getFieldName(), filter);
		 }
		 return map;
	}
	
	public static Map<String,IFilterCondition> convertFilterConditions(List<IFilterCondition> filters)
	{
		if(null == filters)
		{
			return null;
		}
		int size = filters.size();
		if(0 == size)
		{
			return new LinkedHashMap<String, IFilterCondition>(0);
		}
		 Map<String,IFilterCondition> map =new LinkedHashMap<String, IFilterCondition>(size);
		 for(IFilterCondition filter : filters)
		 {
			 map.put(filter.getFieldName(), filter);
		 }
		 return map;
	}
	
	public static Map<String,IOrderByCondition> convertOrderByConditions(IOrderByCondition[] orderbys)
	{
		if(null == orderbys)
		{
			return null;
		}
		int len = orderbys.length;
		if(0 == len)
		{
			return new LinkedHashMap<String, IOrderByCondition>(0);
		}
		 Map<String,IOrderByCondition> map =new LinkedHashMap<String, IOrderByCondition>(len);
		 for(IOrderByCondition filter : orderbys)
		 {
			 map.put(filter.getFieldName(), filter);
		 }
		 return map;
	}
	
	public static Map<String,IOrderByCondition> convertOrderByConditions(List<IOrderByCondition> orderbys)
	{
		if(null == orderbys)
		{
			return null;
		}
		int size = orderbys.size();
		if(0 == size)
		{
			return new LinkedHashMap<String, IOrderByCondition>(0);
		}
		 Map<String,IOrderByCondition> map =new LinkedHashMap<String, IOrderByCondition>(size);
		 for(IOrderByCondition filter : orderbys)
		 {
			 map.put(filter.getFieldName(), filter);
		 }
		 return map;
	}
}
