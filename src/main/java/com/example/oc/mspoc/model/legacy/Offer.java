package com.example.oc.mspoc.model.legacy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.oc.mspoc.util.legacy.OfferConfigurator;

public class Offer {
	private String name = null;
	private List<Product> productList = null;
	Map<String, Iterator<Product>>  itrMap =  new HashMap<String, Iterator<Product>> ();
	
	private boolean isHandled = false;
	
	private OfferConfigurator configurator;
	
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

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public Product addProduct(Product product) {
		if(productList == null ){
			productList = new LinkedList<Product>(); 
		}
		this.productList.add(product);
		return product;
	}
	
	public Product addProduct(String name) {
		Product product =new Product(name);
		return addProduct(product);		
	}
	
	public  static Offer createOffer(String name) {
		Offer offer = new Offer();
		offer.setName(name);
		return offer;
	}
	
	public Product getNextProduct(String productName){
		Iterator<Product> itr = itrMap.get(name);
		Product pr = null;
		if(itr==null){
			itr = productList.iterator();
			itrMap.put(name,itr);			
		}
		while(itr.hasNext()){
			pr = itr.next();
			if(pr.getName().equals(name)|| pr.getCode().equals(name)){
				return pr;
			}
		}
		return null;
	}
	
	public void resetState(){
		itrMap.clear();
	}
	
	public OfferConfigurator getConfigurator(){
		if(configurator == null){
			configurator = new OfferConfigurator(this);
		}
		return configurator;
	}

}
