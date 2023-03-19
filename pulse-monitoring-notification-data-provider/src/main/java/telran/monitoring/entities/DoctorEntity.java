package telran.monitoring.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name="doctors")
public class DoctorEntity {

	@Id
	String email;
	
	String name;
	
}
