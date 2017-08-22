package com.example.oc.mspoc.util.legacy;

import java.util.ArrayList;
import java.util.List;

import com.amdocs.cih.services.orderingactivities.lib.ImplementedProduct;
import com.example.oc.mspoc.model.legacy.Offer;
import com.example.oc.mspoc.model.legacy.Product;

public class OfferConfigurator extends BasicConfigurator {

	public OfferConfigurator( Offer offer){
		setConfiguredOffer(offer);
	}
	

	public OfferConfigurator( String name){
			Offer offer = new Offer();
			offer.setName(name);
	}
	
	
	public void addProduct(String name, int quantity){
		Offer offer=getConfiguredOffer(); 
		for(int i = 0; i< quantity;i++){
		
			Product product =new Product(name);
			offer.addProduct(product);
			
		}
	}
	
	
	public static Offer createB1OfferVoice(int voiceQty){
		return createB1OfferVoiceAndHS(voiceQty,0);
	}
	
	public static Offer createB1OfferVoiceAndHS(int voiceQty,int hsQuantity){
		OfferConfigurator configOffer = new OfferConfigurator( "Business One");
		configOffer.addProduct("B1_Core_GLP", 1);
		configOffer.addProduct("Voice", voiceQty);
		configOffer.addProduct("Business_Internet_Services", hsQuantity);
		return configOffer.getConfiguredOffer();
	}
	
	public static Offer createB1_basicVoiceAnd_basicHS(int voiceQty,int hsQuantity){
		Offer offer = createB1OfferVoiceAndHS(voiceQty,hsQuantity);
		for(int i=0;i<voiceQty ; i++){
			Product voice = offer.getNextProduct("Voice");
			voice.getConfigurator().addPack("Office_Phone_Lite_ML", "ML Office Phone Lite B1 Standard - Contract","2");
			voice.getConfigurator().updateVoiceLine("605802547"+i, "No");
		}
		
		for(int i=0;i<hsQuantity ; i++){
			Product internet = offer.getNextProduct("Business_Internet_Services");
			internet.getConfigurator().addInternetPack("Office Internet Lite", null,"2","SIP2");
			internet.getConfigurator().configureInternetMainComp("605802548"+i);
		}
		return offer;
	}
	
	private ImplementedProduct[] implementedProducts;
	public boolean check(){
		List<Offer> list = new ArrayList<Offer>();
		list.add(getConfiguredOffer());
		return ProductUtils.check(implementedProducts, list);
	}
	
	
	/*public void updateOutput(FullQuoteOutput output){
		Impoutput.getImplementedProductFeedbacksX9()
	}*/
	
}
