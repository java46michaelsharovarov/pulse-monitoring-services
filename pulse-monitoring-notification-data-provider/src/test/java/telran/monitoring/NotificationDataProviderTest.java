package telran.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.NotificationData;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class NotificationDataProviderTest {

	@Autowired
	MockMvc mockMvc;
	
	@Test
	@Sql(scripts = "DoctorsPatientsVisits.sql")
	void test() throws Exception {
		String jsonResponse = mockMvc.perform(get("/visits/last/123"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		NotificationData notificationData = mapper.readValue(jsonResponse, NotificationData.class);
		log.debug("response: {}", jsonResponse);
		assertEquals("doctor2@gmail.com", notificationData.doctorEmail);
		assertEquals("doctor2", notificationData.doctorName);
		assertEquals("Vasya", notificationData.patientName);
	}

}
