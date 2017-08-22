package com.example.oc.mspoc.model.legacy;

import java.util.LinkedList;
import java.util.List;

import com.example.oc.mspoc.util.legacy.ProductConfigurator;

public class Product {
	//id
	private String id = null;
	//in case ind = true, id will be updated from implemented product
	private boolean isCheckRequired = true;
	protected String name = null;
	private String productCode = null;
	
	protected boolean isHandled = false;
	
	private ProductConfigurator configurator;
	
	private List<Component> componentList = new LinkedList<Component>();
	
	private List<Attribute> attributeList = new LinkedList<Attribute>();
	
	public Product(){}
	
	public Product(String name){
		this(null,name);
	}
	
	public Product(String code, String name){
		this.name = name;
		this.productCode =  code;
	}
	
	public List<Attribute> getAttributeList() {
		return attributeList;
	}
	public void setAttributeList(List<Attribute> attributeList) {
		this.attributeList = attributeList;
	}
	public List<Component> getComponentList() {
		return componentList;
	}
	public void setComponentList(List<Component> componentList) {
		this.componentList = componentList;
	}
	
	public void addComponent(Component component) {
		this.componentList.add(component);
	}
	public boolean isHandled() {
		return isHandled;
	}
	public void setHandled(boolean isHandled) {
		this.isHandled = isHandled;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return productCode;
	}
	
	public void setCode(String productCode) {
		this.productCode = productCode;
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
	public Component addComponent(String name){
		Component component =new Component(name,Component.MODE_ADD);
		addComponent(component);
		return component;
	}
	
	public Component updateComponent(String name){
		Component component =new Component(name,Component.MODE_UPDATE);
		addComponent(component);
		return component;
	}
	
	public ProductConfigurator getConfigurator(){
		if(configurator == null){
			configurator = new ProductConfigurator(this);
		}
		return configurator;
	}
	
}
