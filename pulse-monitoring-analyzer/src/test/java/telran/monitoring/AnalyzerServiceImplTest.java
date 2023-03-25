package telran.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import telran.monitoring.entities.LastProbe;
import telran.monitoring.model.*;
import telran.monitoring.repo.LastProbeRepository;
import telran.monitoring.service.AnalyzerService;

@SpringBootTest
class AnalyzerServiceImplTest {

	private static final int PULSE_JUMP_VALUE = 200;
	private static final int PULSE_VALUE = 70;
	private static final Long PATIENT_NO_REDIS_DATA = 1L;
	private static final Long PATIENT_NO_JUMP = 2L;
	private static final Long PATIENT_JUMP = 3L;
	
	@MockBean
	LastProbeRepository repository;
	
	@Autowired
	AnalyzerService service;
	
	private LastProbe probeJump = new LastProbe(PATIENT_JUMP, PULSE_JUMP_VALUE);
	private LastProbe probeNoJump = new LastProbe(PATIENT_NO_JUMP, PULSE_VALUE);;
	
	@BeforeEach
	void setUp() throws Exception {
		when(repository.findById(PATIENT_NO_REDIS_DATA)).thenReturn(Optional.ofNullable(null));
		when(repository.findById(PATIENT_NO_JUMP)).thenReturn(Optional.of(probeNoJump));
		when(repository.findById(PATIENT_JUMP)).thenReturn(Optional.of(probeJump));
	}

	@Test
	void noRedisDataTest() {
		PulseProbe pulseProbe = new PulseProbe(PATIENT_NO_REDIS_DATA, 0, PULSE_VALUE);
		assertNull(service.processPulseProbe(pulseProbe));
	}
	
	@Test
	void patientNoJumpTest() {
		PulseProbe pulseProbe = new PulseProbe(PATIENT_NO_JUMP, 0, PULSE_VALUE);
		assertNull(service.processPulseProbe(pulseProbe));
	}
	
	@Test
	void patientJumpTest() {
		PulseProbe pulseProbe = new PulseProbe(PATIENT_JUMP, 0, PULSE_VALUE);
		assertEquals(PULSE_VALUE, service.processPulseProbe(pulseProbe).currentValue);
	}

}
