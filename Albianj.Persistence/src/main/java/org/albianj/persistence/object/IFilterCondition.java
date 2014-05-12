package org.albianj.persistence.object;


public interface IFilterCondition
{
	/**
	 * 和前面条件的关系
	 * @return
	 */
	public RelationalOperator getRelationalOperator();
	/**
	 * 和前面条件的关系，第一个条件可以不用设置
	 * @param relationalOperator: relationalOperator的某个值
	 */
	public void setRelationalOperator(RelationalOperator relationalOperator);
	
	/**
	 * 得到对象属性字段
	 * @return
	 */
	public String getFieldName();
	
	/**
	 * 设置查询对象的字段名称（不是数据库字段，而是类的属性名称）
	 * @param fieldName
	 */
	public void setFieldName(String fieldName);
	public Class<?> getFieldClass();
	
	/**
	 * 设置字段的数据类型
	 * @param cls
	 */
	public void setFieldClass(Class<?> cls);
	public LogicalOperation getLogicalOperation();
	/**
	 * 设置字段和值的逻辑关系
	 * @param logicalOperation
	 */
	public void setLogicalOperation(LogicalOperation logicalOperation);
	public Object getValue();
	/**
	 * 设置字段的值
	 * @param value
	 */
	public void setValue(Object value);
	
	/**
	 * 开始子条件
	 * 是否子条件必须单独设置
	 */
	public void beginSub();
	/**
	 * 子条件结束
	 * 是否子条件必须单独设置
	 */
	public void closeSub();
	
	public boolean isBeginSub();
	public boolean isCloseSub();
	
	public void set(RelationalOperator ro,String fieldName,Class<?> cls,
			LogicalOperation lo,Object v);
	public void set(String fieldName,Class<?> cls,
			LogicalOperation lo,Object v);
}
