package org.albianj.persistence.impl.context;

import java.util.HashMap;
import java.util.Map;

import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.impl.cached.AlbianObjectsMap;
import org.albianj.persistence.impl.cached.RoutingMap;
import org.albianj.persistence.impl.cached.StorageAttributeMap;
import org.albianj.persistence.impl.db.Command;
import org.albianj.persistence.impl.db.CommandType;
import org.albianj.persistence.impl.db.ICommand;
import org.albianj.persistence.impl.db.ISqlParameter;
import org.albianj.persistence.impl.db.SqlParameter;
import org.albianj.persistence.impl.toolkit.Convert;
import org.albianj.persistence.impl.toolkit.EnumMapping;
import org.albianj.persistence.impl.toolkit.ListToMap;
import org.albianj.persistence.object.IAlbianObjectAttribute;
import org.albianj.persistence.object.IFilterCondition;
import org.albianj.persistence.object.IMemberAttribute;
import org.albianj.persistence.object.IOrderByCondition;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.persistence.object.IRoutingsAttribute;
import org.albianj.persistence.object.IStorageAttribute;
import org.albianj.verify.Validate;

public abstract class FreeReaderJobAdapter implements IReaderJobAdapter
{
	public IReaderJob buildReaderJob(Class<?> cls, String routingName,
			int start, int step, IFilterCondition[] wheres,
			IOrderByCondition[] orderbys)
	{
		String className = cls.getName();
		IRoutingsAttribute routings = (IRoutingsAttribute) RoutingMap
				.get(className);
		IAlbianObjectAttribute albianObject = (IAlbianObjectAttribute) AlbianObjectsMap
				.get(className);
		
		Map<String,IFilterCondition> hashWheres = ListToMap.convertFilterConditions(wheres);
		Map<String,IOrderByCondition> hashOrderbys = ListToMap.convertOrderByConditions(orderbys);
		IRoutingAttribute readerRouting = parserReaderRouting(cls,routingName,hashWheres,hashOrderbys);
		
		StringBuilder sbCols = new StringBuilder();
		StringBuilder sbWhere = new StringBuilder();
		StringBuilder sbOrderby = new StringBuilder();
		StringBuilder sbStatement = new StringBuilder();
		 Map<String,ISqlParameter> paras = new HashMap<String, ISqlParameter>();
		for(String key : albianObject.getMembers().keySet())
		{
			IMemberAttribute member = albianObject.getMembers().get(key);
			if(!member.getIsSave()) continue;
			if(member.getSqlFieldName().equals(member.getName()))
			{
				sbCols.append(member.getSqlFieldName()).append(",");
			}
			else
			{
				sbCols.append(member.getSqlFieldName()).append(" AS ")
				.append(member.getName()).append(",");
			}
		}
		if(0 != sbCols.length()) sbCols.deleteCharAt(sbCols.length() -1);
		if(null != wheres)
		{
			for(IFilterCondition where : wheres)
			{
				IMemberAttribute member = albianObject.getMembers().get(where.getFieldName());
				sbWhere.append(EnumMapping.toRelationalOperators(where.getRelationalOperator()))
						 .append(where.isBeginSub() ? "(" : " ").append(member.getSqlFieldName())
						 .append(EnumMapping.toLogicalOperation(where.getLogicalOperation()))
						.append("#").append(member.getSqlFieldName()).append("#")
						.append(where.isCloseSub() ? ")" : "");
				ISqlParameter para = new SqlParameter();
				para.setName(member.getSqlFieldName());
				para.setSqlFieldName(member.getSqlFieldName());
				para.setSqlType(Convert.toSqlType(where.getFieldClass()));
				para.setValue(where.getValue());
				paras.put(String.format("#%1$s#",member.getSqlFieldName()), para);
			}
		}
		if(null != orderbys)
		{
			for(IOrderByCondition orderby : orderbys)
			{
				IMemberAttribute member = albianObject.getMembers().get(orderby.getFieldName());
				sbOrderby.append(member.getSqlFieldName()).append(" ")
						.append(EnumMapping.toSortOperation(orderby.getSortStyle()))
						.append(",");
			}
		}
		if(0 != sbOrderby.length()) sbOrderby.deleteCharAt(sbOrderby.length() -1);
		String tableName = null;
		if(null == routings || null == routings.getHashMapping())
		{
			tableName = readerRouting.getTableName();
		}
		else
		{
		
			tableName = routings.getHashMapping().mappingReaderTable(readerRouting, hashWheres, hashOrderbys);
			
			tableName = Validate.isNullOrEmptyOrAllSpace(tableName) 
							? readerRouting.getTableName()
								: tableName;
		}
		
		sbStatement.append("SELECT ").append(sbCols).append(" FROM ").append(tableName)
					.append(" WHERE 1=1 ").append(sbWhere);
		if(0 != sbOrderby.length())
		{
			sbStatement.append(" ORDER BY ").append(sbOrderby);
		}
		if(0 <= start && 0 < step)
		{
			sbStatement.append(" LIMIT ").append(start).append(", ").append(step);
		}
		if(0 > start && 0 < step)
		{
			sbStatement.append(" LIMIT ").append(step);
		}
		
		AlbianLoggerService.debug(sbStatement.toString());
		
		ICommand cmd = new Command();
		cmd.setCommandText(sbStatement.toString());
		cmd.setCommandType(CommandType.Text);
		cmd.setParameters(paras);
		
		IReaderJob job = new ReaderJob();
		job.setCommand(cmd);
		IStorageAttribute storage = (IStorageAttribute) StorageAttributeMap.get(readerRouting.getStorageName());
		job.setStorage(storage);
		return job;
	}
	
	protected abstract IRoutingAttribute parserReaderRouting(Class<?> cls,String routingName,Map<String,IFilterCondition> hashWheres,
			Map<String,IOrderByCondition> hashOrderbys);
}
