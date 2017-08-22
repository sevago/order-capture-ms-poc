package com.example.oc.mspoc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amdocs.cih.services.order.lib.RetrieveOrderSummaryOutput;
import com.example.oc.mspoc.OrderCaptureMsPocApplication;
import com.example.oc.mspoc.model.ServiceOutputWrapper;
import com.example.oc.mspoc.service.legacy.OrderSummaryServiceProvider;

@Service
public class OrderSummaryServiceImpl extends OrderCaptureServiceImpl implements OrderSummaryService {
	
	@Autowired
	public void setServiceProvider(OrderSummaryServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }

	@Override
	public void sendMessageToOSQueue(String id) {
		sendMessage(id, OrderCaptureMsPocApplication.OC_OS_MESSAGE_QUEUE);	
	}
	
	@Override
	public Object retrieveOrderSummary(String orderId, boolean mode) {
		return handleServiceCall(orderId, mode);
	}
	
	@Override
	public Object handleServiceResponse(ServiceOutputWrapper outputWrapper) {
		Object returnValue = null;
		if (outputWrapper != null) {    				
			returnValue = ((RetrieveOrderSummaryOutput) outputWrapper.getOutput()).getOrderSummary()[0].getOrderHeader();
		}
    	
    	return returnValue;
	}
	
}
