package telran.monitoring;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.entities.AvgPulseDoc;
import telran.monitoring.model.PulseProbe;
import telran.monitoring.repo.AvgPulseRepository;

@SpringBootApplication
@Slf4j
public class AvgPopulatorAppl {

	@Autowired
	AvgPulseRepository avgPulseRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(AvgPopulatorAppl.class, args);
	}
	
	@Bean
	Consumer<PulseProbe> avgPulseConsumer() {
		return this::getAvgPulseConsumer;
	}
	
	void getAvgPulseConsumer(PulseProbe pulseProbe) {
		log.trace("received pulseprobe of patient {}", pulseProbe.patientId);
		AvgPulseDoc pulseDoc = AvgPulseDoc.of(pulseProbe);
		avgPulseRepository.save(pulseDoc);
	}

}
