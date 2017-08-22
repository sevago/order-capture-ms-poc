package com.example.oc.mspoc.repository;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public interface OrderCaptureRepository {
	
	List<JsonNode> findAll();

	JsonNode find(String id);

	void save(String id, JsonNode object);

}
