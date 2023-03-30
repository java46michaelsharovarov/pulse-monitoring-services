package telran.monitoring.controller;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.service.AvgValuesService;

@RestController
@RequestMapping(value="/pulse/values")
@Slf4j
public class AvgValuesController {

	@Autowired
	AvgValuesService service;
	
	@GetMapping("{id}")
	int getAvgValue(@PathVariable ("id") long patientId,
			@RequestParam (name = "from", required = false) String fromDateTime,
			@RequestParam(name = "to", required=false) String toDateTime) {
		try {
			if(fromDateTime == null && toDateTime == null) {
				log.debug("request to get average pulse value for patient id {}", patientId);
				return service.getAvgValue(patientId);
			} 
			LocalDateTime from = fromDateTime == null ? LocalDateTime.of(1000, 1, 1, 0, 0) : LocalDateTime.parse(fromDateTime);
			LocalDateTime to = toDateTime == null ? LocalDateTime.of(10000, 1, 1, 0, 0) : LocalDateTime.parse(toDateTime);
			log.debug("request to get average pulse value for patient id {} from {} to {}", patientId, from, to);
			try {
				return service.getAvgValue(patientId, from, to);
			} catch (IllegalArgumentException e) {
				log.error(e.getMessage());
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						e.getMessage());
			}
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					e.getMessage());
		}
	}
	
}
