package telran.monitoring.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.NotificationData;
import telran.monitoring.service.DataProviderService;

@Slf4j
@RestController
@RequestMapping("visits")
public class DataProviderController {

	@Autowired
	DataProviderService dataProvider;
	
	@GetMapping("last/{patientId}")
	NotificationData getLastVisit(@PathVariable("patientId") long id) {
		log.debug("request for the last visit by the patient's ID: {}", id);
		NotificationData data;
		try {
			data = dataProvider.getNotificationData(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Patient with id %d does not exist", id));
		}
		return data;
	}
	
}
