package telran.monitoring;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
import telran.monititoring.model.*;
import telran.monitoring.service.AnalyzerService;

@Slf4j
@SpringBootApplication
public class AnalyzerAppl {

	@Autowired
	AnalyzerService analyzerService;

	@Autowired
	StreamBridge streamBridge;

	@Value("${app.binding.name:jumps-out-0}")
	private String bindingName;

	public static void main(String[] args) {
		SpringApplication.run(AnalyzerAppl.class, args);
	}

	@Bean
	Consumer<PulseProbe> pulseProbeConsumer() {
		return this::pulseProbeAnalyzing;		
	}
	
	void pulseProbeAnalyzing(PulseProbe pulseProbe) {
		log.debug("called pulseProbeAnalyzing");
		PulseJump pulseJump = analyzerService.processPulseProbe(pulseProbe);
		if(pulseJump != null) {
			log.debug("called analyzerService.processPulseProbe({}), pulseJump=[{}]", pulseProbe.toString(), pulseJump.toString());
			streamBridge.send(bindingName, pulseJump);
			log.debug("called treamBridge.send({}, {})", bindingName, pulseJump.toString());
		} else {
			log.debug("pulseJump is NULL");
		}
	}
	
}
