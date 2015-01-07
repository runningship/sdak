package org.bc.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.utils.ClassUtil;

public class ModuleManager {

//	private static Map<String,Class<?>> modules = new HashMap<String,Class<?>>();
	private static Map<String ,Handler> handlers = new HashMap<String,Handler>();
	public static void add(String packageName){
		List<Class<?>> list = ClassUtil.getClasssFromPackage(packageName);
		for(Class<?> clazz : list){
			Module xx = clazz.getAnnotation(Module.class);
			if(xx!=null){
				String moduleUrl = StringUtils.removeEnd(xx.name(),"/");
				System.out.println("loaded module "+clazz.getName());
				buildHandler(moduleUrl,clazz);
//				modules.put(,clazz);
				
			}
		}
	}
	
	private static void buildHandler(String moduleUrl, Class<?> clazz) {
		for(Method m : clazz.getDeclaredMethods()){
			WebMethod wm = m.getAnnotation(WebMethod.class);
			if(wm!=null){
				Handler handler = new Handler(clazz,m.getName());
				if(StringUtils.isNotEmpty(wm.alias())){
					handlers.put(moduleUrl +"/"+ wm.alias(), handler);
				}
				if(StringUtils.isNotEmpty(wm.name())){
					handlers.put(moduleUrl +"/"+ wm.name(), handler);
				}
				if(StringUtils.isEmpty(wm.alias()) && StringUtils.isEmpty(wm.name())){
					handlers.put(moduleUrl +"/"+ m.getName(), handler);
				}
				
			}
		}
	}

	public static Handler getHandler(String handlerUrl){
		if(!handlers.containsKey(handlerUrl)){
			return null;
		}
		return handlers.get(handlerUrl);
	}
}


