package telran.monitoring.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;

@RedisHash
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LastProbe {

	@Id
	long patientId;
	
	@Setter
	int value;
	
}
