package telran.monitoring.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.*;
import telran.monitoring.service.VisitsService;

@Validated
@RestController
@RequestMapping("visits")
@Slf4j
public class VisitsController {

	private static final String VALID_ID_MSG = "patient id can't be null or less than 1";
	private static final String REQUEST_TO_ALL_VISITS = "request to receive all patient visits with id: {}";
	private static final String REQUEST_TO_ADD_MSG = "request to add: {}";
	private static final String ADDED_SUCCESSFULLY_MSG = "%s has been added";
	private static final String REGEXP_DATE_MSG = "date should be in format YYYY-MM-DD";
	private static final String REGEXP_DATE = "\\d{4}-(0\\d|1[012])-(0\\d|[12]\\d|3[01])";

	@Autowired
	VisitsService service;

	@PostMapping("add/patient")
	String addPatient(@RequestBody @Valid PatientDto patient) {
		log.debug(REQUEST_TO_ADD_MSG, patient);
		service.addPatient(patient);
		return String.format(ADDED_SUCCESSFULLY_MSG, patient);
	}

	@PostMapping("add/doctor")
	String addDoctor(@RequestBody @Valid DoctorDto doctor) {
		log.debug(REQUEST_TO_ADD_MSG, doctor);
		service.addDoctor(doctor);
		return String.format(ADDED_SUCCESSFULLY_MSG, doctor);
	}

	@PostMapping("add/visit")
	String addVisit(@RequestBody @Valid VisitDto visit) {
		log.debug(REQUEST_TO_ADD_MSG, visit);
		service.addVisit(visit);
		return String.format(ADDED_SUCCESSFULLY_MSG, visit);
	}

	@GetMapping("/{id}")
	List<VisitDto> getVisits(
			@Min(value = 1, message = VALID_ID_MSG) @PathVariable(name = "id") long patientId,
			@Pattern(regexp = REGEXP_DATE, message = REGEXP_DATE_MSG) @RequestParam(name = "from", required = false) String from,
			@Pattern(regexp = REGEXP_DATE, message = REGEXP_DATE_MSG) @RequestParam(name = "to", required = false) String to) {
		if (from == null && to == null) {
			log.debug(REQUEST_TO_ALL_VISITS, patientId);
			return service.getAllVisits(patientId);
		}
		LocalDate dateFrom = from == null ? LocalDate.of(1000, 1, 1) : LocalDate.parse(from);
		LocalDate dateTo = to == null ? LocalDate.now() : LocalDate.parse(to);
		log.debug(REQUEST_TO_ALL_VISITS + " from {} to {}", patientId, dateFrom, dateTo);
		return service.getVisitsDates(patientId, dateFrom, dateTo);
	}
}
