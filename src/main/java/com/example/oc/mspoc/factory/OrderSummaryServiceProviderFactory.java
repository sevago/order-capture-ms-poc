package com.example.oc.mspoc.factory;

import com.example.oc.mspoc.factory.OrderingServiceProviderFactory;
import com.example.oc.mspoc.service.legacy.OrderSummaryServiceProvider;
import com.example.oc.mspoc.service.legacy.OrderingServiceProvider;

public class OrderSummaryServiceProviderFactory implements OrderingServiceProviderFactory {

	private static OrderSummaryServiceProvider SERVICE_INSTANCE;
	
	private OrderSummaryServiceProviderFactory() {
		SERVICE_INSTANCE = new OrderSummaryServiceProvider();
	}
	
	private static class SingletonHelper{
        private static final OrderSummaryServiceProviderFactory FACTORY_INSTANCE = new OrderSummaryServiceProviderFactory();
    }
    
    public static OrderSummaryServiceProviderFactory getInstance() {
        return SingletonHelper.FACTORY_INSTANCE;
    }

	@Override
	public OrderingServiceProvider obtainService() {
		return SERVICE_INSTANCE;
	}
    
}
