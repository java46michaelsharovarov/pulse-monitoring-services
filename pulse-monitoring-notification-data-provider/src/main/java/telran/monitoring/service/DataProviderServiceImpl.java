package telran.monitoring.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import telran.monititoring.model.NotificationData;
import telran.monitoring.proj.VisitData;
import telran.monitoring.repo.VisitRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DataProviderServiceImpl implements DataProviderService {

	@Autowired
	VisitRepository visits;
	
	@Override
	public NotificationData getNotificationData(long patientId) {
		VisitData visit = visits.getLastVisitById(patientId);
		if(visit == null) {
			log.debug("patient with id {} does not exist", patientId);
			throw new NoSuchElementException(String.format("Patient with id %d does not exist", patientId));
		}
		NotificationData data = new NotificationData(visit.getEmail(), visit.getDoctorName(), visit.getPatientName());
		log.debug("last vist patient ID {}: {}", patientId, data.toString());
		return data;
	}

}
