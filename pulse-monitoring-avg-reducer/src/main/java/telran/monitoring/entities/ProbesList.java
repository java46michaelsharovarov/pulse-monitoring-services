package telran.monitoring.entities;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;

@RedisHash
@NoArgsConstructor
@Getter
@ToString
public class ProbesList {

	@Id
	long patientId;
	
	List<Integer> values = new ArrayList<>();

	public ProbesList(long patientId) {
		this.patientId = patientId;
	}
	
}
