package telran.monitoring;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;

import telran.monitoring.entities.AvgPulseDoc;
import telran.monitoring.model.PulseProbe;
import telran.monitoring.repo.AvgPulseRepository;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AvgPulsePopulatorTest {
	
	private static final long PATIENT_ID1 = 123;
	private static final int VALUE1 = 70;
	private static final long PATIENT_ID2 = 124;
	private static final int VALUE2 = 75;
	
	@Autowired
	InputDestination producer;
	
	@Autowired
	AvgPulseRepository pulseRepository;
	
	PulseProbe pulseProbe1 = new PulseProbe(PATIENT_ID1, 0, VALUE1);
	PulseProbe pulseProbe2 = new PulseProbe(PATIENT_ID2, 0, VALUE2);
	AvgPulseDoc doc1 = AvgPulseDoc.of(pulseProbe1);
	AvgPulseDoc doc2 = AvgPulseDoc.of(pulseProbe2);
	String bindingName = "avgPulseConsumer-in-0";

	@Test
	void test() {
		producer.send(new GenericMessage<PulseProbe>(pulseProbe1), bindingName);
		producer.send(new GenericMessage<PulseProbe>(pulseProbe2), bindingName);
		List<AvgPulseDoc> documents = pulseRepository.findAll();
		List<AvgPulseDoc> expected = Arrays.asList(doc1, doc2);
		assertIterableEquals(expected, documents);
	}

}
