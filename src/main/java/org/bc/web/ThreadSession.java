package org.bc.web;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
public class ThreadSession {

	private static final ThreadLocal<HttpSession> HttpSession = new ThreadLocal<HttpSession>();
	
	private static final ThreadLocal<Boolean> superAdmin = new ThreadLocal<Boolean>();
    private ThreadSession() {  
    }  
  
    public static boolean isSuperAdmin() {  
        return superAdmin.get(); 
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
    
}
