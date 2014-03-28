package org.bc.sdak;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 一个基础的包括了增删改查操作的service,支持范型.该service试图包含所有真正的hibernate操作，使的上层service无需面对hibernate.
 * 该service定位为最小单元的serice,提供了最小单元的dao操作，原则上其他复杂service的复杂方法可以通过多个CommonDaoService的方法组成。
 * 当然这里的复杂service的复杂方法指的是有些方法需要多个dao操作才完成一个业务。
 * CommonDaoService的每一个方法都加了@Transactional，意味这开启了事务。但是你需要使用TransactionalServiceHelper来获取一个CommonDaoService实例，
 * 才能是事务生效。
 * @author xzye
 *
 */
public class CommonDaoService {

	protected CommonDaoService(){
		
	}
	
	@Transactional
	public void saveOrUpdate(Object obj){
		Session session = getCurrentSession();
		session.saveOrUpdate(obj);
		session.flush();
	}
	
	@Transactional
	public void delete(Object obj){
		Session session = getCurrentSession();
		session.delete(obj);
		session.flush();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public <T> T get(Class<T> clazz,String id){
		return (T)getCurrentSession().get(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public <T> List<T> listByParams(Class<T> clazz,String hql,String[] keys, Object[] values){
		Session session = getCurrentSession();
		Query query = session.createQuery(hql);
		if(keys!=null){
			for(int i=0;i<keys.length;i++){
				query.setParameter(keys[i], values[i]);
			}
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return query.list();
	}
	
	@Transactional
	public <T> T getUniqueByParams(Class<T> clazz,String hql,String[] keys,Object[] values){
		List<T> list = listByParams(clazz,hql,keys,values);
		if(list==null || list.size()==0){
			return null;
		}
		return (T) list.get(0);
	}
	
	public <T> T getUniqueByKeyValue(Class<T> clazz,String field,Object value){
		return getUniqueByParams(clazz,"from "+clazz.getSimpleName() + " where "+ field +" = :"+field, new String[]{field} , new Object[]{value});
		
	}
	
	private Session getCurrentSession(){
		return SessionFactoryBuilder.buildOrGet().getCurrentSession();
	}
}
