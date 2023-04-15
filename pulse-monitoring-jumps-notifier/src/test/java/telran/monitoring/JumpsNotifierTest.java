package telran.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import telran.monitoring.model.NotificationData;
import telran.monitoring.model.PulseJump;
import telran.monitoring.service.NotificationDataProvider;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@Slf4j
class JumpsNotifierTest {

	@Autowired
	InputDestination producer;

	@MockBean
	NotificationDataProvider dataProvider;

	@RegisterExtension
	GreenMailExtension mailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
			.withConfiguration(GreenMailConfiguration.aConfig().withUser("pulse", "12345.com"));

	private static final long PATIENT_ID = 123;
	private static final long PATIENT_ID1 = 0;
	private static final int PREVIOUS_VALUE = 70;
	private static final int CURRENT_VALUE = 140;
	private static final String DOCTOR_EMAIL = "doctor@gmail.com";
	private static final String DOCTOR_NAME = "Vasya";
	private static final String PATIENT_NAME = "Asya";
	private static final String HOSPITAL_SERVICE_EMAIL = "hospital-service@gmail.com";

	PulseJump pulseJump = new PulseJump(PATIENT_ID, PREVIOUS_VALUE, CURRENT_VALUE);

	@Test
	void test() throws MessagingException {
		when(dataProvider.getData(PATIENT_ID))
		.thenReturn(new NotificationData(DOCTOR_EMAIL, DOCTOR_NAME, PATIENT_NAME));
		log.debug("test");
		producer.send(new GenericMessage<PulseJump>(pulseJump), "jumpsConsumer-in-0");
		log.debug("send");
		MimeMessage message = mailExtension.getReceivedMessages()[0];
		log.debug("message");
		assertEquals(DOCTOR_EMAIL, message.getAllRecipients()[0].toString());
		assertTrue(message.getSubject().contains(PATIENT_NAME));
	}
	
	@Test
	void testNotificationDataIsNull() throws MessagingException {
		when(dataProvider.getData(PATIENT_ID1))
		.thenReturn(null);
		log.debug("test");
		producer.send(new GenericMessage<PulseJump>(pulseJump), "jumpsConsumer-in-0");
		log.debug("send");
		MimeMessage message = mailExtension.getReceivedMessages()[0];
		log.debug("message");
		assertEquals(HOSPITAL_SERVICE_EMAIL, message.getAllRecipients()[0].toString());
	}

}
