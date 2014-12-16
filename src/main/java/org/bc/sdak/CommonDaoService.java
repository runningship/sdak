package org.bc.sdak;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bc.sdak.utils.LogUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.mysql.jdbc.StringUtils;

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
		try{
			session.delete(obj);
		}catch(PropertyValueException ex){
			throw ex;
		}
		session.flush();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public <T> T get(Class<T> clazz,Serializable id){
		if(id==null){
			return null;
		}
		return (T)getCurrentSession().get(clazz, id);
	}
	
	@Transactional
	public <T> T getUnique(Class<T> clazz,Object obj){
		List<String> keys = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
//		String hql = "from "+obj.getClass().getSimpleName()+" where 1=1 ";
		for(Field f : obj.getClass().getDeclaredFields()){
			f.setAccessible(true);
			Object value =  null;
			try {
				value = f.get(obj);
			} catch (Exception e) {
				LogUtil.warning("get property["+f.getName()+"] value fail for bean "+clazz.getName());
				continue;
			}
			if(value==null){
				continue;
			}
			if("".equals(String.valueOf(value))){
				continue;
			}
			keys.add(f.getName());
			values.add(value);
//			hql += " and "+f.getName()+"=:"+f.getName();
		}
		return getUniqueByParams(clazz,keys.toArray(new String[]{}),values.toArray());
	}
	
	@Transactional
	public int execute(String hql, Object... params ){
		Session session = getCurrentSession();
		Query query = session.createQuery(hql);
		if(params!=null){
			for(int i=0;i<params.length;i++){
				query.setParameter(i, params[i]);
			}
		}
		return query.executeUpdate();
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public <T> List<T> listByParams(Class<T> clazz,String hql,String[] keys, Object[] values){
		Session session = getCurrentSession();
		Query query = session.createQuery(hql);
		if(keys!=null){
			for(int i=0;i<keys.length;i++){
				query.setParameter(keys[i], values[i]);
			}
		}
		return query.list();
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public <T> List<T> listByParams(Class<T> clazz,String hql,Object... values){
		Session session = getCurrentSession();
		Query query = session.createQuery(hql);
		if(values!=null){
			for(int i=0;i<values.length;i++){
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}
	
	@Transactional
	public List<Map> listAsMap(String hql,Object... params){
		Session session = getCurrentSession();
		Query query = session.createQuery(hql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if(params!=null){
			for(int i=0;i<params.length;i++){
				query.setParameter(i, params[i]);
			}
		}
		return query.list();
	}
	
	@Transactional
	public List<Map> listSqlAsMap(String sql,Object... params){
		Session session = getCurrentSession();
		Query query = session.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if(params!=null){
			for(int i=0;i<params.length;i++){
				query.setParameter(i, params[i]);
			}
		}
		return query.list();
	}
	
	@Transactional
	public List<Map> listSql(String sql,Object... params){
		Session session = getCurrentSession();
		Query query = session.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if(params!=null){
			for(int i=0;i<params.length;i++){
				query.setParameter(i, params[i]);
			}
		}
		return query.list();
	}
	
	@Transactional
	public <T> T getUniqueByParams(Class<T> clazz,String[] keys,Object[] values){
		StringBuilder sb  = new StringBuilder("from "+clazz.getSimpleName() + " where 1=1 ");
		for(int i=0;i<keys.length;i++){
			sb.append(" and ").append(keys[i]).append("=:").append(keys[i]);
		}
		List<T> list = listByParams(clazz,sb.toString(),keys,values);
		if(list==null || list.size()==0){
			return null;
		}
		return (T) list.get(0);
	}
	
	@Transactional
	public <T> List<T> listByParams(Class<T> clazz,String[] keys,Object[] values){
		StringBuilder sb  = new StringBuilder("from "+clazz.getSimpleName() + " where 1=1 ");
		if(keys!=null && values!=null){
			for(int i=0;i<keys.length;i++){
				sb.append(" and ").append(keys[i]).append("=:").append(keys[i]);
			}
		}
		List<T> list = listByParams(clazz,sb.toString(),keys,values);
		if(list==null ){
			return new ArrayList<T>();
		}
		return list;
	}
	
	@Transactional
	public <T> T getUniqueByKeyValue(Class<T> clazz,String field,Object value){
		return getUniqueByParams(clazz,new String[]{field} , new Object[]{value});
		
	}
	
	@Transactional
	public <T> Page<T> findPage(Page<T> page, String hql, Object... values){
		return findPage(page,hql , false,values);
	}
	
	@Transactional
	public <T> Page<T> findPage(Page<T> page, String hql,boolean returnMap, Object... values)
	  {
		if(!StringUtils.isNullOrEmpty(page.orderBy)){
			hql+=" order by "+page.orderBy+" "+page.order;
		}
		Session session= getCurrentSession();
	    Query q = createQuery(session,hql, values);
	    if (page.isAutoCount()) {
	      long totalCount = countHqlResult(hql, values);
	      page.setTotalCount(totalCount);
	    }
	    if(returnMap){
	    	q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	    }
	    if(page.getPageSize()==-1){
		    q.setMaxResults((int) page.getTotalCount());
	    }else{
		    q.setMaxResults(page.getPageSize());
	    }
	    q.setFirstResult(page.getFirstOfPage() - 1);
	    page.setResult(q.list());
	    return page;
	  }
	
	@Transactional
	public long countHql(String hql, Object... values)
	  {
	    Long count = Long.valueOf(0L);
	    String fromHql = hql;
	    int orderIndex = fromHql.indexOf("order by");
	    if(orderIndex!=-1){
	    	fromHql = fromHql.substring(0,orderIndex);
	    }

	    String countHql = fromHql;
	    Session session= getCurrentSession();
	    try
	    {
	      count = (Long)createQuery(session,countHql, values).uniqueResult();
	    } catch (Exception e) {
	      throw new RuntimeException("hql can't be auto count, hql is:" + countHql+",params="+values, e);
	    }
	    if(count==null){
	    	return 0;
	    }
	    return count.longValue();
	  }
	
	@Transactional
	public long countHqlResult(String hql, Object... values)
	  {
	    Long count = Long.valueOf(0L);
	    String fromHql = hql;
	    int fromIndex = fromHql.indexOf("from");
	    fromHql = fromHql.substring(fromIndex);
	    int orderIndex = fromHql.indexOf("order by");
	    if(orderIndex!=-1){
	    	fromHql = fromHql.substring(0,orderIndex);
	    }
//	    fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
//	    fromHql = StringUtils.substringBefore(fromHql, "order by");

	    String countHql = "select count(*) " + fromHql;
	    Session session= getCurrentSession();
	    try
	    {
	      count = (Long)createQuery(session,countHql, values).uniqueResult();
	    } catch (Exception e) {
	      throw new RuntimeException("hql can't be auto count, hql is:" + countHql+",params="+values, e);
	    }
	    if(count==null){
	    	return 0;
	    }
	    return count.longValue();
	  }
	
	private Query createQuery(Session session,String queryString, Object... values)
	  {
	    Query query = session.createQuery(queryString);
	    if (values != null) {
	      for (int i = 0; i < values.length; ++i) {
	        query.setParameter(i, values[i]);
	      }
	    }
	    return query;
	  }
	
	private Session getCurrentSession(){
		if(MutilSessionFactoryBuilder.sfm!=null){
			return MutilSessionFactoryBuilder.buildOrGet().getCurrentSession();
		}
		return SessionFactoryBuilder.buildOrGet().getCurrentSession();
	}
}
