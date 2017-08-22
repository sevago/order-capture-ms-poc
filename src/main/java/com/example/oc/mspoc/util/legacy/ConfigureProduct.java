package com.example.oc.mspoc.util.legacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.amdocs.cih.services.oms.lib.NavigationOutputBase;
import com.amdocs.cih.services.oms.rvt.domain.ImplementedProductActionRVT;
import com.amdocs.cih.services.orderingactivities.lib.AvailableProduct;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedAttribute;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedPricingReference;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedProduct;
import com.amdocs.cih.services.orderingactivities.lib.OrderingAttributeDisplayInformation;
import com.amdocs.cih.services.orderingproductcatalog.lib.OrderingCatalogAttribute;
import com.amdocs.cih.services.orderingproductcatalog.lib.OrderingCatalogPricing;
import com.amdocs.cih.services.processmanager.lib.StepInstance;
import com.example.oc.mspoc.model.legacy.Component;
import com.example.oc.mspoc.model.legacy.Pricing;
import com.example.oc.mspoc.model.legacy.Product;

public class ConfigureProduct {

	public NavigationOutputBase currentStepOutput;
	public ImplementedProduct product;
	public StepInstance stepInstance;
	
	public Product productConfiguration = null;
	
	//String caption,int num, List<amdocs.oms.cust.junit.datatypes.Attribute> attributeList, List<Pricing> pricingList, List<Component> componentList
	protected void addComponent(Component component){
		AvailableProduct availProd=ProductUtils.getAvailableProductForComponent(product.getAvailableProduct(), component.getName());
		if(availProd != null){
			ImplementedProduct parent =  ProductUtils.getImplementeProduct(product, availProd.getParent());
		
			if (parent!= null){
				ImplementedProduct comp = new ImplementedProduct();
				comp.setAvailableProduct(availProd);
				comp.setAction(new ImplementedProductActionRVT("AD"));
				comp.setParentImplementedProduct(parent);
				comp.setTemporaryID(getNewID());
				List<ImplementedProduct> childList = new ArrayList<ImplementedProduct>();
				ImplementedProduct children[] = parent.getChildImplementedProducts();
				if (children != null) {
			    	 for (ImplementedProduct child : children) {
			    		 childList.add(child);
					}
				}
		    	 childList.add(comp);
		    	 parent.setChildImplementedProducts(childList.toArray(new ImplementedProduct[0]));
		    	 
		    	 //handle mandatory components
		    	 handleMandatoryComponents(comp);
		    	 
		    	 
		    	 //handle attributes
		    	 createCompAttributes(comp);
		    	 if(component.getAttributeList() !=null && component.getAttributeList().size()>0){
		    		 updateCompAttributes(comp, component.getAttributeList());
		    	 }
		    	 
		    	 
		    	 //ADD pricing
		    	 for (Pricing pricing : component.getPricingList()) {
		    		 if (!pricing.isCheckOnly()) {
		    			 addPricePlan(comp, availProd, pricing.getName());
		    		 }
				}
		    	 
		    	//handle sub components
					if (component.getComponentList() != null && component.getComponentList().size() > 0) {
						
						for (Component child : component.getComponentList()) {
							ConfigureProduct configureProduct = new ConfigureProduct();
							
							Product tempProduct = new Product();
							tempProduct.addComponent(child);
							configureProduct.productConfiguration =  tempProduct;
							
							configureProduct.product = comp;
							
							configureProduct.execute();
						}
						
					}
					
					
					//update id on component if check is required
					if (component.isCheckRequired()) {
						component.setId(comp.getTemporaryID());
					}
		    	 
			}
			else{
				System.out.println();
			}
		}
		else{
			System.out.println("Could not find.");
		}
	}
	
	
	
	protected void updateComponent(Component comp){
		
		ImplementedProduct component = ProductUtils.getImplementeProductByName(product, comp.getName());
		
		if(comp.getAttributeList() !=null && comp.getAttributeList().size()>0){
   		 updateCompAttributes(component, comp.getAttributeList());
   	 	}
		if (comp.getPricingList() != null && comp.getPricingList().size() > 0) {
			AvailableProduct availProd=ProductUtils.getAvailableProduct(product.getAvailableProduct(), comp.getName());
			for (Pricing pricing : comp.getPricingList()) {
				if (!pricing.isCheckOnly()) {
					addPricePlan(component, availProd, pricing.getName());
				}
			}
		}
		
		if (comp.getComponentList() != null && comp.getComponentList().size() > 0) {
			
			for (Component child : comp.getComponentList()) {
				ConfigureProduct configureProduct = new ConfigureProduct();
				
				Product tempProduct = new Product();
				tempProduct.addComponent(child);
				configureProduct.productConfiguration =  tempProduct;
				
				configureProduct.product = component;
				
				configureProduct.execute();
			}
			
		}
		
	}
	
	
	public synchronized String getNewID()
	{
		String uniqueID = UUID.randomUUID().toString();		
		return "C" + uniqueID;
		
	}
	
	public void createCompAttributes(ImplementedProduct comp) {
		
		if (comp.getChildImplementedProducts() != null) {
			for (ImplementedProduct childImplementedProduct : comp.getChildImplementedProducts()) {
				createCompAttributes(childImplementedProduct);
			}
		}
		
		OrderingCatalogAttribute attrs[] = comp.getAvailableProduct().getCatalogProduct().getCatalogProductDetails().getCatalogAttributes();
		List<ImplementedAttribute> iAttr = new ArrayList<ImplementedAttribute>();
		
		//prepare mandatory attribute map
		OrderingAttributeDisplayInformation[] orderingAttributeDisplayInformationArr = comp.getAvailableProduct().getProductDisplayInformation().getAttributesDisplayInformation();
		Map<String,OrderingAttributeDisplayInformation> mandatoryAttrMap = new HashMap<String,OrderingAttributeDisplayInformation>();
		
		for (OrderingAttributeDisplayInformation orderingAttributeDisplayInformation : orderingAttributeDisplayInformationArr) {
			if (orderingAttributeDisplayInformation.isMandatory()) {
				mandatoryAttrMap.put(orderingAttributeDisplayInformation.getCatalogAttributeID(), orderingAttributeDisplayInformation);
			}
		}
		
		int i=0;
		for (OrderingCatalogAttribute attr : attrs) {
			
			String value = null;
			
			if (comp.getImplementedAttributes() != null) {
				for (ImplementedAttribute implementedAttribute : comp.getImplementedAttributes()) {
					if (attr.getCatalogAttributeID().equals(implementedAttribute.getCatalogAttribute().getCatalogAttributeID())
							&& implementedAttribute.getSelectedValue() != null && !"".equals(implementedAttribute.getSelectedValue())
							&& !"PLS".equals(implementedAttribute.getSelectedValue())
							&& !"Please Select".equals(implementedAttribute.getSelectedValue())
							&& !"PleaseSelect".equals(implementedAttribute.getSelectedValue())) {
						value = implementedAttribute.getSelectedValue();
						break;
					}
				}
			}
			
			
			
			OrderingAttributeDisplayInformation attDispInfo = mandatoryAttrMap.get(attr.getCatalogAttributeID());
			/*if (attDispInfo != null && !attDispInfo.isEnabled()) {
				continue attrList;
			}*/
			
			ImplementedAttribute iAttrribute= new ImplementedAttribute();
			iAttrribute.setCatalogAttribute(attr);
			
			if ("None".equals(comp.getAction().getLocalizedValue())) {
				comp.setAction(new ImplementedProductActionRVT("UP"));
			}
			
			if (value != null) {
				iAttrribute.setSelectedValue(value);
				iAttr.add(iAttrribute);
				continue;
			}
			
			
			if (attr.getCatalogAttributeDetails().getDefaultValue() == null || "".equals(attr.getCatalogAttributeDetails().getDefaultValue())
					|| "PLS".equals(attr.getCatalogAttributeDetails().getDefaultValue())
					|| "Please Select".equals(attr.getCatalogAttributeDetails().getDefaultValue())
					|| "PleaseSelect".equals(attr.getCatalogAttributeDetails().getDefaultValue())) {
				if (attDispInfo != null) {
					
					if (attr.getCatalogAttributeDetails().getAttributeDataType().getAttributeDataTypeDetails().getValidValues() != null
							&& attr.getCatalogAttributeDetails().getAttributeDataType().getAttributeDataTypeDetails().getValidValues().length > 0 ) {
						//handle "Please select" value
						if (("PLS".equals(attr.getCatalogAttributeDetails().getAttributeDataType().getAttributeDataTypeDetails().getValidValues()[0].getCode())
								|| "Please Select".equals(attr.getCatalogAttributeDetails().getAttributeDataType().getAttributeDataTypeDetails().getValidValues()[0].getCode())
										|| "PleaseSelect".equals(attr.getCatalogAttributeDetails().getAttributeDataType().getAttributeDataTypeDetails().getValidValues()[0].getCode()))
								&& attr.getCatalogAttributeDetails().getAttributeDataType().getAttributeDataTypeDetails().getValidValues().length > 1) {
							iAttrribute.setSelectedValue(attr.getCatalogAttributeDetails().getAttributeDataType().getAttributeDataTypeDetails().getValidValues()[1].getCode());
						} else {
							iAttrribute.setSelectedValue(attr.getCatalogAttributeDetails().getAttributeDataType().getAttributeDataTypeDetails().getValidValues()[0].getCode());
						}
					} else {
						iAttrribute.setSelectedValue("1");
					}
					
				}
			} else {
				iAttrribute.setSelectedValue(attr.getCatalogAttributeDetails().getDefaultValue());
			}

			i++;
			
			iAttr.add(iAttrribute);
		}	
		
		comp.setImplementedAttributes(iAttr.toArray(new ImplementedAttribute[0]));
	}

	public void updateCompAttributes(ImplementedProduct comp,
			List<com.example.oc.mspoc.model.legacy.Attribute> attributeList) {
		
		for (com.example.oc.mspoc.model.legacy.Attribute attribute : attributeList) {
			if (!attribute.isCheckOnly()) {
				ImplementedAttribute attr = ProductUtils.getImplementedAttribute(comp, attribute.getName());
				if(attr!=null){
					attr.setSelectedValue(attribute.getValue());
					comp.setAction(new ImplementedProductActionRVT("UP"));
				}
			}
		}
		
	}
		
	
	public void execute() 
	{
		List<Component> componentList = productConfiguration.getComponentList();
		for (Component component : componentList) {
			if (component.getMode() == 'A') {
				addComponent(component);
			} else if (component.getMode() == 'U') {
				updateComponent(component);
			}
			else if (component.getMode() == 'C') { //check
				updateForCheck(component);
			}
			
		}
		
		
		
	}

	private void updateForCheck(Component component) {

		ImplementedProduct implementedProduct = ProductUtils.getImplementeProductByName(product, component.getName());
		
		if (component.isCheckRequired()) {
			component.setId(implementedProduct.getTemporaryID());
		}
		
		if (component.getComponentList() != null && component.getComponentList().size() > 0) {
			
			for (Component child : component.getComponentList()) {
				
				ImplementedProduct comp = ProductUtils.getImplementeProductByName(product, child.getName());
				
				ConfigureProduct configureProduct = new ConfigureProduct();
				
				Product tempProduct = new Product();
				tempProduct.addComponent(child);
				configureProduct.productConfiguration =  tempProduct;
				
				configureProduct.product = comp;
				
				configureProduct.execute();
			}
			
		}
		
	}



	private void handleMandatoryComponents(ImplementedProduct implementedProduct) {
		
		AvailableProduct[] availableProductChildrenArr = implementedProduct.getAvailableProduct().getChildAvailableProducts();
		
		for (AvailableProduct availableProductChild : availableProductChildrenArr) {
			
			int numMandatoryComponents = availableProductChild.getCatalogRelation().getCatalogRelationDetails().getDefaultQuantity();
			
			for (int i = 0; i < numMandatoryComponents; i ++) {
				//add component
				
				ImplementedProduct comp = new ImplementedProduct();
				comp.setAvailableProduct(availableProductChild);
				comp.setAction(new ImplementedProductActionRVT("AD"));
				comp.setParentImplementedProduct(implementedProduct);
				comp.setTemporaryID(getNewID());
				List<ImplementedProduct> childList = new ArrayList<ImplementedProduct>();
				ImplementedProduct children[] = implementedProduct.getChildImplementedProducts();
				if (children != null) {
			    	 for (ImplementedProduct child : children) {
			    		 childList.add(child);
					}
				}
		    	 childList.add(comp);
		    	 implementedProduct.setChildImplementedProducts(childList.toArray(new ImplementedProduct[0]));
		    	 
		    	 //handle attributes
		    	 createCompAttributes(comp);
		    	 
		    	 //check for mandatory children
		    	if (comp.getChildImplementedProducts() != null) {
			    	for (ImplementedProduct chiledImplementedProduct : comp.getChildImplementedProducts()) {
			    		 handleMandatoryComponents(chiledImplementedProduct);
					}
		    	}
		    	 
			}
			
			
			
		}
		
	}




	//TODO - change work on specific implemented product (required when more then 1 component added form the same type)
	private void addPricePlan(ImplementedProduct component, AvailableProduct availableProduct, String pptoAdd) {
		
		
		AvailableProduct availProd= ProductUtils.getAvailableProductForPricing(availableProduct,pptoAdd);
	   	 if(availProd != null ){
		    	 OrderingCatalogPricing price= ProductUtils.getAvailablePricing(availProd,pptoAdd);
		    	 ImplementedProduct comp = ProductUtils.getImplementeProduct(component,availProd);
		    	 ImplementedPricingReference pp = new ImplementedPricingReference();
		    	 pp.setCatalogPricing(price);
		    	 pp.setAction(new ImplementedProductActionRVT("AD"));
		    	 List<ImplementedPricingReference> pricePlans = new ArrayList<ImplementedPricingReference>();
	//	    	 ImplementedPricingReference currentPPs[] = comp.getImplementedPricings();
	//	    	 for (ImplementedPricingReference implementedPricingReference : currentPPs) {
	//				pricePlans.add(implementedPricingReference);
	//			}
		    	 pricePlans.add(pp);
		    	 comp.setImplementedPricings(pricePlans.toArray(new ImplementedPricingReference[0]));
	   	 }
		
	}


	
}
