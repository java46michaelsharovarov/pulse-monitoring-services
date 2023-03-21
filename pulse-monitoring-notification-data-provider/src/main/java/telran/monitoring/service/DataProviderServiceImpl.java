package telran.monitoring.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import telran.monititoring.model.NotificationData;
import telran.monitoring.repo.DoctorRepository;
import telran.monitoring.repo.PatientRepository;
import telran.monitoring.repo.VisitRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DataProviderServiceImpl implements DataProviderService {

	@Autowired
	VisitRepository visitRepository;
	
	@Autowired
	DoctorRepository doctorRepository;
	
	@Autowired
	PatientRepository patientRepository;
	
	@Override
	public NotificationData getNotificationData(long patientId) {
		String doctorEmail = visitRepository.getDoctorEmail(patientId);
		if(doctorEmail == null) {
			log.debug("patient with id {} does not exist", patientId);
			throw new NoSuchElementException(String.format("Patient with id %d does not exist", patientId));
		}
		String doctorName = doctorRepository.findById(doctorEmail).get().getName();
		String patientName = patientRepository.findById(patientId).get().getName();
		NotificationData data = new NotificationData(doctorEmail, doctorName, patientName);
		log.debug("last vist patient ID {}: {}", patientId, data.toString());
		return data;
	}

}
