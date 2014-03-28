package org.bc.sdak.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BeanUtil {

	public static String toString(Object obj,String... filter){
		List<String> filterList = new ArrayList<String>();
		for(int i=0;i<filter.length;i++){
			filterList.add(filter[i]);
		}
		StringBuffer sb = new StringBuffer(obj.getClass().getName());
		sb.append(":[");
		for(Field f : obj.getClass().getDeclaredFields()){
			if(filterList.contains(f.getName())){
				continue;
			}
			Object value = null;
			try {
				value = f.get(obj);
			} catch (Exception e) {
				continue;
			}
			sb.append(f.getName()).append("=").append(value).append(";");
		}
		sb = sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}
}
