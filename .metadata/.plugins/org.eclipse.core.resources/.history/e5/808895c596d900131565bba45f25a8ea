package org.albianj.persistence.object;

import java.util.List;
import java.util.Map;


/**
 * @author Administrator
 *
 */
public interface IAlbianObjectHashMapping
{
	/*
	 * 数据库级数据路由，把数据路由到某个（或某几个）数据库
	 * 根据对象配置的数据路由，散列数据到各个路由
	 * @routings: 对象所有的写配置路由（来自rounting.xml）
	 * @obj: 需要散列的对象，可以根据此对象的某些属性决定需要保存到的路由
	 * return： 返回保存此对象需要的数据路由集合（同一份数据可以保存多个副本）
	 */
	public List<IRoutingAttribute> mappingWriterRouting(Map<String,IRoutingAttribute> routings,IAlbianObject obj);
	
	/*
	 * 数据库级数据路由，把数据路由到某个数据库
	 * 根据对象配置的数据路由和查询对象的条件，查找最合适的数据路由
	 * @routings： 对象所有的读配置路由（来自routing.xml)
	 * @whers： 查询对象的查询条件，可以根据查询条件得到合适的路由
	 * @orderbys： 查询对象的排序条件，可以根据排序条件得到合适的路由
	 * return： 读取数据的数据路由 （读取只有一份最适合要求的数据可读）
	 */
	public IRoutingAttribute mappingReaderRouting(Map<String,IRoutingAttribute> routings,Map<String,IFilterCondition> wheres,
			Map<String,IOrderByCondition> orderbys);
	
	/*
	 * 表级数据路由，把数据路由到mappingWriterRouting确定的路由中的某个表
	 * @routing： 由mappingWriterRouting返回的某个路由
	 * @obj： 需要散列的对象，可以根据此对象的某些属性决定路由的表
	 * return： 此数据路由的表名
	 */
	public String mappingWriterTable(IRoutingAttribute routing,IAlbianObject obj);

	/*
	 * 表级数据路由，把数据路由到mappingReaderRouting确定的路由中的某个表
	 * @routing： mappingReaderRouting确定的某个数据路由
	 * @whers： 查询对象的查询条件，可以根据查询条件得到合适的路由
	 * @orderbys： 查询对象的排序条件，可以根据排序条件得到合适的路由
	 */
	public String mappingReaderTable(IRoutingAttribute routing,Map<String,IFilterCondition> wheres,
			Map<String,IOrderByCondition> orderbys);
}
