package org.bc.sdak.utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.bc.sdak.Page;
import org.bc.sdak.utils.JsonDateValueProcessor;

public class JSONHelper {

	public static JSONObject toJSON(Page<?> page){
		return toJSON(page,"");
	}
	public static JSONObject toJSON(Page<?> page,String timeFormat){
		if(page==null){
			return new JSONObject();
		}
		JSONObject jobj = JSONObject.fromObject(page);
		jobj.remove("result");
		JSONArray arr = new JSONArray();
		JsonConfig cfg = new JsonConfig();
		cfg.setIgnorePublicFields(false);
		cfg.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor(timeFormat));
		cfg.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor(timeFormat));
		for(Object obj : page.getResult()){
			if(obj instanceof Map && page.mergeResult){
				arr.add(mergeMap((Map)obj ,cfg));
			}else{
				arr.add(JSONObject.fromObject(obj,cfg));
			}
		}
		jobj.put("data", arr);
		return jobj;
	}
	
	private static JSONObject mergeMap(Map obj, JsonConfig cfg) {
		JSONObject result = new JSONObject();
		for(Object key : obj.keySet()){
			if(obj.get(key).getClass().getAnnotation(Entity.class)!=null){
				result.putAll(JSONObject.fromObject(obj.get(key),cfg));
			}else{
				result.put(key, obj.get(key));
			}
		}
		return result;
	}
	
	public static JSONObject toJSON(Object obj , String timeFormat){
		if(obj==null){
			return new JSONObject();
		}
		JsonConfig cfg = new JsonConfig();
		cfg.setIgnorePublicFields(false);
		cfg.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor(timeFormat));
		cfg.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor(timeFormat));
		return JSONObject.fromObject(obj, cfg);
	}
	
	public static JSONObject toJSON(Object obj){
		return toJSON(obj,"");
	}
	
	public static JSONArray toJSONArray(List<?> list){
		return toJSONArray(list , "");
	}
	public static JSONArray toJSONArray(List<?> list , String timeFormat){
		if(list==null){
			return new JSONArray();
		}
		JSONArray arr = new JSONArray();
		JsonConfig cfg = new JsonConfig();
		cfg.setIgnorePublicFields(false);
		cfg.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		cfg.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor());
		cfg.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor(timeFormat));
		cfg.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor(timeFormat));
		for(Object obj : list){
			if(obj instanceof Enum){
				arr.add(JSONArray.fromObject(obj,cfg));
			}else{
				arr.add(JSONObject.fromObject(obj,cfg));
			}
		}
		return arr;
	}
	
}
