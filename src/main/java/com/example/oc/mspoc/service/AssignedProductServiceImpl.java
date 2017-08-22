package com.example.oc.mspoc.service;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amdocs.cih.services.assignedproduct.lib.AssignedProductHeader;
import com.amdocs.cih.services.assignedproduct.lib.SearchAssignedProductHierarchy;
import com.amdocs.cih.services.assignedproduct.lib.SearchAssignedProductOutput;
import com.example.oc.mspoc.OrderCaptureMsPocApplication;
import com.example.oc.mspoc.model.ServiceOutputWrapper;
import com.example.oc.mspoc.service.legacy.SearchAssignedProductServiceProvider;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class AssignedProductServiceImpl extends OrderCaptureServiceImpl implements AssignedProductService {
	
	@Autowired
	public void setServiceProvider(SearchAssignedProductServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }

	@Override
	public void sendMessageToAPQueue(String id) {
		sendMessage(id, OrderCaptureMsPocApplication.OC_AP_MESSAGE_QUEUE);	
	}
	
	@Override
	public JsonNode searchAssignedProduct(String customerId, boolean mode) {
		return handleServiceCall(customerId, mode);
	}
	
	@Override
	public Object handleServiceResponse(ServiceOutputWrapper outputWrapper) {
		Object returnValue = null;
		if (outputWrapper != null) {    			
    		SearchAssignedProductHierarchy[] assignedProductHierarchies = ((SearchAssignedProductOutput) outputWrapper.getOutput()).getSearchAssignedProductHierarchies();
			Stream<SearchAssignedProductHierarchy> inStream = Arrays.stream(assignedProductHierarchies);	
			Stream<AssignedProductHeader> outStream = inStream.map(item -> {
				return item.getParentAssignedProduct().getAssignedProductHeader();
			});		
			returnValue = outStream.toArray();
		}
		
    	return returnValue;
	}
	
}
