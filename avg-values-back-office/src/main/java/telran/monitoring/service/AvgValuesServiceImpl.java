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

	private static final String EMPTY = "empty";
	private static final String DATE_ERROR_MSG = "date 'from' %s can't be after date 'to' %s";
	private static final String NOT_EXIST_MSG = "patient with id: %d does not exist";
	private static final String MSG_AVG_PULSE_VALUE = "average pulse value for patient ID {}: {}";
	
	@Autowired
	AvgPulseProbeRepository avgPulseProbeRepository;
	
	@Override
	public int getAvgValue(long patientId, LocalDateTime from, LocalDateTime to) {
		checkPatientId(patientId);
		if(from.isAfter(to)) {
			String msg = String.format(DATE_ERROR_MSG, from, to);
			log.error(msg);
			throw new IllegalStateException(msg);
		}
		int avg;
		try {
			avg = avgPulseProbeRepository.getAvgValueByPatientIdAndDateRange(patientId, from, to);
		} catch (Exception e) {
			throw new NoSuchElementException(EMPTY);
		}
		log.debug("from {} to {} " + MSG_AVG_PULSE_VALUE, from, to, patientId, avg);
		return avg;
	}

	@Override
	public int getAvgValue(long patientId) {
		checkPatientId(patientId);
		int avg = avgPulseProbeRepository.getAvgValueByPatientId(patientId);
		log.debug(MSG_AVG_PULSE_VALUE, patientId, avg);
		return avg;
	}

	private void checkPatientId(long patientId) {
		if(!avgPulseProbeRepository.existsByPatientId(patientId)) {
			String msg = String.format(NOT_EXIST_MSG, patientId);
			log.error(msg);
			throw new NoSuchElementException(msg);
		}
	}

}
