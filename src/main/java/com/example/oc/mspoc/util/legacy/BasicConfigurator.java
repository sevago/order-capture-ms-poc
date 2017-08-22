package com.example.oc.mspoc.util.legacy;

import com.example.oc.mspoc.model.legacy.Component;
import com.example.oc.mspoc.model.legacy.Offer;
import com.example.oc.mspoc.model.legacy.Product;


public class BasicConfigurator {

	
	private Component configuredComponent;
	private Product configuredProduct;
	private Offer configuredOffer;
	/**
	 * @return the configuredComponent
	 */
	public Component getConfiguredComponent() {
		return configuredComponent;
	}
	/**
	 * @param configuredComponent the configuredComponent to set
	 */
	protected void setConfiguredComponent(Component configuredComponent) {
		this.configuredComponent = configuredComponent;
	}
	/**
	 * @return the configuredProduct
	 */
	public Product getConfiguredProduct() {
		return configuredProduct;
	}
	/**
	 * @param configuredProduct the configuredProduct to set
	 */
	protected void setConfiguredProduct(Product configuredProduct) {
		this.configuredProduct = configuredProduct;
	}
	/**
	 * @return the configuredOffer
	 */
	public Offer getConfiguredOffer() {
		return configuredOffer;
	}
	/**
	 * @param configuredOffer the configuredOffer to set
	 */
	protected void setConfiguredOffer(Offer configuredOffer) {
		this.configuredOffer = configuredOffer;
	}
	
}
