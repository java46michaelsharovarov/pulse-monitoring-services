package telran.monitoring.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.NotificationData;
import telran.monitoring.repo.DoctorRepository;
import telran.monitoring.repo.PatientRepository;
import telran.monitoring.repo.VisitRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DataProviderServiceImpl implements DataProviderService {

	private static final String LAST_VISIT_MSG = "last visit patient ID {}: {}";
	private static final String NOT_EXIST_MSG = "patient with id: %d does not exist";

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
			String msg = String.format(NOT_EXIST_MSG, patientId);
			log.debug(msg);
			throw new NoSuchElementException(msg);
		}
		String doctorName = doctorRepository.findById(doctorEmail).get().getName();
		String patientName = patientRepository.findById(patientId).get().getName();
		NotificationData data = new NotificationData(doctorEmail, doctorName, patientName);
		log.debug(LAST_VISIT_MSG, patientId, data.toString());
		return data;
	}

}
