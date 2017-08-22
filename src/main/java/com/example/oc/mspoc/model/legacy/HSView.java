package com.example.oc.mspoc.model.legacy;

import com.amdocs.cih.services.assignedproduct.lib.AssignedPricingReferenceHeader;
import com.amdocs.cih.services.assignedproduct.lib.SearchAssignedProductHierarchy;

public class HSView extends ProductView {
	
	protected String pictureLink = "img/internet.jpg";
	
	public  void buildComponentMap (SearchAssignedProductHierarchy searchAssignedProductHierarchy) {
	
		ViewGroup[] viewGroup = new ViewGroup[3];
		viewGroup[0] = new ViewGroup();
		viewGroup[1] = new ViewGroup();
		viewGroup[2] = new ViewGroup();
		viewGroup[0].setGroupName("Packs");
		viewGroup[1].setGroupName("Security Features");
		viewGroup[2].setGroupName("Promotions and Discounts");
		this.setComponentMap(viewGroup);
		
	
	buildComponentMapRec( searchAssignedProductHierarchy) ;
		
	
	}
	
	private void buildComponentMapRec(SearchAssignedProductHierarchy searchAssignedProductHierarchy) {
		
		if (searchAssignedProductHierarchy.getParentAssignedProduct() != null) {
			String productType = searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedProductHeader().getProductType().getValueAsString();
			
			@SuppressWarnings("unused")
			ViewGroup vgComp = null;
			if ("PCKS".equals(productType)) {
				SearchAssignedProductHierarchy[] searchAssignedProductHierarchyChildren =  searchAssignedProductHierarchy.getChildAssignedProductList();
				if (searchAssignedProductHierarchyChildren != null && this.getComponentMap()[0] != null) {
					
					for (SearchAssignedProductHierarchy searchAssignedProductHierarchyChild : searchAssignedProductHierarchyChildren) {
						this.getComponentMap()[0].addGroupMember(searchAssignedProductHierarchyChild.getParentAssignedProduct().getAssignedProductHeader().getName().getLocalizedValue());
					}
				}
				
				//vgComp = this.getComponentMap()[0];
			} 
			else if ("SECU".equals(productType)) {
				SearchAssignedProductHierarchy[] searchAssignedProductHierarchyChildren =  searchAssignedProductHierarchy.getChildAssignedProductList();
				if (searchAssignedProductHierarchyChildren != null && this.getComponentMap()[0] != null) {
					
					for (SearchAssignedProductHierarchy searchAssignedProductHierarchyChild : searchAssignedProductHierarchyChildren) {
						this.getComponentMap()[1].addGroupMember(searchAssignedProductHierarchyChild.getParentAssignedProduct().getAssignedProductHeader().getName().getLocalizedValue());
					}
				}
			}
			
			ViewGroup vgPP = this.getComponentMap()[2];
			
			
			AssignedPricingReferenceHeader[] assignedPricingReferenceHeaderArr = searchAssignedProductHierarchy.getParentAssignedProduct().getAssignedPricing();
			
			for (AssignedPricingReferenceHeader assignedPricingReferenceHeader : assignedPricingReferenceHeaderArr) {
//				if ("DS".equals(assignedPricingReferenceHeader.getPricePlanTypeX3().getValueAsString())) {
					vgPP.addGroupMember(assignedPricingReferenceHeader.getPricingName().getLocalizedValue());
//				}
			}
			
		}
		
		SearchAssignedProductHierarchy[] searchAssignedProductHierarchyChildren =  searchAssignedProductHierarchy.getChildAssignedProductList();
		if (searchAssignedProductHierarchyChildren == null || searchAssignedProductHierarchyChildren.length == 0) {
			return;
		}

		for (SearchAssignedProductHierarchy searchAssignedProductHierarchyChild : searchAssignedProductHierarchyChildren) {
			buildComponentMapRec(searchAssignedProductHierarchyChild);
		}
		
	}

}
