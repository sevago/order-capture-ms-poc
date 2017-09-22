package com.example.oc.mspoc.model;

import com.amdocs.cih.services.order.lib.RetrieveOrderSummaryOutput;

//@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RetrieveOrderSummaryOutputWrapper implements ServiceOutputWrapper {
	
	private RetrieveOrderSummaryOutput orderSummaryOutput;
	
	public RetrieveOrderSummaryOutputWrapper (RetrieveOrderSummaryOutput obj) {
		this.orderSummaryOutput = obj;
	}

	public Object getOutput() {
		return orderSummaryOutput;
	}

	public void setOutput(Object orderSummaryOutput) {
		this.orderSummaryOutput = (RetrieveOrderSummaryOutput) orderSummaryOutput;
	}
}
