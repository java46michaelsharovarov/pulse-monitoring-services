package telran.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import telran.monitoring.model.DoctorDto;
import telran.monitoring.model.PatientDto;
import telran.monitoring.service.VisitsService;

@SpringBootTest
@AutoConfigureMockMvc
class VisitsControllerTest {

//	private static final long NON_EXISTENT_PATIENT_ID = 126L;
	private static final long NEW_PATIENT_ID = 125L;
//	private static final String NEW_DOCTOR_EMAIL = "doctor123@gmail.com";
//	private static final String NON_EXISTENT_DOCTOR_EMAIL = "d@gmail.com";
	PatientDto patientDto = new PatientDto(NEW_PATIENT_ID, "Petya");
//	DoctorDto doctorDto = new DoctorDto(NEW_DOCTOR_EMAIL, "Moshe");
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	VisitsService service;
	
	@BeforeEach
	void setUp() throws Exception {
//		when(service.getAllVisits(NEW_PATIENT_ID)).thenThrow(IllegalStateException.class);
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
