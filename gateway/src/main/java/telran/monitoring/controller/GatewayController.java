package telran.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import telran.monitoring.service.ProxyService;

@RestController
@Slf4j
public class GatewayController {

	private static final String RECEIVED_REQUEST_MSG = "received {} request: {}?{}";
	
	@Autowired
	ProxyService proxyService;
	
	@GetMapping("/**")
	ResponseEntity<byte[]> getRequests(ProxyExchange<byte[]> proxy, HttpServletRequest request) {
		log.debug(RECEIVED_REQUEST_MSG, request.getMethod(), request.getRequestURL(), request.getQueryString());
		return proxyService.proxyRouting(proxy, request, HttpMethod.GET);
	}
	
	@PostMapping("/**")
	ResponseEntity<byte[]> postRequests(ProxyExchange<byte[]> proxy, HttpServletRequest request) {
		log.debug(RECEIVED_REQUEST_MSG, request.getMethod(), request.getRequestURL(), request.getQueryString());
		return proxyService.proxyRouting(proxy, request, HttpMethod.POST);
	}
}
