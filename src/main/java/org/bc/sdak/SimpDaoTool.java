package org.bc.sdak;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;

public class SimpDaoTool {
	private static CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public static CommonDaoService getGlobalCommonDaoService(){
		return service;
	}
}
