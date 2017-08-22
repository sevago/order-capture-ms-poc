package com.example.oc.mspoc.factory;

import com.example.oc.mspoc.service.legacy.OrderingServiceProvider;

public interface OrderingServiceProviderFactory {
	
	/**
	 * obtain ordering service
	 * @return ordering service instance, or {@code null} if none
	 */
	OrderingServiceProvider obtainService();
	
}
