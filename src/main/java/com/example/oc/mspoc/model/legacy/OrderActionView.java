package com.example.oc.mspoc.model.legacy;

public class OrderActionView {
	private String orderActionId;
	private String referenceNumber;
	private String orderActionType;
	private String orderActionStatus;
	private String product;
	private String productApId;
	private String dueDate;
	private boolean isOverdue;
	
	private ComponentView implementedProduct;
	
	public ComponentView getImplementedProduct() {
		return implementedProduct;
	}
	public void setImplementedProduct(ComponentView implementedProduct) {
		this.implementedProduct = implementedProduct;
	}
	public String getOrderActionId() {
		return orderActionId;
	}
	public void setOrderActionId(String orderActionId) {
		this.orderActionId = orderActionId;
	}
	public String getOrderActionType() {
		return orderActionType;
	}
	public void setOrderActionType(String orderActionType) {
		this.orderActionType = orderActionType;
	}
	public String getOrderActionStatus() {
		return orderActionStatus;
	}
	public void setOrderActionStatus(String orderActionStatus) {
		this.orderActionStatus = orderActionStatus;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getProductApId() {
		return productApId;
	}
	public void setProductApId(String productApId) {
		this.productApId = productApId;
	}
	public boolean isOverdue() {
		return isOverdue;
	}
	public void setOverdue(boolean isOverdue) {
		this.isOverdue = isOverdue;
	}
	
}
