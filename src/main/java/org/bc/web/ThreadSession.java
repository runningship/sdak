package org.bc.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class ThreadSession {

	private static final ThreadLocal<HttpSession> HttpSession = new ThreadLocal<HttpSession>();
	
	private static final ThreadLocal<HttpServletResponse> HttpServletResponse = new ThreadLocal<HttpServletResponse>();
	
	private static final ThreadLocal<Boolean> superAdmin = new ThreadLocal<Boolean>();
	
	private static final ThreadLocal<String> domain = new ThreadLocal<String>();
    private ThreadSession() {  
    }  
  
    public static boolean isSuperAdmin() {  
        return superAdmin.get(); 
    }  
  
    public static String getDomain(){
    	return domain.get();
    }
    public static void setDomain(String domain){
    	ThreadSession.domain.set(domain);
    }
    public static void setSuperAdminr(boolean sup) {  
    	superAdmin.set(sup);
    }
    
    public static HttpSession getHttpSession(){
    	return HttpSession.get();
    }
    
    public static void setHttpSession(HttpSession session){
    	HttpSession.set(session);
    }
    
    public static void setHttpServletResponse(HttpServletResponse response){
    	HttpServletResponse.set(response);
    }

	public static HttpServletResponse getHttpservletresponse() {
		return HttpServletResponse.get();
	}
    
}
