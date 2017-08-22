package com.example.oc.mspoc.service;

public interface AssignedProductService {
	
	Object searchAssignedProduct(String customer, boolean mode);
	
	void sendMessageToAPQueue(String id);

}
