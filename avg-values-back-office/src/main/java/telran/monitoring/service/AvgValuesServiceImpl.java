package telran.monitoring.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.repo.AvgPulseProbeRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AvgValuesServiceImpl implements AvgValuesService {

	@Autowired
	AvgPulseProbeRepository avgPulseProbeRepository;
	
	@Override
	public int getAvgValue(long patientId, LocalDateTime from, LocalDateTime to) {
		if(!avgPulseProbeRepository.existsByPatientId(patientId)) {
			log.error("patient with id:{} does not exist", patientId);
			throw new NoSuchElementException(String.format("patient with id:%d does not exist", patientId));
		}
		if(from.isAfter(to)) {
			log.error("date 'from' {} cannot be after date 'to' {}", from, to);
			throw new IllegalArgumentException(String.format("date 'from' %s cannot be after date 'to' %s", from, to));
		}
		int avg = avgPulseProbeRepository.getAvgValueByPatientIdAndDateRange(patientId, from, to);
		log.debug("average pulse value for patient ID {} from {} to {}: {}", patientId, from, to, avg);
		return avg;
	}

	@Override
	public int getAvgValue(long patientId) {
		int avg = avgPulseProbeRepository.getAvgValueByPatientId(patientId);
		log.debug("average pulse value for patient ID {}: {}", patientId, avg);
		return avg;
	}

}
