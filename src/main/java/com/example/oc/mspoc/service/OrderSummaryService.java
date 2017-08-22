package com.example.oc.mspoc.service;

public interface OrderSummaryService {
	
	Object retrieveOrderSummary(String id, boolean mode);
	
	void sendMessageToOSQueue(String id);

}
