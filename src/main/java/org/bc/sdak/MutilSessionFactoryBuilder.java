package org.bc.sdak;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;

import org.bc.sdak.utils.ClassUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class MutilSessionFactoryBuilder {

	public static SessionFactoryMapper sfm =null;
	private static Map<String,SessionFactory> sfMap = new HashMap<String, SessionFactory>();
	
	public synchronized static SessionFactory buildOrGet(){
		String sfKey = sfm.getKey();
		SessionFactory sf = sfMap.get(sfKey);
		if(sf == null){
			Configuration cfg = new Configuration();
			Map<String, String> settings = sfm.getSettings();
			for(String key : settings.keySet()){
				cfg.setProperty(key,settings.get(key));
			}
			String packages = settings.get("annotated.packages");
			if(packages!=null){
				packages = packages.replace("\n", "");
				addAnnotatedClass(cfg , packages.split(";"));
			}
			ServiceRegistry registry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
			sf = cfg.buildSessionFactory(registry);
			sfMap.put(sfKey, sf);
		}
		return sf;
	}
	
	private static void addAnnotatedClass(Configuration cfg ,String[] packages){
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
