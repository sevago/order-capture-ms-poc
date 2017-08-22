package com.example.oc.mspoc.util.legacy;

import java.util.List;

import com.amdocs.cih.services.orderingactivities.lib.AvailableProduct;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedAttribute;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedPricingReference;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedProduct;
import com.amdocs.cih.services.orderingproductcatalog.lib.OrderingCatalogPricing;
import com.example.oc.mspoc.model.legacy.Attribute;
import com.example.oc.mspoc.model.legacy.Component;
import com.example.oc.mspoc.model.legacy.Offer;
import com.example.oc.mspoc.model.legacy.Pricing;
import com.example.oc.mspoc.model.legacy.Product;

public class ProductUtils {
	
	
	public static AvailableProduct getAvailableProductForComponent(
			AvailableProduct availableProduct, String caption) {
		if(availableProduct.getCatalogProduct().getCatalogProductDetails().getCatalogProductName().getLocalizedValue().equals(caption)
				||availableProduct.getCatalogProduct().getCatalogProductDetails().getCatalogProductCode().equals(caption)){
			return availableProduct;
		}
		
		
			AvailableProduct children[] =  availableProduct.getChildAvailableProducts();
			for (AvailableProduct child : children) {
				availableProduct = getAvailableProductForComponent(child,caption);
				if (availableProduct != null) {
					return availableProduct;
				}
			}
		
		return null;
	}
	
	
	public static ImplementedProduct getImplementeProduct(ImplementedProduct product,
			AvailableProduct availProd) {

		if(product.getAvailableProduct().getAvailableProductID().equals(availProd.getAvailableProductID())){
			return product;
		}
		
		ImplementedProduct children[] =  product.getChildImplementedProducts();
		if (children != null) {
			for (ImplementedProduct child : children) {
				product = getImplementeProduct(child, availProd);
				if(product != null)
					return product;
			}	
		}
		
		return null;
	}
	
	
	public static ImplementedAttribute getImplementedAttribute(
			ImplementedProduct comp, String attrName) {
			ImplementedAttribute attributes[] =  comp.getImplementedAttributes();
			for (ImplementedAttribute attr : attributes) {
				//TODO: remove the getLocalizedValue logic
//				if(attr.getCatalogAttribute().getCatalogAttributeDetails().getName().getLocalizedValue().equals(attrName)
//						|| attr.getCatalogAttribute().getCatalogAttributeDetails().getCodeX9().equals(attrName)){
//					return attr;
//				}
			}
			
			ImplementedProduct[] children = comp.getChildImplementedProducts();
			ImplementedAttribute attr = null;
			if (children != null) {
				for (ImplementedProduct child : children) {
					attr = getImplementedAttribute(child, attrName);
					if (attr != null) {
						return attr;
					}
				}
			}
			
			return null;
	}
	
	public static AvailableProduct getAvailableProduct(
			AvailableProduct availableProduct, String caption) {
		
		if(availableProduct.getCatalogProduct().getCatalogProductDetails().getCatalogProductName().getLocalizedValue().equals(caption)
				||availableProduct.getCatalogProduct().getCatalogProductDetails().getCatalogProductCode().equals(caption)){
			return availableProduct;
		}
		
		
			AvailableProduct children[] =  availableProduct.getChildAvailableProducts();
			for (AvailableProduct child : children) {
				availableProduct = getAvailableProductForPricing(child,caption);
				if (availableProduct != null) {
					return availableProduct;
				}
			}
		
		return null;
	}
	
	
	public static AvailableProduct getAvailableProductForPricing(
			AvailableProduct availableProduct, String pptoAdd) {
		OrderingCatalogPricing price= getAvailablePricing(availableProduct,pptoAdd);
		
		if(price == null){
			AvailableProduct children[] =  availableProduct.getChildAvailableProducts();
			for (AvailableProduct child : children) {
				availableProduct = getAvailableProductForPricing(child,pptoAdd);
				if (availableProduct != null) {
					return availableProduct;
				}
			}
		}
		else
			return availableProduct;
		return null;
	}
	
	
	public static OrderingCatalogPricing getAvailablePricing(
			AvailableProduct availableProduct, String pptoAdd2) {
		OrderingCatalogPricing[] orderingCatalogPricingArr = availableProduct.getCatalogPricings();
		
		for (OrderingCatalogPricing orderingCatalogPricing : orderingCatalogPricingArr) {
			//TODO: remove the getLocalizeValue logic
			if (pptoAdd2.equals(orderingCatalogPricing.getCatalogPricingDetails().getName().getLocalizedValue())
					|| pptoAdd2.equals(orderingCatalogPricing.getCatalogPricingID())) {
				return orderingCatalogPricing;
			}
		}
		return null;
	
	}
	
	

	
	public static ImplementedProduct getImplementeProductByName(ImplementedProduct product,
			String name) {

		if(product.getAvailableProduct().getCatalogProduct().getCatalogProductDetails().getCatalogProductCode().equals(name)){
			return product;
		}
		
		ImplementedProduct children[] =  product.getChildImplementedProducts();
		if (children != null) {
			for (ImplementedProduct child : children) {
				product = getImplementeProductByName(child, name);
				if(product != null)
					return product;
			}	
		}
		
		return null;
	}
	
	
	public static ImplementedProduct getImplementeProductByTempId(ImplementedProduct product,
			String tempId) {

		if(product.getTemporaryID().equals(tempId)){
			return product;
		}
		
		ImplementedProduct children[] =  product.getChildImplementedProducts();
		if (children != null) {
			for (ImplementedProduct child : children) {
				product = getImplementeProductByTempId(child, tempId);
				if(product != null)
					return product;
			}	
		}
		
		return null;
	}
	
	
	public static boolean check(ImplementedProduct[] productArr, List<Offer> offerList) {
		
		for (Offer offer : offerList) {
			List<Product> productList = offer.getProductList();
			for (Product productConfiguration : productList) {
				ImplementedProduct foundProduct = null;
				for (ImplementedProduct product : productArr) {
					if(product.getTemporaryID().equals(productConfiguration.getId())) {
						foundProduct = product;
						break;
					}
					
				}
				
				if (productConfiguration.isCheckRequired()) {
					
					
					if (foundProduct == null) {
						System.out.println("Product " + productConfiguration.getName() + " with id " + productConfiguration.getId() + "was not found!");
						return false;
					}
				}
				
				if (productConfiguration.getAttributeList() != null && productConfiguration.getAttributeList().size() > 0) {
					for (Attribute attribute : productConfiguration.getAttributeList()) {
						if (attribute.isCheckRequired()) {
							ImplementedAttribute foundImplementedAttribute = null;
							for (ImplementedAttribute implementedAttribute : foundProduct.getImplementedAttributes()) {
								if (implementedAttribute.getCatalogAttribute().getCatalogAttributeDetails().getName().getLocalizedValue().equals(attribute.getName())) {
									foundImplementedAttribute = implementedAttribute;
									break;
								}
							}
							if (foundImplementedAttribute == null) {
								System.out.println("Attribute " + attribute.getName() + "was not found under product " + productConfiguration.getName() + " with id " + productConfiguration.getId());
								return false;
							} else {
								if (attribute.getValue() != null && !attribute.getValue().equals(foundImplementedAttribute.getSelectedValue())) {
									System.out.println("Attribute " + attribute.getName() + "under product " + productConfiguration.getName() + " with id " + productConfiguration.getId() + "has wrong value: " + foundImplementedAttribute.getSelectedValue() + " (" + attribute.getValue() + ").");
									return false;
								}
							}
						}
					}
				}
				
				
				if (productConfiguration.getComponentList() != null) {
					for (Component component : productConfiguration.getComponentList()) {
						if(!checkComponent(component, foundProduct)) {
							return false;
						}
					}
				}
				
			}
			
		}
		
		return true;
	}


	private static boolean checkComponent(Component component,
			ImplementedProduct product) {
		
		
		ImplementedProduct foundComponent = getImplementeProductByTempId(product, component.getId());
		
		if (component.isCheckRequired()) {
			if (foundComponent == null) {
				System.out.println("Component " + component.getName() + " with id " + component.getId() + "was not found!");
				return false;
			}
			
			if (component.getAttributeList() != null) {
				for (Attribute attribute : component.getAttributeList()) {
					if (attribute.isCheckRequired()) {
						ImplementedAttribute foundImplementedAttribute = null;
						for (ImplementedAttribute implementedAttribute : foundComponent.getImplementedAttributes()) {
							if (implementedAttribute.getCatalogAttribute().getCatalogAttributeDetails().getName().getLocalizedValue().equals(attribute.getName())) {
								foundImplementedAttribute = implementedAttribute;
								break;
							}
						}
						if (foundImplementedAttribute == null) {
							System.out.println("Attribute " + attribute.getName() + "was not found under component " + component.getName() + " with id " + component.getId());
							return false;
						} else {
							if (attribute.getValue() != null && !attribute.getValue().equals(foundImplementedAttribute.getSelectedValue())) {
								System.out.println("Attribute " + attribute.getName() + "under component " + component.getName() + " with id " + component.getId() + "has wrong value: " + foundImplementedAttribute.getSelectedValue() + " (" + attribute.getValue() + ").");
								return false;
							}
						}
					}
				}
			}
			
			if (component.getPricingList() != null) {
				for (Pricing pricing : component.getPricingList()) {
					if (pricing.isCheckRequired()) {
						ImplementedPricingReference foundImplementedPricing = null;
						for (ImplementedPricingReference implementedPricing : foundComponent.getImplementedPricings()) {
							if (pricing.getName().equals(implementedPricing.getName().getLocalizedValue())) {
								foundImplementedPricing = implementedPricing;
							}
						}
						
						if (foundImplementedPricing == null) {
							System.out.println("Pricing " + pricing.getName() + "was not found under component " + component.getName() + " with id " + component.getId());
							return false;
						}
					}
				}
			}
			
		}
		
		
		
		if (component.getComponentList()!= null) {
			for (Component childComponent : component.getComponentList()) {
				if (!checkComponent(childComponent, product)) {
					return false;
				}
			}
		}
		return true;
	}
	
}