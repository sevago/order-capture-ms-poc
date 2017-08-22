package com.example.oc.mspoc.model.legacy;

import java.util.LinkedList;
import java.util.List;

public class ComponentView {
	
	private String id;
	private String name;
	private String itemType;
	private String serviceId;
	private String activity = "New";
	private String oneTimeTotal;
	private String monthlyTotal;
	private String previousMonthlyTotal;
	private String serviceType;
	private List<ComponentView> subItems;
	private List<AttributeView> attributes;
	private InstallationAddress installationAddress;
	
	private String pictureLink;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getOneTimeTotal() {
		return oneTimeTotal;
	}
	public void setOneTimeTotal(String oneTimeTotal) {
		this.oneTimeTotal = oneTimeTotal;
	}
	public String getMonthlyTotal() {
		return monthlyTotal;
	}
	public void setMonthlyTotal(String monthlyTotal) {
		this.monthlyTotal = monthlyTotal;
	}
	public String getPreviousMonthlyTotal() {
		return previousMonthlyTotal;
	}
	public void setPreviousMonthlyTotal(String previousMonthlyTotal) {
		this.previousMonthlyTotal = previousMonthlyTotal;
	}
	public List<ComponentView> getSubItems() {
		return subItems;
	}
	public void setSubItems(List<ComponentView> subItems) {
		this.subItems = subItems;
	}
	
	public void addSubItems(ComponentView subItem) {
		if (this.subItems == null) {
			this.subItems = new LinkedList<ComponentView>();
		}
		this.subItems.add(subItem);
	}
	public String getPictureLink() {
		return pictureLink;
	}
	public void setPictureLink(String pictureLink) {
		if (pictureLink != null) {
			this.pictureLink = pictureLink;
		} else {
			if ("High Speed Internet Services".equals(name)) {
				this.pictureLink = "img/internet.jpg";
			} else if ("TELUS TV".equals(name)) {
				this.pictureLink = "img/telustv.jpg";
			} else if ("Single Line".equals(name)) {
				this.pictureLink = "img/homephone.jpg";
			}
		}
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public List<AttributeView> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<AttributeView> attributes) {
		this.attributes = attributes;
	}
	
	
	public void addAttribute(AttributeView attribute) {
		if (this.attributes == null) {
			this.attributes = new LinkedList<AttributeView>();
		}
		this.attributes.add(attribute);
	}
	public InstallationAddress getInstallationAddress() {
		return installationAddress;
	}
	public void setInstallationAddress(InstallationAddress installationAddress) {
		this.installationAddress = installationAddress;
	}
	
}
