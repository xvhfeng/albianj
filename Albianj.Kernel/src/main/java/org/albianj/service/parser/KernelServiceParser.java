package org.albianj.service.parser;

import java.util.Properties;

import org.albianj.io.Path;
import org.albianj.kernel.AlbianLevel;
import org.albianj.kernel.AlbianStartupMode;
import org.albianj.kernel.KernelSetting;
import org.albianj.logger.AlbianLoggerService;
import org.albianj.service.FreeAlbianService;
import org.albianj.verify.Validate;

public class KernelServiceParser extends FreeAlbianService implements
		IServiceParser {
	private final static String file = "kernel.properties";

	public void init() {
		try {
			Properties props = PropertiesParser.load(Path
					.getExtendResourcePath(KernelSetting.getAlbianKernelConfigFilePath() + file));
			parser(props);
		} catch (Exception e) {
			AlbianLoggerService.error(e,
					"load the kernel properties file is error.");
			throw new RuntimeException(e);
		}
	}

	public void parser(Properties props) {
		String id = PropertiesParser.getValue(props, "Id");
		if (Validate.isNullOrEmptyOrAllSpace(id))
			throw new RuntimeException("the kernel is is null pr empty.");
		KernelSetting.setKernelId(id);
//		KernelSetting.setKernelKey(PropertiesParser.getValue(props, "Key"));
		String appName = PropertiesParser.getValue(props, "AppName");
		if (Validate.isNullOrEmptyOrAllSpace(appName))
			throw new RuntimeException("The appName is null or empty.");
		KernelSetting.setAppName(appName);
		String coreSize = PropertiesParser
				.getValue(props, "ThreadPoolCoreSize");
		if (Validate.isNullOrEmptyOrAllSpace(coreSize)) {
			KernelSetting.setThreadPoolCoreSize(5);
		} else {
			KernelSetting.setThreadPoolCoreSize(new Integer(coreSize));
		}
		String maxSize = PropertiesParser.getValue(props, "ThreadPoolMaxSize");
		if (Validate.isNullOrEmptyOrAllSpace(maxSize)) {
			KernelSetting.setThreadPoolMaxSize(Runtime.getRuntime()
					.availableProcessors() * 2 + 1);
		} else {
			KernelSetting.setThreadPoolMaxSize(new Integer(maxSize));
		}
		
		String sLevel = PropertiesParser.getValue(props, "Level");
		if (Validate.isNullOrEmptyOrAllSpace(sLevel)
				|| sLevel.equalsIgnoreCase("debug")) {
			KernelSetting.setAlbianLevel(AlbianLevel.Debug);
		} else {
			KernelSetting.setAlbianLevel(AlbianLevel.Release);
		}
		
		String sMode = PropertiesParser.getValue(props, "StartupMode");
		if(Validate.isNullOrEmptyOrAllSpace(sMode)
				|| sMode.equalsIgnoreCase("normal")){
			KernelSetting.setAlbianStartupMode(AlbianStartupMode.Normal);
		} else {
			KernelSetting.setAlbianStartupMode(AlbianStartupMode.Async);
		}
	}

	@Override
	public void loading() throws RuntimeException {
		init();
		super.loading();
	}

}
