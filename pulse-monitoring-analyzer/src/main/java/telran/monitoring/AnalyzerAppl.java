package telran.monitoring;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import telran.monititoring.model.*;
import telran.monitoring.service.AnalyzerService;

@SpringBootApplication
public class AnalyzerAppl {

	Logger LOG = LoggerFactory.getLogger(AnalyzerAppl.class);
	
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
	
	@SuppressWarnings("unused")
	void pulseProbeAnalyzing(PulseProbe pulseProbe) {
		PulseJump pulseJump = analyzerService.processPulseProbe(pulseProbe);
		LOG.debug("called analyzerService.processPulseProbe({}), pulseJump=[{}]", pulseProbe.toString(), pulseJump.toString());
		if(pulseJump != null) {
			streamBridge.send(bindingName, pulseJump);
			LOG.debug("called treamBridge.send({}, {})", bindingName, pulseJump.toString());
		} else {
			LOG.debug("pulseJump is NULL");
		}
	}
	
}
