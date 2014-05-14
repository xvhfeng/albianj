package org.albianj.logger;

import java.net.URL;
import java.util.Formatter;

import org.albianj.io.Path;
import org.albianj.kernel.KernelSetting;
import org.albianj.runtime.IStackTrace;
import org.albianj.runtime.RuningTrace;
import org.albianj.service.AlbianServiceException;
import org.albianj.service.FreeAlbianService;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * albanj的log类，由log4j实现和改进而来
 * 
 * @author Seapeak
 * 
 */
public class AlbianLoggerService extends FreeAlbianService implements
		IAlbianLoggerService {
	public static final String MARK_ERROR = "!";
	public static final String MARK_WARN = "@";
	public static final String MARK_INFO = "$";
	public static final String MARK_DEBUG = "*";

	private final static String ALBIAN_LOGGER = "_ALBIAN_LOGGER_";
	private static Logger logger;

	@Override
	public void loading() throws AlbianServiceException {
		try {
			if (KernelSetting.getAlbianConfigFilePath().startsWith("http://")) {
				DOMConfigurator.configure(new URL(Path
						.getExtendResourcePath(KernelSetting
								.getAlbianConfigFilePath() + "log4j.xml")));
			} else {

				DOMConfigurator.configure(Path
						.getExtendResourcePath(KernelSetting
								.getAlbianConfigFilePath() + "log4j.xml"));
			}

			super.loading();
			logger = LoggerFactory.getLogger(ALBIAN_LOGGER);
		} catch (Exception exc) {
			throw new AlbianServiceException(exc.getMessage(), exc.getCause());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.albianj.logger.IAlbianLoggerService#error(java.lang.String)
	 */
	public static void error(String format, Object... values) {
		if (null == logger)
			return;
		logger.error(getErrorMsg(format, values));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.albianj.logger.IAlbianLoggerService#warn(java.lang.String)
	 */
	public static void warn(String format, Object... values) {
		if (null == logger)
			return;
		logger.warn(getWarnMsg(format, values));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.albianj.logger.IAlbianLoggerService#info(java.lang.String)
	 */
	public static void info(String format, Object... values) {
		if (null == logger)
			return;
		logger.info(getInfoMsg(format, values));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.albianj.logger.IAlbianLoggerService#debug(java.lang.String)
	 */
	public static void debug(String format, Object... values) {
		if (null == logger)
			return;
		logger.debug(getDebugMsg(format, values));
	}

	public static void error(Exception e, String format, Object... values) {
		if (null == logger)
			return;
		logger.error(getErrorMsg(e, format, values));
	}

	public static void warn(Exception e, String format, Object... values) {
		if (null == logger)
			return;
		logger.warn(getWarnMsg(e, format, values));
	}

	public static void info(Exception e, String format, Object... values) {
		if (null == logger)
			return;
		logger.info(getInfoMsg(e, format, values));
	}

	public static void debug(Exception e, String format, Object... values) {
		if (null == logger)
			return;
		logger.debug(getDebugMsg(e, format, values));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.albianj.logger.IAlbianLoggerService#getErrorMsg(java.lang.String)
	 */
	public static String getErrorMsg(String format, Object... values) {
		return getMessage(MARK_ERROR, format, values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.albianj.logger.IAlbianLoggerService#getWarnMsg(java.lang.String)
	 */
	public static String getWarnMsg(String format, Object... values) {
		return getMessage(MARK_WARN, format, values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.albianj.logger.IAlbianLoggerService#getInfoMsg(java.lang.String)
	 */
	public static String getInfoMsg(String format, Object... values) {
		return getMessage(MARK_INFO, format, values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.albianj.logger.IAlbianLoggerService#getDebugMsg(java.lang.String)
	 */
	public static String getDebugMsg(String format, Object... values) {
		return getMessage(MARK_DEBUG, format, values);
	}

	public static String getErrorMsg(Exception e, String format,
			Object... values) {
		return getMessage(MARK_ERROR, e, format, values);
	}

	public static String getWarnMsg(Exception e, String format,
			Object... values) {
		return getMessage(MARK_WARN, e, format, values);
	}

	public static String getInfoMsg(Exception e, String format,
			Object... values) {
		return getMessage(MARK_INFO, e, format, values);
	}

	public static String getDebugMsg(Exception e, String format,
			Object... values) {
		return getMessage(MARK_DEBUG, e, format, values);
	}

	protected static String getMessage(String level, Exception e,
			String format, Object... values) {
		IStackTrace trace = RuningTrace.getTraceInfo(e);

		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format("%s.Trace:%s,Exception:%s", level, trace.toString(),
				e.getMessage());
		if (null != values)
			f.format(format, values);
		return f.toString();
	}

	protected static String getMessage(String level, String format,
			Object... values) {
		IStackTrace trace = RuningTrace.getTraceInfo();
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format("%s.Trace:%s", level, trace.toString());
		if (null != values)
			f.format(format, values);
		return f.toString();
	}

}
