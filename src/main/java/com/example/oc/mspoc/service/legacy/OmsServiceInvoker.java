package com.example.oc.mspoc.service.legacy;

import amdocs.uams.UamsNotFoundException;
import amdocs.uams.UamsPasswordCredential;
import amdocs.uams.UamsSecurityException;
import amdocs.uams.UamsSystem;
import amdocs.uams.login.direct.UamsDirectLoginContext;
import amdocs.uamsimpl.client.login.direct.DirectLoginServiceWrapper;
import amdocs.uamsx.external.ActivityNotSupportedException;
import amdocs.uamsx.external.OperationException;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.amdocs.cih.common.core.CihFilterInfo;
import com.amdocs.cih.common.core.CihSortInfo;
import com.amdocs.cih.common.core.CihStringOperator;
import com.amdocs.cih.common.core.EntityIDBaseAssist;
import com.amdocs.cih.common.core.MaskInfo;
import com.amdocs.cih.common.core.PaginationInfo;
import com.amdocs.cih.common.core.sn.ApplicationContext;
import com.amdocs.cih.common.datatypes.OrderingContext;
import com.amdocs.cih.common.datatypes.ValidValue;
import com.amdocs.cih.common.datatypes.ValidationFeedback;
import com.amdocs.cih.exception.DataNotFoundException;
import com.amdocs.cih.exception.EntityKeyNotFoundException;
import com.amdocs.cih.exception.IllegalEntityStateException;
import com.amdocs.cih.exception.InvalidUsageException;
import com.amdocs.cih.exception.UnauthorizedAccessException;
import com.amdocs.cih.services.assignedproduct.lib.APEnitityIDBaseIDVersionAssist;
import com.amdocs.cih.services.assignedproduct.lib.AssignedOfferID;
import com.amdocs.cih.services.assignedproduct.lib.AssignedOfferRef;
import com.amdocs.cih.services.assignedproduct.lib.AssignedProductHierarchyRef;
import com.amdocs.cih.services.assignedproduct.lib.AssignedProductID;
import com.amdocs.cih.services.assignedproduct.lib.AssignedProductRef;
import com.amdocs.cih.services.assignedproduct.lib.RetrieveAssignedProductHierarchyInput;
import com.amdocs.cih.services.assignedproduct.lib.SearchAssignedProductHierarchy;
import com.amdocs.cih.services.assignedproduct.lib.SearchAssignedProductOutput;
import com.amdocs.cih.services.customerprofile.lib.CustomerProfileHeader;
import com.amdocs.cih.services.customerprofile.lib.CustomerProfileID;
import com.amdocs.cih.services.customerprofile.lib.CustomerProfileRef;
import com.amdocs.cih.services.oms.exceptions.ConsumerOutOfSyncException;
import com.amdocs.cih.services.oms.exceptions.EntityLockedException;
import com.amdocs.cih.services.oms.exceptions.ExternalSystemException;
import com.amdocs.cih.services.oms.exceptions.IllegalNavigationException;
import com.amdocs.cih.services.oms.exceptions.OperationTimedOutException;
import com.amdocs.cih.services.oms.exceptions.OrderNegotiationCompletedException;
import com.amdocs.cih.services.oms.interfaces.IOmsServicesRemote;
import com.amdocs.cih.services.oms.interfaces.IOmsServicesRemoteHome;
import com.amdocs.cih.services.oms.lib.CloseStepInput;
import com.amdocs.cih.services.oms.lib.LogoutRequest;
import com.amdocs.cih.services.oms.lib.NavigationOutputBase;
import com.amdocs.cih.services.oms.lib.NextStepInput;
import com.amdocs.cih.services.oms.lib.PreviousStepInput;
import com.amdocs.cih.services.oms.lib.RetrieveAssignedProductHierarchyOutput;
import com.amdocs.cih.services.oms.lib.SaveStepDataInput;
import com.amdocs.cih.services.oms.lib.SearchFilterInfo;
import com.amdocs.cih.services.oms.lib.SearchInfo;
import com.amdocs.cih.services.oms.lib.SearchOrderOutput;
import com.amdocs.cih.services.oms.lib.SelectionCriteria;
import com.amdocs.cih.services.oms.lib.StartOrderInput;
import com.amdocs.cih.services.oms.rvt.domain.ActivityNameRVT;
import com.amdocs.cih.services.oms.rvt.domain.ImplementedProductActionRVT;
import com.amdocs.cih.services.oms.rvt.domain.OrderActionTypeRVT;
import com.amdocs.cih.services.oms.rvt.domain.ReferenceRelationActionRVT;
import com.amdocs.cih.services.oms.rvt.domain.ReplaceOfferOperationRVT;
import com.amdocs.cih.services.oms.rvt.domain.RuleValidationStageRVT;
import com.amdocs.cih.services.oms.rvt.referencetable.OrderActionReasonRVT;
import com.amdocs.cih.services.oms.rvt.referencetable.SalesChannelRVT;
import com.amdocs.cih.services.order.lib.CancelOfferData;
import com.amdocs.cih.services.order.lib.CancelOrderFromProcessInput;
import com.amdocs.cih.services.order.lib.CreateOrderInput;
import com.amdocs.cih.services.order.lib.CreateOrderOutput;
import com.amdocs.cih.services.order.lib.Order;
import com.amdocs.cih.services.order.lib.OrderDataSettingOptions;
import com.amdocs.cih.services.order.lib.OrderDetails;
import com.amdocs.cih.services.order.lib.OrderID;
import com.amdocs.cih.services.order.lib.OrderRef;
import com.amdocs.cih.services.order.lib.ReplaceOfferOrderActionsSummary;
import com.amdocs.cih.services.order.lib.RetrieveAssignedProductForConfigurationInput;
import com.amdocs.cih.services.order.lib.RetrieveAssignedProductForConfigurationOutput;
import com.amdocs.cih.services.order.lib.RetrieveOrderSummaryInput;
import com.amdocs.cih.services.order.lib.RetrieveOrderSummaryOutput;
import com.amdocs.cih.services.order.lib.UpdateOrderDetailsInput;
import com.amdocs.cih.services.order.lib.UpdateOrderInput;
import com.amdocs.cih.services.order.lib.UpdateOrderOutput;
import com.amdocs.cih.services.orderaction.lib.OrderAction;
import com.amdocs.cih.services.orderaction.lib.OrderActionData;
import com.amdocs.cih.services.orderaction.lib.OrderActionDetails;
import com.amdocs.cih.services.orderaction.lib.OrderActionID;
import com.amdocs.cih.services.orderaction.lib.OrderActionRef;
import com.amdocs.cih.services.orderaction.lib.OrderActionSummary;
import com.amdocs.cih.services.orderaction.lib.RetrieveOrderActionInput;
import com.amdocs.cih.services.orderaction.lib.RetrieveOrderActionOutput;
import com.amdocs.cih.services.orderaction.lib.UpdateOrderActionInput;
import com.amdocs.cih.services.orderingactivities.lib.AvailableOffer;
import com.amdocs.cih.services.orderingactivities.lib.AvailableProduct;
import com.amdocs.cih.services.orderingactivities.lib.AvailableReferenceRelation;
import com.amdocs.cih.services.orderingactivities.lib.BaseSDO;
import com.amdocs.cih.services.orderingactivities.lib.CheckOrderedProductCompatibilityInput;
import com.amdocs.cih.services.orderingactivities.lib.CheckOrderedProductCompatibilityOutput;
import com.amdocs.cih.services.orderingactivities.lib.CompatibilityChecksOptions;
import com.amdocs.cih.services.orderingactivities.lib.CompatibilityRulesInput;
import com.amdocs.cih.services.orderingactivities.lib.GetReplaceOffersAndProductsAvailableOperationsInput;
import com.amdocs.cih.services.orderingactivities.lib.GetReplaceOffersAndProductsAvailableOperationsOutput;
import com.amdocs.cih.services.orderingactivities.lib.GetReplaceOffersResultsInput;
import com.amdocs.cih.services.orderingactivities.lib.GetReplaceOffersResultsOutput;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedAttribute;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedOffer;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedPricingReference;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedProduct;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedProductFeedback;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedReferenceRelation;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedTopLevelProductDetails;
import com.amdocs.cih.services.orderingactivities.lib.NegotiateInstallationAddressSDO;
import com.amdocs.cih.services.orderingactivities.lib.OffersAndProductsSDO;
import com.amdocs.cih.services.orderingactivities.lib.OrderingAttributeDisplayInformation;
import com.amdocs.cih.services.orderingactivities.lib.ProductConfigurationSDO;
import com.amdocs.cih.services.orderingactivities.lib.ReplaceOfferInfo;
import com.amdocs.cih.services.orderingactivities.lib.ReplaceOfferProductData;
import com.amdocs.cih.services.orderingactivities.lib.ReplaceOfferProductResults;
import com.amdocs.cih.services.orderingactivities.lib.ReplaceOffersAndProductsOperation;
import com.amdocs.cih.services.orderingactivities.lib.ReplaceOffersData;
import com.amdocs.cih.services.orderingactivities.lib.ReplaceOffersSDO;
import com.amdocs.cih.services.orderingactivities.lib.RetrieveCatalogProductForConfigurationInput;
import com.amdocs.cih.services.orderingactivities.lib.RetrieveCatalogProductForConfigurationOutput;
import com.amdocs.cih.services.orderingactivities.lib.SearchNegotiatedOffersAndProductsOutput;
import com.amdocs.cih.services.orderingactivities.lib.SearchReplaceableOffersOutput;
import com.amdocs.cih.services.orderingactivities.lib.SearchReplaceableOffersResults;
import com.amdocs.cih.services.orderingproductcatalog.lib.OrderingAttributeDataTypeDetails;
import com.amdocs.cih.services.orderingproductcatalog.lib.OrderingCatalogAttribute;
import com.amdocs.cih.services.orderingproductcatalog.lib.OrderingCatalogOffer;
import com.amdocs.cih.services.orderingproductcatalog.lib.OrderingCatalogOfferID;
import com.amdocs.cih.services.orderingproductcatalog.lib.OrderingCatalogPricing;
import com.amdocs.cih.services.processmanager.lib.OrderingProcessGroupInstanceDataObject;
import com.amdocs.cih.services.processmanager.lib.OrderingProcessInstanceDataObject;
import com.amdocs.cih.services.processmanager.lib.StepInstance;
import com.example.oc.mspoc.model.legacy.Offer;
import com.example.oc.mspoc.model.legacy.OrderView;
import com.example.oc.mspoc.model.legacy.Product;
import com.example.oc.mspoc.util.legacy.ConfigureProduct;
import com.example.oc.mspoc.util.legacy.ViewHandler;

public class OmsServiceInvoker {
	public IOmsServicesRemote serviceInvoker;

	DirectLoginServiceWrapper svc;
	
	InitialContext ic = null;
	
	AvailableOffer availableOffers[];

	NegotiateInstallationAddressSDO negotiateInstallationAddressSDO;
	NegotiateInstallationAddressSDO negotiateInstallationAddressSDOs[];
	
	OffersAndProductsSDO offersAndProductsSDO;
	
	NavigationOutputBase 	currentStepOutput;
	
	NextStepInput 			nextStepInput;
	OrderID oId = null ;
	Map<String, OrderingAttributeDataTypeDetails> atrDTMap = new HashMap<String, OrderingAttributeDataTypeDetails>();
	
	//NextStepOutput 		nextStepOutput; 
	public String tkt="EXT<TksmauKZueiOgZ2I4}K5kYJMc8Bxl7Thq5eT7E0;appId=CC;>";
	

	public String customer_id = "3002";
	String addressId = "10";
	String siteId = "10";
	public String OrderId = "200006705" ;
	String orderActionId = "18666" ;
	String serviceID= "6044326656";
	String productApId = "400099375";
	String offerNames[] = {"Business One"};
	int offerQuantity = 1;
	String apId;
	List<AssignedProductID> apIdList = new ArrayList<AssignedProductID>();
		
	String CATALOG_OFFERS_CN = "CatalogOffers";
	String EXISTING_TARGET_OFFERS_CN = "ExistingTargetOffers";
	String EXISTING_SOURCE_OFFERS_CN = "ExistingSourceOffers";
	
	private String omsHost;
	private String omsPort;
	private String uamsUser;
	private String uamsPass;
	
	public List<Offer> offerList = new LinkedList<Offer>(); 
	
	public boolean isConfigurationHandled = false;
	
	private static OmsServiceInvoker si= null;
	
	private OmsServiceInvoker(String omsHost, String omsPort, String uamsUser, String uamsPass) {		
		this.omsHost = omsHost;
		this.omsPort = omsPort;
		this.uamsUser = uamsUser;
		this.uamsPass = uamsPass;
	}
	
	public synchronized static OmsServiceInvoker getInstance(String omsHost, String omsPort, String uamsUser, String uamsPass) throws Exception{
		if (si == null) {
			System.setProperty("SEC_SRV_CONN", omsHost +":" + omsPort);
			System.setProperty("amdocs.uams.config.resource", "res/gen/client");
			System.setProperty("java.security.auth.login.config", "C:\\workspace\\soc9\\WebContent\\WEB-INF\\lib\\wl_jaas.config");
			si = new OmsServiceInvoker(omsHost, omsPort, uamsUser, uamsPass);
			si.login();			
		}
		return si;
	}
	
	

	public void logout() 
	{
		DirectLoginServiceWrapper svc = null;
		try 
		{
			svc = (DirectLoginServiceWrapper) UamsSystem.getService(null, UamsSystem.LN_UAMS_DIRECT_LOGIN);
		} 
		catch (UamsSecurityException e1) 
		{
			e1.printStackTrace();
		} 
		catch (UamsNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		svc.logout(tkt);	
		
		LogoutRequest logoutReq =  new LogoutRequest();
		logoutReq.setSecurityToken(tkt);
		try 
		{
			serviceInvoker.logout(new ApplicationContext(),getOrderingContext(), logoutReq,null);
			
			System.out.println("Logout successful");
		} 
		catch (InvalidUsageException e) 
		{
			e.printStackTrace();
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}		
	}

	
	
	public void saveStepData() {
		
		SaveStepDataInput input = new SaveStepDataInput();
		
		input.setSharedStepInstancesData(nextStepInput.getSharedStepInstancesData());
		input.setStepInstances(nextStepInput.getStepInstances());
		input.setStepContext(nextStepInput.getStepContext());
		
		try {
			 currentStepOutput  = serviceInvoker.saveStepData(
							new ApplicationContext(),
							getOrderingContext(), 
							input, 
							null);

			System.out.println("saveStepData completed successfully");
			handleStep();
			
		} catch (IllegalNavigationException e) {
			
			e.printStackTrace();
		} catch (InvalidUsageException e) {
			
			e.printStackTrace();
		} catch (EntityLockedException e) {
			
			e.printStackTrace();
		} catch (ConsumerOutOfSyncException e) {
			
			e.printStackTrace();
		} catch (OrderNegotiationCompletedException e) {
			
			e.printStackTrace();
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}

		System.out.println("saveStepData successfully completed");
			

	}
	
	private ImplementedOffer[] createImplProdcore() {
		
		
		ImplementedOffer[] impOfferList= new ImplementedOffer[offerList.size()];
		
		List<Offer> offerList = this.offerList;
		int i = 0;
		for (Offer offerDetails : offerList) {
			
			AvailableOffer offer= getAvailableOffer(offerDetails.getName());
			
			ImplementedOffer impOffer =  new ImplementedOffer();
			impOffer.setAvailableOffer(offer);
			
			impOffer.setTemporaryID(getNewID());
			
			List<Product> productList = offerDetails.getProductList();
			
			List<ImplementedProduct> lmplementedProductList = new ArrayList<ImplementedProduct>();
			
			for (Product product : productList) {
				
				AvailableProduct avaliableProduct = getAvaliableProduct(product.getName(),offer);
				
				//for (int k = 0; k < product.getCount(); k++) {
				
					ImplementedProduct impProduct =  new ImplementedProduct();
					impProduct.setAvailableProduct(avaliableProduct); 
					
					impProduct.setActualBulkQuantity(0);
					
					ImplementedTopLevelProductDetails topLevelProductDetails = new ImplementedTopLevelProductDetails();
					topLevelProductDetails.setImplementedOffer(impOffer);
					topLevelProductDetails.setInstancesQuantity(1);
					impProduct.setImplementedTopLevelProductDetails(topLevelProductDetails);
					impProduct.setTemporaryID(getNewID());
					
					
					lmplementedProductList.add(impProduct);
					
				//}
				
			}
			
			ImplementedProduct [] implementedProductArr = new ImplementedProduct[lmplementedProductList.size()];
			impOffer.setChildImplementedProducts(lmplementedProductList.toArray(implementedProductArr));
			
			impOfferList[i] = impOffer;
			i++;
		}
		
		
		
		return impOfferList;
				
	}
	
	private AvailableProduct getAvaliableProduct(String name,
			AvailableOffer offer) {
		
		for (AvailableProduct availableProduct : offer.getChildAvailableProducts()) {
			
			if (name.equals(availableProduct.getCatalogProduct().getCatalogProductDetails().getCatalogProductCode())){
				return availableProduct;
			}
			
		}
		
		return null;
	}

	private AvailableOffer getAvailableOffer(String offerNames2) {
		AvailableOffer offer = null;
		for( int i =0; i<availableOffers.length;i++){
			if(availableOffers[i].getCatalogOffer().getCatalogOfferDetails().getName()
					.getLocalizedValue().equals(offerNames2)){
				offer = availableOffers[i]; 
				break;
			}
		}
		return offer;
	}

	public ValidationFeedback getNextStep() {
		
		ValidationFeedback validationFeedback = null;
		
		if(currentStepOutput.getNavigationResult().getAvailableNavigationActions() == null){
			System.out.println("No more Next steps");
			return validationFeedback;
		}
		else{
			for(int i =0 ; i< currentStepOutput.getNavigationResult().getAvailableNavigationActions().length;i++){
				if("NX".equalsIgnoreCase(currentStepOutput.getNavigationResult().getAvailableNavigationActions()[i].getAction().getLocalizedValue())){
					if(!currentStepOutput.getNavigationResult().getAvailableNavigationActions()[i].isAllowed()){
						System.out.println("Next not allowed in this activity");
						return validationFeedback;
					}
				}
			}
		}
		try {
			currentStepOutput  = serviceInvoker.getNextStep(
							new ApplicationContext(),
							getOrderingContext(), 
							nextStepInput, 
							null);
			System.out.println("getNextStep successfully completed");
			handleStep();
						

			
		} catch (IllegalNavigationException e) {
			
			e.printStackTrace();
		} catch (InvalidUsageException e) {
			
			e.printStackTrace();
		} catch (EntityLockedException e) {
			
			e.printStackTrace();
		} catch (ConsumerOutOfSyncException e) {
			
			e.printStackTrace();
		} catch (OrderNegotiationCompletedException e) {
			
			e.printStackTrace();
		} catch (RemoteException e) {
			
			e.printStackTrace();
		} catch (OperationTimedOutException e) {
			
			e.printStackTrace();
		} catch (UnauthorizedAccessException e) {
			
			e.printStackTrace();
		}

		
		if (currentStepOutput.getNavigationResult() != null) {
			validationFeedback = currentStepOutput.getNavigationResult().getMessages();
		}
		
		return validationFeedback;
		
	}
	
	
	public void getPreviousStep() {
		
		if(currentStepOutput.getNavigationResult().getAvailableNavigationActions() == null){
			System.out.println("No more Prev steps");
			return;
		}
		else{
			for(int i =0 ; i< currentStepOutput.getNavigationResult().getAvailableNavigationActions().length;i++){
				if("PV".equalsIgnoreCase(currentStepOutput.getNavigationResult().getAvailableNavigationActions()[i].getAction().getLocalizedValue())){
					if(!currentStepOutput.getNavigationResult().getAvailableNavigationActions()[i].isAllowed()){
						System.out.println("Next not allowed in this activity");
						return;
					}
				}
			}
		}
		PreviousStepInput input =  new PreviousStepInput();
		input.setConfirmationChecksApproved(true);
		ValidValue option = new ValidValue();
        option.setCode("SOV");
		input.setSaveOptions(option);
		input.setSharedStepInstancesData(nextStepInput.getSharedStepInstancesData());
		input.setStepContext(nextStepInput.getStepContext());
		input.setStepInstances(nextStepInput.getStepInstances());
		
		try {
			currentStepOutput  = serviceInvoker.getPreviousStep(
							new ApplicationContext(),
							getOrderingContext(), 
							input, 
							null);
			System.out.println("getPreviousStep successfully completed");
			handleStep();
			
		} catch (IllegalNavigationException e) {
			
			e.printStackTrace();
		} catch (InvalidUsageException e) {
			
			e.printStackTrace();
		} catch (EntityLockedException e) {
			
			e.printStackTrace();
		} catch (ConsumerOutOfSyncException e) {
			
			e.printStackTrace();
		} catch (OrderNegotiationCompletedException e) {
			
			e.printStackTrace();
		} catch (RemoteException e) {
			
			e.printStackTrace();
		} 

		
			
		
	}



	public String login() throws RemoteException,
			ActivityNotSupportedException, OperationException,
			UamsSecurityException, UamsNotFoundException {

		String loginStr = UamsSystem.LN_UAMS_DIRECT_LOGIN;
		DirectLoginServiceWrapper directLoginWrapper = (DirectLoginServiceWrapper) UamsSystem
				.getService(null, loginStr);
		UamsDirectLoginContext loginContext = (UamsDirectLoginContext) directLoginWrapper
				.login(uamsUser, uamsPass);
		tkt = loginContext.getTicket();

		return tkt;

	}
	
	public void renewTicket() throws RemoteException,
		ActivityNotSupportedException, OperationException,
		UamsSecurityException, UamsNotFoundException{
		
		String loginStr = UamsSystem.LN_UAMS_DIRECT_LOGIN;
		DirectLoginServiceWrapper directLoginWrapper = (DirectLoginServiceWrapper) UamsSystem
				.getService(null, loginStr);
		
		 if (!directLoginWrapper.isValidTicket(tkt)) {
			 logout();
			 login();
		 }
	}
	

	public void startOrder()  {

		com.amdocs.cih.services.order.lib.Order omsOrder = new com.amdocs.cih.services.order.lib.Order();

		StartOrderInput input = new StartOrderInput();
		input.setOrder(omsOrder);
		
		CustomerProfileHeader customer =  new CustomerProfileHeader();
		CustomerProfileID customerId = new CustomerProfileID();
		customerId.setCustomerId(customer_id);
		customer.setCustomerProfileID(customerId);
		omsOrder.setCustomerProfileHeader(customer);
		
//		if(oId != null)
//			omsOrder.setOrderID(oId);
		
		OrderActionData orderActionData = new OrderActionData();
		OrderAction orderAction = new OrderAction();
		OrderActionDetails orderActionDetails = new OrderActionDetails();
		OrderActionTypeRVT actionType = new OrderActionTypeRVT("PR");		
		orderActionDetails.setActionType(actionType);
		orderAction.setOrderActionDetails(orderActionDetails);
		orderActionData.setOrderActionInfo(orderAction);
		input.setOrderActionsData(new OrderActionData[]{orderActionData});
	
		input.setMarkOrderAsSaved(true);
		OrderingContext orderingContext = getOrderingContext();
			try {
				currentStepOutput = serviceInvoker.startOrderAndGetCurrentStep(
						new ApplicationContext(),
						orderingContext, 
						input, 
						null);
				
				oId = ((OrderingProcessGroupInstanceDataObject) currentStepOutput
						.getNavigationResult().getSharedStepInstnacesData()
						.getProcessGroupInstance().getProcessGroupDataObject())
						.getOrderHeader().getOrderID();
				OrderId = EntityIDBaseAssist.getOMSMasterId(oId);
				System.out.println("startOrderAndGetCurrentStep successfully completed OrderId="+OrderId);
				
				handleStep();
				
			} catch (IllegalNavigationException e) {
				
				e.printStackTrace();
			} catch (InvalidUsageException e) {
				
				e.printStackTrace();
			} catch (DataNotFoundException e) {
				
				e.printStackTrace();
			} catch (EntityLockedException e) {
				
				e.printStackTrace();
			} catch (OperationTimedOutException e) {
				
				e.printStackTrace();
			} catch (UnauthorizedAccessException e) {
				
				e.printStackTrace();
			} catch (OrderNegotiationCompletedException e) {
				
				e.printStackTrace();
			} catch (IllegalEntityStateException e) {
				
				e.printStackTrace();
			} catch (RemoteException e) {
				
				e.printStackTrace();
			}
			
			

	}


	
	public void closeStep()  {

		CloseStepInput input = new CloseStepInput();
		
		input.setConfirmationChecksApproved(true);
		input.setStepInstances(nextStepInput.getStepInstances());
		input.setSharedStepInstancesData(nextStepInput.getSharedStepInstancesData());
		
			OrderingContext orderingContext = getOrderingContext();
			try {
				serviceInvoker.closeStep(
						new ApplicationContext(),
						orderingContext, 
						input, 
						null);
				
				//OrderId = currentStepOutput.getNavigationResult().getStepInstances()[0].getProcessInstance().getProcessDataObject().
				//handleStep();
				
				//oId = null;
				System.out.println("closeStep successfully completed");
				
			} catch (IllegalNavigationException e) {
				
				e.printStackTrace();
			} catch (InvalidUsageException e) {
				
				e.printStackTrace();
			} catch (EntityLockedException e) {
				
				e.printStackTrace();
			}  catch (OrderNegotiationCompletedException e) {
				
				e.printStackTrace();
			} catch (RemoteException e) {
				
				e.printStackTrace();
			} catch (ConsumerOutOfSyncException e) {
				
				e.printStackTrace();
			}			
	}
	
	private void handleStep() 
	{
		ValidationFeedback messages= currentStepOutput.getNavigationResult().getMessages();
		if( messages != null)
		{
			System.out.println(messages.getStatus().getLocalizedValue() + " messages");
			for(int i =0 ; i<messages.getMessages().length;i++)
			{				
				System.out.println(messages.getMessages()[i].getMessageText());
				if(messages.getMessages()[i].getMessageParameters()!=null)
				{
					for(int j =0 ;j<messages.getMessages()[i].getMessageParameters().length;j++)
					{
						System.out.println(messages.getMessages()[i].getMessageParameters()[j].getStringValue());
					}
				}
			}
		}
		
		if(currentStepOutput.getNavigationResult().getAvailableNavigationActions() == null)
		{
			System.out.println("No more steps to handle ");
			return;
		}
		
		String logicalName = currentStepOutput.getNavigationResult().getStepMetaData().getScreenLogicalName().getValueAsString();
		//based on logical name 
		//	1. display relavant data from output
		//	2. call services if required.
		//	3. prepare next step input.
		System.out.println("Current activity Logical Name: " + logicalName);
	
		if(logicalName.equals("NegotiateInstallationAddress"))
		{
			handleNegotiateInstallationAddress();			
		}
		else if(logicalName.equals("SelectOffersAndProducts"))
		{
			handleSelectOffersAndProducts();
			//cancelOrderOfferFromProcess();
		}
		else if(logicalName.equals("NegotiateProductConfigurationQuote"))
		{
//			handleGeneralStep();
			handleNPC();
			
			//saveStepData();
		}
		else if(logicalName.equals("ConfirmQuote"))
		{
			//getFullQuote();
			handleGeneralStep();
		}
		else if (logicalName.equals("ReplaceOffersAndProductsFull"))
		{
			handleReplaceOffer();
		}
		else
		{
			handleGeneralStep();
		}		
	}

	private ImplementedOffer[] getSourceOffers(ReplaceOffersSDO replaceOfferSDO)
	{
		System.out.println("Existing source offers:");
		
		for (ImplementedOffer srcOffer : replaceOfferSDO.getSourceOffers())
		{
			String existingOfferApId = EntityIDBaseAssist.getOMSMasterId(srcOffer.getAssignedOfferID());
//			String existingOfferApId = EntityIDBaseAssist.setOMSMasterId(srcOffer.getAssignedOfferID(), "123");
			System.out.println(existingOfferApId + " - " + srcOffer.getAvailableOffer().getCatalogOffer().getCatalogOfferDetails().getName().getLocalizedValue());
		}
		
		return replaceOfferSDO.getSourceOffers();
	}
	
	private ImplementedOffer[] getTargetOffers(String workingMode, SearchReplaceableOffersResults[] roResults)
	{
			System.out.println("Target offers:");
			ImplementedOffer[] targetOffersArray = null;
			for (SearchReplaceableOffersResults roResult : roResults)
			{
				if (roResult.getOffersIdentifier().equals(workingMode))
				{
					targetOffersArray = roResult.getImplementedOffers();
					for (ImplementedOffer implOffer : roResult.getImplementedOffers())
					{
						String offerId = null;
						if (implOffer.getAssignedOfferID() != null)
						{
							offerId = EntityIDBaseAssist.getOMSMasterId(implOffer.getAssignedOfferID());
						}
						
						if (offerId != null)
						{
							System.out.println(offerId + " - " + implOffer.getAvailableOffer().getCatalogOffer().getCatalogOfferDetails().getName().getLocalizedValue());
						}
						else
						{
							System.out.println(implOffer.getAvailableOffer().getCatalogOffer().getCatalogOfferDetails().getName().getLocalizedValue());
						}
					}
				}
			}
		
		return targetOffersArray;
	}
	
	private Map<String, String>  getSourceProducts(ReplaceOffersSDO replaceOfferSDO)
	{
		System.out.println("Existing source products:");
		Map<String, String> apIdToNameMap = new HashMap<String, String>();
		for (ImplementedOffer implOffer: replaceOfferSDO.getSourceOffers()) 
		{
			for (ImplementedProduct prod : implOffer.getChildImplementedProducts())
			{
				AssignedProductID apId = prod.getAssignedProductID();
				apIdToNameMap.put(EntityIDBaseAssist.getOMSMasterId(apId), prod.getAvailableProduct().getCatalogProduct().getCatalogProductDetails().getCatalogProductName().getLocalizedValue());
				System.out.println(EntityIDBaseAssist.getOMSMasterId(apId) + " - " + prod.getAvailableProduct().getCatalogProduct().getCatalogProductDetails().getCatalogProductName().getLocalizedValue());
			}
		}
		
		return apIdToNameMap;
	}
	
	private void printAvailableRoOperations(ReplaceOfferProductResults[] roOperResultsArray, Map<String, String> sourceApIdToNameMap)
	{
		for (ReplaceOfferProductResults roProductResult : roOperResultsArray)
		{
			String apId = EntityIDBaseAssist.getOMSMasterId(roProductResult.getSourceItem().getAssignedProductID());
			String prodName = sourceApIdToNameMap.get(apId);
			
			System.out.println("Operations available for " + prodName + " product:");
			ReplaceOffersAndProductsOperation[] availOpers = roProductResult.getAvailableOperations();
			for (ReplaceOffersAndProductsOperation availOper : availOpers)
			{
				String availOperName = availOper.getOperation().getLocalizedValue();
				String targetOfferName = null;
				String targetOfferId = null;
								
				if (availOper.getTargetOffer() != null)
				{
					targetOfferName = availOper.getTargetOffer().getAvailableOffer().getCatalogOffer().getCatalogOfferDetails().getName().getLocalizedValue();
					if (availOper.getTargetOffer().getAssignedOfferID() != null)
					{
						targetOfferId = EntityIDBaseAssist.getOMSMasterId(availOper.getTargetOffer().getAssignedOfferID());
					}
				}
				
				System.out.println(availOperName + (targetOfferName != null ? " to " : "") + (targetOfferId != null ? targetOfferId + " - " : "") + (targetOfferName != null ? targetOfferName : ""));
			}
		}
	}
	
	private void handleReplaceOffer()
	{
		System.out.println("Handle Replace Offer step START");
		
		String workingMode = CATALOG_OFFERS_CN; 
		
		ReplaceOffersSDO replaceOfferSDO = (ReplaceOffersSDO) currentStepOutput.getNavigationResult().getSharedStepInstnacesData().getSharedDataObject();
		ImplementedOffer[] sourceOfferArray = getSourceOffers(replaceOfferSDO);
		Map<String, String> sourceApIdToNameMap = getSourceProducts(replaceOfferSDO);
		
		//search target offers for each input offer
		SearchReplaceableOffersOutput searchRoOutput = searchReplaceableOffers(workingMode, sourceOfferArray);
		SearchReplaceableOffersResults[] roResults = searchRoOutput.getSearchReplaceableOffersResults();		
		ImplementedOffer[] targetOffersArray = getTargetOffers(workingMode, roResults);
										
		//get available operations for each source product
		GetReplaceOffersAndProductsAvailableOperationsOutput availOpersOut = getAvailableActionsForReplaceOffer(workingMode, sourceOfferArray, targetOffersArray);
		ReplaceOfferProductResults[] roOperResultsArray = availOpersOut.getReplaceOfferProductResults();
		printAvailableRoOperations(roOperResultsArray, sourceApIdToNameMap);
		
		//set the input for cease/transfer of current products
		//this will generate Change Part Of RO and Cease part of RO OAs
		ReplaceOffersData[] roDataArray = prepareReplaceOfferInputExistingProducts(sourceOfferArray, targetOffersArray, roOperResultsArray);
				
		//prepare the input for adding new optional products
		// this should generate PR type OAs
		ReplaceOffersData roData = prepareReplaceOfferInputNewProducts(targetOffersArray);
		
		//add new PR roData to the roDataArray
		List<ReplaceOffersData> roDataList = new ArrayList<ReplaceOffersData>(Arrays.asList(roDataArray));
		roDataList.add(roData);
		roDataArray = roDataList.toArray(new ReplaceOffersData[roDataList.size()]);
		
		//call svc and send everything in one go - no incremental
		GetReplaceOffersResultsOutput roResultsOutput = getReplaceOffersResult(false, roDataArray);
		printValidationMessages(roResultsOutput.getValidationFeedback());
		printGeneratedRoOrderActions(roResultsOutput);
			
		//check the generated order actions - there is no PR OA for the optional product
		createNextStepInputForReplaceOffer(replaceOfferSDO, roResultsOutput);
		
		System.out.println("Handle Replace Offer step COMPLETE successfully");
	}
	
	private void printGeneratedRoOrderActions(GetReplaceOffersResultsOutput roResults)
	{
		System.out.println("Generated order actions:");
		for (StepInstance stepInst : roResults.getOrderActions())
		{
			OrderingProcessInstanceDataObject dataObj = (OrderingProcessInstanceDataObject) stepInst.getProcessInstance().getProcessDataObject();
			String productName = dataObj.getAssignedProductHeader().getName().getLocalizedValue();
			String oaType = dataObj.getOrderActionHeader().getOrderActionType().getLocalizedValue();
			
			System.out.println(productName + " - " + oaType);
		}
	}
	
	private void createNextStepInputForReplaceOffer(ReplaceOffersSDO replaceOfferSDO, GetReplaceOffersResultsOutput roResultsOutput)
	{
		nextStepInput = new NextStepInput();
		
		nextStepInput.setSharedStepInstancesData(currentStepOutput.getNavigationResult().getSharedStepInstnacesData());
		nextStepInput.setStepContext(currentStepOutput.getNavigationResult().getStepContext());
		nextStepInput.setStepInstances(roResultsOutput.getOrderActions());
	}
	
	private void printValidationMessages(ValidationFeedback validationFeedback)
	{
		if (validationFeedback != null)
		{
			System.out.println(validationFeedback.getStatus().getLocalizedValue() + " messages:");
			for(int i = 0 ; i < validationFeedback.getMessages().length;i++)
			{				
				System.out.println(validationFeedback.getMessages()[i].getMessageText());
				if(validationFeedback.getMessages()[i].getMessageParameters()!= null)
				{
					for(int j =0 ;j < validationFeedback.getMessages()[i].getMessageParameters().length;j++)
					{
						System.out.println(validationFeedback.getMessages()[i].getMessageParameters()[j].getStringValue());
					}
				}
			}
		}
	}
	
	private ReplaceOffersData[] prepareReplaceOfferInputExistingProducts(ImplementedOffer[] srcOffersArray, ImplementedOffer[] targetOfferArray, ReplaceOfferProductResults[] availableOperationsArray)
	{
		List<ReplaceOffersData> roDataList = new ArrayList<ReplaceOffersData>();
		for (ImplementedOffer srcOffer : srcOffersArray)
		{
			for (ImplementedProduct implProduct : srcOffer.getChildImplementedProducts())
			{
				String apId1 = EntityIDBaseAssist.getOMSMasterId(implProduct.getAssignedProductID());
				ReplaceOffersAndProductsOperation[] availableRopOperationsArray = null;
				for (ReplaceOfferProductResults ropResult : availableOperationsArray)
				{
					String apId2 = EntityIDBaseAssist.getOMSMasterId(ropResult.getSourceItem().getAssignedProductID());
					if (apId1.equals(apId2))
					{
						availableRopOperationsArray = ropResult.getAvailableOperations();
						break;
					}
				}
				
				ReplaceOffersData roData = doRoMapping(implProduct, targetOfferArray, availableRopOperationsArray);
				if (roData != null)
				{
					roDataList.add(roData);
				}
			}
		}
		
		return roDataList.toArray(new ReplaceOffersData[roDataList.size()]);
	}
	
	private ReplaceOffersData prepareReplaceOfferInputNewProducts(ImplementedOffer[] targetOfferArray)
	{
		ImplementedOffer implOffer = targetOfferArray[1];
		AvailableProduct availProd = implOffer.getAvailableOffer().getChildAvailableProducts()[0];
		
		ImplementedProduct implProd = new ImplementedProduct();
		implProd.setAvailableProduct(availProd);
		implProd.setTemporaryID(getNewID());
		
		implOffer.setChildImplementedProducts(new ImplementedProduct[] {implProd});
		
		ReplaceOffersData roData = new ReplaceOffersData();
		roData.setSourceProducts(null);
		roData.setTargetOffers(new ImplementedOffer[] {implOffer});
		
		return roData;
	}
	
	private ReplaceOffersData doRoMapping(ImplementedProduct srcProduct, ImplementedOffer[] targetOffers, ReplaceOffersAndProductsOperation[] availableOpers)
	{
		ReplaceOfferOperationRVT transferRoOperation = new ReplaceOfferOperationRVT("TR");
		
		String apId  = EntityIDBaseAssist.getOMSMasterId(srcProduct.getAssignedProductID());
		ReplaceOfferOperationRVT selectedOper = null;
		ImplementedOffer selectedTargetOffer = null;
		
		if (apId.equals("500046452"))
		{
			//BIS1
			selectedOper = transferRoOperation;
			selectedTargetOffer = targetOffers[1];
		}
		else 
		if (apId.equals("500046460"))
		{
			//BIS2
			selectedOper = transferRoOperation;
			selectedTargetOffer = targetOffers[1];
		}
		else
		if (apId.equals("500046467"))
		{
			//GLP
			selectedOper = transferRoOperation;
			selectedTargetOffer = targetOffers[1];
		}
		else 
		if (apId.equals("500046469"))
		{
			//Voice
			selectedOper = transferRoOperation;
			selectedTargetOffer = targetOffers[1];
		}
		
		ReplaceOffersData existingRoData = new ReplaceOffersData();
		if (selectedOper != null)
		{
			ReplaceOffersAndProductsOperation roOper = new ReplaceOffersAndProductsOperation();
			roOper.setOperation(selectedOper);
			roOper.setTargetOffer(selectedTargetOffer);

			ReplaceOfferInfo roInfo = new ReplaceOfferInfo();
			roInfo.setSelectedReplaceOfferAndProductOperation(roOper);

			srcProduct.setReplaceOfferInfo(roInfo);
		}
				
		existingRoData.setSourceProducts(new ImplementedProduct[] {srcProduct});
		if (selectedTargetOffer != null)
		{
			existingRoData.setTargetOffers(new ImplementedOffer[] {selectedTargetOffer});
		}
		
		return existingRoData;
	}
	
	private SearchReplaceableOffersOutput searchReplaceableOffers(String workingMode, ImplementedOffer[] sourceOffersArray)
	{
		System.out.println("SearchReplaceableOffers for mode " + workingMode + " START");
		
		List<CihFilterInfo> filterList = new ArrayList<CihFilterInfo>();
		filterList.addAll(buildNewCihFilterInfo("customerID", new String[] { customer_id }, CihStringOperator.EQUAL));
		
		String apOfferIdConcatStr = "";
		int i = 0;
		for (ImplementedOffer sourceOffer : sourceOffersArray)
		{
			AssignedOfferID apOfferId = sourceOffer.getAssignedOfferID();
			String apOfferIdStr = EntityIDBaseAssist.getOMSMasterId(apOfferId);
			if (i > 0)
			{
				apOfferIdConcatStr += ";";
			}
			apOfferIdConcatStr += apOfferIdStr;
			i++;
		}
		
		filterList.addAll(buildNewCihFilterInfo("sourceOfferIDs", new String[] { apOfferIdConcatStr }, CihStringOperator.EQUAL));
		filterList.addAll(buildNewCihFilterInfo("expansionLevel", new Integer[] { 2 }, CihStringOperator.EQUAL));
		filterList.addAll(buildNewCihFilterInfo("eligibilityCode", new String[] { "EA" }, CihStringOperator.EQUAL));
		
		if (workingMode.equals(EXISTING_SOURCE_OFFERS_CN))
		{
			filterList.addAll(buildNewCihFilterInfo("retrievePricingForMode", new String[] { EXISTING_SOURCE_OFFERS_CN }, CihStringOperator.EQUAL));
		}
		filterList.addAll(buildNewCihFilterInfo("includeExpiredOffers", new String[] { "false" }, CihStringOperator.EQUAL));
		
		
		filterList.addAll(buildNewCihFilterInfo("workingMode", new String[] { workingMode }, CihStringOperator.EQUAL));
		
		CihFilterInfo[] filters = (CihFilterInfo[])filterList.toArray(new CihFilterInfo[filterList.size()]);
		
		SelectionCriteria selectionCriteria = new SelectionCriteria();
		selectionCriteria.setFilterCriteria(filters);
		selectionCriteria.setSortCriteria(new CihSortInfo[0]);

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setMaxListSize(200);

		OrderingContext orderingContext = getOrderingContext();
		SearchReplaceableOffersOutput searchRoOutput = null;
		
		try 
		{
			searchRoOutput = serviceInvoker.searchReplaceableOffers(
					new ApplicationContext(),
					orderingContext,
					selectionCriteria,
					new SearchInfo(),
					paginationInfo,
					null);
			
			System.out.println("SearchReplaceableOffers COMPLETE successfully");
		} 
		catch (InvalidUsageException e) 
		{
			e.printStackTrace();
		} 
		catch (EntityLockedException e) 
		{
			e.printStackTrace();
		} 
		catch (OperationTimedOutException e) 
		{
			e.printStackTrace();
		} 
		catch (UnauthorizedAccessException e) 
		{
			e.printStackTrace();
		} 
		catch (ConsumerOutOfSyncException e) 
		{
			e.printStackTrace();
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}
		
		return searchRoOutput;
	}
	
	private GetReplaceOffersAndProductsAvailableOperationsOutput getAvailableActionsForReplaceOffer(String workingMode, ImplementedOffer[] sourceOffersArray, ImplementedOffer[] targetOffersArray)
	{
		System.out.println("GetReplaceOffersAndProductsAvailableOperations START");
		
		List<ReplaceOfferProductData> roProductDataList = new ArrayList<ReplaceOfferProductData>();
				
		for (ImplementedOffer implOffer : sourceOffersArray)
		{
			for (ImplementedProduct implProduct : implOffer.getChildImplementedProducts())
			{
				AssignedProductRef apRef = new AssignedProductRef();
				apRef.setAssignedProductID(implProduct.getAssignedProductID());
				
				ReplaceOfferProductData roData = new ReplaceOfferProductData();
				roData.setSourceItem(apRef);
				
				if (workingMode.equals(CATALOG_OFFERS_CN))
				{
					List<OrderingCatalogOffer> catalogTargetOfferList = new ArrayList<OrderingCatalogOffer>();
					
					for (ImplementedOffer implOffer2 : targetOffersArray)
					{
						catalogTargetOfferList.add(implOffer2.getAvailableOffer().getCatalogOffer());
					}
					roData.setTargetCatalogOffers(catalogTargetOfferList.toArray(new OrderingCatalogOffer[catalogTargetOfferList.size()]));
				}
				else if (workingMode.equals(EXISTING_TARGET_OFFERS_CN))
				{
					List<AssignedOfferID> customerTargetOfferList = new ArrayList<AssignedOfferID>();
					
					for (ImplementedOffer implOffer2 : targetOffersArray)
					{
						customerTargetOfferList.add(implOffer2.getAssignedOfferID());
					}
					roData.setTargetCustomerOffers(customerTargetOfferList.toArray(new AssignedOfferID[customerTargetOfferList.size()]));
				}
								
				roData.setIsCalculateMigrateOperation(false);	
				roProductDataList.add(roData);
			}
		}
					
		GetReplaceOffersAndProductsAvailableOperationsInput input = new GetReplaceOffersAndProductsAvailableOperationsInput();
		ReplaceOfferProductData[] roProductDataArray = new ReplaceOfferProductData[roProductDataList.size()];
		roProductDataList.toArray(roProductDataArray);
		input.setReplaceOfferProductData(roProductDataArray);
		
		OrderingContext orderingContext = getOrderingContext();
		GetReplaceOffersAndProductsAvailableOperationsOutput availOpersOut= null;
		try 
		{
			availOpersOut = serviceInvoker.getReplaceOffersAndProductsAvailableOperations(
					new ApplicationContext(),
					orderingContext,
					input, 
					null);
			
			System.out.println("GetReplaceOffersAndProductsAvailableOperations COMPLETE successfully");
		} 
		catch (InvalidUsageException e) 
		{
			e.printStackTrace();
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}
		
		return availOpersOut;
	}
	
	private GetReplaceOffersResultsOutput getReplaceOffersResult(boolean incremental, ReplaceOffersData[] roDataArray)
	{
		System.out.println("GetReplaceOffersResults START");
		
		GetReplaceOffersResultsInput input = new GetReplaceOffersResultsInput();
		input.setIncremental(incremental);
		input.setReplaceOffersData(roDataArray);		
		
		GetReplaceOffersResultsOutput output = null;
		OrderingContext orderingContext = getOrderingContext();
		try 
		{
			output = serviceInvoker.getReplaceOffersResults(
					new ApplicationContext(),
					orderingContext,
					input, 
					null);
		} 
		catch (IllegalNavigationException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidUsageException e) 
		{
			e.printStackTrace();
		} 
		catch (DataNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (EntityLockedException e) 
		{
			e.printStackTrace();
		} 
		catch (OperationTimedOutException e) 
		{
			e.printStackTrace();
		} 
		catch (UnauthorizedAccessException e) 
		{
			e.printStackTrace();
		} 
		catch (ConsumerOutOfSyncException e) 
		{
			e.printStackTrace();
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}
		
		System.out.println("GetReplaceOffersResults COMPLETE successfully");
		return output;
	}
	
	String pptoAdd = "SL Office Phone Lite B1 Save - Contract";
	
	private void handleNPC() 
	{
		StepInstance stepInsts[] = currentStepOutput.getNavigationResult().getStepInstances();
		StepInstance astepinstance[] = stepInsts;
		
		if (!isConfigurationHandled) {
			     for (StepInstance stepInstance : astepinstance) {
			    	 ImplementedProduct product = ((ProductConfigurationSDO)stepInstance.getStepDataObject()).getImplementedProduct();
			    	 
			    	 ConfigureProduct cofigureProduct = new ConfigureProduct();


			    	 List<Offer> offerList = this.offerList;
			    	 
			    	 offerLoop:
			    	 for (Offer offer : offerList) {
			    		 List<Product> productList = offer.getProductList();
			    		 for (Product prod : productList) {
			    			 if (!prod.isHandled() && prod.getName().equals(product.getAvailableProduct().getCatalogProduct().getCatalogProductDetails().getCatalogProductCode())) {
			    				 cofigureProduct.productConfiguration = prod;
			    				 prod.setHandled(true);
			    				 break offerLoop;
			    			 }
							
						}
			    	}
			    	 
			    	 
			    	 cofigureProduct.product = product;
			    	 
			    	 if (cofigureProduct.productConfiguration.isCheckRequired()) {
			    		 cofigureProduct.productConfiguration.setId(cofigureProduct.product.getTemporaryID());
			    	 }
			    	 
			    	 //handle attributes for product
			    	 cofigureProduct.createCompAttributes(cofigureProduct.product);
			    	 if(cofigureProduct.productConfiguration.getAttributeList() !=null && cofigureProduct.productConfiguration.getAttributeList().size()>0){
			    		 cofigureProduct.updateCompAttributes(cofigureProduct.product, cofigureProduct.productConfiguration.getAttributeList());
			    	 }
			    	 
			    	 cofigureProduct.execute();
			    	 
			    	 
				}
			     
			     isConfigurationHandled = true;
		}
		  handleGeneralStep();
//		  nextStepInput.setConfirmationChecksApproved(true);      
		
	}
	
	
	
	public OrderingAttributeDisplayInformation[] attributeDisplayInformations;
	public Comparator<ImplementedAttribute> attributesComparator = new Comparator<ImplementedAttribute>(){
		public int compare(ImplementedAttribute a1, ImplementedAttribute a2)
		{
			Integer val1=0;
			Integer val2=0;
			for (OrderingAttributeDisplayInformation attributeDisplayInformation : attributeDisplayInformations) {
				
				if(a1.getCatalogAttribute().getCatalogAttributeID().equalsIgnoreCase(
						attributeDisplayInformation.getCatalogAttributeID())){
					val1 = attributeDisplayInformation.getDisplayOrder();
				}
				if(a2.getCatalogAttribute().getCatalogAttributeID().equalsIgnoreCase(
						attributeDisplayInformation.getCatalogAttributeID())){
					val2 = attributeDisplayInformation.getDisplayOrder();
				} 
			}			
			return val1.compareTo(val2);
		}
	};

	
	protected void validateProduct(StepInstance stepInstance) {
		CompatibilityRulesInput input = new CompatibilityRulesInput();
		
		input.setConfirmationChecksApproved(true);
		input.setImplementedProducts(new ImplementedProduct[]{((ProductConfigurationSDO)stepInstance.getStepDataObject()).getImplementedProduct()});
		input.setStepContext(currentStepOutput.getNavigationResult().getStepContext());
		
		try {
			serviceInvoker.checkCompatibilityRules(new ApplicationContext(), getOrderingContext(),input,null);
			
			System.out.println("checkCompatibilityRules completed successfully");
		} catch (IllegalNavigationException e) {
			e.printStackTrace();
		} catch (InvalidUsageException e) {
			e.printStackTrace();
		} catch (DataNotFoundException e) {
			e.printStackTrace();
		} catch (EntityLockedException e) {
			e.printStackTrace();
		} catch (OperationTimedOutException e) {
			e.printStackTrace();
		} catch (UnauthorizedAccessException e) {
			e.printStackTrace();
		} catch (ConsumerOutOfSyncException e) {
			e.printStackTrace();
		} catch (OrderNegotiationCompletedException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

	private void handleGeneralStep() 
	{
		nextStepInput = new NextStepInput();
		
		nextStepInput.setSharedStepInstancesData(currentStepOutput.getNavigationResult().getSharedStepInstnacesData());
		
		nextStepInput.setStepInstances(currentStepOutput.getNavigationResult().getStepInstances());
		nextStepInput.setStepContext(currentStepOutput.getNavigationResult().getStepContext());		
	}

	private void handleNegotiateInstallationAddress() {
		if(currentStepOutput.getNavigationResult().getStepMetaData().getSynchronizationMode().equals("noSelection")){
			negotiateInstallationAddressSDO = (NegotiateInstallationAddressSDO)currentStepOutput.getNavigationResult().getSharedStepInstnacesData().getSharedDataObject();
			createNextStepInputForNegotiateInstallationAddress(negotiateInstallationAddressSDO);
			createNextStepInput(negotiateInstallationAddressSDO);
		}
		else if(currentStepOutput.getNavigationResult().getStepMetaData().getSynchronizationMode().equals("SO")){
			negotiateInstallationAddressSDOs = new NegotiateInstallationAddressSDO[currentStepOutput.getNavigationResult().getStepInstances().length];
			for(int i =0; i < currentStepOutput.getNavigationResult().getStepInstances().length;i++){
				
				if(currentStepOutput.getNavigationResult().getStepInstances()[i].isActive()){
					negotiateInstallationAddressSDOs[i] = (NegotiateInstallationAddressSDO)currentStepOutput.getNavigationResult().getStepInstances()[i].getStepDataObject();
					createNextStepInputForNegotiateInstallationAddress(negotiateInstallationAddressSDOs[i]);
				}
			}
			createNextStepInput(negotiateInstallationAddressSDOs);
		}
		
	}
	

	private void createNextStepInputForNegotiateInstallationAddress(NegotiateInstallationAddressSDO negotiateInstallationAddressSDO2) {
		negotiateInstallationAddressSDO2.setAddressID(addressId);
		negotiateInstallationAddressSDO2.setSiteID(siteId);
	}


	private void handleSelectOffersAndProducts() {
		offersAndProductsSDO = (OffersAndProductsSDO)currentStepOutput.getNavigationResult().getSharedStepInstnacesData().getSharedDataObject();
		getAvailableOffers();
		//createOrderAction();
		createNextStepInputForSelectOffersAndProducts();
	}

	
	private void createNextStepInputForSelectOffersAndProducts() {

		offersAndProductsSDO.setSelectedOffers(createImplProdcore());
		
		createNextStepInput(offersAndProductsSDO);
		
	}

	
	
	private void createNextStepInput(BaseSDO sdo) {
		handleGeneralStep();		
		nextStepInput.getSharedStepInstancesData().setSharedDataObject(sdo);		
	}
	
	
	private void createNextStepInput(BaseSDO[] sdo) {
		handleGeneralStep();
		StepInstance [] stepInstances = nextStepInput.getStepInstances();
		for(int i=0 ; i< sdo.length ; i++){			
			stepInstances[i].setStepDataObject(sdo[i]);
		}		
	}

	private void getAvailableOffers() {
		SelectionCriteria selectionCriteria = new SelectionCriteria();	

		List<CihFilterInfo> filterList = new ArrayList<CihFilterInfo>();
		
		CihFilterInfo[] filters =null;
		
		filterList.addAll(buildNewCihFilterInfo("customerID",
		new String[] { customer_id }, CihStringOperator.EQUAL));

		

		CihFilterInfo myInfo = new SearchFilterInfo();
		myInfo.setFilterField("expansionLevel");
		myInfo.setFilterOpCode(CihStringOperator.EQUAL.getOpCode());
		myInfo.setFilterValueList(new Integer [] {new Integer(5)});
		
		filterList.add(myInfo);

		filters = (CihFilterInfo[])filterList.toArray(new CihFilterInfo[filterList.size()]);

		selectionCriteria.setFilterCriteria(filters);
		SearchInfo sInfo = new SearchInfo();
		PaginationInfo pInfo = new PaginationInfo();
		pInfo.setMaxListSize(100);
		
		try {
			SearchNegotiatedOffersAndProductsOutput Offers = serviceInvoker
					.searchNegotiatedOffersAndProducts(new ApplicationContext(),
							getOrderingContext(),selectionCriteria , sInfo, pInfo,
							null);
			availableOffers = Offers.getSearchResults();
			for (AvailableOffer offer : availableOffers) {
				System.out.println(offer.getCatalogOffer().getCatalogOfferDetails().getName().getLocalizedValue() +"   :   "
						+offer.getEligibilityValue().getLocalizedValue());
			}
		} catch (InvalidUsageException e) {
			
			e.printStackTrace();
		} catch (EntityLockedException e) {
			
			e.printStackTrace();
		} catch (OperationTimedOutException e) {
			
			e.printStackTrace();
		} catch (UnauthorizedAccessException e) {
			
			e.printStackTrace();
		} catch (ConsumerOutOfSyncException e) {
			
			e.printStackTrace();
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
		System.out.println("searchNegotiatedOffersAndProducts successfully completed");	

	}
	

	
	
	public void cancelOrderOfferFromProcess()

	{

		ApplicationContext applicationContext = new ApplicationContext();

		OrderingContext orderingContext = getOrderingContext();

		CancelOrderFromProcessInput cancelOrderFromProcessInput = new CancelOrderFromProcessInput();

		cancelOrderFromProcessInput
				.setSharedStepInstancesData(currentStepOutput.getNavigationResult()

						.getSharedStepInstnacesData());

		cancelOrderFromProcessInput.setStepContext(currentStepOutput.getNavigationResult().getStepContext());

		cancelOrderFromProcessInput.setStepInstances(currentStepOutput.getNavigationResult().getStepInstances());

		CancelOfferData[] cancelOfferData = new CancelOfferData[1];

		cancelOfferData[0] = new CancelOfferData();

		AssignedOfferRef assignedOfferRef = new AssignedOfferRef();

		OffersAndProductsSDO offersAndProductsSDO = (OffersAndProductsSDO) currentStepOutput.getNavigationResult()

				.getSharedStepInstnacesData().getSharedDataObject();

		AssignedOfferID assignedOfferID = offersAndProductsSDO
				.getSelectedOffers(0).getAssignedOfferID();

		assignedOfferRef.setAssignedOfferID(assignedOfferID);

		cancelOfferData[0].setAssignedOfferRef(assignedOfferRef);

		cancelOrderFromProcessInput.setOffersToCancel(cancelOfferData);

//		ValidateCancelOrderInput inp = new ValidateCancelOrderInput();
//		ValidateCancelOrderOutput validateCancelOrderOutput=null;
//		validateCancelOrderOutput.getReasonList()[0].isReasonTextRequired()
		
		new OrderActionReasonRVT("code","Localized Value");
		new OrderActionReasonRVT("code");
		

		try {
			serviceInvoker
					.cancelOrderFromProcess(applicationContext, orderingContext,

					cancelOrderFromProcessInput, null);
			
			System.out.println("After cancelOrderFromProcess");
		} catch (IllegalNavigationException e) {
			e.printStackTrace();
		} catch (InvalidUsageException e) {
			e.printStackTrace();
		} catch (EntityLockedException e) {
			e.printStackTrace();
		} catch (ConsumerOutOfSyncException e) {
			e.printStackTrace();
		} catch (OrderNegotiationCompletedException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	
	
		
	/*public FullQuoteOutput getFullQuote() {
		OrderingContext orderingContext = getOrderingContext();

		FullQuoteInput input =  new FullQuoteInput();	
		input.setIsPendingIncludedX9(true);
		
		OrderID id =  new OrderID();
		EntityIDBaseAssist.setOMSMasterId(id, OrderId);
		id.setOrderID(OrderId);
		input.setOrderIDX9(id);
		input.setIsPendingIncludedX9(true);	
		FullQuoteOutput fullQuoteOutput = null;
		
		try {
			
			fullQuoteOutput = serviceInvoker.getFullQuote(
					new ApplicationContext(), orderingContext,input , null);
		
			System.out.println("getFullQuote successfully completed");
			
			
		} catch (InvalidUsageException e) {
			
			e.printStackTrace();
		} catch (RemoteException e) {
			
			e.printStackTrace();
		} catch (ExternalSystemException e) {
			e.printStackTrace();
		}
		
		return fullQuoteOutput;
	}
*/
	
//	ThreadLocal<IOmsServicesRemote> tlsi=new ThreadLocal<IOmsServicesRemote>();
	
	public IOmsServicesRemote getServiceInvoker() throws NamingException, RemoteException,
			CreateException {

//		if (tlsi.get()!=null) {
//			System.out.println("cached");
//			return tlsi.get();
//		}
		try {
			renewTicket();
		} catch (UamsSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UamsNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ActivityNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ic = getinitialContext();

		IOmsServicesRemoteHome omsServices = (IOmsServicesRemoteHome) PortableRemoteObject
				.narrow(
						ic
								.lookup("omsserver_weblogic.com.amdocs.cih.services.oms.interfaces.IOmsServicesRemote"),
						IOmsServicesRemoteHome.class);

		serviceInvoker = omsServices.create();
		
//		tlsi.set(serviceInvoker);
		
		return serviceInvoker;

	}

	private InitialContext getinitialContext() throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL,"t3://" + omsHost +":" + omsPort);
		env.put(Context.SECURITY_CREDENTIALS, tkt);
		env.put(Context.SECURITY_PRINCIPAL, tkt);

		InitialContext ic = new InitialContext(env);
		return ic;
	}

	

	public OrderingContext getOrderingContext() {
		OrderingContext oContext = new OrderingContext();
		oContext.setUserID("oms");
		oContext.setSecurityToken(tkt);
		oContext.setLocale(Locale.ENGLISH);
		oContext.setSalesChannel(new SalesChannelRVT("TELUS"));
		//oContext.setCachingMode(CachingMode.CachingModeFullCatalog);
		return oContext;
	}
	
	public synchronized String getNewID()
	{
		String uniqueID = UUID.randomUUID().toString();		
		return "C" + uniqueID;
		
	}
	
	public static CihFilterInfo buildNewCihFilterInfo(String field, Object value, CihStringOperator operator) 
	{
		CihFilterInfo myInfo = new SearchFilterInfo();
		myInfo.setFilterField(field);
		myInfo.setFilterOpCode(operator.getOpCode());
		myInfo.setFilterValueList(new Object [] {value});
		return myInfo;
	}
	
	public static List<CihFilterInfo> buildNewCihFilterInfo(String field, Object[] values, CihStringOperator operator) 
	{
		List<CihFilterInfo> filters = new ArrayList<CihFilterInfo>(values.length);
		
		if (operator == CihStringOperator.IN) {
			CihFilterInfo myInfo = new SearchFilterInfo();
			myInfo.setFilterField(field);
			myInfo.setFilterOpCode(operator.getOpCode());
			myInfo.setFilterValueList(values);
			((SearchFilterInfo)myInfo).setLogicalConnector("AND");
			
			filters.add(myInfo);
			return filters;
		}
		
		for (int i =0; i< values.length;i++)
		{
			CihFilterInfo filter= buildNewCihFilterInfo(field, values[i], CihStringOperator.EQUAL);
			filters.add(filter);
			if(i==0)
			{
				((SearchFilterInfo)filter).setLogicalConnector("AND");
			}
			else
			{
				((SearchFilterInfo)filter).setLogicalConnector("OR");
			}	
		}
		return filters;
	}

	public String  startProvideOrderAndReachNPC()  {
	
		startOrder(); // NIA

		getNextStep(); //SOAP
		getNextStep();// UOAA
		getNextStep();// NPC
		
		
		saveStepData();
		
		return OrderId;
	}

	public String startProvideOrderAndReachConfirmQuote()  {
		startOrder(); // NIA

		getNextStep(); //SOAP
		getNextStep();// UOAA
		getNextStep();// NPC
		getNextStep();// confirmQuote
		
		saveStepData();
		return OrderId;
	}
	
	
	public SearchOrderOutput getOrderList(String customerId, String[] orderStatus) throws Exception
	{
		SearchOrderOutput out = null;
		


			SelectionCriteria obj = new SelectionCriteria();
			
			
			List<CihFilterInfo> filterList = new ArrayList<CihFilterInfo>();

			
			filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("CustomerProfileID", new String[]{customerId}, CihStringOperator.EQUAL));
			
			filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("OrderStatusCode", orderStatus, CihStringOperator.IN));
			
			CihFilterInfo[] filters = (CihFilterInfo[])filterList.toArray(new CihFilterInfo[filterList.size()]);
			
			obj.setFilterCriteria(filters);
			obj.setSortCriteria(new CihSortInfo[0]);
			
			
			OrderingContext ctx = getOrderingContext();
//		    ctx.setSecurityToken(ticket);
		    MaskInfo maskInfo = new MaskInfo();
		    
		    
		    PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setMaxListSize(2000);
			
			getServiceInvoker();
		    out = serviceInvoker.searchOrder(new ApplicationContext(), ctx, obj, null, paginationInfo, maskInfo);
		    

		return out;
	    
		
	}
	
	public SearchAssignedProductOutput searchAssignedProduct(String customerId)
	{
		
		SelectionCriteria obj = new SelectionCriteria();
		
		
		List<CihFilterInfo> filterList = new ArrayList<CihFilterInfo>();

		
		filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("CustomerProfileID", new String[]{customerId}, CihStringOperator.EQUAL));
		
		filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("SubLevelReturnInd", new String[] { new String("TL") }, CihStringOperator.EQUAL));
		
		filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("ProductState", new String[] { new String("AS") }, CihStringOperator.EQUAL));
		
		CihFilterInfo[] filters = (CihFilterInfo[])filterList.toArray(new CihFilterInfo[filterList.size()]);
		
		obj.setFilterCriteria(filters);
		obj.setSortCriteria(new CihSortInfo[0]);
		
		OrderingContext ctx = getOrderingContext();
	    MaskInfo maskInfo = new MaskInfo();
	    
	    
	    PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setMaxListSize(2000);
		SearchAssignedProductOutput out = null;
		try {
			getServiceInvoker();
			out = serviceInvoker.searchAssignedProduct(new ApplicationContext(), ctx, obj, null, paginationInfo, maskInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    
		return out;
		
	}
	
	
	public RetrieveAssignedProductHierarchyOutput retrieveAssignedProductHierarchy(AssignedProductHierarchyRef[] assignedProductHierarchyRefList)
	{
		
		
		RetrieveAssignedProductHierarchyInput obj = new RetrieveAssignedProductHierarchyInput();
		
		
	
		obj.setAssignedProductHierarchyRef(assignedProductHierarchyRefList);
		
		obj.setHierarchyLevel("AL");
		
		OrderingContext ctx = getOrderingContext();
	    MaskInfo maskInfo = new MaskInfo();
	    
	    
	    PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setMaxListSize(2000);
		
		RetrieveAssignedProductHierarchyOutput out = null;
		
		try {
			getServiceInvoker();
			out = serviceInvoker.retrieveAssignedProductHierarchy(new ApplicationContext(), ctx, obj, paginationInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return out;
		
	}
	
	public RetrieveAssignedProductHierarchyOutput searchAssignedProductHierarchy(String customerId) {
		SearchAssignedProductOutput searchAssignedProductOutput = searchAssignedProduct(customerId);
		
		List<AssignedProductHierarchyRef> assignedProductHierarchyRefList = new LinkedList<AssignedProductHierarchyRef>();
		for (SearchAssignedProductHierarchy searchAssignedProductHierarchy : searchAssignedProductOutput.getSearchAssignedProductHierarchies()) {
			
			if (searchAssignedProductHierarchy != null && searchAssignedProductHierarchy.getParentAssignedProduct() !=null
					&& searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedProductHeader()!= null 
					&& searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedProductHeader().getAssignedProductID()!= null) {
				AssignedProductRef assignedProductRef =  new AssignedProductRef(); 
				assignedProductRef.setAssignedProductID(searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedProductHeader().getAssignedProductID());
				
				AssignedProductHierarchyRef assignedProductHierarchyRef  = new AssignedProductHierarchyRef();
				assignedProductHierarchyRef.setRootAssignedProduct(assignedProductRef);
				
				assignedProductHierarchyRefList.add(assignedProductHierarchyRef);
			}
			
		}
		RetrieveAssignedProductHierarchyOutput out = null;
		if (assignedProductHierarchyRefList.size() > 0) {
		
			out = retrieveAssignedProductHierarchy(assignedProductHierarchyRefList.toArray(new AssignedProductHierarchyRef[assignedProductHierarchyRefList.size()]));
		
		}
		
		return out;
	}
	
	
	public RetrieveOrderSummaryOutput getOrderSummary (String orderReferenceNumber) throws Exception
	{
		RetrieveOrderSummaryOutput out = null;
	    try {
	    	getServiceInvoker();
	    	RetrieveOrderSummaryInput obj = new RetrieveOrderSummaryInput();
			
			OrderRef[] value = new OrderRef[1];
			OrderRef tmp = new OrderRef();
			OrderID val = new OrderID();
			val.setOrderID(orderReferenceNumber);
			tmp.setOrderID(val );
			value[0] = tmp;
			obj.setOrderRef(value );
			
			obj.setAttributesRequired(true);
			
			OrderingContext ctx = getOrderingContext();
		    MaskInfo maskInfo = new MaskInfo();

		    out = serviceInvoker.retrieveOrderSummary(new ApplicationContext(), ctx, obj, maskInfo);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return out;
		
	    
		
	}
	
	
	public RetrieveAssignedProductForConfigurationOutput retrieveAssignedProductForConfiguration(String apId) throws Exception
	{
		
		
		RetrieveAssignedProductForConfigurationInput obj = new RetrieveAssignedProductForConfigurationInput();
		
		AssignedProductRef apRef = new AssignedProductRef();
		AssignedProductID assignedProductID = new AssignedProductID();
		
		EntityIDBaseAssist.setOMSMasterId(assignedProductID, apId);
		
		apRef.setAssignedProductID(assignedProductID);
		
		AssignedProductRef[] assignedProductRef = new AssignedProductRef[1];
		assignedProductRef[0] = apRef;
		obj.setAssignedProducts(assignedProductRef);
		
		OrderingContext ctx = getOrderingContext();
		
		obj.setOrderActionType(new OrderActionTypeRVT("CH"));
	    	
	    RetrieveAssignedProductForConfigurationOutput out = null;
		
	    CompatibilityChecksOptions compatibilityChecksOptions = new CompatibilityChecksOptions();
		compatibilityChecksOptions.setActivity(new ActivityNameRVT("OMSActivities.acActOAAttributes"));
		obj.setCompatibilityChecksOptions(compatibilityChecksOptions);
		
		getServiceInvoker();
		out = serviceInvoker.retrieveAssignedProductForConfiguration(new ApplicationContext(), ctx, obj, new MaskInfo());

		
		return out;
		
	}
	

	public RetrieveCatalogProductForConfigurationOutput retrieveCatalogProductForConfiguration(String custID, String offID) throws Exception
	{
		
		
		RetrieveCatalogProductForConfigurationInput input = new RetrieveCatalogProductForConfigurationInput();
		input.setQuotationRequired(false);
		
		ImplementedOffer off = new ImplementedOffer();
		ImplementedOffer[] value = new ImplementedOffer[1];
		
		AvailableOffer val = new AvailableOffer();
		
		OrderingCatalogOffer pcOffer = new OrderingCatalogOffer();
		OrderingCatalogOfferID offerId = new OrderingCatalogOfferID();
		offerId.setCatalogOfferID(offID);
		EntityIDBaseAssist.setOMSMasterId(offerId, offID);
		pcOffer.setCatalogOfferID(offerId );
		val.setCatalogOffer(pcOffer );
		
		off.setAvailableOffer(val );
		
		value[0] = off;
		input.setCatalogProducts(value );
		
		
		CompatibilityChecksOptions compatibilityChecksOptions = new CompatibilityChecksOptions();
		compatibilityChecksOptions.setActivity(new ActivityNameRVT("NegotiateProdGen"));
		input.setProductCatalogConfigurationOptions(compatibilityChecksOptions);
		
		CustomerProfileID custProfileID = new CustomerProfileID();
		EntityIDBaseAssist.setOMSMasterId(custProfileID, custID);
	    CustomerProfileRef customerProfileRef = new CustomerProfileRef();
	    customerProfileRef.setCustomerProfileID(custProfileID);
	    
	    input.setValidateProductsCardinality(false);
	    com.amdocs.cih.services.customerprofile.lib.CustomerProfile customerProfile = new com.amdocs.cih.services.customerprofile.lib.CustomerProfile();
	    customerProfile.setCustomerProfileRef(customerProfileRef);
	    input.setCustomerProfile(customerProfile );
	    
	    RetrieveCatalogProductForConfigurationOutput out = serviceInvoker.retrieveCatalogProductForConfiguration
			    (new ApplicationContext(), getOrderingContext(), input, new MaskInfo());
		return out;
		
	}

	public OrderView createROMediaroomOrder(String customerId, RetrieveAssignedProductForConfigurationOutput retrieveApOut, RetrieveCatalogProductForConfigurationOutput retrieveCatalogProductForConfigurationOutput, List<String> itemsToAdd)  throws Exception {
		 CreateOrderInput order = new CreateOrderInput();
		    
		    OrderActionData[] oaArr = new OrderActionData[1];
		    
		    OrderActionData oa = new OrderActionData();
		    
		    ImplementedProduct[] products = configureProducts(retrieveApOut.getAssignedProductsForConfiguration(0).getImplementedProduct(), retrieveCatalogProductForConfigurationOutput, itemsToAdd);
		    
//		    ImplementedProduct[] products = new ImplementedProduct[2];
//		    
//		    ImplementedProduct hsProd =  retrieveApOut.getAssignedProductsForConfiguration(0).getImplementedProduct();
//		    ImplementedProduct hsMainComp = getImplementedProduct(hsProd, hsCompCid);
//		    hsMainComp.setAction(new ImplementedProductActionRVT(updateAction));
//	    	
//	    	hsMainComp.setTemporaryID(getNewID());
//		    
//		    products[0] = retrieveApOut.getAssignedProductsForConfiguration(0).getImplementedProduct();
//		    products[1] = getImplementedProduct(retrieveCatalogProductForConfigurationOutput.getNewProductsForConfiguration(0).getImplementedOffer().getChildImplementedProducts(1),tvMainCompCid);;
		    
		    ReplaceOffersData[] roDataArr = new ReplaceOffersData[1];
		    ReplaceOffersData roData = new ReplaceOffersData();
		    ImplementedProduct[] implPrArr = new ImplementedProduct[1];
		    implPrArr[0] = products[0];
		    roData.setSourceProducts(implPrArr);
		    
		    ImplementedOffer[] tarOffArr = new ImplementedOffer[1];
		    ImplementedOffer implOff = new ImplementedOffer();
		    AvailableOffer avOff = new AvailableOffer();
		    
		    OrderingCatalogOffer pcOffer = new OrderingCatalogOffer();
		    OrderingCatalogOfferID offerId = new OrderingCatalogOfferID();
			offerId.setCatalogOfferID("64965");
			EntityIDBaseAssist.setOMSMasterId(offerId, "64965");
			pcOffer.setCatalogOfferID(offerId );
		    
		    avOff.setCatalogOffer(pcOffer);
			implOff.setAvailableOffer(avOff );
			ImplementedProduct[] tvArr = new ImplementedProduct[1];
			tvArr[0] = products[1];
			implOff.setChildImplementedProducts(tvArr);
		    tarOffArr[0] = implOff ;
			roData.setTargetOffers(tarOffArr );
		    
		    roDataArr[0] = roData;
		    
			oa.setImplementedOffers(null );
			oa.setReplaceOffersData(roDataArr);
		    
			OrderAction oaInfo = new OrderAction();
			OrderActionDetails oaDetails = new OrderActionDetails();
			
			OrderActionTypeRVT prRVT = new OrderActionTypeRVT(oaType);
			
			oaDetails.setActionType(prRVT );
			oaDetails.setDueDate(new Date());
			oaDetails.setServiceRequireDate(new Date());
			
			oaInfo.setOrderActionDetails(oaDetails );
			//TODO - return
			//oaInfo.setBypassClearanceX9(true);
			oa.setOrderActionInfo(oaInfo);
			
		    oaArr[0] = oa;
			order.setOrderActions(oaArr );
		    
			Order ord = new Order();
			
			CustomerProfileHeader customer = new CustomerProfileHeader();
			CustomerProfileID custProfileID = new CustomerProfileID();
			EntityIDBaseAssist.setOMSMasterId(custProfileID, customerId);
			
			customer.setCustomerProfileID(custProfileID );
			ord.setCustomerProfileHeader(customer);
			
			//TODO - return
			//ord.setIsAutoX9(true);
			
			OrderDataSettingOptions options = new OrderDataSettingOptions();
			options.setCompatibilityCheckRequired(false);
			options.setEligibilityCheckRequired(false);
			options.setQuotationRequired(false);
			options.setAttributesRequired(true);//req to return av prod
			options.setValidateProductsCardinality(false);
			order.setBehaviorOptions(options );
			
			order.setOrder(ord );
	
		    CreateOrderOutput result=new CreateOrderOutput(); 
		    
		    result = serviceInvoker.createOrder(new ApplicationContext(), getOrderingContext(), order, new MaskInfo());
		    
		    //TODO call
		    CheckOrderedProductCompatibilityOutput rulesOut = null;
		    
		    if (result != null && result.getCreatedOrderData()!= null && result.getCreatedOrderData().getOrderHeader() != null)
		    {
		    	
		    	
		    	//Check rules
		    	 CheckOrderedProductCompatibilityInput rules = new CheckOrderedProductCompatibilityInput();
				    
				    CompatibilityChecksOptions ruleOptions = new CompatibilityChecksOptions();
				    ruleOptions.setActivity(new ActivityNameRVT("NegotiateProdGen"));
				    ruleOptions.setConfirmationChecksApproved(true);
				    ruleOptions.setRuleValidationStage(new RuleValidationStageRVT("Init"));
				    ruleOptions.setRuleExecutionContextX3("BeforeNegotiation");
					rules.setCheckRulesOptions(ruleOptions );
					
					OrderActionSummary[] oaSummary = result.getCreatedOrderData().getReplaceOfferOrderActionsSummary(0).getOrderActionSummary();
					
					ImplementedProduct[] ipArray = new ImplementedProduct[2];
					ipArray[0] = oaSummary[0].getProduct();
					ipArray[1] = oaSummary[1].getProduct();
					
					
					
					rules.setModifiedProducts(ipArray);
					
					
				rulesOut = serviceInvoker.checkOrderedProductCompatibility(new ApplicationContext(), getOrderingContext(), rules, new MaskInfo());
				
			
				
		    
				
		    }
		    
		    //update order
		    UpdateOrderOutput upOut = null;
		    ImplementedProduct[] ipArr = null;
		    
		    if (rulesOut != null) {
		    	ImplementedProductFeedback[] implementedProductFeedbackArr = rulesOut.getModifiedProductsCompatibilityCheckResults();
		    	
		    	products = new ImplementedProduct[2];
				products[0] = implementedProductFeedbackArr[0].getImplementedProduct();
				products[1] = implementedProductFeedbackArr[1].getImplementedProduct();
		    	
		    	
		    	ipArr =  configureProducts(result,  itemsToAdd);
		    } else {
		    	if ( result != null && result.getCreatedOrderData()!= null && result.getCreatedOrderData().getOrderHeader() != null)
			    {
		    		ipArr =  configureProducts(result,  itemsToAdd);
			    }
		    	
		    }
		    
		    
		  
		    
		    if (ipArr != null && ipArr.length == 2)
		    {

		    	ImplementedProduct hs = ipArr[0];
		    	
		    	ImplementedProduct tv = ipArr[1];
		    	
		    	
				configureMediaroomReference(hs, tv, tv.getAvailableProduct() );
		    	
		    	UpdateOrderInput upOrd = new UpdateOrderInput();
				

				
		    	Order upOrder = new Order();
		    	
		    	OrderActionData[] oaArray = new OrderActionData[2];
			    
			    upOrder.setOrderID(result.getCreatedOrderData().getOrderHeader().getOrderID());
			    
			    OrderActionData hsOaData = new OrderActionData();
			    
			    ImplementedProduct[] hsProdArr = new ImplementedProduct[1];
			    hsProdArr[0] = hs;

				//			    modProdArr[1] = tv;
			    OrderActionDetails hsOaDet = new OrderActionDetails();
			    hsOaDet.setActionType(new OrderActionTypeRVT("RG"));
			    oaInfo.setOrderActionDetails(hsOaDet);
			    OrderAction oaInfoHS = new OrderAction();
			    oaInfoHS.setOrderActionDetails(hsOaDet);
				
			    hsOaData.setOrderActionInfo(oaInfoHS );
			    
				hsOaData.setModifiedProducts(hsProdArr );
			    
				oaArray[0] = hsOaData ;
				
				 OrderActionData ttvOaData = new OrderActionData();
				    
			    ImplementedProduct[] tvProdArr = new ImplementedProduct[1];
			    tvProdArr[0] = tv;
//				    modProdArr[1] = tv;
			    
			    OrderAction oaInfoTV = new OrderAction();
			    OrderActionDetails tvOaDet = new OrderActionDetails();
			    tvOaDet.setActionType(new OrderActionTypeRVT("PR"));
				oaInfoTV.setOrderActionDetails(tvOaDet );
				ttvOaData.setOrderActionInfo(oaInfoTV );
			    ttvOaData.setModifiedProducts(tvProdArr );
				oaArray[1] = ttvOaData ;
			    
			    upOrd.setOrder(upOrder);
			    options.setAttributesRequired(true);//required to return av prod
			    options.setCompatibilityCheckRequired(false);//rules
			    options.setReturnValidationMessages(true);
			    options.setValidateProductsCardinality(true);
			    CompatibilityChecksOptions compOption = new CompatibilityChecksOptions();
			    compOption.setActivity(new ActivityNameRVT("NegotiateProdGen"));
			    compOption.setConfirmationChecksApproved(true);
				options.setCompatibilityChecksOptions(compOption );
			    upOrd.setBehaviorOptions(options);
			    
			    upOrd.setOrderActions(oaArray);
						    
//			    System.out.println("API UpdateOrder Start");
//			    System.out.println("UpdateOrder Rquest: HS product: ");
//			    LogHelper.getInstance().printProduct(hs, 0, true, false);
//			    
//			    System.out.println("UpdateOrder Rquest: TTV product: ");
//			    LogHelper.getInstance().printProduct(tv, 0, true, false);
			    
			    upOut = serviceInvoker.updateOrder(new ApplicationContext(), getOrderingContext(), upOrd, new MaskInfo());
		    }
		    
		    OrderView orderView = null;
		    if (upOut != null && upOut.getUpdatedOrderData()!= null && upOut.getUpdatedOrderData().getOrderHeader() != null)
		    {
		    	
		    	
		    	//Check rules
		    	 CheckOrderedProductCompatibilityInput rules = new CheckOrderedProductCompatibilityInput();
				    
				    CompatibilityChecksOptions ruleOptions = new CompatibilityChecksOptions();
				    ruleOptions.setActivity(new ActivityNameRVT("NegotiateProdGen"));
				    ruleOptions.setConfirmationChecksApproved(true);
				    ruleOptions.setRuleValidationStage(new RuleValidationStageRVT("Complete"));
				    ruleOptions.setRuleExecutionContextX3("AfterNegotiation");
					rules.setCheckRulesOptions(ruleOptions );
					
					OrderActionSummary[] oaSummary = result.getCreatedOrderData().getReplaceOfferOrderActionsSummary(0).getOrderActionSummary();
					
					ImplementedProduct[] ipArray = new ImplementedProduct[2];
					ipArray[0] = oaSummary[0].getProduct();
					ipArray[1] = oaSummary[1].getProduct();
					
					
					
					rules.setModifiedProducts(ipArray);
					
					
				rulesOut = serviceInvoker.checkOrderedProductCompatibility(new ApplicationContext(), getOrderingContext(), rules, new MaskInfo());
				
			
				if (rulesOut.getModifiedProductsCompatibilityCheckResults() != null && rulesOut.getModifiedProductsCompatibilityCheckResults().length > 0)
				{
					
					orderView = ViewHandler
							.getOrderViewForRetrieveOrderSummary( upOut.getUpdatedOrderData(), rulesOut);
					
				}
		    
				
		    } else {
		    	orderView = ViewHandler
						.getOrderViewForRetrieveOrderSummary( upOut.getUpdatedOrderData());
		    }
		    
		return orderView;
		}
	
	//Catalog IDs
	private final String packCID = "25";
	private final String hsCompCid = "1";
	private final String packCompCaption = "64905";
	private final String hsMainCompCid = "22";
	private final String tvMainCompCid = "62";
	private final String hsProdCid = "51285";
	private final String ttvProdCid = "111";
	private final String comboCid = "66992";
	private final String iptvComboCid = "20815398";
	private final String iptvUltimum = "20941038";
	
	private final String oaType = "RO";
	private final String addAction = "AD";
	private final String remAction = "RM";
	private final String updateAction = "UP";
	private Map hsPackNames = null;
	//Attributes
	private final String localSer = "30614";//Internet type
	private final String commitmentPeriod = "30627";//Commitment period
	private final String installingParty = "30901";//installing Party
	//values
	private final String localSerValue = "NON";//Internet type
	private final String commitmentPeriodValue = "MTM";//Commitment period
	private final String installingPartyValue = "SELFIN";//installing Party
	
	
		private ImplementedProduct[] configureProducts(ImplementedProduct hs,RetrieveCatalogProductForConfigurationOutput out,  List<String> itemsToAdd) 
		{
			ImplementedProductActionRVT updateActionRVT = new ImplementedProductActionRVT(updateAction);
	//		ImplementedProduct hs = convert2ImplProd(hs.getAssignedProductHierarchies(0));//getImplementedProduct(apHout, hsMainCompCid);
			ImplementedProduct hsMainComp = getImplementedProduct(hs, hsCompCid);
	    	hsMainComp.setAction(updateActionRVT);
	    	
	    	hsMainComp.setAction(new ImplementedProductActionRVT(updateAction));
//	    	hsMainComp.setTemporaryID(getNewID());
		    
		    ImplementedProduct hsPackComp = new ImplementedProduct();
		    hsPackComp = getChildImplComponent (hs, packCID);
		    hsPackComp.setAction(new ImplementedProductActionRVT(updateAction));
//		    hsPackComp.setTemporaryID(getNewID());
		    
		    AvailableProduct availProdComp = hs.getAvailableProduct();//getAvailableProduct (roOut.getImplementedOffers(0).getAvailableOffer(), hsMainCompCid);//roOut.getImplementedOffers(0).getAvailableOffer().getChildAvailableProducts(0).getParent();
		    if ( availProdComp == null)
		    {
		    	availProdComp = getAvailableProduct(out.getNewProductsForConfiguration(0).getImplementedOffer().getAvailableOffer(), hsMainCompCid);
		    }
		    AvailableProduct turboAvailComp = getAvailableProduct(availProdComp,packCompCaption);
		    AvailableProduct hsProdCompAv = getAvailableProduct(availProdComp,hsCompCid);
		    
//		    hsPackNames = new HashMap();
//			hsPackNames.put("54155", "High Speed Enhanced (v1)");
//			hsPackNames.put("54255", "High Speed Extreme");
//			hsPackNames.put("61551", "High Speed Turbo");
//			hsPackNames.put("61611", "High Speed Turbo 25");
//			hsPackNames.put("21045748", "Internet 100");
//			hsPackNames.put("20860979", "Internet 50");
//		    
//		    configurePacks(hsPackComp,turboAvailComp);
//		    
//		    
//		    if (hsMainComp != null && hsProdCompAv != null && hsProdCompAv.getCatalogProduct() != null &&
//		    		hsProdCompAv.getCatalogProduct().getCatalogProductDetails() != null)
//			{
//				
//				
//				ImplementedAttribute localSerImplAttr = convert2ImplAttr(localSer, localSerValue, hsMainComp, hsProdCompAv);
//				ImplementedAttribute commitmentPeriodImplAttr = convert2ImplAttr(commitmentPeriod, commitmentPeriodValue, hsMainComp, hsProdCompAv);
//				ImplementedAttribute installingPartyImplAttr = convert2ImplAttr(installingParty, installingPartyValue, hsMainComp, hsProdCompAv);
//				
//				ImplementedAttribute[] implAttrArr1 = new ImplementedAttribute[3];
//				implAttrArr1[0] = localSerImplAttr;
//				implAttrArr1[1] = commitmentPeriodImplAttr;
//				implAttrArr1[2] = installingPartyImplAttr;
//				hsMainComp.setImplementedAttributes(implAttrArr1);
//				
//			}
//		    handlePricing(hs, hs.getAvailableProduct());
		    
		    resetAction(hs);
		    
	    	ImplementedProduct tv = getProductFromOffer(out.getNewProductsForConfiguration(0).getImplementedOffer(), tvMainCompCid);
//	    	configureTTV(tv,tvAvail, itemsToAdd); //TODO
	    	
	    	handlePricing(tv,tv.getAvailableProduct());
	    	
	    	hs.setAvailableProduct(null);
	    	
	    	ImplementedProduct[] products = new ImplementedProduct[2];
			products [0] = hs;
	    	products[1] = tv;
	    	
	    	return products;
		}
		
		private ImplementedProduct getProductFromOffer(ImplementedOffer implementedOffer, String cid) {
			ImplementedProduct children[] =  implementedOffer.getChildImplementedProducts();
			if (children != null)
			{
				for (ImplementedProduct child : children) {
					if(cid.equals(EntityIDBaseAssist.getOMSMasterId(child.getAvailableProduct().getCatalogProduct().getCatalogProductID()))) {
						return child;
					}
				}
			}
			return null;
		}
		
		private ImplementedProduct getImplementedProduct(ImplementedProduct prod, String packCaption) 
		{
			
			if(prod.getName() != null && prod.getName().getValueAsString().equals(packCaption))
				return prod;
			else
			{
				if (prod.getAvailableProduct() != null && prod.getAvailableProduct().getCatalogProduct() != null &&
						packCaption.equals(EntityIDBaseAssist.getOMSMasterId(prod.getAvailableProduct().getCatalogProduct().getCatalogProductID())))
						return prod;
			}
			
			ImplementedProduct children[] =  prod.getChildImplementedProducts();
			if (children != null)
			{
				for (ImplementedProduct child : children) {
					prod = getImplementedProduct(child,packCaption);
					if (prod != null) {
						return prod;
					}
				}
			}
			
				return null;
		}

		private ImplementedProduct getChildImplComponent(ImplementedProduct hsConfig, String cid) 
		{
			ImplementedProduct child = null;
			
			if (hsConfig != null && hsConfig.getChildImplementedProducts() != null && hsConfig.getChildImplementedProducts().length > 0)
			{
				for (int i = 0; i < hsConfig.getChildImplementedProducts().length; i++)
				{
					child = hsConfig.getChildImplementedProducts()[i];
					if (child.getName() != null && child.getName().getValueAsString() != null && cid.equals(child.getName().getValueAsString()))
						return child;
					else if (child.getAvailableProduct() != null && child.getAvailableProduct().getCatalogProduct() != null)
					{
						String catID = EntityIDBaseAssist.getOMSMasterId(child.getAvailableProduct().getCatalogProduct().getCatalogProductID());
						if (cid.equals(catID))
								return child;
					}
				}
			}
			
			return null;
		}
		
		private AvailableProduct getAvailableProduct(AvailableOffer availableOffer,String packCompCaption2) 
		{
			// TODO Auto-generated method stub
			AvailableProduct retVal = null;
			if (availableOffer != null && availableOffer.getChildAvailableProducts() != null && availableOffer.getChildAvailableProducts().length > 0)
			{
				for (int i = 0; i < availableOffer.getChildAvailableProducts().length; i++)
				{
					retVal = availableOffer.getChildAvailableProducts(i);
					if (retVal.getCatalogProduct() != null && retVal.getCatalogProduct().getCatalogProductID() != null && 
							packCompCaption2.equals(EntityIDBaseAssist.getOMSMasterId(retVal.getCatalogProduct().getCatalogProductID())))
						return retVal;
				}
				
			}
			
			return null;
		}
		
		private AvailableProduct getAvailableProduct(AvailableProduct availProd,String packCompCaption) 
		{
			
			/*if (availProd.getCatalogProduct().getCatalogProductDetails().getCatalogProductName().getLocalizedValue().equals(packCompCaption))
				return availProd;*/
			
			if (availProd.getCatalogProduct().getCatalogProductDetails().getCatalogProductName().getValueAsString().equals(packCompCaption))
				return availProd;
			
			AvailableProduct children[] =  availProd.getChildAvailableProducts();
			for (AvailableProduct child : children) {
				availProd = getAvailableProduct(child,packCompCaption);
				if (availProd != null) {
					return availProd;
				}
			}
		
			
			return null;
		}
		
		private void configurePacks(ImplementedProduct packs,AvailableProduct turboComp) 
		{
			if (packs != null)
			{
				ImplementedProduct comp = convert2ImplProd(turboComp, packs);
				
				//Commitment period
				OrderingCatalogAttribute commitmentPeriodAttr = getOrderingCatalogAttribute(turboComp, commitmentPeriod);
				
				
				ImplementedAttribute commitmentPeriodImplAttr = convert2ImplAttr(commitmentPeriod, commitmentPeriodValue, comp, turboComp);
				
				ImplementedAttribute[] implAttrArr = new ImplementedAttribute[1];
				implAttrArr[0] = commitmentPeriodImplAttr;
				comp.setImplementedAttributes(implAttrArr);
//				comp.setTemporaryID(getNewID());
				comp.setAction(new ImplementedProductActionRVT(updateAction));
				
		    	
			}
			
		}
		
		private ImplementedProduct convert2ImplProd(AvailableProduct avProd, ImplementedProduct parent)
		{
			ImplementedProduct comp = new ImplementedProduct();
			comp.setAvailableProduct(avProd);
			comp.setAction(new ImplementedProductActionRVT(addAction));
			comp.setParentImplementedProduct(parent);
			comp.setTemporaryID(getNewID());
			List<ImplementedProduct> childList = new ArrayList<ImplementedProduct>();
			ImplementedProduct children[] = parent.getChildImplementedProducts();
			boolean isPresent = false;
			if (children != null) 
			{
		    	 for (ImplementedProduct child : children) 
		    	 {
		    		 //child found already in PC
		    		 if (EntityIDBaseAssist.getOMSMasterId(child.getAvailableProduct().getCatalogProduct().getCatalogProductID()).equals(
		    				 EntityIDBaseAssist.getOMSMasterId(avProd.getCatalogProduct().getCatalogProductID()))
		    				 && !(new ImplementedProductActionRVT(remAction)).equals(child.getAction())) {
		    			 return child;
		    		 }
//		    		 if (hsPackNames != null && !hsPackNames.entrySet().isEmpty() && child.getName() != null &&
//		    				 hsPackNames.get(child.getName().getValueAsString()) != null)
//		    		 {
//		    			child.setAction(new ImplementedProductActionRVT(remAction)); 
//		    			isPresent = true;
//		    		 }
//		    		 else if (hsPackNames != null && !hsPackNames.entrySet().isEmpty() && child.getAvailableProduct() != null
//		    				 && child.getAvailableProduct().getCatalogProduct() != null
//		    				 && hsPackNames.get(EntityIDBaseAssist.getOMSMasterId(child.getAvailableProduct().getCatalogProduct().getCatalogProductID())) != null)
//		    		 {
//			    			child.setAction(new ImplementedProductActionRVT(remAction));
//			    			isPresent = true;
//			    	 }
//		    		 if (!isPresent && ((child.getName() != null && !comboCid.equals(child.getName().getValueAsString())) || (child.getAvailableProduct() != null
//		    				 && child.getAvailableProduct().getCatalogProduct() != null && !comboCid.equals(EntityIDBaseAssist.getOMSMasterId(child.getAvailableProduct().getCatalogProduct().getCatalogProductID()))) ))
//		    		 {
		    			 childList.add(child);
//		    		 }
				}
			}
			comp.setName(avProd.getCatalogProduct().getCatalogProductDetails().getCatalogProductName());
			
			//handle mandatory price plans
			if( comp.getAvailableProduct() != null && comp.getAvailableProduct().getCatalogPricings() != null) {
				List<ImplementedPricingReference> implementedPricingReferenceList = new ArrayList<ImplementedPricingReference>();
				for (OrderingCatalogPricing orderingCatalogPricing : comp.getAvailableProduct().getCatalogPricings()) {
					if (orderingCatalogPricing.getCatalogPricingDetails().isMandatory()) {
						ImplementedPricingReference implementedPricingReference = new ImplementedPricingReference();
						implementedPricingReference.setAction(new ImplementedProductActionRVT(addAction));
						implementedPricingReference.setCatalogPricing(orderingCatalogPricing);
						
						implementedPricingReferenceList.add(implementedPricingReference);
						
					}
				}
			
				if (implementedPricingReferenceList.size() > 0) {
					comp.setImplementedPricings(implementedPricingReferenceList.toArray(new ImplementedPricingReference[implementedPricingReferenceList.size()]));
				}
			}
			
			
			childList.add(comp);
			parent.setChildImplementedProducts(childList.toArray(new ImplementedProduct[0]));
			parent.setAction(new ImplementedProductActionRVT(updateAction));
			
			return comp;
		}
		
		private void removeAllPacks(ImplementedProduct parent, Map packList)
		{

			ImplementedProduct children[] = parent.getChildImplementedProducts();
			List <ImplementedProduct>  childrenList = new ArrayList<ImplementedProduct>();
			boolean isPresent = false;
			if (children != null) 
			{
		    	 for (ImplementedProduct child : children) 
		    	 {
		    		 if (packList.get(EntityIDBaseAssist.getOMSMasterId(child.getAvailableProduct().getCatalogProduct().getCatalogProductID())) == null) {

		    			 childrenList.add(child);
		    		 }
		    	 }
		    	 
		    	 parent.setChildImplementedProducts(childrenList.toArray(new ImplementedProduct[childrenList.size()]));
			}

		}
		
		private void setPacksAttributes(ImplementedProduct parent)
		{

			ImplementedProduct children[] = parent.getChildImplementedProducts();
			if (children != null) 
			{
		    	 for (ImplementedProduct child : children) 
		    	 {
		    		 if (child.getAvailableProduct().getServiceTypeX3() == null || "ADSP".equals(child.getAvailableProduct().getServiceTypeX3())) {

		    			//Commitment period
		 				ImplementedAttribute commitmentPeriodImplAttr = convert2ImplAttr(commitmentPeriod, commitmentPeriodValue, child, child.getAvailableProduct());
		 				
		 				ImplementedAttribute[] implAttrArr = new ImplementedAttribute[1];
		 				implAttrArr[0] = commitmentPeriodImplAttr;
		 				child.setImplementedAttributes(implAttrArr);
//		 				child.setTemporaryID(getNewID());
		 				child.setAction(new ImplementedProductActionRVT(updateAction));
		    		 }
		    	 }
		    	 
			}

		}
		
		private ImplementedAttribute convert2ImplAttr(String attrCID, String value, ImplementedProduct comp, AvailableProduct avProd)
		{
//			String commitmentPeriod = "30627";//Commitment period
			OrderingCatalogAttribute availAttribute = getOrderingCatalogAttribute(avProd, attrCID);
			
			ImplementedAttribute implAttribute = new ImplementedAttribute();
			
			implAttribute.setCatalogAttribute(availAttribute);
			implAttribute.setSelectedValue(value);
			
			/*ImplementedAttribute[] implAttrArr = new ImplementedAttribute[1];
			implAttrArr[0] = commitmentPeriodImplAttr;
			comp.setImplementedAttributes(implAttrArr);*/
			
			return implAttribute;
		}
		
		private OrderingCatalogAttribute getOrderingCatalogAttribute(AvailableProduct hsCompProdAV, String attrCid) 
		{
			OrderingCatalogAttribute[] atrs = hsCompProdAV.getCatalogProduct().getCatalogProductDetails().getCatalogAttributes();
			OrderingCatalogAttribute tmp = null;
			for (int i = 0; i < atrs.length; i++)
			{
				tmp = atrs[i];
				if (attrCid.equals(tmp.getCatalogAttributeDetails().getName().getValueAsString()))
					break;
			}
			return tmp;
		}
		
		
		private void handlePricing(ImplementedProduct implPr,AvailableProduct availProd) 
		{
			AvailableProduct currAv = null;
			if (implPr.getName() != null)
				currAv = getAvailableProduct(availProd, implPr.getName().getValueAsString());
			else
			{
				String catID = EntityIDBaseAssist.getOMSMasterId(implPr.getAvailableProduct().getCatalogProduct().getCatalogProductID());
				currAv = getAvailableProduct(availProd, catID);
			}
			
			if (currAv != null)
				copyCatalogPricingToImplProd(implPr, currAv);
			else
			{
				if (availProd.getCatalogPricings() != null && availProd.getCatalogPricings().length > 0 && implPr.getImplementedPricings() != null && implPr.getImplementedPricings().length > 0)
				{
					OrderingCatalogPricing catPrice = null;
					ImplementedPricingReference implPrice = null;
					for (int i = 0; i < implPr.getImplementedPricings().length; i++)
					{
						for (int j = 0; j < availProd.getCatalogPricings().length; j++)
						{
							catPrice = availProd.getCatalogPricings(j);
							implPrice = implPr.getImplementedPricings(i);
							if (implPrice.getName() != null && implPrice.getName().getLocalizedValue() != null && !implPrice.getName().getLocalizedValue().isEmpty()
									&& catPrice.getCatalogPricingDetails().getName().getLocalizedValue() != null && catPrice.getCatalogPricingDetails().getName().getLocalizedValue().equals(implPrice.getName().getLocalizedValue()))
							{
								implPrice.setCatalogPricing(catPrice);
							}
						}
					}
				}
			}
			
			ImplementedProduct children[] =  implPr.getChildImplementedProducts();
			if (children != null)
			{
				for (ImplementedProduct child : children) 
				{
					handlePricing(child,availProd);
				}
			}
		}
		
		private void resetAction(ImplementedProduct implPr) 
		{
			//if (implPr.getAvailableProduct() != null && implPr.getAvailableProduct().getCatalogProduct() != null && 
				//	!"61551".equals(implPr.getAvailableProduct().getCatalogProduct().getCatalogProductID().getCatalogProductID()))
//			if (implPr.getAction() != null && !remAction.equals(implPr.getAction().getValueAsString()))
				implPr.setAction(new ImplementedProductActionRVT("UP"));
			
			if (implPr != null) {
				ImplementedProduct children[] = implPr.getChildImplementedProducts();
				if (children != null && children.length > 0)
				{
					for (ImplementedProduct child : children) 
					{
						resetAction(child);
					}
				}
			}
		}
		
		private void resetActionAD(ImplementedProduct implPr) 
		{
			//if (implPr.getAvailableProduct() != null && implPr.getAvailableProduct().getCatalogProduct() != null && 
				//	!"61551".equals(implPr.getAvailableProduct().getCatalogProduct().getCatalogProductID().getCatalogProductID()))
//			if (implPr.getAction() != null && !remAction.equals(implPr.getAction().getValueAsString()))
				implPr.setAction(new ImplementedProductActionRVT("AD"));
			
			if (implPr != null) {
				ImplementedProduct children[] = implPr.getChildImplementedProducts();
				if (children != null && children.length > 0)
				{
					for (ImplementedProduct child : children) 
					{
						resetAction(child);
					}
				}
			}
		}
		
		
		private void configureTTV(ImplementedProduct implementedProduct,AvailableProduct availableProduct,  List<String> itemsToAdd) 
		{
			
			/*30506 installationIndicator
			commitmentPeriodInYears		30627 for ttv
			*/
			ImplementedAttribute[] tvAtrs = new ImplementedAttribute[2];
			ImplementedAttribute installationIndicator = convert2ImplAttr("30506", "CUST", implementedProduct, availableProduct);
			ImplementedAttribute commitmentPeriodInYears = convert2ImplAttr("30627", commitmentPeriodValue, implementedProduct, availableProduct);
			tvAtrs[0] = installationIndicator;
			tvAtrs[1] = commitmentPeriodInYears;
			implementedProduct.setImplementedAttributes(tvAtrs);
			
			findAndUpdateComponent(implementedProduct,getAvailableProduct(availableProduct,"62138"));	    
		    findAndUpdateComponent(implementedProduct,getAvailableProduct(availableProduct,"50380"));
		    findAndUpdateComponent(implementedProduct,getAvailableProduct(availableProduct,"325"));
		    findAndUpdateComponent(implementedProduct,getAvailableProduct(availableProduct,"50441"));
		    findAndUpdateComponent(implementedProduct,getAvailableProduct(availableProduct,"326"));
		    findAndUpdateComponent(implementedProduct,getAvailableProduct(availableProduct,"53434"));
		    
		    //set attrs
		    ImplementedProduct equipmentImpl = getImplementedProduct(implementedProduct, "51360"); //Equipment
		    AvailableProduct equipmentAvail = getAvailableProduct(availableProduct, "51360"); //Equipment
		    
		    /*
		     * 30470
	31163
	30465
		     */
		    
		    ImplementedAttribute micCode = convert2ImplAttr("30661", "1638338", equipmentImpl, equipmentAvail);
		    ImplementedAttribute delivMethod = convert2ImplAttr("30465", "INS", equipmentImpl, equipmentAvail);
		    ImplementedAttribute acquisitionType = convert2ImplAttr("30470", "RE", equipmentImpl, equipmentAvail);
		    ImplementedAttribute description = convert2ImplAttr("31163", "Optik HD PVR", equipmentImpl, equipmentAvail);
			
		    ImplementedAttribute[] implAttrArr = new ImplementedAttribute[4];
			implAttrArr[0] = micCode;
			implAttrArr[1] = delivMethod;
			implAttrArr[2] = acquisitionType;
			implAttrArr[3] = description;

			equipmentImpl.setImplementedAttributes(implAttrArr);
			equipmentImpl.setAction(new ImplementedProductActionRVT(updateAction));
			
			//set attrs
		    ImplementedProduct techQuestImpl = getImplementedProduct(implementedProduct, "50380"); //Tech questioner 
		    AvailableProduct techQuestAvail = getAvailableProduct(availableProduct, "50380"); //Tech questioner 
		    
		    ImplementedAttribute Alternate_CBR = convert2ImplAttr("20410298", "6044322345", techQuestImpl, techQuestAvail);
//		    ImplementedAttribute Profile_Upgrade = convert2ImplAttr("20788568", "No", techQuestImpl, techQuestAvail);
		    ImplementedAttribute Rent_Or_Own = convert2ImplAttr("83488", "Own", techQuestImpl, techQuestAvail);
		    ImplementedAttribute On_Site_Manager = convert2ImplAttr("83510", "No", techQuestImpl, techQuestAvail);
		    ImplementedAttribute TV_Sets = convert2ImplAttr("20410318", "1", techQuestImpl, techQuestAvail);
		    ImplementedAttribute CBR = convert2ImplAttr("20410288", "6044321234", techQuestImpl, techQuestAvail);
		    ImplementedAttribute Adult_Presence = convert2ImplAttr("20410308", "Yes", techQuestImpl, techQuestAvail);
//		    ImplementedAttribute Upgrade_Confirm = convert2ImplAttr("20788578", "No", techQuestImpl, techQuestAvail);
		    
		    List<ImplementedAttribute> techQuestImplAttrArr = new ArrayList<ImplementedAttribute>();
		    techQuestImplAttrArr.add(Alternate_CBR);
		    techQuestImplAttrArr.add(Rent_Or_Own);
		    techQuestImplAttrArr.add(On_Site_Manager);
		    techQuestImplAttrArr.add(TV_Sets);
		    techQuestImplAttrArr.add(CBR);
		    techQuestImplAttrArr.add(Adult_Presence);
//		    techQuestImplAttrArr.add(Upgrade_Confirm);

			
			techQuestImpl.setImplementedAttributes(techQuestImplAttrArr.toArray(new ImplementedAttribute[techQuestImplAttrArr.size()]));
			
			
//			if (isConfigureCombos)
//	    	{
//				findAndUpdateComponent(implementedProduct,getAvailableProduct(availableProduct,comboCid));
//				findAndUpdateComponent(implementedProduct,getAvailableProduct(availableProduct,iptvComboCid));
//				if(isToConfigureDoubleCombos)
//					findAndUpdateComponent(implementedProduct,getAvailableProduct(availableProduct,iptvUltimum));
//				//configureCombos(implementedProduct, implementedProduct.getAvailableProduct());
//	    	}
			
			//number 0 is HP pack CID
			for (int i = 1; i <itemsToAdd.size();i++) {
				String cid = itemsToAdd.get(i);
				findAndUpdateComponent(implementedProduct,getAvailableProduct(availableProduct,cid));
			}
			
			handlePricing(implementedProduct,availableProduct);
			resetAction(implementedProduct); //TODO check
			
		}
		
		
		private void copyCatalogPricingToImplProd(ImplementedProduct hsCompProd,AvailableProduct hsCompProdAV) 
		{
			if (hsCompProd.getImplementedPricings() != null && hsCompProd.getImplementedPricings().length > 0) 
			{
				Map<String, ImplementedPricingReference> implPricingMap = new HashMap<String, ImplementedPricingReference>();
				for (int i = 0; i < hsCompProd.getImplementedPricings().length; i++) 
				{
					ImplementedPricingReference implPrice = hsCompProd.getImplementedPricings()[i];
					if (implPrice.getName() != null)
						implPricingMap.put(implPrice.getName().getValueAsString(),implPrice);
					else
					{
						implPricingMap.put(EntityIDBaseAssist.getOMSMasterId(implPrice.getCatalogPricing().getCatalogPricingID()),implPrice);
					}
				}
				boolean catPricingNotFound = true;
				Map<String, OrderingCatalogPricing> catalogPricingMap = new HashMap<String, OrderingCatalogPricing>();
				for (int i = 0; i < hsCompProdAV.getCatalogPricings().length; i++) 
				{
					catPricingNotFound = false;
					OrderingCatalogPricing catalogPrice = hsCompProdAV.getCatalogPricings()[i];
					catalogPricingMap.put(catalogPrice.getCatalogPricingDetails().getName().getValueAsString(), catalogPrice);
				}
				if (catPricingNotFound)
				{
					for (int i = 0; i < hsCompProd.getImplementedPricings().length; i++) 
					{
						ImplementedPricingReference implPrice = hsCompProd.getImplementedPricings()[i];
						if (implPrice.getCatalogPricing() != null && implPrice.getCatalogPricing().getCatalogPricingDetails() != null
								&& implPrice.getCatalogPricing().getCatalogPricingID() != null)
						{
							catalogPricingMap.put(EntityIDBaseAssist.getOMSMasterId(implPrice.getCatalogPricing().getCatalogPricingID()), implPrice.getCatalogPricing());
						}
					}
				}
				for (Iterator it = implPricingMap.entrySet().iterator(); it.hasNext();) 
				{
					Entry ent = (Entry) it.next();
					ImplementedPricingReference tmp = (ImplementedPricingReference) ent.getValue();
					if (tmp.getName() != null)
						tmp.setCatalogPricing(catalogPricingMap.get(tmp.getName().getValueAsString()));
					else
					{
						tmp.setCatalogPricing(catalogPricingMap.get(EntityIDBaseAssist.getOMSMasterId(tmp.getCatalogPricing().getCatalogPricingID())));
					}
				}
			}
		}
		
		
		private boolean findAndUpdateComponent(ImplementedProduct implPr, AvailableProduct availableComponent) 
		{
			String parentCid  = EntityIDBaseAssist.getOMSMasterId(availableComponent.getParent().getCatalogProduct().getCatalogProductID());
			
			boolean isFound = false;
			if ((implPr.getParentImplementedProduct() == null && availableComponent.getParent().getParent() == null) //for main component CID come for product
					|| (implPr.getAvailableProduct() != null && implPr.getAvailableProduct().getCatalogProduct() != null && 
					parentCid.equals(EntityIDBaseAssist.getOMSMasterId(implPr.getAvailableProduct().getCatalogProduct().getCatalogProductID())))
					|| (implPr.getName() != null && parentCid.equals(implPr.getName().getValueAsString())))
				isFound = true;
			
			if (isFound)
			{
				convert2ImplProd( availableComponent, implPr);
				return true;
			}
			ImplementedProduct children[] =  implPr.getChildImplementedProducts();
			if (children != null) {
				for (ImplementedProduct child : children) 
				{
					if(findAndUpdateComponent(child,availableComponent)) {
						return true;
					}
				}
			}
				
			return false;	
				
				
		}
		
		
		private void configureMediaroomReference(ImplementedProduct implementedProductHS, ImplementedProduct implementedProductTv, AvailableProduct availableProduct) 
		{
			/*ImplementedProduct implementedProductTv = getImplementedProductFromOffer(implOffer, "62");
			ImplementedProduct implementedProductHS = getImplementedProductFromOffer(implOffer, "22");
			AvailableProduct availableProduct = getAvailableProductFromOffer(availableOffer, "62");
			*/
			
			ImplementedReferenceRelation implementedReferenceRelation = new ImplementedReferenceRelation();
			AvailableReferenceRelation availableReferenceRelation = availableProduct.getAvailableReferenceRelations(0);
			
			implementedReferenceRelation.setAvailableReferenceRelation(availableReferenceRelation);
			implementedReferenceRelation.setPerformedAction(new ReferenceRelationActionRVT("AD"));
			
			implementedReferenceRelation.setReferencedProduct(implementedProductHS);
			
			ImplementedReferenceRelation[] implementedReferenceRelationArr = new ImplementedReferenceRelation[1];
			implementedReferenceRelationArr[0] = implementedReferenceRelation;
			implementedProductTv.setImplementedReferenceRelations(implementedReferenceRelationArr);
			
		}
		
		
		private ImplementedProduct[] configureProducts (CreateOrderOutput result,   List<String> itemsToAdd)
		{
			OrderActionSummary[] oaSummary = result.getCreatedOrderData().getReplaceOfferOrderActionsSummary(0).getOrderActionSummary();
			ImplementedProduct hs = getImplementedProduct(oaSummary, hsMainCompCid);
			ImplementedProduct tv = getImplementedProduct(oaSummary, tvMainCompCid);
			
			ImplementedProduct[] products = new ImplementedProduct[2];
			products[0] = hs;
			products[1] = tv;
			
			return configureProducts(products, itemsToAdd);
		}
		
		
		private ImplementedProduct[] configureProducts (ImplementedProduct[] products,   List<String> itemsToAdd)
		{
			ImplementedProduct hs = products[0];
	    	ImplementedProductActionRVT updateActionRVT = new ImplementedProductActionRVT(updateAction);
			hs.setAction(updateActionRVT );
			
			ImplementedProduct tv = products[1];
			tv.setAction(updateActionRVT);
			
			
	    	
	    	ImplementedProduct hsMainComp = getImplementedProduct(hs, hsCompCid);
	    	hsMainComp.setAction(updateActionRVT);
	    	
	    	hsMainComp.setAction(new ImplementedProductActionRVT(updateAction));
//	    	hsMainComp.setTemporaryID(getNewID());
		    
		    ImplementedProduct hsPackComp = new ImplementedProduct();
		    hsPackComp = getChildImplComponent (hs, packCID);
		    hsPackComp.setAction(new ImplementedProductActionRVT(updateAction));
//		    hsPackComp.setTemporaryID(getNewID());
		    
		    AvailableProduct availProdComp = hs.getAvailableProduct();//getAvailableProduct (roOut.getImplementedOffers(0).getAvailableOffer(), hsMainCompCid);//roOut.getImplementedOffers(0).getAvailableOffer().getChildAvailableProducts(0).getParent();
		    
		    AvailableProduct turboAvailComp = getAvailableProduct(availProdComp,packCompCaption);
		    AvailableProduct hsProdCompAv = getAvailableProduct(availProdComp,hsCompCid);
		    
		    hsPackNames = new HashMap();
			hsPackNames.put("54155", "High Speed Enhanced (v1)");
			hsPackNames.put("54255", "High Speed Extreme");
			hsPackNames.put("61551", "High Speed Turbo");
			hsPackNames.put("61611", "High Speed Turbo 25");
			hsPackNames.put("21045748", "Internet 100");
			hsPackNames.put("20860979", "Internet 50");
		    
		    removeAllPacks(hsPackComp, hsPackNames);
		    findAndUpdateComponent(hs,getAvailableProduct(availProdComp,itemsToAdd.get(0)));
		    setPacksAttributes(hsPackComp);
//		    configurePacks(hsPackComp,turboAvailComp);
		    
		    
		    if (hsMainComp != null && hsProdCompAv != null && hsProdCompAv.getCatalogProduct() != null &&
		    		hsProdCompAv.getCatalogProduct().getCatalogProductDetails() != null)
			{
				
				
				ImplementedAttribute localSerImplAttr = convert2ImplAttr(localSer, localSerValue, hsMainComp, hsProdCompAv);
				ImplementedAttribute commitmentPeriodImplAttr = convert2ImplAttr(commitmentPeriod, commitmentPeriodValue, hsMainComp, hsProdCompAv);
				ImplementedAttribute installingPartyImplAttr = convert2ImplAttr(installingParty, installingPartyValue, hsMainComp, hsProdCompAv);
				
				ImplementedAttribute[] implAttrArr1 = new ImplementedAttribute[3];
				implAttrArr1[0] = localSerImplAttr;
				implAttrArr1[1] = commitmentPeriodImplAttr;
				implAttrArr1[2] = installingPartyImplAttr;
				hsMainComp.setImplementedAttributes(implAttrArr1);
				
			}
		    handlePricing(hs, hs.getAvailableProduct());
		    
		    configureTTV(tv,tv.getAvailableProduct(), itemsToAdd);
	    	
		    resetAction(hs);
			resetAction(tv);
		    
	    	handlePricing(tv,tv.getAvailableProduct());
	    	
	    	
	    	
	    	products[0] = hs;
	    	products[1] = tv;
	    	
	    	return products;
		}
		
		private ImplementedProduct getImplementedProduct(OrderActionSummary[] oaSummary, String prodCid) 
		{
			if (oaSummary != null && oaSummary.length > 0)
			{
				for (OrderActionSummary oaSum : oaSummary)
				{
					if (oaSum.getProduct().getName() != null && oaSum.getProduct().getName().getValueAsString() != null &&
							prodCid.equals(oaSum.getProduct().getName().getValueAsString()))
						return oaSum.getProduct();
					else if (oaSum.getProduct().getAvailableProduct() != null && oaSum.getProduct().getAvailableProduct().getCatalogProduct() != null
							&& oaSum.getProduct().getAvailableProduct().getCatalogProduct().getCatalogProductID() != null
							&& prodCid.equals(EntityIDBaseAssist.getOMSMasterId(oaSum.getProduct().getAvailableProduct().getCatalogProduct().getCatalogProductID())))
					{
						return oaSum.getProduct();
					}
						
				}
			}
			return null;
		}

		public void setNewDueDate(String[] orderactionIds, String appointmentDate) {
			Date dueDate = null;
			try {
				dueDate =new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.ENGLISH).parse(appointmentDate);
			} catch (ParseException e) {
				return;
			}
			
			RetrieveOrderActionInput  retrieveOrderActionInput  = new RetrieveOrderActionInput ();
			OrderActionRef[] orderActionRefArr = new OrderActionRef[2];
			
			OrderActionRef orderActionRef1 = new OrderActionRef();
			OrderActionID id1 =  new OrderActionID();
			EntityIDBaseAssist.setOMSMasterId(id1, orderactionIds[0]);
			orderActionRef1.setOrderActionID(id1);
			orderActionRefArr[0] =orderActionRef1; 
			
			OrderActionRef orderActionRef2 = new OrderActionRef();
			OrderActionID id2 =  new OrderActionID();
			EntityIDBaseAssist.setOMSMasterId(id2, orderactionIds[1]);
			orderActionRef2.setOrderActionID(id2);
			orderActionRefArr[1] =orderActionRef2; 
			
			retrieveOrderActionInput.setOrderActionRef(orderActionRefArr);
			
			try {
				getServiceInvoker();
				RetrieveOrderActionOutput retrieveOrderActionOutput = serviceInvoker.retrieveOrderAction(new ApplicationContext(),
								getOrderingContext(), retrieveOrderActionInput, new MaskInfo());
				OrderAction[] orderActionArr = retrieveOrderActionOutput.getOrderAction();
				
				for (OrderAction orderAction : orderActionArr) {
					
					orderAction.getOrderActionDetails().setDueDate(dueDate);
					orderAction.getOrderActionDetails().setServiceRequireDate(dueDate);
					UpdateOrderActionInput updateOrderActionInput = new UpdateOrderActionInput();
					
					updateOrderActionInput.setOrderAction(orderAction);
					updateOrderActionInput.setConfirmationChecksApproved(true);

					serviceInvoker.updateOrderAction(new ApplicationContext(),
							getOrderingContext(), updateOrderActionInput, new MaskInfo());
					
				}
			
			} catch (InvalidUsageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalEntityStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EntityLockedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EntityKeyNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DataNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CreateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
		}

		public void closeInitialContext() {
			if (ic != null) {
				try {
					ic.close();
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		public static List<CihFilterInfo> buildNewCihFilterInfoForInstatement(String field, Object[] values) 
		{
			List<CihFilterInfo> filters = new ArrayList<CihFilterInfo>(values.length);
			
//			for (int i =0; i< values.length;i++)
			{
				CihFilterInfo myInfo = new SearchFilterInfo();
				myInfo.setFilterField(field);
				myInfo.setFilterOpCode(CihStringOperator.IN.getOpCode());
				myInfo.setFilterValueList(values);
				filters.add(myInfo);
				((SearchFilterInfo)myInfo).setLogicalConnector("AND");				
			}
			return filters;
		}
			
}