package telran.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import telran.monitoring.repo.ProbesListRepository;
import telran.monitoring.service.AvgReducerService;
import telran.monitoring.entities.*;
import telran.monitoring.model.*;

@SpringBootTest
class AvgReducerServiceTest {

	static final Long PATIENT_NO_REDIS_DATA = 123L;
	private static final Long PATIENT_NO_AVG = 125L;
	private static final int VALUE = 100;
	private static final Long PATIENT_AVG = 127L;

	@Autowired
	AvgReducerService service;
	
	@MockBean
	ProbesListRepository probesRepository;

	static ProbesList listNoAvgValue = new ProbesList(PATIENT_NO_AVG);
	static ProbesList listAvgValue = new ProbesList(PATIENT_AVG);
	static List<Integer> emptyProbeList;
	static List<Integer> oneProbeList;

	@BeforeAll
	static void setUp() {
		emptyProbeList = listNoAvgValue.getValues();
		oneProbeList = listAvgValue.getValues();
		oneProbeList.add(VALUE);
	}

	@BeforeEach
	void redisMocking() {
		when(probesRepository.findById(PATIENT_NO_REDIS_DATA)).thenReturn(Optional.ofNullable(null));
		when(probesRepository.findById(PATIENT_NO_AVG)).thenReturn(Optional.of(listNoAvgValue));
		when(probesRepository.findById(PATIENT_AVG)).thenReturn(Optional.of(listAvgValue));
	}

	@Test
	void noRedisDataTest() {
		assertNull(service.reduce(new PulseProbe(PATIENT_NO_REDIS_DATA, 0, VALUE)));
	}

	@Test
	void noAvgTest() {
		assertNull(service.reduce(new PulseProbe(PATIENT_NO_AVG, 0, VALUE)));
		assertEquals(1, emptyProbeList.size());
	}

	@Test
	void avgTest() {
		assertEquals(VALUE, service.reduce(new PulseProbe(PATIENT_AVG, 0, VALUE)));
		assertEquals(0, oneProbeList.size());
	}
}
