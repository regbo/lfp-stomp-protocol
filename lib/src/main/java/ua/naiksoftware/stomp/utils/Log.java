package ua.naiksoftware.stomp.utils;

import org.slf4j.LoggerFactory;

public class Log {

	public static void v(String tag, String message) {
		v(tag, message, null);
	}

	public static void v(String tag, String message, Throwable t) {
		if (t == null)
			LoggerFactory.getLogger(tag).trace(message);
		else
			LoggerFactory.getLogger(tag).trace(message, t);
	}

	public static void d(String tag, String message) {
		d(tag, message, null);
	}

	public static void d(String tag, String message, Throwable t) {
		if (t == null)
			LoggerFactory.getLogger(tag).debug(message);
		else
			LoggerFactory.getLogger(tag).debug(message, t);
	}

	public static void i(String tag, String message) {
		i(tag, message, null);
	}

	public static void i(String tag, String message, Throwable t) {
		if (t == null)
			LoggerFactory.getLogger(tag).info(message);
		else
			LoggerFactory.getLogger(tag).info(message, t);
	}

	public static void w(String tag, String message) {
		w(tag, message, null);
	}

	public static void w(String tag, String message, Throwable t) {
		if (t == null)
			LoggerFactory.getLogger(tag).warn(message);
		else
			LoggerFactory.getLogger(tag).warn(message, t);
	}

	public static void e(String tag, String message) {
		e(tag, message, null);
	}

	public static void e(String tag, String message, Throwable t) {
		if (t == null)
			LoggerFactory.getLogger(tag).error(message);
		else
			LoggerFactory.getLogger(tag).error(message, t);
	}

}