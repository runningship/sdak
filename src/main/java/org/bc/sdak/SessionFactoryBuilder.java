package org.bc.sdak;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;

import org.bc.sdak.utils.ClassUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class SessionFactoryBuilder {

	private static SessionFactory sf = null;
	private static Configuration cfg = null;
	private static Map<String,String> settings = new HashMap<String,String>();
	
	public static void applySettings(Map<String,String> settings){
		SessionFactoryBuilder.settings = settings;
	}
	
	public static SessionFactory buildOrGet(){
		if(sf == null){
			cfg = new Configuration();
			for(String key : settings.keySet()){
				cfg.setProperty(key,settings.get(key));
			}
			String packages = settings.get("annotated.packages");
			if(packages!=null){
				packages = packages.replace("\n", "");
				addAnnotatedClass(packages.split(";"));
			}
//			cfg.setProperty(AvailableSettings.URL, "jdbc:mysql://localhost:3306/herb");
//			cfg.setProperty(AvailableSettings.USER, "root");
//			cfg.setProperty(AvailableSettings.PASS, "admin");
//			cfg.setProperty(AvailableSettings.SHOW_SQL, "true");
//			cfg.setProperty(AvailableSettings.DRIVER,"com.mysql.jdbc.Driver");
//			cfg.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
//			cfg.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//			cfg.setProperty(AvailableSettings.HBM2DDL_AUTO, "update");
			//cfg.configure();
//			cfg.addAnnotatedClass(Herb.class);
//			cfg.addAnnotatedClass(HerbProperty.class);
//			cfg.addAnnotatedClass(Prescription.class);
//			cfg.addAnnotatedClass(EffectIngredient.class);
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
