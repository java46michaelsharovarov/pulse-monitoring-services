package telran.monitoring.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.entities.DoctorEntity;
import telran.monitoring.entities.PatientEntity;
import telran.monitoring.model.NotificationData;
import telran.monitoring.repo.DoctorRepository;
import telran.monitoring.repo.PatientRepository;
import telran.monitoring.repo.VisitRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DataProviderServiceImpl implements DataProviderService {

	private static final String DOCTOR_EMAIL_MSG = "doctor email is {}";
	private static final String LAST_VISIT_MSG = "last visit patient ID {}: {}";
	private static final String PATIENT_NOT_EXIST_MSG = "patient with id: %d does not exist";
	private static final String DOCTOR_NOT_EXIST_MSG = "doctor with email: %s does not exist";
	private static final String VISIT_NOT_EXIST_MSG = "no visits for patient with id: %d";

	@Autowired
	VisitRepository visitRepository;
	
	@Autowired
	DoctorRepository doctorRepository;
	
	@Autowired
	PatientRepository patientRepository;
	
	@Override
	public NotificationData getNotificationData(long patientId) {
		String doctorEmail = visitRepository.getDoctorEmail(patientId);
		log.debug(DOCTOR_EMAIL_MSG, doctorEmail);
		if(doctorEmail == null || doctorEmail.isEmpty()) {
			String msg = String.format(VISIT_NOT_EXIST_MSG, patientId);
			log.debug(msg);
			throw new NoSuchElementException(msg);
		}
		DoctorEntity doctor = doctorRepository.findById(doctorEmail).orElse(null);
		if(doctor == null) {
			String msg = String.format(DOCTOR_NOT_EXIST_MSG, doctorEmail);
			log.debug(msg);
			throw new NoSuchElementException(msg);
		}
		String doctorName = doctor.getName();
		PatientEntity patient = patientRepository.findById(patientId).orElse(null);
		if(patient == null) {
			String msg = String.format(PATIENT_NOT_EXIST_MSG, patientId);
			log.debug(msg);
			throw new NoSuchElementException(msg);
		}
		String patientName = patient.getName();
		NotificationData data = new NotificationData(doctorEmail, doctorName, patientName);
		log.debug(LAST_VISIT_MSG, patientId, data.toString());
		return data;
	}

}
