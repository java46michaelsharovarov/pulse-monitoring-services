package telran.monitoring.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.service.DataProviderService;

@Slf4j
@RestController
@RequestMapping("visits")
public class DataProviderController {

	@Autowired
	DataProviderService dataProvider;
	
	@GetMapping("last/{patientId}")
	String getLastVisit(@PathVariable("patientId") long id) {
		log.debug("request for the last visit by the patient's ID: {}", id);
		String data;
		try {
			data = dataProvider.getNotificationData(id).toString();
		} catch (NoSuchElementException e) {
			return e.getMessage();
		}
		return data;
	}
	
}
