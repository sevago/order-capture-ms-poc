package com.example.oc.mspoc.util.legacy;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.amdocs.cih.common.core.EntityIDBaseAssist;
import com.amdocs.cih.common.datatypes.Message;
import com.amdocs.cih.services.assignedproduct.lib.SearchAssignedProductHierarchy;
import com.amdocs.cih.services.order.lib.OrderSummary;
import com.amdocs.cih.services.order.lib.OrderedOfferSummary;
import com.amdocs.cih.services.order.lib.ReplaceOfferOrderActionsSummary;
import com.amdocs.cih.services.orderaction.lib.OrderActionSummary;
import com.amdocs.cih.services.orderingactivities.lib.CheckOrderedProductCompatibilityOutput;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedAttribute;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedOfferFeedback;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedPricingReference;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedProduct;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedProductFeedback;
import com.example.oc.mspoc.model.legacy.AttributeView;
import com.example.oc.mspoc.model.legacy.CompatibilityMessage;
import com.example.oc.mspoc.model.legacy.ComponentView;
import com.example.oc.mspoc.model.legacy.HSView;
import com.example.oc.mspoc.model.legacy.InstallationAddress;
import com.example.oc.mspoc.model.legacy.OrderActionView;
import com.example.oc.mspoc.model.legacy.OrderView;
import com.example.oc.mspoc.model.legacy.OrderingDashboard;
import com.example.oc.mspoc.model.legacy.ProductView;
import com.example.oc.mspoc.model.legacy.SingleLineView;
import com.example.oc.mspoc.model.legacy.TVView;

public class ViewHandler {
	
	public static final DecimalFormat df = new DecimalFormat("0.00");
	
	public static ProductView[] getProductView(SearchAssignedProductHierarchy[] searchAssignedProductHierarchies) {
		
		List<ProductView> productViewList = new LinkedList<ProductView>(); 
		for (SearchAssignedProductHierarchy searchAssignedProductHierarchy : searchAssignedProductHierarchies) {
			ProductView productView = null;
			String productName = searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedProductHeader().getName().getLocalizedValue();
			if ("Single Line".equals(productName)) {
				productView = new SingleLineView();
			} else if ("High Speed".equals(productName)){
				productView = new HSView();
			} else if ("TELUS TV".equals(productName)){
				productView = new TVView();
			} else {
				productView = new ProductView();
			}
			productView.setProductName(searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedProductHeader().getName().getLocalizedValue());
			productView.setServiceId(searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedProductHeader().getServiceID());
			if (searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedProductHeader().getInstallationAddress() != null) {
				productView.setInstallationAddress(searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedProductHeader().getInstallationAddress().getFormattedAddress1());
			}
			
			productView.setApId(EntityIDBaseAssist.getOMSMasterId(searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedProductHeader().getAssignedProductID()));
			
			productView.buildComponentMap(searchAssignedProductHierarchy);
			
			productViewList.add(productView);
		}
		
		return productViewList.toArray(new ProductView[productViewList.size()]);
	}
	
	public static OrderView getOrderViewForRetrieveOrderSummary(OrderSummary orderSummary, CheckOrderedProductCompatibilityOutput checkOrderedProductCompatibilityOutput) {
		OrderView orderView = new OrderView();
		
		orderView.setOrderId(EntityIDBaseAssist.getOMSMasterId(orderSummary.getOrderHeader().getOrderID()));
		orderView.setReferenceNumber(orderSummary.getOrderHeader().getOrderID().getOrderID());
		orderView.setCustomerId(orderSummary.getOrderHeader().getCustomerProfileHeader().getCustomerProfileID().getCustomerId());
		orderView.setOrderMode(orderSummary.getOrderHeader().getOrderMode().getValueAsString());

			List<OrderActionView> orderActionViewList = new LinkedList<OrderActionView>();	
			
			OrderedOfferSummary[] orderedOfferSummaries = orderSummary.getProvideOrderActionsSummary();
			
			if (orderedOfferSummaries != null) {
				for (OrderedOfferSummary orderedOfferSummary : orderedOfferSummaries) {
					for (OrderActionSummary orderActionSummary : orderedOfferSummary.getOrderActionSummary()) {
						OrderActionView orderActionView = createOrderActionViewFromOrderActionSummary(orderActionSummary, orderView);
						orderActionViewList.add(orderActionView);
					}
				}
			}
			if (orderSummary.getNonProvideOrderActionsSummary() != null) {
				for (OrderActionSummary orderActionSummary : orderSummary.getNonProvideOrderActionsSummary()) {
					OrderActionView orderActionView = createOrderActionViewFromOrderActionSummary(orderActionSummary, orderView);
					orderActionViewList.add(orderActionView );
				}
				
				
			}
			
			if (orderSummary.getReplaceOfferOrderActionsSummary() != null) {
				for (ReplaceOfferOrderActionsSummary replaceOfferOrderActionsSummary : orderSummary.getReplaceOfferOrderActionsSummary()) {
					if (replaceOfferOrderActionsSummary.getOrderActionSummary() != null) {
						for (OrderActionSummary orderActionSummary : replaceOfferOrderActionsSummary.getOrderActionSummary()) {
							OrderActionView orderActionView = createOrderActionViewFromOrderActionSummary(orderActionSummary, orderView);
							orderActionViewList.add(orderActionView );
						}
					}
					
				}
			}

			
			orderView.setOrderActionView(orderActionViewList.toArray(new OrderActionView[orderActionViewList.size()]));
			
			if (checkOrderedProductCompatibilityOutput != null) {
				List<CompatibilityMessage> compatibilityMessageList = new LinkedList<CompatibilityMessage>();
				if( checkOrderedProductCompatibilityOutput.getReplacedOfferProductsCompatibilityCheckResult() != null ) {
					for (ImplementedOfferFeedback implementedOfferFeedback : checkOrderedProductCompatibilityOutput.getReplacedOfferProductsCompatibilityCheckResult()) {
						//TODO						
					}
				}
				if (checkOrderedProductCompatibilityOutput.getModifiedProductsCompatibilityCheckResults() != null) {
					for (ImplementedProductFeedback implementedProductFeedback : checkOrderedProductCompatibilityOutput.getModifiedProductsCompatibilityCheckResults()) {
						List<CompatibilityMessage> compatibilityMessages = createCompatibilityMessage(implementedProductFeedback);
						compatibilityMessageList.addAll(compatibilityMessages);
					}
					
				}
				
				orderView.setCompatabilityMessages(compatibilityMessageList.toArray(new CompatibilityMessage[compatibilityMessageList.size()]));
			}

		
		return orderView;
	}
	
	private static List<CompatibilityMessage> createCompatibilityMessage(ImplementedProductFeedback implementedProductFeedback) {
		List<CompatibilityMessage> compatibilityMessageList = new LinkedList<CompatibilityMessage>();
		
		if (implementedProductFeedback.getValidationFeedback() != null) {
			for (Message message : implementedProductFeedback.getValidationFeedback().getMessages()) {
				CompatibilityMessage compatibilityMessage = new CompatibilityMessage();
				compatibilityMessage.setProductName(implementedProductFeedback.getImplementedProduct().getAvailableProduct().getCatalogProduct().getCatalogProductDetails().getCatalogProductName().getLocalizedValue());
				compatibilityMessage.setSeverity(message.getMessageSeverity().getLocalizedValue());
				compatibilityMessage.setMessageText(message.getMessageText());
				compatibilityMessageList.add(compatibilityMessage);
			}
		}
		
		
		
		return compatibilityMessageList;
	}

	private static OrderActionView createOrderActionViewFromOrderActionSummary (OrderActionSummary orderActionSummary, OrderView orderView) {
		OrderActionView orderActionView = new OrderActionView();
		orderActionView.setOrderActionId(EntityIDBaseAssist.getOMSMasterId(orderActionSummary.getOrderActionHeader().getOrderActionID()));
		orderActionView.setReferenceNumber(orderActionSummary.getOrderActionHeader().getOrderActionID().getOrderActionID());
		orderActionView.setOrderActionType(orderActionSummary.getOrderActionHeader().getOrderActionType().getLocalizedValue());
		orderActionView.setOrderActionStatus(orderActionSummary.getOrderActionHeader().getOrderActionStatus().getLocalizedValue());
		orderActionView.setProductApId(EntityIDBaseAssist.getOMSMasterId(orderActionSummary.getProduct().getAssignedProductID()));
		orderActionView.setDueDate(OrderingDashboard.df.format(orderActionSummary.getOrderActionHeader().getDueDate()));
		orderActionView.setOverdue((Utils.getEndOfDay(new Date())).compareTo(Utils.getEndOfDay(orderActionSummary.getOrderActionHeader().getDueDate())) >  0);
		
//		if (orderView.isDraft() && orderActionSummary.getOrderActionHeader().getCaseIdX3() != null
//				&& orderActionSummary.getOrderActionHeader().getCaseIdX3().length() > 0) {
			orderView.setDraft(true);
//		}
				
		ComponentView product = new ComponentView();
		product.setServiceId(orderActionSummary.getProduct().getServiceID());
//		handleAddress(product, orderActionSummary.getProduct());
		createProductView(product, orderActionSummary.getProduct());
		orderActionView.setImplementedProduct(product);
		orderActionView.setProduct(orderActionSummary.getProduct().getAvailableProduct().getCatalogProduct().getCatalogProductDetails().getCatalogProductName().getLocalizedValue());
		
		product.setPictureLink(null);
		
		return orderActionView;
	}

	private static void createProductView(ComponentView componentView,
			ImplementedProduct product2) {
		componentView.setName(product2.getAvailableProduct().getCatalogProduct().getCatalogProductDetails().getCatalogProductName().getLocalizedValue());
		componentView.setId(EntityIDBaseAssist.getOMSMasterId(product2.getAssignedProductID()));
		componentView.setItemType("CO");
		componentView.setActivity(product2.getAction().getLocalizedValue());
		componentView.setOneTimeTotal(df.format(product2.getTotalPrices().getTotalOC()));
		componentView.setMonthlyTotal(df.format(product2.getTotalPrices().getTotalRC()));
		componentView.setPreviousMonthlyTotal(df.format(product2.getTotalPrices().getOriginalTotalRC()));
//		componentView.setServiceType(product2.getAvailableProduct().getServiceTypeX3());
		
		for (ImplementedAttribute implementedAttribute : product2.getImplementedAttributes()) {
			if (ScenarioHandler.isAttributeVisible(product2, implementedAttribute)) {
				componentView.addAttribute(new AttributeView(implementedAttribute.getCatalogAttribute().getCatalogAttributeDetails().getName().getLocalizedValue(), ScenarioHandler.getAttributeDecodeValue(implementedAttribute) ));
			}
		}
		
		//children
		for (ImplementedProduct childProduct : product2.getChildImplementedProducts()) {
			ComponentView componentViewChild = new ComponentView();
			
			createProductView(componentViewChild, childProduct);
			
			componentView.addSubItems(componentViewChild);
		}
		
		//price plans
		for (ImplementedPricingReference implementedPricingReference : product2.getImplementedPricings()) {
			if (implementedPricingReference.getCatalogPricing() != null) {
				ComponentView pricePlan = new ComponentView();
				
				pricePlan.setItemType("PP");
				pricePlan.setName(implementedPricingReference.getCatalogPricing().getCatalogPricingDetails().getName().getLocalizedValue());
				pricePlan.setActivity(implementedPricingReference.getAction().getLocalizedValue());
				pricePlan.setOneTimeTotal(df.format(implementedPricingReference.getTotalPrices().getTotalOC()));
				pricePlan.setMonthlyTotal(df.format(implementedPricingReference.getTotalPrices().getTotalRC()));
				pricePlan.setPreviousMonthlyTotal(df.format(implementedPricingReference.getTotalPrices().getOriginalTotalRC()));
				componentView.addSubItems(pricePlan);
			}
		}
		
	}

	private static void handleAddress(ComponentView componentView,
			ImplementedProduct product) {
		InstallationAddress installationAddress = new InstallationAddress();
		installationAddress.setCity(product.getInstallationAddress().getCity());
		installationAddress.setCoID(product.getInstallationAddress().getCoIDX3());
		installationAddress.setFmsAddressID(product.getInstallationAddress().getFmsAddressIDX3());
		installationAddress.setPostcode(product.getInstallationAddress().getPostcode());
		installationAddress.setState(product.getInstallationAddress().getState().getValueAsString());
		installationAddress.setStreetName(product.getInstallationAddress().getStreetName());
		installationAddress.setStreetNumber(product.getInstallationAddress().getStreetNumber());
		componentView.setInstallationAddress(installationAddress);
		
	}
	
	public static OrderView getOrderViewForRetrieveOrderSummary(OrderSummary orderSummary) {
		return getOrderViewForRetrieveOrderSummary(orderSummary, null);
	}
			
}
