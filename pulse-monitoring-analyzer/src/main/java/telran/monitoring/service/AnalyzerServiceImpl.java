package telran.monitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import telran.monititoring.model.PulseJump;
import telran.monititoring.model.PulseProbe;
import telran.monitoring.entities.LastProbe;
import telran.monitoring.repo.LastProbeRepository;

@Service
@Slf4j
public class AnalyzerServiceImpl implements AnalyzerService {

	@Autowired
	LastProbeRepository lastProbeRepository;
	
	@Value("${app.jump.threshold:0.3}")
	double jumpThreshold;

	@Override
	@Transactional
	public PulseJump processPulseProbe(PulseProbe probe) {
		PulseJump res = null;
		LastProbe lastProbe = lastProbeRepository.findById(probe.patientID).orElse(null);
		if (lastProbe == null) {
			lastProbe = new LastProbe(probe.patientID, probe.value);
			log.debug("lastProbe is NULL. New lastProbe is: {}", probe.toString());
		} else {
			if (isJump(lastProbe.getValue(), probe.value)) {
				res = new PulseJump(probe.patientID, lastProbe.getValue(), probe.value);
				log.debug("isJump = true. {}", res.toString());
			}
			lastProbe.setValue(probe.value);
			log.trace("lastProbe updated: {}", probe.toString());
		}
		lastProbeRepository.save(lastProbe);
		log.debug("lastProbe saved in lastProbeRepository");
		return res;
	}

	private boolean isJump(int lastValue, int currentValue) {
		int delta = Math.abs(currentValue - lastValue);
		return delta >= lastValue * jumpThreshold;
	}

}
