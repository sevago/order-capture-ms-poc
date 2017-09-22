package com.example.oc.mspoc.listener;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.oc.mspoc.service.AssignedProductService;

@Component
public class AssignedProductMessageListener {
	
	private static final Logger log = LoggerFactory.getLogger(AssignedProductMessageListener.class);
	
	private AssignedProductService assignedProductService;
    
    @Autowired
	public void setAssignedProductService(AssignedProductService assignedProductService) { this.assignedProductService = assignedProductService; }

    public void receiveMessage(Map<String, String> message) {
        assignedProductService.searchAssignedProduct(message.get("id"), false);
    }
}
