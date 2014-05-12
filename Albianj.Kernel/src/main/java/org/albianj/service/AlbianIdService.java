package org.albianj.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.albianj.kernel.KernelSetting;
import org.albianj.logger.AlbianLoggerService;
import org.albianj.text.StringHelper;
import org.albianj.verify.Validate;

/**
 * 唯一ID生成器 ID为asiic字符串，长度为32.模式（kernel-appname-timestamep-serial）如下：
 * kernel标识符（来自kernel.properties中的key），长度为4 appname
 * 业务名称（默认为kernel），长度为7,appname中不支持“0” timestamep
 * id生成时间戳，精确到秒，形如YYYYMMDDHHmmSS，长度为14 serial-number 序列号，长度为4
 * 
 * @Important：所有的自定义字符串小于最大长度时，左对齐添加0
 * @author Seapeak
 * 
 */
public class AlbianIdService {
	/**
	 * 按照默认业务“kernel”生成唯一ID。
	 * 
	 * @return 32位长的asiic字符串
	 */
	public synchronized static String generate() {
		return generate("Kenerl");
	}

	static AtomicInteger serial = new AtomicInteger(0);

	/**
	 * 根据appname生成全站唯一ID
	 * 
	 * @param appname
	 *            ：你需要生成的业务名称，必须是asiic字符串，长度<=7,大于7的将截取前7位
	 * @return 32位长的asiic字符串
	 */
	public synchronized static String generate(String appName) {
		// 这回这个种子再碰撞我真的要去买豆腐去了
		Random rnd = new Random();
		rnd.setSeed(10000);
		int numb = rnd.nextInt(10000);
		numb = (numb ^ serial.getAndIncrement()) % 10000;// 为了确保万无一失
		serial.compareAndSet(10000, 0);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String app = appName;
		if (app.length() < 7) {
			app = StringHelper.padLeft(app, 7);
		}
		if (app.length() > 7) {
			app = app.substring(0, 7);
		}

		return String.format("%1$s-%2$s-%3$s-%4$04d",
				StringHelper.padLeft(KernelSetting.getKernelId(), 4), app,
				dateFormat.format(new Date()), numb);
	}

	@SuppressWarnings("static-access")
	public synchronized static String generate32UUID() {
		return UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
	}

	public static String getAppName(String id) {
		if (Validate.isNullOrEmptyOrAllSpace(id)) {
			return null;
		}
		String[] strs = id.split("-");
		if (4 != strs.length) {
			AlbianLoggerService.error("id:%1$s is fail.", id);
			return null;
		}
		return StringHelper.censoredZero(strs[1]);

	}

	public static Date getGenerateDateTime(String id) {
		if (Validate.isNullOrEmptyOrAllSpace(id)) {
			return null;
		}
		String[] strs = id.split("-");
		if (4 != strs.length) {
			AlbianLoggerService.error("id:%1$s is fail.", id);
			return null;
		}

		DateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d;
		try {
			d = f.parse(strs[2]);
		} catch (ParseException e) {
			AlbianLoggerService.error("id:%1$s is fail.", id);
			return null;
		}
		return d;
	}

	public static Calendar getGenerateTime(String id) {
		if (Validate.isNullOrEmptyOrAllSpace(id)) {
			return null;
		}
		String[] strs = id.split("-");
		if (4 != strs.length) {
			AlbianLoggerService.error("id:%1$s is fail.", id);
			return null;
		}

		DateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = null;
		try {
			d = f.parse(strs[2]);
		} catch (ParseException e) {
			AlbianLoggerService.error("id:%1$s is fail.", id);
			return null;
		}
		Calendar cal = Calendar.getInstance(); // 什么时候有这个类型的？date的方法还nm迁移了
		cal.setTime(d);
		return cal;
	}
}
