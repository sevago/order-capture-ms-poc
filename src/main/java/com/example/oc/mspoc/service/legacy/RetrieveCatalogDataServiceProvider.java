package com.example.oc.mspoc.service.legacy;

import java.rmi.RemoteException;

import org.springframework.stereotype.Service;

import com.amdocs.cih.common.core.EntityIDBaseAssist;
import com.amdocs.cih.common.core.sn.ApplicationContext;
import com.amdocs.cih.exception.InvalidUsageException;
import com.amdocs.cih.services.customerprofile.lib.CustomerProfile;
import com.amdocs.cih.services.customerprofile.lib.CustomerProfileID;
import com.amdocs.cih.services.customerprofile.lib.CustomerProfileRef;
import com.amdocs.cih.services.orderingactivities.lib.AvailableOffer;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedOffer;
import com.amdocs.cih.services.orderingactivities.lib.RetrieveCatalogProductForConfigurationInput;
import com.amdocs.cih.services.orderingactivities.lib.RetrieveCatalogProductForConfigurationOutput;
import com.amdocs.cih.services.orderingproductcatalog.lib.OrderingCatalogOffer;
import com.amdocs.cih.services.orderingproductcatalog.lib.OrderingCatalogOfferID;
import com.example.oc.mspoc.model.RetrieveCatalogDataOutputWrapper;

import amdocs.oms.connector.Log;
import amdocs.oms.connector.Message;

@Service
public class RetrieveCatalogDataServiceProvider extends OrderingServiceProviderImpl {
	
	private final static String HIGH_SPEED_OFFER_CID = "50580";
	private final static String OPTIK_TV_OFFER_CID = "21175459";
	private final static String PIK_TV_OFFER_CID = "40937304";
	private final static String CUSTOMER_ID = "10017899";
	
	private String fname = "C:\\oms\\omstests\\log\\" + this.getClass().getName() + "_" + System.currentTimeMillis()+ ".log";
	
	public RetrieveCatalogDataOutputWrapper invoke(String offerId) {
		
		if (ejbService == null) initialize();
		
		Log clientLog = Log.getLog(this.getClass().getName());
		Log.initLog(fname, encoding, 3);
		clientLog.logMessage(1, Message.START, "START Logging");
		
		RetrieveCatalogProductForConfigurationInput  input = new RetrieveCatalogProductForConfigurationInput();
		
		String customerID = CUSTOMER_ID;
		CustomerProfileRef customer = new CustomerProfileRef();
		CustomerProfileID custProfileID = new CustomerProfileID();
		EntityIDBaseAssist.setOMSMasterId(custProfileID, customerID);
		custProfileID.setCustomerId(customerID);
		customer.setCustomerProfileID(custProfileID );		
		CustomerProfile customerProfile = new CustomerProfile();
		customerProfile.setCustomerProfileRef(customer);
		
		String offId = PIK_TV_OFFER_CID;
		OrderingCatalogOfferID orderingCatalogOfferID = new OrderingCatalogOfferID();
		orderingCatalogOfferID.setCatalogOfferID(offId);
		EntityIDBaseAssist.setOMSMasterId(orderingCatalogOfferID, offId);
		OrderingCatalogOffer orderingCatalogOffer = new OrderingCatalogOffer();
		orderingCatalogOffer.setCatalogOfferID(orderingCatalogOfferID);
		AvailableOffer availableOffer = new AvailableOffer();
		availableOffer.setCatalogOffer(orderingCatalogOffer);
		ImplementedOffer implementedOffer = new ImplementedOffer();
		implementedOffer.setAvailableOffer(availableOffer);
		
		input.setCatalogProducts(new ImplementedOffer[1]);
		input.setCatalogProducts(0, implementedOffer);
		input.setCustomerProfile(customerProfile);
		input.setQuotationRequired(false);
		
		RetrieveCatalogProductForConfigurationOutput output = null;
		
		try {
			output = iOmsServicesRemote.retrieveCatalogProductForConfiguration(new ApplicationContext(), ctx, input, null);
		} catch (InvalidUsageException | RemoteException e) {
			e.printStackTrace();
		}
		
		clientLog.logMessage(1, Message.STAY, "Result of retrieveCatalogProductForConfiguration");
	    logObject(clientLog, output);		    
	    clientLog.logMessage(1, Message.STOP, "End Logging");
	    System.out.println("Finsihed");
	    clientLog.flush();
	    
	    return new RetrieveCatalogDataOutputWrapper(output);
	}

}
