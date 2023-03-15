package telran.monitoring;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import telran.monititoring.model.PulseProbe;
import telran.monitoring.service.AvgReducerService;

@SpringBootApplication
public class AvgReducerAppl {

	@Autowired
	AvgReducerService service;
	
	@Autowired
	StreamBridge streamBridge;

	@Value("${app.binding.name:average-out-0}")
	private String bindingName;
	
	public static void main(String[] args) {
		SpringApplication.run(AvgReducerAppl.class, args);
	}

	@Bean
	Consumer<PulseProbe> pulseProbConsumerAvg() {
		return this::pulseProbAvgReducing;
	}
	
	void pulseProbAvgReducing(PulseProbe probe) {
		Integer avg = service.reduce(probe);
		if(avg != null) {
			streamBridge.send(bindingName, new PulseProbe(probe.patientID, 0, avg));
		}
	}
}
