package org.albianj.persistence.impl.storage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.albianj.kernel.AlbianLevel;
import org.albianj.kernel.KernelSetting;
import org.albianj.logger.AlbianLoggerService;
import org.albianj.persistence.impl.cached.DataSourceMap;
import org.albianj.persistence.impl.cached.StorageAttributeMap;
import org.albianj.persistence.object.DatabaseStyle;
import org.albianj.persistence.object.IStorageAttribute;
import org.albianj.persistence.object.impl.StorageAttribute;
import org.albianj.security.DESCoder;
import org.albianj.verify.Validate;
import org.albianj.xml.XmlParser;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Element;

public class StorageService extends FreeStorageParser {

	public final static String DEFAULT_STORAGE_NAME = "!@#$%Albianj_Default_Storage%$#@!";
	public final static String DRIVER_CLASSNAME = "com.mysql.jdbc.Driver";

	// <Storage>
	// <Name>1thStorage</Name>
	// <DatabaseStyle>MySql</DatabaseStyle>
	// <Server>localhost</Server>
	// <Database>BaseInfo</Database>
	// <Uid>root</Uid>
	// <Password>xuhf</Password>
	// <Pooling>false</Pooling>
	// <MinPoolSize>10</MinPoolSize>
	// <MaxPoolSize>20</MaxPoolSize>
	// <Timeout>60</Timeout>
	// <Charset>gb2312</Charset>
	// <Transactional>true</Transactional>
	//<TransactionLevel>0</TransactinLevel>
	// </Storage>

	public void loading() {
		super.init();
	}

	@Override
	protected void parserStorages(@SuppressWarnings("rawtypes") List nodes)
			throws StorageAttributeException {
		if (Validate.isNullOrEmpty(nodes)) {
			AlbianLoggerService.error("Storage node is null or size is 0.");
			return;
		}
		for (int i = 0; i < nodes.size(); i++) {
			IStorageAttribute storage = parserStorage((Element) nodes.get(i));
			if (null == storage) {
				throw new StorageAttributeException(
						"parser storage.xml is error.");
			}
			StorageAttributeMap.insert(storage.getName(), storage);
			if (i == 0) {
				StorageAttributeMap.insert(DEFAULT_STORAGE_NAME, storage);
			}
			DataSource ds = setupDataSource(storage);
			DataSourceMap.insert(storage.getName(), ds);
		}
	}

	@Override
	protected IStorageAttribute parserStorage(Element node) {
		String name = XmlParser.getSingleChildNodeValue(node, "Name");
		if (null == name) {
			AlbianLoggerService
					.error("There is no name attribute in the storage node.");
			return null;
		}
		String databaseStyle = XmlParser.getSingleChildNodeValue(node,
				"DatabaseStyle");
		String server = XmlParser.getSingleChildNodeValue(node, "Server");
		if (null == server) {
			AlbianLoggerService
					.error("There is no server attribute in the storage node.");
			return null;
		}
		String database = XmlParser.getSingleChildNodeValue(node, "Database");
		if (null == database) {
			AlbianLoggerService
					.error("There is no database attribute in the storage node.");
			return null;
		}
		String user = XmlParser.getSingleChildNodeValue(node, "User");
		if (null == user) {
			AlbianLoggerService
					.error("There is no uid attribute in the storage node.");
			return null;
		}
		String password = XmlParser.getSingleChildNodeValue(node, "Password");
		String pooling = XmlParser.getSingleChildNodeValue(node, "Pooling");
		String minPoolSize = XmlParser.getSingleChildNodeValue(node,
				"MinPoolSize");
		String maxPoolSize = XmlParser.getSingleChildNodeValue(node,
				"MaxPoolSize");
		String timeout = XmlParser.getSingleChildNodeValue(node, "Timeout");
		String charset = XmlParser.getSingleChildNodeValue(node, "Charset");
		String transactional = XmlParser.getSingleChildNodeValue(node,
				"Transactional");
		String transactionLevel = XmlParser.getSingleChildNodeValue(node,
				"TransactionLevel");

		IStorageAttribute storage = new StorageAttribute();
		storage.setName(name);
		if (null == databaseStyle) {
			storage.setDatabaseStyle(DatabaseStyle.MySql);
		} else {
			String style = databaseStyle.trim().toLowerCase();
			storage.setDatabaseStyle("sqlserver".equalsIgnoreCase(style) ? DatabaseStyle.SqlServer
					: "oracle".equalsIgnoreCase(style) ? DatabaseStyle.Oracle
							: DatabaseStyle.MySql);
		}
		storage.setServer(server);
		storage.setDatabase(database);
		storage.setUser(user);
		storage.setPassword(Validate.isNullOrEmptyOrAllSpace(password) ? ""
				: password);
		storage.setPooling(Validate.isNullOrEmptyOrAllSpace(pooling) ? true
				: new Boolean(pooling));
		storage.setMinSize(Validate.isNullOrEmptyOrAllSpace(minPoolSize) ? 5
				: new Integer(minPoolSize));
		storage.setMaxSize(Validate.isNullOrEmptyOrAllSpace(maxPoolSize) ? 20
				: new Integer(maxPoolSize));
		storage.setTimeout(Validate.isNullOrEmptyOrAllSpace(timeout) ? 30
				: new Integer(timeout));
		storage.setCharset(Validate.isNullOrEmptyOrAllSpace(charset) ? null
				: charset);
		storage.setTransactional(Validate
				.isNullOrEmptyOrAllSpace(transactional) ? true : new Boolean(
				transactional));
		if(storage.getTransactional()){
			if(Validate.isNullOrEmpty(transactionLevel)){
				//default level and do not means no suppert tran
				storage.setTransactionLevel(Connection.TRANSACTION_NONE);
			} else {
				if(transactionLevel.equalsIgnoreCase("READ_UNCOMMITTED")){
					storage.setTransactionLevel(Connection.TRANSACTION_READ_UNCOMMITTED);
				} else if(transactionLevel.equalsIgnoreCase("READ_COMMITTED")){
					storage.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
				}else if(transactionLevel.equalsIgnoreCase("REPEATABLE_READ")){
					storage.setTransactionLevel(Connection.TRANSACTION_REPEATABLE_READ);
				}else if(transactionLevel.equalsIgnoreCase("SERIALIZABLE")){
					storage.setTransactionLevel(Connection.TRANSACTION_SERIALIZABLE);
				}else{
					//default level and do not means no suppert tran
					storage.setTransactionLevel(Connection.TRANSACTION_NONE);
				}
			}
		}

		return storage;
	}

	public DataSource setupDataSource(IStorageAttribute storageAttribute) {
		BasicDataSource ds = null;
		try
		{
		 ds = new BasicDataSource();
		}catch(Exception e){
			System.err.println(e);
		}
		try {
			String url = FreeStorageParser
					.generateConnectionUrl(storageAttribute);
			ds.setDriverClassName(DRIVER_CLASSNAME);
			ds.setUrl(url);
			
			if (AlbianLevel.Debug == KernelSetting.getAlbianLevel()) {
				ds.setUsername(storageAttribute.getUser());
				ds.setPassword(storageAttribute.getPassword());
			} else {
				ds.setUsername(DESCoder.decrypt(storageAttribute.getUser()));
				ds.setPassword(DESCoder.decrypt(storageAttribute.getPassword()));
			}
			
			if (storageAttribute.getTransactional()) {
				ds.setDefaultAutoCommit(false);
				if(Connection.TRANSACTION_NONE != storageAttribute.getTransactionLevel()) {
					ds.setDefaultTransactionIsolation(storageAttribute.getTransactionLevel());
				}
			}
			ds.setDefaultReadOnly(false);
			if (storageAttribute.getPooling()) {
				ds.setInitialSize(storageAttribute.getMinSize());
				ds.setMaxIdle(storageAttribute.getMaxSize());
				ds.setMinIdle(storageAttribute.getMinSize());
			} else {
				ds.setInitialSize(5);
				ds.setMaxIdle(10);
				ds.setMinIdle(5);
			}
			ds.setMaxWait(storageAttribute.getTimeout() * 1000);
			ds.setRemoveAbandoned(true);
			ds.setRemoveAbandonedTimeout(storageAttribute.getTimeout());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return ds;
	}

	public synchronized static Connection getConnection(String storageName) {
		DataSource ds = (DataSource) DataSourceMap.get(storageName);
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			AlbianLoggerService.error(e,
					"Get the '%s' connection form connection pool is error.",
					storageName);
			return null;
		}
	}

}
