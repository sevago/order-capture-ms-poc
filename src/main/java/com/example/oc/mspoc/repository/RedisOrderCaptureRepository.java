package com.example.oc.mspoc.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.example.oc.mspoc.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;

@Repository
public class RedisOrderCaptureRepository implements OrderCaptureRepository {
		
	private RedisTemplate<String, Object> redisTemplate;	

	@Autowired
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;	
	}

	@Override
	public JsonNode find(String id) {
		String jsonString = null;
		ValueOperations<String, Object> ops = this.redisTemplate.opsForValue();		
		
		if (this.redisTemplate.hasKey(id)) {
			jsonString = (String) ops.get(id);
			System.out.println(">>> REDIS REPO <<< [find]: found key " + id + ", value = " + ops.get(id));
		} else {
			System.out.println(">>> REDIS REPO <<< [find]: key " + id + " is not found.");
		}
			
		return JsonUtil.stringToJson(jsonString);		
	}

	@Override
	public List<JsonNode> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(String id, JsonNode json) {	
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		if (!redisTemplate.hasKey(id)) {		
			ops.set(id, JsonUtil.jsonToString(json));		
			System.out.println(">>> REDIS REPO <<< [save]: new value for key: " + id + " is added.");
		} else {
			System.out.println(">>> REDIS REPO <<< [save]: " + id + " key is already present.");
		}	
	}

}
