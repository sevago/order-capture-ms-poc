package com.example.oc.mspoc.service;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amdocs.cih.services.orderingactivities.lib.ImplementedOffer;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedOfferFeedback;
import com.amdocs.cih.services.orderingactivities.lib.RetrieveCatalogProductForConfigurationOutput;
import com.example.oc.mspoc.datatype.ServiceOutputWrapper;
import com.example.oc.mspoc.exception.EmptyOutputObjectException;
import com.example.oc.mspoc.service.legacy.RetrieveCatalogDataServiceProvider;
import com.example.oc.mspoc.util.EmptyObject;

@Service
public class CatalogDataServiceImpl extends OrderCaptureServiceImpl implements  CatalogDataService {
	
	@Autowired
	public void setServiceProvider(RetrieveCatalogDataServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }
	
	@Override
	public Object getCatalogOffer(String offerId, boolean mode) {
		return handleServiceCall(offerId, mode);
	}

	@Override
	public Object handleServiceResponse(ServiceOutputWrapper outputWrapper)  {
		if (outputWrapper.getOutput() == EmptyObject.EMPTY_OUTPUT_OBJECT) {
			throw new EmptyOutputObjectException();
		}
		ImplementedOfferFeedback[] offerFeedbacks = ((RetrieveCatalogProductForConfigurationOutput) outputWrapper.getOutput()).getNewProductsForConfiguration();
		Stream<ImplementedOffer> outStream = Arrays.asList(offerFeedbacks).stream().map(item -> {
			return item.getImplementedOffer();
		});
    	return outStream.toArray();
	}

	
}
