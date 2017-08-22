package com.example.oc.mspoc.util.legacy;

import com.example.oc.mspoc.model.legacy.Component;

public class ComponentConfigurator extends BasicConfigurator {

	public ComponentConfigurator(Component comp){
		setConfiguredComponent(comp);
	}
	
	/**
	 * 
	 * @param noOfRings value for Number of Rings attribute
	 * @return
	 */
	public Component addVoiceMail(String noOfRings){
		Component voiceMail = getConfiguredComponent().addComponent("VoiceMail");
		updateVoiceMailAttributes(voiceMail, noOfRings);
		return voiceMail;
	}
	
	protected Component updateVoiceMailAttributes(Component voiceMail, String noOfRings){
		voiceMail.addAttribute("Number of Rings",noOfRings);
		return voiceMail;
	}	
		
}
