package com.example.oc.mspoc.model.legacy;

import java.util.LinkedList;
import java.util.List;

import com.example.oc.mspoc.util.legacy.ComponentConfigurator;

public class Component {
	
	public final static char MODE_ADD = 'A';
	public final static char MODE_UPDATE = 'U';
	public final static char MODE_CHECK = 'C';
	
	
	//id
	private String id = null;
	
	//in case ind = true, id will be updated from implemented product
	private boolean isCheckRequired = false;
	
	protected String name = null;
	//A - add/U - update
	private char mode = 'A';
	
	private List<Attribute> attributeList = new LinkedList<Attribute>();

	private List<Pricing> pricingList = new LinkedList<Pricing>();
	
	private List<Component> componentList = new LinkedList<Component>();
	
	private ComponentConfigurator configurator;
	
	@Deprecated
	public Component() {
	}
	
	public Component(String name, char mode) {
		this.name = name;
		if (mode == MODE_ADD || mode == MODE_UPDATE) {
			this.mode = mode;
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isCheckRequired() {
		return isCheckRequired;
	}

	public void setCheckRequired(boolean isCheckRequired) {
		this.isCheckRequired = isCheckRequired;
	}

	
	public List<Component> getComponentList() {
		return componentList;
	}


	public void setComponentList(List<Component> componentList) {
		this.componentList = componentList;
	}
	
	public void addComponentList(Component component) {
		this.componentList.add(component);
	}


	public List<Pricing> getPricingList() {
		return pricingList;
	}


	public void setPricingList(List<Pricing> pricingList) {
		this.pricingList = pricingList;
	}
	
	
	public void addPricing(Pricing pricing) {
		this.pricingList.add(pricing);
	}
	
	public void addPricing(String name) {
		Pricing pricing = new Pricing(); 
		pricing.setName(name);
		this.pricingList.add(pricing);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public char getMode() {
		return mode;
	}


	public void setMode(char mode) {
		this.mode = mode;
	}


	public List<Attribute> getAttributeList() {
		return attributeList;
	}


	public void setAttributeList(List<Attribute> attributeList) {
		this.attributeList = attributeList;
	}

	public void addAttribute(Attribute attribute) {
		this.attributeList.add(attribute);
	}
	
	public void addAttribute(String name, String value) {
		Attribute attribute = new Attribute();
		attribute.setName(name);
		attribute.setValue(value);
		this.attributeList.add(attribute);
	}
	
	public Component addComponent(String name){
		Component component =new Component(name,Component.MODE_ADD);
		addComponentList(component);
		return component;
	}
	
	public Component updateComponent(String name){
		Component component =new Component(name,Component.MODE_UPDATE);
		addComponentList(component);
		return component;
	}
	
	public ComponentConfigurator getConfigurator(){
		if(configurator == null){
			configurator = new ComponentConfigurator(this);
		}
		return configurator;
	}
	
}
