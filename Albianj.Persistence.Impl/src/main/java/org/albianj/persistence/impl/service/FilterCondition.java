package org.albianj.persistence.impl.service;

import org.albianj.persistence.object.IFilterCondition;
import org.albianj.persistence.object.LogicalOperation;
import org.albianj.persistence.object.RelationalOperator;

public class FilterCondition implements IFilterCondition
{
	private RelationalOperator relationalOperator = RelationalOperator.And;
	private String fieldName = null;
	private Class<?> fieldClass = String.class;
	private LogicalOperation logicalOperation = LogicalOperation.Equal;
	private Object value = null;
	private boolean beginSub = false;
	private boolean closeSub = false;

	public RelationalOperator getRelationalOperator()
	{
		return relationalOperator;
	}
	public void setRelationalOperator(RelationalOperator relationalOperator)
	{
		this.relationalOperator = relationalOperator;
	}
	public String getFieldName()
	{
		return fieldName;
	}
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}
	public Class<?> getFieldClass()
	{
		return fieldClass;
	}
	public void setFieldClass(Class<?> fieldClass)
	{
		this.fieldClass = fieldClass;
	}
	public LogicalOperation getLogicalOperation()
	{
		return logicalOperation;
	}
	public void setLogicalOperation(LogicalOperation logicalOperation)
	{
		this.logicalOperation = logicalOperation;
	}
	public Object getValue()
	{
		return this.value;
	}
	
	public void setValue(Object value)
	{
		this.value = value;
	}
	public void beginSub(){
		this.beginSub = true;
	}
	public void closeSub(){
		this.closeSub = true;	
	}
	public boolean isBeginSub(){
		return this.beginSub;
	}
	public boolean isCloseSub(){
		return this.closeSub;
	}
	
	public void set(RelationalOperator ro,String fieldName,Class<?> cls,
			LogicalOperation lo,Object v){
		this.relationalOperator = ro;
		this.fieldName = fieldName;
		this.fieldClass = cls;
		this.logicalOperation = lo;
		this.value = v;
	}
	public void set(String fieldName,Class<?> cls,
			LogicalOperation lo,Object v){
		set(RelationalOperator.And,fieldName,cls,lo,v);
	}
}
