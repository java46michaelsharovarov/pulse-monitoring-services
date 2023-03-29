package telran.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import telran.monitoring.service.AvgValuesService;

@SpringBootTest
@AutoConfigureMockMvc
class AvgValuesControllerTest {

	private static final Integer AVG_VALUE_BY_PATIENT_ID = 70;
	private static final Integer AVG_VALUE_BY_DATES = 80;

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	AvgValuesService service;
	
	@BeforeEach
	void setUp() throws Exception {
		when(service.getAvgValue(ArgumentMatchers.anyLong()))
		.thenReturn(AVG_VALUE_BY_PATIENT_ID);
		when(service.getAvgValue(ArgumentMatchers.anyLong(),
				ArgumentMatchers.any(LocalDateTime.class),
				ArgumentMatchers.any(LocalDateTime.class)))
		.thenReturn(AVG_VALUE_BY_DATES);
	}

	@Test
	void byPatientIdTest() throws IllegalStateException, UnsupportedEncodingException, Exception {
		String res = mockMvc.perform(get("/pulse/values/123")).andDo(print())
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(AVG_VALUE_BY_PATIENT_ID, Integer.parseInt(res));
	}
	
	@Test
	void byPatientIdAndDatesTest() throws IllegalStateException, UnsupportedEncodingException, Exception {
		String res = mockMvc.perform(get("/pulse/values/123")
				.param("from", "2023-01-01T12:00")
				.param("to", "2023-01-02T12:00"))
				.andDo(print())
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(AVG_VALUE_BY_DATES, Integer.parseInt(res));
	}
	
	@Test
	void byPatientIdAndFromDateTest() throws IllegalStateException, UnsupportedEncodingException, Exception {
		String res = mockMvc.perform(get("/pulse/values/123")
				.param("from", "2023-01-01T12:00"))
				.andDo(print())
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(AVG_VALUE_BY_DATES, Integer.parseInt(res));
	}
	
	@Test
	void byPatientIdAndToDateTest() throws IllegalStateException, UnsupportedEncodingException, Exception {
		String res = mockMvc.perform(get("/pulse/values/123")
				.param("to", "2023-01-01T12:00"))
				.andDo(print())
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(AVG_VALUE_BY_DATES, Integer.parseInt(res));
	}

}
