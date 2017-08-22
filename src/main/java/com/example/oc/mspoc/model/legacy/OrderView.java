package com.example.oc.mspoc.model.legacy;

public class OrderView {
	private String orderId;
	private String referenceNumber;
	private String status;
	private String statusCode;
	private String customerId;
	private String orderMode;
	private boolean isDraft = true;
	
	private OrderActionView[] orderActionView;
	
	private CompatibilityMessage[] compatabilityMessages;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public OrderActionView[] getOrderActionView() {
		return orderActionView;
	}
	public void setOrderActionView(OrderActionView[] orderActionView) {
		this.orderActionView = orderActionView;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public boolean isDraft() {
		return isDraft;
	}
	public void setDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}
	public String getOrderMode() {
		return orderMode;
	}
	public void setOrderMode(String orderMode) {
		this.orderMode = orderMode;
	}
	public CompatibilityMessage[] getCompatabilityMessages() {
		return compatabilityMessages;
	}
	public void setCompatabilityMessages(
			CompatibilityMessage[] compatabilityMessages) {
		this.compatabilityMessages = compatabilityMessages;
	}
		
}
