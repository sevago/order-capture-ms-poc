package com.example.oc.mspoc.service.legacy;

import com.example.oc.mspoc.datatype.ServiceOutputWrapper;

import amdocs.oms.connector.Log;

public interface OrderingServiceProvider {
	
	void initialize();
	
	ServiceOutputWrapper invoke(String id);
	
	void logObject(Log clientLog, Object objectToLog);
	
}
