package org.bc.sdak;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.hibernate.Session;

/**
 * TransactionalServiceHelper主要构建一个service的动态代理类，基于注解来管理事务。
 * 可以很好处理service方法相互调用时嵌套事务问题是TransactionalServiceHelper主要目的。
 * 如有serviceA.methodA1,serivceB.methodB1,serviceC.methodC1, B1和C1都有各自的事务，这时A1要连续调用B1,C1并且希望这个是一个大事务，
 * 此时只需要在serviceA.methodA1上加上@Transactional即可。TransactionalServiceHelper所返回service代理类会自动开启最外层事务而关闭
 * 内部事务.
 * @author xzye
 *
 */
public class TransactionalServiceHelper implements MethodInterceptor{

	@SuppressWarnings("unchecked")
	public static <T> T getTransactionalService(Class<T> clazz){
		TransactionalServiceHelper dh = new TransactionalServiceHelper();
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(dh);
		return (T) enhancer.create();
	}

	public Object intercept(Object obj, Method method, Object[] args,MethodProxy target) throws Throwable {
		boolean needTransaction = method.getAnnotation(Transactional.class)==null ? false:true;
		if(needTransaction == false){
			return target.invokeSuper(obj, args);
		}
		Session session = SessionFactoryBuilder.buildOrGet().getCurrentSession();
		boolean inParentTrans = session.getTransaction().isParticipating();
		
		if(inParentTrans==false){
			session.beginTransaction();
			//System.out.println("begin transaction");
		}
		try{
			Object result = target.invokeSuper(obj, args);
			if(inParentTrans==false){
				session.getTransaction().commit();
				//System.out.println("commit transaction");
			}
			return result;
		}catch(Exception ex){
			ex.printStackTrace();
			session.getTransaction().rollback();
			System.out.println("rollback transaction");
			throw ex;
		}
	}
}
