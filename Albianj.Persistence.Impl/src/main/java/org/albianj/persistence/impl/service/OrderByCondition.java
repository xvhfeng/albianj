package org.albianj.persistence.impl.service;

import org.albianj.persistence.object.IOrderByCondition;
import org.albianj.persistence.object.SortStyle;

public class OrderByCondition implements IOrderByCondition
{
	private String fieldName = null;
	private SortStyle sortStyle = SortStyle.Asc;
	
	public String getFieldName()
	{
		return fieldName;
	}
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}
	public SortStyle getSortStyle()
	{
		return sortStyle;
	}
	public void setSortStyle(SortStyle sortStyle)
	{
		this.sortStyle = sortStyle;
	}
}
