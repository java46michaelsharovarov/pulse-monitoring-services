package telran.monitoring.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name="visits")
public class VisitEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	long id;
	
	@ManyToOne
	@JoinColumn(name = "email")
	DoctorEntity doctor;
	
	@ManyToOne
	@JoinColumn(name = "patientid")
	PatientEntity patient;
	
	LocalDate date;
	
}
