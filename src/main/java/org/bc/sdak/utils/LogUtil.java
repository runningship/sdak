package org.bc.sdak.utils;

import java.util.logging.Level;
import java.util.logging.Logger;


public class LogUtil {

	public static void log(Level level, String msg , Throwable ex){
		String name = new Exception().getStackTrace()[0].getClass().getName();
		Logger log = Logger.getLogger(name);
		log.log(level, msg,ex);
	}
	
	public static void info(String msg){
		String name = new Exception().getStackTrace()[0].getClass().getName();
		Logger log = Logger.getLogger(name);
		log.info(msg);
	}
	
	public static void warning(String msg){
		String name = new Exception().getStackTrace()[0].getClass().getName();
		Logger log = Logger.getLogger(name);
		log.warning(msg);
	}
}
