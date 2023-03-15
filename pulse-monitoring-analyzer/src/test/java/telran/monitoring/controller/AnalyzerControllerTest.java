package telran.monitoring.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.monititoring.model.*;
import telran.monitoring.AnalyzerAppl;
import telran.monitoring.service.AnalyzerService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AnalyzerControllerTest {
	
	Logger LOG = LoggerFactory.getLogger(AnalyzerAppl.class);
	
	@Autowired
	InputDestination producer;
	
	@Autowired
	OutputDestination consumer;
	
	@MockBean
	AnalyzerService service;
	
	ObjectMapper mapper = new ObjectMapper();
	
	PulseProbe probeNoJump = new PulseProbe(1, 0, 70);
	PulseProbe probeJump = new PulseProbe(2, 0, 200);
	PulseJump pulseJump = new PulseJump(2, 100, 200);

	@BeforeEach
	void mockingService() {
		when(service.processPulseProbe(probeJump)).thenReturn(pulseJump);
		when(service.processPulseProbe(probeNoJump)).thenReturn(null);
	}

	@Test
	void receivingProbeNoJump() {
		LOG.debug("receivingProbeNoJump : call producer.send({}, 'pulseProbeConsumer-in-0')", probeNoJump.toString());
		producer.send(new GenericMessage<PulseProbe>(probeNoJump), "pulseProbeConsumer-in-0");
		Message<byte[]> message = consumer.receive(10, "jumps-out-0");
		assertNull(message);
		LOG.debug("receivingProbeNoJump : called consumer.receive(10, \"jumps-out-0\"), message=[NULL]");
	}

	@Test
	void receivingProbeJump() throws StreamReadException, DatabindException, IOException {
		LOG.debug("receivingProbeJump : call producer.send({}, 'pulseProbeConsumer-in-0')", probeJump.toString());
		producer.send(new GenericMessage<PulseProbe>(probeJump), "pulseProbeConsumer-in-0");
		Message<byte[]> message = consumer.receive(10, "jumps-out-0");
		assertNotNull(message);
		PulseJump jump = mapper.readValue(message.getPayload(), PulseJump.class);
		LOG.debug("receivingProbeJump : called consumer.receive(10, \"jumps-out-0\"), message=[{}]", jump.toString());
		assertEquals(pulseJump, jump);
	}

}
