package org.bc.sdak;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.Entity;

import org.bc.sdak.utils.ClassUtil;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class SessionFactoryBuilder {

	private static SessionFactory sf = null;
	private static Configuration cfg = new Configuration();
	private static Map<String,String> settings = new HashMap<String,String>();
	
	public static void applySettings(Map<String,String> settings , Interceptor nterceptor){
		SessionFactoryBuilder.settings = settings;
		if(nterceptor!=null){
			cfg.setInterceptor(nterceptor);
		}
	}
	
	public static void applySettings(Map<String,String> settings){
		applySettings(settings , null);
	}
	
	public synchronized static SessionFactory buildOrGet(){
		if(sf == null){
			for(String key : settings.keySet()){
				cfg.setProperty(key,settings.get(key));
			}
			
			String packages = settings.get("annotated.packages");
			if(packages!=null){
				packages = packages.replace("\n", "");
				addAnnotatedClass(packages.split(";"));
			}
			ServiceRegistry registry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
			sf = cfg.buildSessionFactory(registry);
		}
		return sf;
	}
	
	private static void addAnnotatedClass(String[] packages){
		for(String packageName : packages){
			for(Class<?> clazz : ClassUtil.getClasssFromPackage(packageName)){
				Entity anno = clazz.getAnnotation(Entity.class);
				if(anno==null){
					continue;
				}
				cfg.addAnnotatedClass(clazz);
				System.out.println("annotated class entity" + clazz + " added");
			}
		}
	}
	
}
