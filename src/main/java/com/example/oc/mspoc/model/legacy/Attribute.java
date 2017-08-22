package com.example.oc.mspoc.model.legacy;

public class Attribute {
	
	//in case attribute should only be checked (not add and not pp) ind should obe set true
	private boolean isCheckOnly = false;
	private boolean isCheckRequired = false;
	
	private String name = null;
	private String value = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isCheckOnly() {
		return isCheckOnly;
	}
	public void setCheckOnly(boolean isCheckOnly) {
		this.isCheckOnly = isCheckOnly;
	}
	
	public boolean isCheckRequired() {
		return isCheckRequired;
	}
	public void setCheckRequired(boolean isCheckRequired) {
		this.isCheckRequired = isCheckRequired;
	}
	
}
