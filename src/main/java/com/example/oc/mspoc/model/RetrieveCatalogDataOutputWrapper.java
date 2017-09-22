package com.example.oc.mspoc.model;

import com.amdocs.cih.services.orderingactivities.lib.RetrieveCatalogProductForConfigurationOutput;

public class RetrieveCatalogDataOutputWrapper implements ServiceOutputWrapper {
	
	private RetrieveCatalogProductForConfigurationOutput retrieveCatalogDataOutput;
	
	public RetrieveCatalogDataOutputWrapper (RetrieveCatalogProductForConfigurationOutput obj) {
		this.retrieveCatalogDataOutput = obj;
	}

	public Object getOutput() {
		return retrieveCatalogDataOutput;
	}

	public void setOutput(Object retrieveCatalogDataOutput) {
		this.retrieveCatalogDataOutput = (RetrieveCatalogProductForConfigurationOutput) retrieveCatalogDataOutput;
	}
}
