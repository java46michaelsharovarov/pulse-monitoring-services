package telran.monitoring.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.*;
import telran.monitoring.repo.*;
import telran.monitoring.entities.*;

@Service
@Slf4j
public class VisitsServiceImpl implements VisitsService {

	@Autowired
	DoctorRepository doctorRepository;
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	VisitRepository visitRepository;
	
	@Override
	public void addPatient(PatientDto patientDto) {
		if(patientRepository.existsById(patientDto.patientId)) {
			log.error("patient with id {} already exist", patientDto.patientId);
			throw new IllegalStateException(String.format("patient with id %d already exist", patientDto.patientId));
		}
		PatientEntity patient = new PatientEntity(patientDto.patientId, patientDto.patientName);
		patientRepository.save(patient);
		log.debug("{} saved in repository", patientDto);
	}

	@Override
	public void addDoctor(DoctorDto doctorDto) {
		if(doctorRepository.existsById(doctorDto.doctorEmail)) {
			log.error("doctor with email {} already exist", doctorDto.doctorEmail);
			throw new IllegalStateException(String.format("doctor with email %s already exist", doctorDto.doctorEmail));
		}
		DoctorEntity doctor = new DoctorEntity(doctorDto.doctorEmail, doctorDto.doctorName);
		doctorRepository.save(doctor);
		log.debug("{} saved in repository", doctorDto);
	}

	@Override
	public void addVisit(VisitDto visitDto) {
		PatientEntity patient = patientRepository.findById(visitDto.patientId).orElse(null);
		if(patient == null) {
			log.error("patient with id:{} does not exist", visitDto.patientId);
			throw new NoSuchElementException(String.format("patient with id:%d does not exist", visitDto.patientId));
		}
		DoctorEntity doctor = doctorRepository.findById(visitDto.doctorEmail).orElse(null);
		if(doctor == null) {
			log.error("doctor with email:{} does not exist", visitDto.doctorEmail);
			throw new NoSuchElementException(String.format("doctor with email:%s does not exist", visitDto.doctorEmail));
		}
		LocalDate date = LocalDate.parse(visitDto.date);
		VisitEntity visit = new VisitEntity(doctor, patient, date);
		visitRepository.save(visit);
		log.debug("{} saved in repository", visitDto);
	}

	@Override
	public List<VisitDto> getAllVisits(long patientId) {
		if(!patientRepository.existsById(patientId)) {
			log.error("patient with id:{} does not exist", patientId);
			throw new NoSuchElementException(String.format("patient with id:%d does not exist", patientId));
		}
		List<VisitEntity> visits = visitRepository.findByPatientId(patientId);
		if(visits == null) {
			log.debug("visits is NULL");
			return Collections.emptyList();
		}
		List<VisitDto> res = fromEntityToDto(visits);
		log.debug("all visits of patient with id {}: {}", patientId, res);
		return res;
	}

	@Override
	public List<VisitDto> getVisitsDates(long patientId, LocalDate from, LocalDate to) {
		if(!patientRepository.existsById(patientId)) {
			log.error("patient with id:{} does not exist", patientId);
			throw new NoSuchElementException(String.format("patient with id:%d does not exist", patientId));
		}
		if(from.isAfter(to)) {
			log.error("{} after {}", from, to);
			throw new IllegalArgumentException(String.format("date 'from' %s cannot be after date 'to' %s", from, to));
		}
		List<VisitEntity> visits = visitRepository.findByPatientIdAndBetweenDate(patientId, from, to);
		if(visits == null) {
			log.debug("visits is NULL");
			return Collections.emptyList();
		}
		List<VisitDto> res = fromEntityToDto(visits);
		log.debug("all visits of patient with id {} from {} to {}: {}", patientId, from, to, res);
		return res;
	}

	private List<VisitDto> fromEntityToDto(List<VisitEntity> visits) {
		return visits.stream().map(e-> new VisitDto(e.getPatient().getId(), e.getDoctor().getEmail(), e.getDate().toString())).toList();
	}

}
