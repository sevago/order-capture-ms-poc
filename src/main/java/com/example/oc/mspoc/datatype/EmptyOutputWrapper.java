package com.example.oc.mspoc.datatype;

import com.example.oc.mspoc.util.EmptyObject;

public class EmptyOutputWrapper implements ServiceOutputWrapper {

	private EmptyObject serviceOutput = EmptyObject.EMPTY_OUTPUT_OBJECT;
	
	public EmptyOutputWrapper() {}
	
	@Override
	public Object getOutput() {
		return serviceOutput;
	}
}
