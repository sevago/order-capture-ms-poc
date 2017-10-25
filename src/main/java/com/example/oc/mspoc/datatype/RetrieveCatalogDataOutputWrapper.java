package com.example.oc.mspoc.datatype;

import com.amdocs.cih.services.orderingactivities.lib.RetrieveCatalogProductForConfigurationOutput;

public class RetrieveCatalogDataOutputWrapper implements ServiceOutputWrapper {
	
	private RetrieveCatalogProductForConfigurationOutput retrieveCatalogDataOutput;
	
	public RetrieveCatalogDataOutputWrapper (RetrieveCatalogProductForConfigurationOutput obj) {
		this.retrieveCatalogDataOutput = obj;
	}

	public Object getOutput() {
		return retrieveCatalogDataOutput;
	}
}
