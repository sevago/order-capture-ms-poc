package com.example.oc.mspoc.service;

import com.example.oc.mspoc.datatype.ServiceOutputWrapper;
import com.example.oc.mspoc.exception.EmptyOutputObjectException;
import com.fasterxml.jackson.databind.JsonNode;

public interface OrderCaptureService {
	
	void addInstanceToRepository(String id, Object value);
	
	JsonNode findInstanceInRepository(String id);
	
	Object handleServiceResponse(ServiceOutputWrapper outputWrapper);

	JsonNode handleServiceCall(String id, boolean mode);

	void sendMessage(String id, String queueName);

}
