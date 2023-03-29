package telran.monitoring.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.monitoring.entities.VisitEntity;

public interface VisitRepository extends JpaRepository<VisitEntity, Long>{
	
	@Query(value="select doctor_email from visits "
			+ "where patient_id = :patientId "
			+ "order by date desc limit 1", nativeQuery = true)
	String getDoctorEmail(long patientId);

	List<VisitEntity> findByPatientId(long patientId);

	@Query(value="select id, doctor_email, patient_id, date from visits "
			+ "where patient_id = :patientId "
			+ "and date between :from and :to", nativeQuery = true)
	List<VisitEntity> findByPatientIdAndBetweenDate(long patientId, LocalDate from, LocalDate to);
			
}
