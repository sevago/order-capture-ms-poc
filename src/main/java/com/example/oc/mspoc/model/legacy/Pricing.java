package com.example.oc.mspoc.model.legacy;

public class Pricing {
	
	//in case pp should only be checked (not add and not pp) ind should obe set true
	private boolean isCheckOnly = false;
	private boolean isCheckRequired = false;

	private String name = null;
	
	
	public boolean isCheckOnly() {
		return isCheckOnly;
	}

	public void setCheckOnly(boolean isCheckOnly) {
		this.isCheckOnly = isCheckOnly;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCheckRequired() {
		return isCheckRequired;
	}
	public void setCheckRequired(boolean isCheckRequired) {
		this.isCheckRequired = isCheckRequired;
	}

}
