package com.example.oc.mspoc.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.oc.mspoc.repository.OrderCaptureRepository;
import com.example.oc.mspoc.service.legacy.OrderingServiceProvider;
import com.example.oc.mspoc.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public abstract class OrderCaptureServiceImpl implements OrderCaptureService {
	
	private static final Logger log = LoggerFactory.getLogger(OrderCaptureServiceImpl.class);
	
	protected OrderCaptureRepository repository;	
	protected OrderingServiceProvider serviceProvider;
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	public void setRepository(OrderCaptureRepository repository) { this.repository = repository; }
	
	@Autowired
	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) { this.rabbitTemplate = rabbitTemplate; }
	
	protected static final String NOT_FOUND_MESSAGE = "Instance was not found in the persistence store, fetching details in asynchronous fashion, try again now.";	
	
	@Override
	public void addInstanceToRepository(String id, Object value) {
    	repository.save(id, JsonUtil.objectToJson(value));
	}
	
	@Override
	public JsonNode findInstanceInRepository(String id) {
    	return repository.find(id);
	}
	
	@Override
	public JsonNode handleServiceCall(String id, boolean mode) {
		JsonNode json = findInstanceInRepository(id);
		if (json == null) {
			CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> serviceProvider.invoke(id))
				.thenApply(this::handleServiceResponse)
				.thenAccept(newInstance -> addInstanceToRepository(id, newInstance))
				.exceptionally(error -> {
					error.printStackTrace();
			        return null;
				});

			if (!mode) {
				try {
					completableFuture.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				json = findInstanceInRepository(id);
			}			
		}
		
		return json != null ? json : JsonUtil.objectToJson(NOT_FOUND_MESSAGE);
	}

	@Override
    public void sendMessage(String id, String queueName) {
        Map<String, String> actionmap = new HashMap<>();
        actionmap.put("id", id);
        log.debug("Sending message {} to {} queue", actionmap, queueName);
        rabbitTemplate.convertAndSend(queueName, actionmap);
    }
	
}
