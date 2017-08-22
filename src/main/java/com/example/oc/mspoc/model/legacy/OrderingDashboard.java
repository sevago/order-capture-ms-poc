package com.example.oc.mspoc.model.legacy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amdocs.cih.common.core.EntityIDBaseAssist;
import com.amdocs.cih.services.order.lib.SearchOrder;
import com.example.oc.mspoc.util.legacy.Utils;

public class OrderingDashboard {
	
	public static DateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss");
	
	private String customerId;
	private String customerName;
	private ProductView[] products;
	private OrderView[] orderViews;
	
	private String status = "SU";
	private String statusMessage;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public ProductView[] getProducts() {
		return products;
	}
	public void setProducts(ProductView[] products) {
		this.products = products;
	}
	public OrderView[] getOrderViews() {
		return orderViews;
	}
	public void setOrderViews(OrderView[] orderViews) {
		this.orderViews = orderViews;
	}

	
	public static OrderingDashboard createOrderingDashboard(SearchOrder[] searchOrders, ProductView[] products, String customerId){
		
		OrderingDashboard orderingDashboard = new OrderingDashboard();
		if (searchOrders != null && searchOrders.length >0) {
			orderingDashboard.setCustomerId(searchOrders[0].getOrderHeader().getCustomerProfileHeader().getCustomerProfileID().getCustomerId());
			orderingDashboard.setCustomerName(searchOrders[0].getOrderHeader().getCustomerProfileHeader().getCustomerName());
		} else {
			orderingDashboard.setCustomerId(customerId);
			orderingDashboard.setCustomerName("ID: " + customerId);
		}
		orderingDashboard.setProducts(products);
		
		if (searchOrders != null) {
			OrderView[] orderViews = new OrderView[searchOrders.length];
			
			for (int i = 0; i < searchOrders.length; i++) {
				OrderView orderView = new OrderView();
				orderView.setOrderId(EntityIDBaseAssist.getOMSMasterId(searchOrders[i].getOrderHeader().getOrderID()));
				orderView.setReferenceNumber(searchOrders[i].getOrderHeader().getOrderID().getOrderID());
				orderView.setStatus(searchOrders[i].getOrderHeader().getOrderStatus().getLocalizedValue());
				orderView.setStatusCode(searchOrders[i].getOrderHeader().getOrderStatus().getValueAsString());
				orderView.setOrderMode((searchOrders[i].getOrderHeader().getOrderMode().getValueAsString()));
				
				
				
				if (searchOrders[i].getSearchOrderAction() != null) {
					OrderActionView[] orderActionViews = new OrderActionView[searchOrders[i].getSearchOrderAction().length];
					
					for (int j = 0; j < searchOrders[i].getSearchOrderAction().length; j++) {
						OrderActionView orderActionView = new OrderActionView();
						orderActionView.setOrderActionId(EntityIDBaseAssist.getOMSMasterId(searchOrders[i].getSearchOrderAction(j).getOrderActionHeader().getOrderActionID()));
						orderActionView.setReferenceNumber(searchOrders[i].getSearchOrderAction(j).getOrderActionHeader().getOrderActionID().getOrderActionID());
						orderActionView.setOrderActionType(searchOrders[i].getSearchOrderAction(j).getOrderActionHeader().getOrderActionType().getLocalizedValue());
						orderActionView.setOrderActionStatus(searchOrders[i].getSearchOrderAction(j).getOrderActionHeader().getOrderActionStatus().getLocalizedValue());
						orderActionView.setProduct(searchOrders[i].getSearchOrderAction(j).getAssignedProductHeader().getProductType().getLocalizedValue());
						orderActionView.setProductApId(EntityIDBaseAssist.getOMSMasterId(searchOrders[i].getSearchOrderAction(j).getAssignedProductHeader().getAssignedProductID()));
						orderActionView.setDueDate(df.format(searchOrders[i].getSearchOrderAction(j).getOrderActionHeader().getDueDate()));
						orderActionView.setOverdue((Utils.getEndOfDay(new Date())).compareTo(Utils.getEndOfDay(searchOrders[i].getSearchOrderAction(j).getOrderActionHeader().getDueDate())) >  0);
						
//						if (orderView.isDraft() && searchOrders[i].getSearchOrderAction(j).getOrderActionHeader().getCaseIdX3() != null
//								&& searchOrders[i].getSearchOrderAction(j).getOrderActionHeader().getCaseIdX3().length() > 0) {
							orderView.setDraft(true);
//						}
						
						orderActionViews[j] = orderActionView;
					}
					
					orderView.setOrderActionView(orderActionViews);
				}
				
				
				orderViews[i] = orderView;
			}
			
			orderingDashboard.setOrderViews(orderViews);
		
		}
		
		return orderingDashboard;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
}
