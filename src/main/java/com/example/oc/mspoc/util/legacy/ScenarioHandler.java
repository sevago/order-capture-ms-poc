package com.example.oc.mspoc.util.legacy;

import com.amdocs.cih.common.datatypes.ValidValue;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedAttribute;
import com.amdocs.cih.services.orderingactivities.lib.ImplementedProduct;

public class ScenarioHandler {
	
	public static boolean isAttributeVisible(ImplementedProduct component, ImplementedAttribute attribute) {
	//		for (com.amdocs.cih.services.orderingactivities.lib.OrderingAttributeDisplayInformation attributesDisplayInformation : component.getProductDisplayInformation().getAttributesDisplayInformation()) {
	//			if (attribute.getCatalogAttribute().getCatalogAttributeID().equals(attributesDisplayInformation.getCatalogAttributeID())) {
	//				if (attributesDisplayInformation.isVisible()) {
	//					return true;
	//				} else {
	//					return false;
	//				}
	//			}
	//		}
		//	
	//		return false;
		
		return true;
	}
	
	public static String getAttributeDecodeValue(ImplementedAttribute attribute) {
		String attrDecodeValue = attribute.getSelectedValue();
		ValidValue[] vv = attribute.getCatalogAttribute().getCatalogAttributeDetails().getAttributeDataType().getAttributeDataTypeDetails().getValidValues();
		if (attrDecodeValue != null && vv != null) {
			for (ValidValue validValue : vv) {
				if (attrDecodeValue.equals(validValue.getCode())) {
					attrDecodeValue =  validValue.getName();
					break;
				}
			}
		}
		
		return attrDecodeValue;
	}

}
