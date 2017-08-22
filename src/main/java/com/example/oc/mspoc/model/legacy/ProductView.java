package com.example.oc.mspoc.model.legacy;

import com.amdocs.cih.services.assignedproduct.lib.SearchAssignedProductHierarchy;

public class ProductView {
	protected transient String pictureLink;
	private String productName;
	private String apId;
	private String serviceId;
	private String installationAddress;
	private ViewGroup[] componentMap;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getInstallationAddress() {
		return installationAddress;
	}
	public void setInstallationAddress(String installationAddress) {
		this.installationAddress = installationAddress;
	}

	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public  void buildComponentMap (SearchAssignedProductHierarchy searchAssignedProductHierarchy) {
		
	}
	public ViewGroup[] getComponentMap() {
		return componentMap;
	}
	public void setComponentMap(ViewGroup[] componentMap) {
		this.componentMap = componentMap;
	}
	public String getPictureLink() {
		return pictureLink;
	}
	public void setPictureLink(String pictureLink) {
		this.pictureLink = pictureLink;
	}
	public String getApId() {
		return apId;
	}
	public void setApId(String apId) {
		this.apId = apId;
	}
	
}
