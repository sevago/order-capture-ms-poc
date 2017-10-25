package com.example.oc.mspoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.oc.mspoc.service.AssignedProductService;
import com.example.oc.mspoc.service.CatalogDataService;
import com.example.oc.mspoc.service.OrderSummaryService;

@RestController
//@RequestMapping("/api")
public class OrderCaptureRestController {
	
	private CatalogDataService catalogDataService;
	private OrderSummaryService orderSummaryService;
	private AssignedProductService assignedProductService;
	
	@Autowired
	public void setCatalogDataService(CatalogDataService catalogDataService) { this.catalogDataService = catalogDataService; }
	
	@Autowired
	public void setOrderSummaryService(OrderSummaryService orderSummaryService) { this.orderSummaryService = orderSummaryService; }
	
	@Autowired
	public void setAssignedProductService(AssignedProductService assignedProductService) { this.assignedProductService = assignedProductService; }
	
	@RequestMapping("/")
    HttpStatus home() {
        return HttpStatus.OK;
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/api/catalog/offer")
    public Object getCatalogData(@RequestParam(required = true, value = "offerId") String offerId, @RequestParam(required = false, value = "asyncMode", defaultValue = "true") boolean asyncMode) {
		return catalogDataService.getCatalogOffer(offerId, asyncMode);
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/api/customer/{orderId}/summary")
    public Object getOrderSummary(@PathVariable String orderId, @RequestParam(required = false, value = "asyncMode", defaultValue = "true") boolean asyncMode) {
		return orderSummaryService.retrieveOrderSummary(orderId, asyncMode);
    }

	@RequestMapping(method = RequestMethod.GET, value = "/api/customer/{customerId}/product")
    public Object searchAssignedProduct(@PathVariable String customerId, @RequestParam(required = false, value = "asyncMode", defaultValue = "true") boolean asyncMode) {
		return assignedProductService.searchAssignedProduct(customerId, asyncMode);
    }

	@RequestMapping(method = RequestMethod.POST, value = "/api/message")
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
