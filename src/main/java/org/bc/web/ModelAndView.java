package org.bc.web;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class ModelAndView {

	public String jsp;
	
	public String redirect;
	
	public String contentType;
	
	public JSONObject data = new JSONObject();
	public Map<String,Object> jspData = new HashMap<String,Object>();
	public String returnText = "";
	public boolean encodeReturnText;
	public ModelAndView(){
		
	}
}
