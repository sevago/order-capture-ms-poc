package com.example.oc.mspoc.util.legacy;

import com.example.oc.mspoc.model.legacy.Component;
import com.example.oc.mspoc.model.legacy.Product;

public class ProductConfigurator extends BasicConfigurator {

	
	public ProductConfigurator(Product product){
		setConfiguredProduct(product);
	}
	
	
	public Component addComponent(String name){
		Product product=getConfiguredProduct(); 
		Component component =new Component(name,Component.MODE_ADD);
		product.addComponent(component);
		return component;
	}
	/**
	 * 			
	 * @param subscriptionNo  - value of Subscription No attribute
	 * @param adslInd  - value of ADSL Indicator attribute
	 * @return
	 */
	public Component updateVoiceLine(String subscriptionNo,  String adslInd){
		Component voiceLine = getConfiguredProduct().updateComponent("Voice_Line");
		updateVoiceLineAttributes(voiceLine, subscriptionNo, adslInd);
		return voiceLine;
	}
	
	/**
	 * 
	 * @param subscriptionNo  - value of Subscription No attribute
	 * @param adslInd  - value of ADSL Indicator attribute
	 * @return
	 */
	public Component addVoiceLine(String subscriptionNo,  String adslInd){
		Component voiceLine = getConfiguredProduct().addComponent("Voice_Line");
		updateVoiceLineAttributes(voiceLine, subscriptionNo, adslInd);
		return voiceLine;
	}	
	
	protected Component updateVoiceLineAttributes(Component voiceLine, String subscriptionNo,  String adslInd){
		
		voiceLine.addAttribute("ADSL Indicator",adslInd);
		voiceLine.addAttribute("Subscription No.",subscriptionNo);
		return voiceLine;
	}
	
	/**
	 * 
	 * @param packName - name of pack to be added
	 * @param pricePlan -  priceplan of pack to be added.
	 * @param commitment - value of CommitmentPeriod attribute
	 * @return
	 */
	public Component addPack(String packName, String pricePlan, String commitment){
		Component pack = getConfiguredProduct().addComponent(packName);
		if(pricePlan !=null){
			pack.addPricing(pricePlan);
		}
		if(commitment!=null){
			pack.addAttribute("CommitmentPeriod", commitment);
		}
		return pack;
	}
	
	/**
	 * 
	 * @param packName - name of pack to be added
	 * @param pricePlan -  priceplan of pack to be added.
	 * @param commitment - value of CommitmentPeriod attribute
	 * @param staticIpUpgrade value of Static IP Upgrade attribute
	 * @return
	 */
	public Component addInternetPack(String packName, String pricePlan, String commitment, String staticIpUpgrade){
		Component pack = addPack(packName, pricePlan, commitment);
		pack.addAttribute("Static IP Upgrade", staticIpUpgrade);
		return pack;
	}
	/**
	 * 
	 * @param refNum  - value of Reference No. attribute
	 * @return
	 */
	public Component configureInternetMainComp(String refNum){
		Component mainComp = getConfiguredProduct().addComponent("Business_Internet_Services");
		mainComp.addAttribute("Reference No.", refNum);
		return mainComp;
	}
		
}
