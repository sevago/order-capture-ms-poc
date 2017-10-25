package com.example.oc.mspoc.service.legacy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amdocs.cih.common.datatypes.OrderingContext;
import com.amdocs.cih.services.oms.interfaces.IOmsServicesRemote;

import amdocs.oms.connector.Log;
import amdocs.oms.connector.Message;
import amdocs.oms.connector.SerializedObjectLogger;

@Service
public abstract class OrderingServiceProviderImpl implements OrderingServiceProvider {
	
	@Value("${app.oms.uams.user}")
	private String uamsUser;
	
	@Value("${app.oms.uams.password}")
	private String uamsPassword;
	
	@Value("${app.oms.pt61.host}")
	private String omsHost;
	
	@Value("${app.oms.pt61.port}")
	private String omsPort;
	
	@Value("${app.oms.api.log.directory}")
	protected String omsApiLogDirectory;
	
	protected OmsServiceInvoker ejbService;
	protected IOmsServicesRemote iOmsServicesRemote;
	protected OrderingContext ctx;
	
	protected final static String encoding = "UTF-8";
	
	public void initialize() {		
    	try {
    		ejbService = OmsServiceInvoker.getInstance(omsHost, omsPort, uamsUser, uamsPassword);
    		iOmsServicesRemote = ejbService.getServiceInvoker();
			ctx = ejbService.getOrderingContext();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logObject(Log clientLog, Object objectToLog) 
	{
		if (objectToLog == null)
		{
			clientLog.logMessage(Message.DEBUG, Message.NONUM, "null");
		}
		else
		{
			String returnedObjPackageName = objectToLog.getClass().getName();
			String returnedObjName = returnedObjPackageName.substring(returnedObjPackageName.lastIndexOf(".")+1);
			clientLog.logMessage(Message.DEBUG, Message.NONUM, SerializedObjectLogger.serializeObject(returnedObjName, objectToLog));
		}	
	}

}
