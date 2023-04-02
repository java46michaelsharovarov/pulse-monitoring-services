package telran.monitoring;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.*;
import telran.monitoring.service.NotificationDataProvider;

@SpringBootApplication
@Slf4j
public class JumpsNotifierAppl {

	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	NotificationDataProvider dataProvider;
	
	@Value("${app.mail.subject: Pulse Jump Notification}")
	String subject;

	public static void main(String[] args) {
		SpringApplication.run(JumpsNotifierAppl.class, args);
	}
	
	@Bean
	Consumer<PulseJump> jumpsConsumer() {
		return this::jumpProcessing;
	}

	void jumpProcessing(PulseJump jump) {
		log.trace("received jump: [{}]", jump);
		sendMail(jump);
	}

	private void sendMail(PulseJump jump) {
		NotificationData data = dataProvider.getData(jump.patientId);
		SimpleMailMessage smm = new SimpleMailMessage();
		String text = getMailText(jump, data);
		smm.setTo(data.doctorEmail);
		smm.setSubject(subject + " " + data.patientName);
		smm.setText(text);
		mailSender.send(smm);
		log.trace("sent text mail {}", text);
	}

	private String getMailText(PulseJump jump, NotificationData data) {
		return String.format("Dear Dr. %s%nPatient %s has pulse jump%n"
				+ "previous value: %d; current value: %d%n", 
				data.doctorName, data.patientName, jump.previousValue, jump.currentValue);
	}
	
}
