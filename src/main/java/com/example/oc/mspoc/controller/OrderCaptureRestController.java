package com.example.oc.mspoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.oc.mspoc.service.AssignedProductService;
import com.example.oc.mspoc.service.OrderSummaryService;

@RestController
@RequestMapping("/api")
public class OrderCaptureRestController {
	
	private OrderSummaryService orderSummaryService;
	
	private AssignedProductService assignedProductService;
	
	@Autowired
	public void setOrderSummaryService(OrderSummaryService orderSummaryService) { this.orderSummaryService = orderSummaryService; }
	
	@Autowired
	public void setAssignedProductService(AssignedProductService assignedProductService) { this.assignedProductService = assignedProductService; }
	
	@RequestMapping("/")
    String home() {
        return "redirect:/summary/535726915A";
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/summary/{orderId}")
    public Object getOrderSummary(@PathVariable String orderId, @RequestParam(required = false, value = "asyncMode", defaultValue = "true") boolean asyncMode) {
			
		return orderSummaryService.retrieveOrderSummary(orderId, asyncMode);
    }

	@RequestMapping(method = RequestMethod.GET, value = "/product/{customerId}")
    public Object searchAssignedProduct(@PathVariable String customerId, @RequestParam(required = false, value = "asyncMode", defaultValue = "true") boolean asyncMode) {
			
		return assignedProductService.searchAssignedProduct(customerId, asyncMode);
    }
	
	@RequestMapping(method = RequestMethod.POST, value = "/message")
    public Object sendMessageToQueue(@RequestParam(required = true, value = "queue", defaultValue = "ap") String queue, @RequestParam(required = true, value = "id") String id) {
		switch(queue) {
			case "ap":
				assignedProductService.sendMessageToAPQueue(id);
				break;
			case "os":
				orderSummaryService.sendMessageToOSQueue(id);
				break;
			default:
		}
		
		return "OK";
    }
}
