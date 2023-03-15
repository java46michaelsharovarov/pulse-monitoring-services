package telran.monitoring.service;

import telran.monititoring.model.PulseProbe;

public interface AvgReducerService {

	Integer reduce(PulseProbe probe);
	
}
