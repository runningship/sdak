package org.bc.sdak.utils;

import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


public class LogUtil {

	
	public static void log(Priority p, String msg , Throwable ex){
		String name = new Exception().getStackTrace()[0].getClass().getName();
		Logger log = getLogger(name);
		if(ex!=null){
			log.log(p, msg, ex);
		}else{
			log.log(p, msg);
		}
	}
	
	public static void info(String msg){
		String name = new Exception().getStackTrace()[0].getClass().getName();
		Logger log = getLogger(name);
		log.info(msg);
	}
	
	public static void warning(String msg){
		String name = new Exception().getStackTrace()[0].getClass().getName();
		Logger log = getLogger(name);
		log.warn(msg);
	}
	
	private static Logger getLogger(String name){
		return Logger.getLogger(name);
//		return TLogger.getTLogger(name).log;
	}
	
}
