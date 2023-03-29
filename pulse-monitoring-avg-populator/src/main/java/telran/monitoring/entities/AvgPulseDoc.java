package telran.monitoring.entities;

import java.time.*;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import telran.monitoring.model.PulseProbe;

@Document(collection = "documents")
@EqualsAndHashCode
@Getter
public class AvgPulseDoc {

	long patientId;
	LocalDateTime dateTime;
	int value;
	
	private AvgPulseDoc(long patientId, LocalDateTime dateTime, int value) {
		this.patientId = patientId;
		this.dateTime = dateTime;
		this.value = value;
	}
	
	public static AvgPulseDoc of(PulseProbe pulseProbe) {
		return new AvgPulseDoc(pulseProbe.patientId,
				LocalDateTime.ofInstant(Instant.ofEpochMilli(pulseProbe.timestamp),
						ZoneId.systemDefault()), pulseProbe.value);
	}
	
}
