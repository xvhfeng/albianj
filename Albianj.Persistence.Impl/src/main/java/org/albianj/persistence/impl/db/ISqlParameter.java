package org.albianj.persistence.impl.db;

public interface ISqlParameter
{
	public int getSqlType();
	public void setSqlType(int sqlType);
	public String getName();
	public void setName(String name);
	public Object getValue();
	public void setValue(Object value);
	public void setSqlFieldName(String sqlFieldName);
	public String getSqlFieldName();
}
