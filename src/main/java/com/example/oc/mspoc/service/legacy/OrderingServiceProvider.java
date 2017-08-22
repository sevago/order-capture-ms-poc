package com.example.oc.mspoc.service.legacy;

import com.example.oc.mspoc.model.ServiceOutputWrapper;

import amdocs.oms.connector.Log;

public interface OrderingServiceProvider {
	
	ServiceOutputWrapper invoke(String id);
	
	void logObject(Log clientLog, Object objectToLog);
	
}
