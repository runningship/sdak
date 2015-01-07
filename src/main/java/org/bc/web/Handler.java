package org.bc.web;

public class Handler{
	Class<?> moduleClass;
	String method;
	
	public Handler(Class<?> clazz,String method){
		this.moduleClass = clazz;
		this.method = method;
	}
	
	public Class<?> getModuleClass(){
		return this.moduleClass;
	}
	
	public String getMethod(){
		return this.method;
	}
}
