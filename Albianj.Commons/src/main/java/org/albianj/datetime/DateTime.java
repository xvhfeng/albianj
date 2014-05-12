package org.albianj.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
	public static final String DEFAULT_FORMAT = "yyyyMMddHHmmss";
	public static final String CHINESE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String getDateTimeString() {
		return getDateTimeString(new Date(), DEFAULT_FORMAT);
	}

	public static String getDateTimeString(String format) {
		return getDateTimeString(new Date(), format);
	}

	public static String getDateTimeString(Date date) {
		return getDateTimeString(date, DEFAULT_FORMAT);
	}

	public static String getDateTimeString(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * @param begin
	 * @param end
	 * @return timespan seconds
	 */
	public static long getTimeSpan(Date begin, Date end) {
		return (end.getTime() - begin.getTime()) / 1000;
	}
}
