package org.bc.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;

public class GrandFilter implements Filter {

	private String encodeString;
	private static Map<String , CtClass>ctMap = new HashMap<String , CtClass>();

	private UserOfflineHandler userOfflineHandler;
	
	// Filter注销方法
	@Override
	public void destroy() {

	}

	// filter要实现的功能
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 设置字符集
		request.setCharacterEncoding(encodeString);
		response.setCharacterEncoding(encodeString);
		HttpServletRequest req  = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		ThreadSession.setHttpSession(req.getSession());
		ThreadSession.setHttpServletResponse(resp);
		String jspPath = req.getServletPath();
		String path = "";
		boolean isJSP = false;
		if(jspPath.endsWith(".jsp")){
			isJSP = true;
			req.setAttribute("projectName", req.getServletContext().getContextPath());
			path = jspPath.replace(".jsp", "");
		}else{
			path = jspPath.replace("/c/", "/");
			resp.setContentType("text/html");
		}
		
		Handler handler = ModuleManager.getHandler(path);
		if(handler==null){
			chain.doFilter(request, response);
			return;
		}
		Object manager = TransactionalServiceHelper.getTransactionalService(handler.getModuleClass());
		if(manager==null){
			chain.doFilter(request, response);
			return;
		}
		if(StringUtils.isEmpty(handler.getMethod())){
			chain.doFilter(request, response);
			return;
		 }
		try{
			Object[] params = buildParamForMethod(manager,handler.getMethod(),req);
			ModelAndView mv = ServletHelper.call(manager,handler.getMethod(),params);
			if(isJSP){
				mv.jsp = path+".jsp";
			}
			
			if(mv.redirect!=null){
				resp.sendRedirect(mv.redirect);
			}else if(mv.jsp==null){
				if(mv.outputByService==false){
					if(StringUtils.isNotEmpty(mv.contentType)){
						resp.setContentType(mv.contentType);
					}
					if(StringUtils.isNotEmpty(mv.returnText)){
						if(mv.encodeReturnText){
							resp.getWriter().write(Escape.escape(mv.returnText));
						}else{
							resp.getWriter().write(mv.returnText);
						}
					}else{
						resp.getWriter().write(mv.data.toString());
					}
				}else{
					// 由service 负责输出
				}
				
			}else{
				if(mv.outputByService==false){
					ServletHelper.fillMV(req,mv);
					RequestDispatcher rd = req.getRequestDispatcher(mv.jsp);
					if(rd==null){
						resp.setStatus(404);
						resp.getWriter().println("404 : page not found");
					}else{
						rd.forward(req, resp);
					}
				}else{
					// 由service 负责输出
				}
				
			}
		}catch(Exception ex){
			resp.setStatus(500);
			//go to error page 
			if(ex instanceof GException){
				processGException(req,resp, (GException)ex);
			}else if (ex instanceof InvocationTargetException ){
				InvocationTargetException iex = (InvocationTargetException)ex;
				if(iex.getTargetException() instanceof GException ){
					processGException(req,resp, (GException)iex.getTargetException());
				}else{
					LogUtil.log(Level.ERROR,"internal server error",iex.getTargetException());
					iex.getTargetException().printStackTrace(resp.getWriter());
				}
			}else{
				LogUtil.log(Level.ERROR,"internal server error",ex);
				ex.printStackTrace(resp.getWriter());
			}
		}
		// 继续向下执行，如果还有其他filter继续调用其他filter，没有的话将消息发送给服务器或客户端
//		chain.doFilter(request, response);
	}

	// 初始化方法
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//
		encodeString = filterConfig.getInitParameter("encoding");
		String userOfflineHandlerClassName = filterConfig.getInitParameter("userOfflineHandler");
		try {
			userOfflineHandler = (UserOfflineHandler)Class.forName(userOfflineHandlerClassName).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
private void processGException(HttpServletRequest req ,HttpServletResponse resp ,GException ex){
		
		resp.setStatus(400);
		JSONObject jobj = new JSONObject();
		if(ex.getType()==PlatformExceptionType.ParameterMissingError){
			jobj.put("type",PlatformExceptionType.ParameterMissingError.toString());
			jobj.put("field", ex.getField());
		}else if(ex.getType()==PlatformExceptionType.ParameterTypeError){
			jobj.put("type",PlatformExceptionType.ParameterTypeError.toString());
			jobj.put("field", ex.getField());
			jobj.put("msg", ex.getMessage());
		}else if(ex.getType()==PlatformExceptionType.UserOfflineException){
			userOfflineHandler.handle(req ,resp);
		}else{
			jobj.put("msg", ex.getMessage());
		}
		LogUtil.log(Level.WARN, "警告", ex);
		try {
			resp.getWriter().println(jobj.toString());
		} catch (IOException e) {
			LogUtil.log(Level.WARN, "输出错误信息到客户端失败", e);;
		}
	}
	
	private Object[] buildParamForMethod(Object manager,String method, HttpServletRequest req){
		Object[] params = null;
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = ctMap.get(manager.getClass().getName());
		if(cc==null){
			cc = ctMap.get(manager.getClass().getSuperclass().getName());
		}
		if(cc==null){
			String ctcName = manager.getClass().getName();
			try {
				cc = pool.getCtClass(ctcName);
			} catch (NotFoundException e) {
				pool.appendClassPath(new ClassClassPath(manager.getClass()));
			}
			if(cc==null){
				//get again
				try {
					cc = pool.getCtClass(ctcName);
				} catch (NotFoundException ex) {
					try {
						ctcName = manager.getClass().getSuperclass().getName();
						cc = pool.getCtClass(ctcName);
					} catch (NotFoundException e) {
						LogUtil.log(Level.WARN, "class not found", ex);
						return new Object[]{};
					}
				}
			}
			if(cc!=null){
				ctMap.put(ctcName, cc);
			}
		}else{
//			LogUtil.info("we get ctc class from cache");
		}
		
		for(CtMethod cm : cc.getDeclaredMethods()){
			if(cm.getName().equals(method)){
//				LogUtil.info("start to build parameters for "+manager.getClass().getName()+"."+method);
				params = ServletHelper.buildParamters(cm , req);
			}
		}
		return params;
	}
}
