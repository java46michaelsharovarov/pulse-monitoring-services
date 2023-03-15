package telran.monitoring.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import telran.monitoring.service.AvgReducerService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AvgReducerControllerTest {
	
	@Autowired
	InputDestination producer;
	
	@Autowired
	OutputDestination consumer;
	
	@MockBean
	AvgReducerService service;
	
	PulseProbe pulseProbe1 = new PulseProbe(1, 1, 70);
	PulseProbe pulseProbe2 = new PulseProbe(1, 2, 80);
	Integer avg = 75;

	@BeforeEach
	void mockingService() {
		when(service.reduce(pulseProbe2)).thenReturn(avg);
		when(service.reduce(pulseProbe1)).thenReturn(null);
	}

	@Test
	void receivingPulseProbe1() {
		producer.send(new GenericMessage<PulseProbe>(pulseProbe1), "pulseProbConsumerAvg-in-0");
		Message<byte[]> message = consumer.receive(10, "average-out-0");
		assertNull(message);
	}

	@Test
	void receivingPulseProbe2() throws StreamReadException, DatabindException, IOException {
		producer.send(new GenericMessage<PulseProbe>(pulseProbe2), "pulseProbConsumerAvg-in-0");
		Message<byte[]> message = consumer.receive(10, "average-out-0");
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		PulseProbe receivedAvg = mapper.readValue(message.getPayload(), PulseProbe.class);
		assertEquals(avg, receivedAvg.value);
	}

}
