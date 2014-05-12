package org.albianj.persistence.object;


public interface IOrderByCondition
{
	public String getFieldName();
	public void setFieldName(String fieldName);
	public SortStyle getSortStyle();
	public void setSortStyle(SortStyle sortStyle);
}
