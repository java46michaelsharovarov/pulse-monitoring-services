package telran.monitoring.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.*;
import telran.monitoring.repo.*;
import telran.monitoring.entities.*;

@Service
@Slf4j
@Transactional
public class VisitsServiceImpl implements VisitsService {

	private static final String ALL_VISITS_MSG = "all visits of patient with id {}: {}";
	private static final String NO_VISITS_MSG = "patient with id: {} has no visits";
	private static final String VISIT_SAVED_IN_REPOSITORY_MSG = "visit - doctorEmail: {}, patient Id: {}, date: {} is saved in repository";
	private static final String DOCTOR_NOT_EXIST_MSG = "doctor with email: %s does not exist";
	private static final String PATIENT_NOT_EXIST_MSG = "patient with id: %d does not exist";
	private static final String DOCTOR_EXIST_MSG = "doctor with email %s already exist";
	private static final String SAVED_IN_REPOSITORY_MSG = "{} is saved in repository";
	private static final String PATIENT_EXIST_MSG = "patient with id %d already exist";
	private static final String DATE_ERROR_MSG = "date 'from' %s can't be after date 'to' %s";

	@Autowired
	DoctorRepository doctorRepository;
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	VisitRepository visitRepository;
	
	@Override
	public void addPatient(PatientDto patientDto) {
		if(patientRepository.existsById(patientDto.patientId)) {
			String msg = String.format(PATIENT_EXIST_MSG, patientDto.patientId);
			log.error(msg);
			throw new IllegalStateException(msg);
		}
		PatientEntity patient = new PatientEntity(patientDto.patientId, patientDto.patientName);
		patientRepository.save(patient);
		log.debug(SAVED_IN_REPOSITORY_MSG, patientDto);
	}

	@Override
	public void addDoctor(DoctorDto doctorDto) {
		if(doctorRepository.existsById(doctorDto.doctorEmail)) {
			String msg = String.format(DOCTOR_EXIST_MSG, doctorDto.doctorEmail);
			log.error(msg);
			throw new IllegalStateException(msg);
		}
		DoctorEntity doctor = new DoctorEntity(doctorDto.doctorEmail, doctorDto.doctorName);
		doctorRepository.save(doctor);
		log.debug(SAVED_IN_REPOSITORY_MSG, doctorDto);
	}

	@Override
	public void addVisit(VisitDto visitDto) {
		PatientEntity patient = patientRepository.findById(visitDto.patientId).orElse(null);
		if(patient == null) {
			String msg = String.format(PATIENT_NOT_EXIST_MSG, visitDto.patientId);
			log.error(msg);
			throw new NoSuchElementException(msg);
		}
		DoctorEntity doctor = doctorRepository.findById(visitDto.doctorEmail).orElse(null);
		if(doctor == null) {
			String msg = String.format(DOCTOR_NOT_EXIST_MSG, visitDto.doctorEmail);
			log.error(msg);
			throw new NoSuchElementException(msg);
		}
		LocalDate date = LocalDate.parse(visitDto.date);
		VisitEntity visit = new VisitEntity(doctor, patient, date);
		visitRepository.save(visit);
		log.debug(VISIT_SAVED_IN_REPOSITORY_MSG, visitDto.doctorEmail, visitDto.patientId, visitDto.date);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<VisitDto> getAllVisits(long patientId) {
		checkPatientId(patientId);
		List<VisitEntity> visits = visitRepository.findByPatientId(patientId);
		if(visits == null) {
			log.debug(NO_VISITS_MSG, patientId);
			return Collections.emptyList();
		}
		List<VisitDto> res = fromEntityToDto(visits);
		log.debug(ALL_VISITS_MSG, patientId, res);
		return res;
	}

	@Transactional(readOnly = true)
	@Override
	public List<VisitDto> getVisitsDates(long patientId, LocalDate from, LocalDate to) {
		checkPatientId(patientId);
		if(from.isAfter(to)) {
			String msg = String.format(DATE_ERROR_MSG, from, to);
			log.error(msg);
			throw new IllegalStateException(msg);
		}
		List<VisitEntity> visits = visitRepository.findByPatientIdAndBetweenDate(patientId, from, to);
		if(visits == null) {
			log.debug(NO_VISITS_MSG, patientId);
			return Collections.emptyList();
		}
		List<VisitDto> res = fromEntityToDto(visits);
		log.debug("from {} to {} " + ALL_VISITS_MSG, from, to, patientId, res);
		return res;
	}

	private void checkPatientId(long patientId) {
		if(!patientRepository.existsById(patientId)) {
			String msg = String.format(PATIENT_NOT_EXIST_MSG, patientId);
			log.error(msg);
			throw new NoSuchElementException(msg);
		}
	}

	private List<VisitDto> fromEntityToDto(List<VisitEntity> visits) {
		return visits.stream().map(e-> new VisitDto(e.getPatient().getId(), e.getDoctor().getEmail(), e.getDate().toString())).toList();
	}

}
