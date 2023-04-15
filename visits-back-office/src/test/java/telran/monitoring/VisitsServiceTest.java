package telran.monitoring;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.monitoring.model.*;
import telran.monitoring.repo.DoctorRepository;
import telran.monitoring.repo.PatientRepository;
import telran.monitoring.repo.VisitRepository;
import telran.monitoring.service.VisitsService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VisitsServiceTest {

	private static final String DATE_1 = "2023-01-01";
	private static final String DATE_2 = "2023-01-02";
	private static final String DATE_3 = "2023-01-03";
	private static final long NON_EXISTENT_PATIENT_ID = 126L;
	private static final long NEW_PATIENT_ID = 125L;
	private static final String NEW_DOCTOR_EMAIL = "doctor123@gmail.com";
	private static final String NON_EXISTENT_DOCTOR_EMAIL = "d@gmail.com";
	PatientDto patientDto = new PatientDto(NEW_PATIENT_ID, "Petya");
	DoctorDto doctorDto = new DoctorDto(NEW_DOCTOR_EMAIL, "Moshe");
	VisitDto visitDto1 = new VisitDto(NEW_PATIENT_ID, NEW_DOCTOR_EMAIL, DATE_1);
	VisitDto visitDto2 = new VisitDto(NEW_PATIENT_ID, NEW_DOCTOR_EMAIL, DATE_2);
	VisitDto visitDto3 = new VisitDto(NEW_PATIENT_ID, NEW_DOCTOR_EMAIL, DATE_3);

	@Autowired
	VisitsService service;
	
	@Autowired
	DoctorRepository doctorRepository;
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	VisitRepository visitRepository;
	
	@Test
	@Order(1)
	void addPatientTest() {
		assertNull(patientRepository.findById(NEW_PATIENT_ID).orElse(null));
		service.addPatient(patientDto);
		assertEquals(NEW_PATIENT_ID, patientRepository.findById(NEW_PATIENT_ID).orElse(null).getId());
	}
	
	@Test
	@Order(2)
	void addExistPatientTest() {
		assertThrows(IllegalStateException.class, () -> service.addPatient(patientDto));
	}
	
	@Test
	@Order(3)
	void addDoctorTest() {
		assertNull(doctorRepository.findById(NEW_DOCTOR_EMAIL).orElse(null));
		service.addDoctor(doctorDto);
		assertEquals(NEW_DOCTOR_EMAIL, doctorRepository.findById(NEW_DOCTOR_EMAIL).orElse(null).getEmail());
	}
	
	@Test
	@Order(4)
	void addExistDoctorTest() {
		assertThrows(IllegalStateException.class, () -> service.addDoctor(doctorDto));
	}
	
	@Test
	@Order(5)
	void addVisitTest() {
		service.addVisit(visitDto1);
		assertEquals(NEW_PATIENT_ID, visitRepository.findAll().get(0).getPatient().getId());
	}
	
	@Test
	@Order(6)
	void addExistVisitTest() {
		assertThrows(IllegalStateException.class, () -> service.addDoctor(doctorDto));
	}	
	
	@Test
	@Order(7)
	void addVisitWithNonExistentPatientAndDoctorTest() {
		assertThrows(NoSuchElementException.class, () -> service.addVisit(new VisitDto(NON_EXISTENT_PATIENT_ID, NEW_DOCTOR_EMAIL, DATE_1)));
		assertThrows(NoSuchElementException.class, () -> service.addVisit(new VisitDto(NEW_PATIENT_ID, NON_EXISTENT_DOCTOR_EMAIL, DATE_1)));
	}
	
	@Test
	@Order(8)
	void getAllVisitsTest() {
		List<VisitDto> expected = List.of(visitDto1);
		assertIterableEquals(expected, service.getAllVisits(NEW_PATIENT_ID));
		service.addVisit(visitDto2);
		expected = List.of(visitDto1, visitDto2);
		assertIterableEquals(expected, service.getAllVisits(NEW_PATIENT_ID));
	}
	
	@Test
	@Order(9)
	void getAllVisitsNonExistenPatientTest() {
		assertThrows(NoSuchElementException.class, () -> service.getAllVisits(NON_EXISTENT_PATIENT_ID));
	}

	@Test
	@Order(10)
	void getVisitsDatesTest() {
		service.addVisit(visitDto3);
		List<VisitDto> current = service.getVisitsDates(NEW_PATIENT_ID, LocalDate.parse(DATE_1), LocalDate.parse(DATE_2));
		List<VisitDto> expected = List.of(visitDto1, visitDto2);
		assertIterableEquals(expected, current);
		current = service.getVisitsDates(NEW_PATIENT_ID, LocalDate.parse(DATE_1), LocalDate.parse(DATE_3));
		expected = List.of(visitDto1, visitDto2, visitDto3);
		assertIterableEquals(expected, current);
	}
	
	@Test
	@Order(11)
	void getVisitsDatesNonExistenPatientTest() {
		assertThrows(NoSuchElementException.class, () -> service.getVisitsDates(NON_EXISTENT_PATIENT_ID, LocalDate.parse(DATE_1), LocalDate.parse(DATE_2)));
	}
	
	@Test
	@Order(12)
	void getVisitsWithReversedDatesTest() {
		assertThrows(IllegalStateException.class, () -> service.getVisitsDates(NEW_PATIENT_ID, LocalDate.parse(DATE_2), LocalDate.parse(DATE_1)));
	}
	
}
