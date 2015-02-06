package org.bc.web;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public interface UserOfflineHandler {

	public void handle(HttpServletRequest req ,ServletResponse response);
}
