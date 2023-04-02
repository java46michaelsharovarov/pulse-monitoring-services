package telran.monitoring.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.*;
import telran.monitoring.service.VisitsService;

@Validated
@RestController
@RequestMapping("visits")
@Slf4j
public class VisitsController {

	private static final String REQUEST_TO_ALL_VISITS = "request to receive all patient visits with id: {}";
	private static final String REQUEST_TO_ADD = "request to add : {}";
	private static final String ADDED_SUCCESSFULLY = "%s has been added";
	
	@Autowired
	VisitsService service;
	
	@PostMapping("add/patient")
	String addPatient(@RequestBody @Valid PatientDto patient) {
		log.debug(REQUEST_TO_ADD, patient);
		try {
			service.addPatient(patient);
		} catch (IllegalStateException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					e.getMessage());
		}
		return String.format(ADDED_SUCCESSFULLY, patient);
	}
	
	@PostMapping("add/doctor")
	String addDoctor(@RequestBody @Valid DoctorDto doctor) {
		log.debug(REQUEST_TO_ADD, doctor);
		try {
			service.addDoctor(doctor);
		} catch (IllegalStateException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					e.getMessage());
		}
		return String.format(ADDED_SUCCESSFULLY, doctor);
	}
	
	@PostMapping("add/visit")
	String addVisit(@RequestBody @Valid VisitDto visit) {
		log.debug(REQUEST_TO_ADD, visit);
		try {
			service.addVisit(visit);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					e.getMessage());
		}
		return String.format(ADDED_SUCCESSFULLY, visit);
	}
	
	@GetMapping("/{id}")
	List<VisitDto> getVisits(@PathVariable(name = "id") long patientId,
			@RequestParam(name = "from", required = false) String from,
			@RequestParam(name = "to", required = false) String to) {
		try {
			if(from == null && to == null) {
				log.debug(REQUEST_TO_ALL_VISITS, patientId);
				return service.getAllVisits(patientId);
			}
			LocalDate dateFrom = from == null ? LocalDate.of(1000, 1, 1) : LocalDate.parse(from);
			LocalDate dateTo = to == null ? LocalDate.of(10000, 1, 1) : LocalDate.parse(to);
			log.debug(REQUEST_TO_ALL_VISITS + " from {} to {}", patientId, from, to);
			try {
				return service.getVisitsDates(patientId, dateFrom, dateTo);
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
