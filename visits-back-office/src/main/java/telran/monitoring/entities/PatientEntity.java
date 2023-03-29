package telran.monitoring.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "patients")
public class PatientEntity {

	@Id
	long id;
	
	String name;
	
}
