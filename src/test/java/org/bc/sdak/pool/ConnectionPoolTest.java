package org.bc.sdak.pool;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SessionFactoryBuilder;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.pool.entity.User;
import org.hibernate.cfg.AvailableSettings;
import org.logicalcobwebs.proxool.ConnectionPoolExceedException;
import org.logicalcobwebs.proxool.ProxoolFacade;

public class ConnectionPoolTest {

	private static void init(){
//		DriverManager.setLogStream(System.out);
		Map<String,String> settings = new HashMap<String , String>();
//		settings.put(AvailableSettings.URL, "jdbc:mysql://localhost:3306/ihouse?characterEncoding=utf-8");
//		settings.put(AvailableSettings.USER, "root");
//		settings.put(AvailableSettings.PASS, "");
		settings.put(AvailableSettings.SHOW_SQL, "false");
		settings.put(AvailableSettings.DRIVER, "com.mysql.jdbc.Driver");
//		settings.put(AvailableSettings.DRIVER, "org.logicalcobwebs.proxool.ProxoolDriver");
		settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
		settings.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
		settings.put(AvailableSettings.HBM2DDL_AUTO, "update");
		settings.put(AvailableSettings.POOL_SIZE, "1");
		
//		settings.put(AvailableSettings.C3P0_MIN_SIZE, "1");
//		settings.put(AvailableSettings.C3P0_MAX_SIZE, "20");
//		settings.put(AvailableSettings.C3P0_TIMEOUT, "1800");//half an hour
//		settings.put(AvailableSettings.C3P0_MAX_STATEMENTS, "10");
		
		//org.hibernate.connection.ProxoolConnectionProvider
		//org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl
		//org.hibernate.service.jdbc.connections.internal.ProxoolConnectionProvider
//		settings.put(AvailableSettings.CONNECTION_PROVIDER, "org.ocean.shibernate.proxool.ProxoolConnectionProvider");
		settings.put(AvailableSettings.PROXOOL_XML, "proxool.xml");//相对目录为classes
		settings.put(AvailableSettings.PROXOOL_EXISTING_POOL, "false");
		settings.put(AvailableSettings.PROXOOL_POOL_ALIAS, "mySqlProxool");
		
		settings.put("annotated.packages", "org.bc.sdak.pool.entity");
		SessionFactoryBuilder.applySettings(settings);
	}
	
	public static void main(String[] args) throws Exception{
		init();
		final CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
		service.listByParams(User.class, "from User", null, null);
//		ConnectionPoolStatisticsIF pool = ProxoolFacade.getConnectionPoolStatistics("mySqlProxool");
//		try {
//			Field field = pool.getClass().getDeclaredField("prototyper");
//			field.setAccessible(true);
//			field.set(pool,new SPrototyper(SPrototyper.getConnectionPool("mySqlProxool")));
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		for(int i=0;i<31;i++){
			new Thread(){
	
				@Override
				public void run() {
					Random r = new Random();
					int t = r.nextInt(300);
					while(true){
						try {
							
							Thread.sleep(t);
						} catch (InterruptedException e) {
							e.printStackTrace();
							break;
						}
						try{
							service.listByParams(User.class, "from User", null, null);
							User user = new User();
							user.uid = UUID.randomUUID().toString();
							service.saveOrUpdate(user);
							service.delete(user);
						}catch(ConnectionPoolExceedException ex){
							System.out.println("wait and re-try...");
						}catch(Exception ex){
							ex.printStackTrace();
						}
						
					}
				}
				
			}.start();
		}
		while(true){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			System.out.println("available count is "+ProxoolFacade.getSnapshot("mySqlProxool").getActiveConnectionCount());
		}
	}
}
