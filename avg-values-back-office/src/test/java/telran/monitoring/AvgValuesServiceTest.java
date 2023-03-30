package telran.monitoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.monitoring.entities.AvgPulseDoc;
import telran.monitoring.model.PulseProbe;
import telran.monitoring.repo.PulseProbesRepository;
import telran.monitoring.service.AvgValuesService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AvgValuesServiceTest {

	private static final int VALUE_1 = 70;
	private static final int VALUE_2 = 80;
	private static final int VALUE_3 = 90;
	private static final int VALUE_4 = 100;
	private static final int VALUE_5 = 110;
	private static final long AVG_VALUE = 75;
	
	private static final long PATIENT_ID = 123;
	private static final long ANOTHER_PATIENT_ID = 124;
	private static final long NON_EXISTENT_PATIENT_ID = 200;

	
	LocalDateTime localDate1 = LocalDateTime.of(2023, 1, 1, 0, 0);
	LocalDateTime localDate2 = LocalDateTime.of(2023, 1, 2, 0, 0);
	LocalDateTime localDate3 = LocalDateTime.of(2023, 1, 3, 0, 0);
	LocalDateTime localDate4 = LocalDateTime.of(2023, 1, 4, 0, 0);
	LocalDateTime localDate5 = LocalDateTime.of(2023, 1, 5, 0, 0);
	long date1 = localDate1.toInstant(ZoneOffset.of("+2")).toEpochMilli();
	long date2 = localDate2.toInstant(ZoneOffset.of("+2")).toEpochMilli();
	long date3 = localDate3.toInstant(ZoneOffset.of("+2")).toEpochMilli();
	long date4 = localDate4.toInstant(ZoneOffset.of("+2")).toEpochMilli();
	long date5 = localDate5.toInstant(ZoneOffset.of("+2")).toEpochMilli();
	
	@Autowired
	AvgValuesService service;
	
	@Autowired
	PulseProbesRepository repository;
	
	@BeforeEach
	void fillRepo() {
		repository.save(AvgPulseDoc.of(new PulseProbe(PATIENT_ID, date1, 0, VALUE_1)));
		repository.save(AvgPulseDoc.of(new PulseProbe(PATIENT_ID, date2, 0, VALUE_2)));
		repository.save(AvgPulseDoc.of(new PulseProbe(PATIENT_ID, date3, 0, VALUE_3)));
		repository.save(AvgPulseDoc.of(new PulseProbe(PATIENT_ID, date4, 0, VALUE_4)));
		repository.save(AvgPulseDoc.of(new PulseProbe(PATIENT_ID, date5, 0, VALUE_5)));
		repository.save(AvgPulseDoc.of(new PulseProbe(ANOTHER_PATIENT_ID, 0, VALUE_1)));
	}
	
	@Test
	@Order(1)
	void getAvgValueByIdTest() {
		assertEquals(VALUE_3, service.getAvgValue(PATIENT_ID));
		assertEquals(VALUE_1, service.getAvgValue(ANOTHER_PATIENT_ID));
	}
	
	@Test
	@Order(2)
	void getAvgValueByIdAndDateTest() {
		assertEquals(AVG_VALUE, service.getAvgValue(PATIENT_ID, localDate1, localDate2));
		assertEquals(VALUE_3, service.getAvgValue(PATIENT_ID, localDate1, localDate5));
	}
	
	@Test
	@Order(3)
	void getAvgValueByNonExistenIdTest() {
		assertThrows(NoSuchElementException.class, () -> service.getAvgValue(NON_EXISTENT_PATIENT_ID, localDate1, localDate2));
	}
	
	@Test
	@Order(4)
	void getVisitsWithReversedDatesTest() {
		assertThrows(IllegalArgumentException.class, () -> service.getAvgValue(PATIENT_ID, localDate2, localDate1));
	}

}
