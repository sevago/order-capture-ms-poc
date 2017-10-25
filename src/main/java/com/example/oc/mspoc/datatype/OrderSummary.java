package com.example.oc.mspoc.datatype;

public class OrderSummary {
	
	private String orderId;
    private String actionType;
    private String productType;
	
	public OrderSummary() {}
	
    public OrderSummary(String orderId, String actionType, String productType) {
        this.setOrderId(orderId);
        this.setActionType(actionType);
        this.setProductType(productType);
    }
 
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Override
    public String toString() {
        return "OrderSummary [" + actionType + " " + productType + " " + orderId + "]";
    }
}
