package telran.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.NotificationData;
import telran.monitoring.service.DataProviderService;

@Validated
@Slf4j
@RestController
@RequestMapping("lastVisit")
public class DataProviderController {
	
	private static final String REQUEST_LAST_VISIT_MSG = "request for the last visit by the patient's ID: {}";
	private static final String VALID_ID_MSG = "patient id can't be null or less than 1";

	@Autowired
	DataProviderService dataProvider;
	
	@GetMapping("{id}")
	NotificationData getLastVisit(@Min(value = 1, message = VALID_ID_MSG) @PathVariable("id") long id) {
		log.debug(REQUEST_LAST_VISIT_MSG, id);
		return dataProvider.getNotificationData(id);
	}
	
}
