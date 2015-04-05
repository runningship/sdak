package org.bc.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
public class ThreadSession {

	private static final ThreadLocal<HttpSession> HttpSession = new ThreadLocal<HttpSession>();
	
	public static final ThreadLocal<HttpServletRequest> HttpServletRequest = new ThreadLocal<HttpServletRequest>();
	
	private static final ThreadLocal<HttpServletResponse> HttpServletResponse = new ThreadLocal<HttpServletResponse>();
	
	private static final ThreadLocal<Boolean> superAdmin = new ThreadLocal<Boolean>();
	
	private static final ThreadLocal<String> cityPY = new ThreadLocal<String>();
	
	private static final ThreadLocal<String> cityCoordinate = new ThreadLocal<String>();
	
    private ThreadSession() {  
    }  
  
    public static boolean isSuperAdmin() {  
        return superAdmin.get(); 
    }  
  
    public static String getCityPY(){
    	return cityPY.get();
    }
    public static void setCityPY(String city){
    	ThreadSession.cityPY.set(city);
    }
    
    public static String getCityCoordinate(){
    	return cityCoordinate.get();
    }
    public static void setCityCoordinate(String coordinate){
    	ThreadSession.cityCoordinate.set(coordinate);
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
