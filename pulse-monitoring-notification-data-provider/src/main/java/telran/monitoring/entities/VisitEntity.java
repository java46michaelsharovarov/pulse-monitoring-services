package telran.monitoring.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name="visits", indexes= {@Index(columnList = "patient_id")} )
public class VisitEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	long id;
	
	@ManyToOne
	@JoinColumn(name = "doctor_email")
	DoctorEntity doctor;
	
	@ManyToOne
	@JoinColumn(name = "patient_id")
	PatientEntity patient;
	
	LocalDate date;
	
}
