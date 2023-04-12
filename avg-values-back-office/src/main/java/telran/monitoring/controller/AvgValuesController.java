package telran.monitoring.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import telran.monitoring.service.AvgValuesService;

@Validated
@RestController
@RequestMapping(value="/pulse/values")
@Slf4j
public class AvgValuesController {

	private static final String VALID_ID_MSG = "patient id can't be null or less than 1";
	private static final String GET_AVG_PULSE_MSG = "request to get average pulse value for patient id {}";
	private static final String REGEXP_DATE_MSG = "date should be in format YYYY-MM-DDThh:mm(:ss.mll)";
	private static final String REGEXP_DATE = "\\d{4}-(0\\d|1[012])-(0\\d|[12]\\d|3[01])T\\d{2}:\\d{2}(:\\d{2})?(.\\d{3})?";
	
	@Autowired
	AvgValuesService service;
	
	@GetMapping("{id}")
	int getAvgValue(@Min(value = 1, message = VALID_ID_MSG) @PathVariable ("id") long patientId,
			@Pattern(regexp = REGEXP_DATE, message = REGEXP_DATE_MSG) @RequestParam (name = "from", required = false) String fromDateTime,
			@Pattern(regexp = REGEXP_DATE, message = REGEXP_DATE_MSG) @RequestParam(name = "to", required=false) String toDateTime) {
		if(fromDateTime == null && toDateTime == null) {
			log.debug(GET_AVG_PULSE_MSG, patientId);
			return service.getAvgValue(patientId);
		} 
		LocalDateTime from = fromDateTime == null ? LocalDateTime.of(1000, 1, 1, 0, 0) : LocalDateTime.parse(fromDateTime);
		LocalDateTime to = toDateTime == null ? LocalDateTime.now() : LocalDateTime.parse(toDateTime);
		log.debug(GET_AVG_PULSE_MSG +" from {} to {}", patientId, from, to);
		return service.getAvgValue(patientId, from, to);
	}
	
}
