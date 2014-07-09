package org.bc.sdak.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TLogger{
	private static String logFileDir = "E:\\code\\zhongbang\\target\\logs";
	
	private static Map<String,TLogger> loggers = new HashMap<String,TLogger>();
	private long endtime;
	Logger log;
	
	public static TLogger getTLogger(String name){
		if(!loggers.containsKey(name)){
			Logger log = Logger.getLogger(name);
			TLogger tlog = new TLogger();
			tlog.log = log;
			tlog.endtime = System.currentTimeMillis() + getLoggerFileInterval();
			log.addHandler(getFileHandler());
			loggers.put(name, tlog);
		}
		TLogger tlog = loggers.get(name);
		if(System.currentTimeMillis()>tlog.endtime){
			//reset file handler.
			for(Handler h : tlog.log.getHandlers()){
				tlog.log.removeHandler(h);
			}
			tlog.log.addHandler(getFileHandler());
			tlog.endtime = System.currentTimeMillis() + getLoggerFileInterval();
		}
		return tlog;
	}
	
	public static FileHandler getFileHandler(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
			FileHandler handler = new FileHandler(logFileDir+File.separator+sdf.format(new Date())+".log");
			handler.setFormatter(new SimpleFormatter());
			return handler;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static long getLoggerFileInterval(){
		return 12*60*60*1000;
	}
}
