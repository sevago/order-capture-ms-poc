package com.example.oc.mspoc.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JsonUtil {
	
	private final static ObjectMapper mapper = new ObjectMapper();
	
	public final static String jsonToString(JsonNode json) {
		String jsonString = null;		
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			jsonString = mapper.writeValueAsString(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
	public final static JsonNode stringToJson(String string) {
		JsonNode json = null;
		if (string != null) {
			try {
				json = mapper.readTree(string);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}			
		return json;
	}
	
	public final static JsonNode objectToJson(Object inputObject) {
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);		
		JsonNode json = mapper.valueToTree(inputObject);
		return json;
	}

}
