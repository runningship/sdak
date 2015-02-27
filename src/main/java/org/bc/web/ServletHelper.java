package org.bc.web;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import javax.persistence.Column;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.GException;
import org.bc.sdak.utils.LogUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ServletHelper {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Map<String,Object> getData(HttpServletRequest req) {
		Map<String,Object> map = new HashMap<String,Object>();
		Enumeration<String> names = req.getParameterNames();
		while(names.hasMoreElements()){
			String key = names.nextElement();
			String[] val = req.getParameterValues(key);
			if(val.length==1 && StringUtils.isEmpty(val[0])){
				continue;
			}
			map.put(key, val);
		}
		return map;
	}
	
	
	public static Object[] buildParamters(CtMethod cm,HttpServletRequest req) {
		Map<String, Object> data = ServletHelper.getData(req);
		MethodInfo methodInfo = cm.getMethodInfo();  
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        CtClass[] pTypes = null;
		try {
			pTypes = cm.getParameterTypes();
		} catch (NotFoundException e1) {
			//this should neven happen.
			LogUtil.log(Level.ERROR, "This should not happen at runtime,probably code issue.", e1);
			return new Object[]{};
		}
        Object[] values = new Object[pTypes.length];
        String paramName = "";
        for(int i=0;i<pTypes.length;i++){
        	try {
        		paramName = attr.variableName(i + pos);
        		String[] pval = (String[]) data.get(paramName);
        		Object obj = null;
        		String typeName = pTypes[i].getName();
//				Class<?> pt = Class.forName(pTypes[i].getName());
        		if("int".equals(typeName) || "java.lang.Integer".equals(typeName)){
        			if(pval==null || pval.length==0){
        				if("int".equals(typeName)){
        					throw new GException(PlatformExceptionType.ParameterMissingError,paramName,"parameter "+paramName+" is missing");
        				}else{
        					obj=null;
        				}
        			}else{
        				obj = Integer.valueOf(pval[0]);
        			}
        		}else if("long".equals(typeName) || "java.lang.Long".equals(typeName)){
        			if(pval==null || pval.length==0){
        				throw new GException(PlatformExceptionType.ParameterMissingError, paramName,"parameter "+paramName+" is missing");
        			}
        			obj = Long.valueOf(pval[0]);
        		}else if("float".equals(typeName) || "java.lang.Float".equals(typeName)){
        			if(pval==null || pval.length==0){
        				throw new GException(PlatformExceptionType.ParameterMissingError,paramName,"parameter "+paramName+" is missing");
        			}
        			obj = Float.valueOf(pval[0]);
        		}else if("double".equals(typeName) || "java.lang.Double".equals(typeName)){
        			if(pval==null || pval.length==0){
        				throw new GException(PlatformExceptionType.ParameterMissingError,paramName,"parameter "+paramName+" is missing");
        			}
        			obj = Double.valueOf(pval[0]);
        		}else if("char".equals(typeName) || "java.lang.Character".equals(typeName)){
        			if(pval==null || pval.length==0){
        				throw new GException(PlatformExceptionType.ParameterMissingError,paramName,"parameter "+paramName+" is missing");
        			}
        			obj = String.valueOf(pval[0]).charAt(0);
        		}else if("java.lang.String".equals(typeName)){
        			if(pval==null || pval.length==0){
        				obj="";
        			}else{
        				obj = pval[0];
        			}
        		}else if("java.util.List".equals(typeName)){
        			List list = new ArrayList();
        			Collections.addAll(list, pval);
        			obj = list;
        		}else{
					obj = Class.forName(pTypes[i].getName()).newInstance();
					setValue(obj,data);
				}
        		values[i] = obj;
			}catch(GException ex ){
				throw ex;
			}catch(Exception ex){
				throw new GException(PlatformExceptionType.MethodParameterError,"parameter ("+pTypes[i].getName()
						+ " " + paramName+") of method ("+cm.getDeclaringClass().getName()+"."+cm.getName()
						+") is not support,method type should be primary type or vo",ex);
			}
//        	catch (InstantiationException | IllegalAccessException ex) {
//				LogUtil.log(Level.SEVERE, "please check code or deployment.", ex);
//				return new Object[]{};
//			}
        }
        return values;
	}
	
	private static void setValue(Object obj, Map<String, Object> data) {
		
		for(Field f : obj.getClass().getFields()){
			String pname=f.getName();
			WebParam wparam = f.getAnnotation(WebParam.class);
			if(wparam!=null && StringUtils.isNotEmpty(wparam.name())){
				pname = wparam.name();
			}
			if(!data.containsKey(pname)){
				Column column = f.getAnnotation(Column.class);
				if(column!=null && column.nullable()==false){
					throw new GException(PlatformExceptionType.ParameterMissingError,pname,pname+"不能为空");
				}
			}
			f.setAccessible(true);
			String[] pval = (String[]) data.get(pname);
			if(pval==null){
				continue;
			}
			try {
				if("float".equals(f.getType().getName()) || "java.lang.Float".equals(f.getType().getName())){
					try{
						f.set(obj, Float.valueOf(String.valueOf(pval[0])));
					}catch(Exception ex){
						throw new GException(PlatformExceptionType.ParameterTypeError,pname , "必须是数字类型");
//						throw new GException(PlatformExceptionType.ParameterMissingError,"无效的数据["+pname+"="+pval[0]+"],必须是数字类型");
					}
				}else if("int".equals(f.getType().getName()) || "java.lang.Integer".equals(f.getType().getName())){
					try{
						f.set(obj, Integer.valueOf(String.valueOf(pval[0])));
					}catch(Exception ex){
						throw new GException(PlatformExceptionType.ParameterTypeError,pname , "必须是数字类型");
//						throw new GException(PlatformExceptionType.ParameterMissingError,"无效的数据["+pname+"="+pval[0]+"],必须是数字类型");
					}
				}else if("long".equals(f.getType().getName()) || "java.lang.Long".equals(f.getType().getName())){
					try{
						f.set(obj, Long.valueOf(String.valueOf(pval[0])));
					}catch(Exception ex){
						throw new GException(PlatformExceptionType.ParameterTypeError,pname , "必须是数字类型");
					}
				} else if(Enum.class.equals(f.getType().getSuperclass())){
					if(data.get(pname)!=null){
						Enum enumVal = Enum.valueOf((Class<Enum>)(f.getType()), pval[0]);
						f.set(obj, enumVal);
					}
        		}else if(java.util.List.class.equals(f.getType())){
        			List list = new ArrayList();
        			Collections.addAll(list, pval);
        			f.set(obj, list);
        		}else if(java.util.Date.class.equals(f.getType())){
        			try {
						Date date = sdf.parse(pval[0]);
						f.set(obj, date);
					} catch (ParseException e) {
						try {
							Date date = dateSdf.parse(pval[0]);
							f.set(obj, date);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
        		}else{
        			if(pval.length>1){
        				String str="";
        				for(int i=0;i<pval.length;i++){
        					str+=pval[i];
        					if(i<pval.length-1){
        						str+=",";
        					}
        				}
        				f.set(obj,str);
        			}else{
        				f.set(obj, pval[0]);
        			}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				LogUtil.warning("set value for "+obj.getClass().getName()+"."+pname+" failed.("+e.getMessage()+")");
			}
		}
	}
	
	public static ModelAndView call(Object manager, String method , Object[] data) throws InvocationTargetException {
		try {
			for(Method m : manager.getClass().getDeclaredMethods()){
				if(!m.getName().equals(method)){
					continue;
				}
				Object result = m.invoke(manager,data);
				if(result instanceof ModelAndView){
					return (ModelAndView) result;
				}
			}
			throw new GException(PlatformExceptionType.MethodReturnTypeError,manager.getClass().getName()+"."+method+" does not return a ModelAndView");
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException ex) {
			throw new GException(PlatformExceptionType.ModuleInvokeError, "",ex);
		}
	}

	public static void fillMV(ServletRequest req, ModelAndView mv) {
		for(Object key : mv.data.keySet()){
			req.setAttribute(String.valueOf(key), mv.data.get(key));
		}
		for(Object key : mv.jspData.keySet()){
			req.setAttribute(String.valueOf(key), mv.jspData.get(key));
		}
	}
	
	public static String getModule(String path){
		if(StringUtils.isEmpty(path)){
			return "";
		}
		path = StringUtils.removeEnd(path, "/");
		return path;
//		return path.substring(path.lastIndexOf("/"));
	}
	
}
