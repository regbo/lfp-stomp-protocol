package ua.naiksoftware.stomp.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Log {

	public static void v(String tag, String message) {
		v(tag, message, null);
	}

	public static void v(String tag, String message, Throwable t) {
		Logger logger = Logger.getLogger(tag);
		Level level = Level.FINEST;
		if (t == null)
			logger.log(level, message);
		else
			logger.log(level, message, t);
	}

	public static void d(String tag, String message) {
		d(tag, message, null);
	}

	public static void d(String tag, String message, Throwable t) {
		Logger logger = Logger.getLogger(tag);
		Level level = Level.CONFIG;
		if (t == null)
			logger.log(level, message);
		else
			logger.log(level, message, t);
	}

	public static void i(String tag, String message) {
		i(tag, message, null);
	}

	public static void i(String tag, String message, Throwable t) {
		Logger logger = Logger.getLogger(tag);
		Level level = Level.INFO;
		if (t == null)
			logger.log(level, message);
		else
			logger.log(level, message, t);
	}

	public static void w(String tag, String message) {
		w(tag, message, null);
	}

	public static void w(String tag, String message, Throwable t) {
		Logger logger = Logger.getLogger(tag);
		Level level = Level.WARNING;
		if (t == null)
			logger.log(level, message);
		else
			logger.log(level, message, t);
	}

	public static void e(String tag, String message) {
		e(tag, message, null);
	}

	public static void e(String tag, String message, Throwable t) {
		Logger logger = Logger.getLogger(tag);
		Level level = Level.SEVERE;
		if (t == null)
			logger.log(level, message);
		else
			logger.log(level, message, t);
	}

	public static void main(String[] args) {
		String template = "	public static void {{{METHOD}}}(String tag, String message) {\r\n"
				+ "		{{{METHOD}}}(tag, message, null);\r\n" + "	}\r\n" + "\r\n"
				+ "	public static void {{{METHOD}}}(String tag, String message, Throwable t) {\r\n"
				+ "		Logger logger = Logger.getLogger(tag);\r\n" + "		Level level = Level.{{{LEVEL}}};\r\n"
				+ "		if (t == null)\r\n" + "			logger.log(level, message);\r\n" + "		else\r\n"
				+ "			logger.log(level, message, t);\r\n" + "	}";
		Map<String, Level> map = new LinkedHashMap<>();
		map.put("v", Level.FINEST);
		map.put("d", Level.CONFIG);
		map.put("i", Level.INFO);
		map.put("w", Level.WARNING);
		map.put("e", Level.SEVERE);
		for (Entry<String, Level> ent : map.entrySet()) {
			String str = template;
			str = str.replaceAll(Pattern.quote("{{{METHOD}}}"), ent.getKey());
			str = str.replaceAll(Pattern.quote("{{{LEVEL}}}"), ent.getValue() + "");
			System.out.println(str + "\n\n");
		}
	}

}