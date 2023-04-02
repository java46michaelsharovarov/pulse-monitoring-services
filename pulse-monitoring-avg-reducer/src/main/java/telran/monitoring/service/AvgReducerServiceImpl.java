package telran.monitoring.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.PulseProbe;
import telran.monitoring.entities.ProbesList;
import telran.monitoring.repo.ProbesListRepository;

@Service
@Slf4j
public class AvgReducerServiceImpl implements AvgReducerService {

	@Value("${app.reducing.size}")
	int reducingSize;
	
	@Autowired
	ProbesListRepository probesListRepository;
	
	@Override
	@Transactional
	public Integer reduce(PulseProbe probe) {
		Integer avg = null;
		ProbesList probesList = probesListRepository.findById(probe.patientId).orElse(null);
		if(probesList == null) {
			probesList = new ProbesList(probe.patientId);
			log.debug("for patient {} no saved pulse values", probe.patientId);
		} else {
			log.trace("for patient {} number of saved pulse values is {}",
					probesList.getPatientId(), probesList.getValues().size());
		}
		List<Integer> values = probesList.getValues();
		values.add(probe.value);
		log.trace("added value: {} in the probesList", probe.value);
		if(values.size() >= reducingSize) {
			avg = calculatingAvg(probesList.getValues());
			log.debug("average pulse value for the patient with ID {}: {} ", probe.patientId, avg);
			values.clear();
			log.debug("the list of values has been cleared");
		}
		probesListRepository.save(probesList);
		log.trace("probesList saved in probesListRepository");
		return avg;
	}

	private int calculatingAvg(List<Integer> values) {
		return values.stream().collect(Collectors.averagingInt(x -> x)).intValue();
 	}
	
	@PostConstruct
	void inintDebugInfo() {
		log.debug("reducing size is {}", reducingSize);
	}

}
