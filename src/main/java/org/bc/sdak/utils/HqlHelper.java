package org.bc.sdak.utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.FetchType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.DateSeparator;

public class HqlHelper {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static String buildDateSegment(String fieldName,String dateStr,DateSeparator sep,List<Object> params){
		if(StringUtils.isEmpty(dateStr)){
			return "";
		}
		try {
			Date date = sdf.parse(dateStr);
			if(sep==DateSeparator.Before){
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(date.getTime());
				cal.add(Calendar.DAY_OF_YEAR, 1);
				params.add(cal.getTime());
				return " and " + fieldName + " < ? ";
			}else{
				params.add(date);
				return " and " + fieldName + " >= ? ";
			}
		} catch (ParseException e) {
			LogUtil.log(Level.WARN, "时间查询错误，dateStr="+dateStr, e);
			return "";
		}
	}
	
	public static String getLazyHql(Class<?> entity){
		StringBuilder hql = new StringBuilder("select ");
		for(Field f: entity.getDeclaredFields()){
			Basic anno = f.getAnnotation(Basic.class);
			if(anno!=null && anno.fetch()==FetchType.LAZY){
				continue;
			}
			hql.append(f.getName()+" as "+ f.getName()).append(",");
		}
		String result = StringUtils.removeEnd(hql.toString(), ",");
		result = result+" from " +entity.getName();
		return result;
	}
	
	public static String getCommonAlis(Class<?> clazz , String alias){
		StringBuilder hql = new StringBuilder();
		for(Field f: clazz.getDeclaredFields()){
			Basic anno = f.getAnnotation(Basic.class);
			if(anno!=null && anno.fetch()==FetchType.LAZY){
				continue;
			}
			hql.append(alias).append(".").append(f.getName()+" as "+ f.getName()).append(",");
		}
		String result = StringUtils.removeEnd(hql.toString(), ",");
		return result;
	}
}
