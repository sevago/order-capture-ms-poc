package com.example.oc.mspoc.model.legacy;

public class AttributeView {
	
	private String name = null;
	private String value = null;
	
	
	public AttributeView() {
	}
	
	public AttributeView(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
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

}
