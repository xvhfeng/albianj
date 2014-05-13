albianj
=======


Albianj使用手册

总述：

	Albianj是一个一站式的开发框架解决方案。Albianj由java写成（其实他还有一个.net版本的）。目前因为项目的需要，Albianj还无法担负起一站式分布式系统开发框架的重任，但是只要花少许的功能更改和增加就可以实现。具体如下：
	1：增加一个分布式锁服务，让对象状态全站点唯一；
	2：增加一个管理服务器，管理Albianj的状态；

	Albianj是一款为互联网企业量身定做的开发框架。它主要解决了互联网企业中经常会碰到但是难以解决的在大数据高并发的前提下，对于数据的一致性和数据的完整性的问题。作为一个框架系统，Albianj还附带了一个简单的IoC框架，用来管理Albianj处理数据的service。
	目前的Albianj，主要由以下的功能：
	1：ID生成器。根据Albianj的配置，生成全站的唯一性的id，这些id带上基本上的属性，主要的信息有，id所属的业务；生成id的所属机器；id的生成时间等等。
	2：数据路由。俗称分库分表。这些原本繁杂的代码由Albianj给你代劳。当使用Albianj保存数据和访问数据的时候，数据会根据你的配置，找到他的路由，路由算法因为牵涉到业务，所以必须由你提供，然后由Albianj引擎在内部调用。
	3：两段提交的事务。在启用数据路由之后，紧接着遇到的问题就是数据的完整性和一致性问题。在Albianj中是使用“伪事务”的方式解决的，其实就是两段提交。
	警告：在数据完整性上，因为分布式的原因，会有一定的数据不一致和数据不完整的几率出现。这是CAP造成的，如果想了解请自行google。
	4：IoC。因为data router和orm需要加载很多的service，所以Albianj自带了一个超轻量级的IoC，但是在合格IoC绝对够用了；
	5：logger。Albianj集成了目前java中比较流行的log4j，不要和我说log4j，老性能不行，够用就可以了；
	6：线程池。Albianj内置了一个线程池，用来解决Albianj的异步启动和数据的缓存问题。
	7：xml解析器。Albianj用dom4j扩展了一个xml的解析器，用来解析Albianj的各个配置文件；
	8：Stack信息。Albianj扩展了对于异常捕获时的stack信息。
	9：加密。因为数据库连接的需要，加入了DEC加密算法，请各位在使用Albianj时自行更改DEC的加密key；Albianj不仅仅只提供DEC对称加密，还集成了MD5，SHA等等加密算法；
	10：密码生成器。Albianj自带了一个密码生成器，主要为了解决数据库用户名和密码的安全问题；
	11：id解析器，分析生成的Id的业务信息，便于数据路由；使用方法（目前版本为单机或者说非分布式版本）：
	首先需要确保在站点的根目录下有config文件夹，在文件夹中有以下几个配置文件：
	kernel.properties：Albianj引擎内核的配置；
	log4j.xml：Albianj集成日志主键的配置；
	service.xml：各种service的配置，Albianj的超轻量级的IoC；
	storage.xml：数据库连接的配置；
	peristence.xml：持久化对象的配置；
	routing.xml：持久化对象对于存储数据库的数据路由；

	
kernel.properties

	id＝0001 
		id是Albianj引擎的唯一标识，如果Albianj开启分布式模式，这个id就是这个Albianj实例在整个系统中的唯一标识，此标识必须唯一，不能有冲突，所以当分布式架构时，需要一个管理器就是去校验这个id是否冲突；
		Albianj最多支持10000台机器，也就是说这个id的范围使用0到9999.

	AppName＝Info
		此配置是业务配置，表明这个Albianj实例所被使用的业务站点名称。
		此配置最长不能超过7位，如果超过7位，将会截取前7位使用；

	ThreadPoolCoreSize＝5
	ThreadPoolMaxSize＝20
		Albianj内置线程池的数量，根据CPU的情况配置，一般是cpu核数的n倍；

	Level＝Debug
		此配置决定了这个站点是运行在online还是offline环境，一般，online环境配置为Release，offline环境配置为Debug。
		当这个配置项为Debug时，对于整个的Albianj没有任何的影响；当这个配置项为Release时，storage.xml中的对于数据库的用户名和密码配置项将会启用安全模式，即加密模式，需要加密串请使用命令行工具Albianj.ConnectionBuildeer生成。
		警告：注意，加密会使用kernnel中的DEC加密算法，线上环境请更改加密的key和slot，并且保证key和slot的安全性；另：key和slot是代码中写死的，不会根据根据配置，因为不安全；

	StartupMode＝Normal
		此配置决定Albianj引擎在站点中启动的方式。Albianj的启动方式有两种：Normal和Async。当配置为Normal时，即顺序启动；当配置为Async时，即为异步启动，就是Albianj的service都有Albianj的IoC通过异步的方式启动，一般适用于需要多处理的Web站点启动；

log4j.xml

	此配置文件基本等同于log4.xml的常规配置，只是为了更好的检查日志，Albianj对于log4j的RollingFileAppender进行了扩展，详细如下：

	<?xml version='1.0' encoding='UTF-8'?>
<!-- log4j的配置信息，基本不需要更改 -->
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">

	<!-- %c 输出日志信息所属的类的全名 %d 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy-MM-dd 
		HH:mm:ss }，输出类似：2002-10-18- 22：10：28 %f 输出日志信息所属的类的类名 %l 输出日志事件的发生位置，即输出日志信息的语句处于它所在的类的第几行 
		%m 输出代码中指定的信息，如log(message)中的message %n 输出一个回车换行符，Windows平台为“rn”，Unix平台为“n” 
		%p 输出优先级，即DEBUG，INFO，WARN，ERROR，FATAL。如果是调用debug()输出的，则为DEBUG，依此类推 %r 输出自应用启动到输出该日志信息所耗费的毫秒数 
		%t 输出产生该日志事件的线程名 -->
	<appender name="STDOUT" class="org.apache.log4j.ConsoleAppender">
		<layout class="org.apache.log4j.PatternLayout">
			<param name="ConversionPattern" value="[%d{yyyy-MM-dd HH:mm:ss}] %p %m%n" />
		</layout>
	</appender>

	<appender name="RollingFileAppender" class="org.albianj.logger.AlbianRollingFileAppender">
        <param name="File" value="logs/" /><!-- 设置日志输出文件名 -->
		<!-- 设置是否在重新启动服务时，在原有日志的基础添加新日志 -->
		<param name="Append" value="false" />
		<param name="MaxBackupIndex" value="-1" />
		<param name="MaxFileSize" value="10240" />
		<param name="FileName" value="albianj" />
		<param name="Format" value="yyyyMMddHHmmss" />
		<param name="Suffix" value="log" />
		<layout class="org.apache.log4j.PatternLayout">
			<param name="ConversionPattern" value="[%d{yyyy-MM-dd HH:mm:ss}] %p %m%n" />
		</layout>
	</appender>


	<!--默认级别，需要输出哪种类型日志，就写哪种类型 -->
	<root>
		<level value="DEBUG" />
		<appender-ref ref="RollingFileAppender" />
		<appender-ref ref="STDOUT" />
	</root>
</log4j:configuration>

更改RollingFileAppender定位
	在<appender name="RollingFileAppender" class="org.albianj.logger.AlbianRollingFileAppender">
中把log的writer定位到Albianj自定义的Appender就可以了。

service.xml

	此文件是配置需要通过Albianj的超轻量级IoC加载的服务；需要通过Albianj超轻量级IoC加载的服务必须满足以下条件：
	1：有接口和实现；
	2：接口必须继承org.albianj.service.IAlbianService, 如无特殊情况（99%的情况）实现类直接继承FreeAlbianService抽象类就可以了，如果在service加载个卸载的过程中有一些资源等操作，请重载各方法。

	 service.xml的配置如下：
	<Service Id="UserService" Type="albianj.explame.service.imp.UserService" />
	id：
		service在Albianj的IoC中的唯一性ID这个Id在一个Albianj实例内必须唯一不能冲突；
	Type：
		service的实现类，Albianj的IoC会根据这个类去初始化你的service；
	
	另：在service.xml中，可以不考虑service之间的依赖关系来配置，Albianj会自动去解决service之间的先后问题，但是Albianj目前不支持在加载过程中两两service之间存在相互引用；

	内置services：
以下service是Albianj引擎内置而且需要的service，必须存在，请勿更改：
<Service Id="logger" Type="org.albianj.logger.AlbianLoggerService" />
	<Service Id="Kernel" Type="org.albianj.service.parser.KernelServiceParser" />
	<Service Id="StorageService"
		Type="org.albianj.persistence.impl.storage.StorageService" />
	<Service Id="RoutingService"
		Type="org.albianj.persistence.impl.routing.RoutingService" />
	<Service Id="PersistenceService"
		Type="org.albianj.persistence.impl.persistence.PersistenceService" />
	<Service Id="ThreadPoolService" Type="org.albianj.concurrent.ThreadPoolService" />


storage.xml

	此文件配置数据库信息，具体如下：
	<Storages>
	<Storage>
		<Name>UserDB</Name>
		<DatabaseStyle>MySql</DatabaseStyle>
		<Server>10.96.210.58</Server>
		<Database>AlbianjUserDB</Database>
		<User>remote</User>
		<Password>db2202</Password>
		<Pooling>true</Pooling>
		<MinPoolSize>10</MinPoolSize>
		<MaxPoolSize>20</MaxPoolSize>
		<Timeout>60</Timeout>
		<Charset>utf8</Charset>
		<Transactional>true</Transactional>
	</Storage>
	</Storages>
		
	各节点信息：
	name：
		这个数据库信息的唯一标识，这个标识会在routing.xml文件中被用到；
	DatabaseStyle：
		你所使用的数据库，比如mysql，oracle等等，目前Albianj只支持mysql，你可以通过实现接口的方式来实现别的数据库的连接，目前此配置项只支持MySql；
	server：
		数据库的IP地址；
	Database：
		数据库的名称；
	User：
		连接数据库的用户名，注意：当kernnel中Level配置为Release时，User需要加密；
	Password：
		连接数据库的用户名，注意：同user节点，当kernnel中Level配置为Release时，User需要加密；
	Pooling：
		是否启用数据库连接池，连接次会使用lazyload方式，所以第一次打开数据库相对会较慢；
	MinPoolSize：
		数据库连接池的最小连接数，在打开数据库连接池时有效；
	MaxPoolSize：
		数据库连接池的最大连接数，在打开数据库连接池时有效；
	Timeout：
		连接超时时间；
	Charset：
		数据库的编码，一般MySql数据库的话，使用utf8
	Transactional：
		数据库引擎是否支持事务；注意：如果需要分布式事务，这里的配置一定要是true；
	
persistence.xml

	此文件配置所有需要持久化的数据类。和service一样，需要Albianj支持的数据对象必须满足以下条件：
	1：必须有接口和实现类；
	2：接口必须继承IAlbianObject，实现必须实现IAlbianObject，一般情况下（99%适用）继承FreeAlbianObject即可；
	3：如果不是自己实现存储机制，请千万不要对数据对象的IsAlbianNew属性进行操作（即调用setsetIsAlbianNew方法）；
	
	AlbianObject的配置如下：
<AlbianObjects>
	<AlbianObject Type="albianj.explame.obj.imp.ReaderInfoLog">
		<Cache Enable="false" LifeTime="300"></Cache>
		<!-- 删除了缓存服务。所以这部分配置一直为false，等待开启了缓存服务，再更加实际情况配置 -->
		<Members> <!-- 所有对象的属性映射，如果使用默认，可以不需要配置 -->
			<Member Name="uid" AllowNull="false"></Member>
			<Member Name="result" AllowNull="false"></Member>
			<Member Name="channel" AllowNull="false"></Member>
			<Member Name="notes" FieldName="Notes" Length="1024" />
			<Member Name="createtime" AllowNull="false" />
			<Member Name="actor" FieldName="Actor" AllowNull="false"
				Length="20" />
			<Member Name="actionip" FieldName="Actionip" AllowNull="false"
				Length="50" />
		</Members>
	</AlbianObject>
</AlbianObjects>
	
	每个AlbianObject即一个数据对象。AlbianObjects支持多AlbianObject配置。
	Type：
		数据对象类，必须实现IAlbianObject；
	cache
		Enable：是否启用缓存，目前因为项目的特殊性，不支持缓存，这部分可以通过开发接口支持；
	LifeTime：
		缓存过期时间，在开启缓存的情况下有效；
Members：
	数据对象类的属性集合，如果全部使用默认，这部分可以为空；
	Member 数据项，每个数据项都有以下几项功能提供；
	
		Name：
			数据项在程序中的名称，一般为属性名称；
		FieldName：
			属性对应的在数据库中的字段名称；
		AllowNull：
			是否允许字段为空，配置值为：true或者false
		Length：
			字段的最长长度；
		PrimaryKey：
			是否为主键，配置值为true或者false；
		IsSave：
			字段是否保存到数据库，配置为true或者false；
		DatabaseType：
			字段在数据库中的类型，目前支持的数据类型为：
			
			 DatabaseType	            数据库类型
			char	                Types.CHAR
			varchar	               Types.VARCHAR
			longvarchar	       Types.LONGVARCHAR
			numeric	               Types.NUMERIC
			decimal	               Types.DECIMAL
			bit	                       Types.BIT
			tinyint	               Types.TINYINT
			smallint	               Types.SMALLINT
			integer	               Types.INTEGER
			bigint	               Types.BIGINT
			real	                       Types.REAL
			float	                       Types.FLOAT
			double	               Types.DOUBLE
			binary	               Types.BINARY
			varbinary	               Types.VARBINARY
			longvarbinary	       Types.LONGVARBINARY
			date	                       Types.DATE
			time	                       Types.TIME
			timestamp	       Types.TIMESTAMP
			clob	                       Types.CLOB
			blob	                       Types.BLOB
			array	               Types.ARRAY
			以上都不是	       Types.VARCHAR
						
	默认为字段类型通过转换成的类型，如果转换找不到类型，即为varchar。如果你配置了DatabaseType，并且配置值不在Albianj支持的行列，默认也为varcahr。
	默认类型转换如下：
		属性类型	              数据库类型
		String	              Types.VARCHAR
		BigDecimal	      Types.NUMERIC
		boolean	              Types.BIT
		integer	              Types.INTEGER
		long	                      Types.BIGINT
		float	                      Types.FLOAT
		double	              Types.DOUBLE
		date	                      Types.DATE
		time	                      Types.TIME
		timestamp	      Types.TIMESTAMP
		clob	                      Types.CLOB
		blob	                      Types.BLOB
		array	              Types.ARRAY
		以上都不是	      Types.VARCHAR
	

routing.xml

	此文件配置数据路由信息，数据对象的数据路由必须实现IAlbianObjectHashMapping接口或者继承FreeAlbianObjectHashMapping抽象类。
	<AlbianObjects>

	<AlbianObject Type="albianj.explame.obj.imp.User"
		HashMapping="albianj.explame.service.imp.UserRouter">
		<WriterRoutings Hash="true">
			<WriterRouting Name="Hash" StorageName="UserDB"
				TableName="User" Enable="true"></WriterRouting>
			<WriterRouting Name="HashID" StorageName="User"
				TableName="User" Enable="true"></WriterRouting>
		</WriterRoutings>
		<ReaderRoutings Hash="true">
			<ReaderRouting Name="Hash" StorageName="UserDB"
				TableName="User" Enable="true"></ReaderRouting>
			<ReaderRouting Name="HashID" StorageName="User"
				TableName="User" Enable="true"></ReaderRouting>
		</ReaderRoutings>
	</AlbianObject>
</AlbianObjects>

每个AlbianObject即一个数据对象。AlbianObjects支持多AlbianObject配置。
	Type：
		数据对象类，必须实现IAlbianObject，其实就是persistence.xml中AlbianObject配置中的Type；
	HashMapping：
		对于这个Type数据对象的数据路由类，这个类必须实现IAlbianObjectHashMapping接口或者继承FreeAlbianObjectHashMapping抽象类。
	WriterRoutings：
		写数据时使用的数据路由，可以支持多个路由，配合HashMapping中的数据路由方法，可以将数据同时保存到一个或者多个路由中，这个保存数据的路由最终有HashMapping中方法提供。
	WriterRouting：数据对象的一个数据路由。
		Name：
			这个路由的名称；
		StorageName：
			数据路由到的数据库；
		TableName：
			数据路由的基表名称，这个名称可能会被HashMapping中的mappingWriterTable更改；
		Enable：
			是否开启这个路由，配置为true或者false，如果配置为false，即关闭这条路由。

	ReaderRoutings和ReaderRouting等同于WriterRoutings和WriterRouting，只是一个负责写入的数据路由，一个负责读取的数据路由。

	配置完routing.xml后，你必须实现HashMapping中的4个方法，具体为：
	mappingWriterRouting
	mappingWriterTable
	mappingReaderRouting
	mappingReaderTable
	
	mappingWriterRouting方法就是负责构建你需要将数据路由到那些路由（其实就是需要将数据保存到那些数据库），这个方法的返回值是一个List，也就是说对于保存数据而言，支持多路由（即保存到多个数据库）。

	mappingWriterTable方法负责构建你对于每个数据路由的具体的表。你在路由你的数据库后对于表也许也有一定的路由规则，这里就是定义的表的数据路由，说白了其实就是需要将数据库保存到哪些表。

	mappingReaderRouting、mappingReaderTable正好和appingWriterRouting、mappingWriterTable相反，它们是负责读取的，因为读取的时候你只可能在一个最适合的数据路由中读取数据，所以mappingReaderRouting的返回值是一个IRoutingAttribute值（单个路由信息，因为最适合的只可能有且只有一个，唯一的），mappingReaderTable和mappingWriterTable一致，没有差别。

启动Albianj引擎

配置完所有的配置后，我们启动Albianj引擎，只要一句代码。在你的站点启动事件中，加入以下代码即可。
		try {
		// 首先，你必须在应用程序启动时第一位启动albianj
		// 每个进程启动一次即可
			AlbianBootService.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("start albianj kernel is fail.");
			e.printStackTrace();
		} finally {
			// 大规模使用时必须卸载albianj，
			// 卸载时albianj会通知配置服务器卸载相关服务
			try {
				AlbianBootService.unload();
			} catch (Exception e) {
				System.err.println("ubload albianj kernel is fail.");
				e.printStackTrace();
			}
		}
