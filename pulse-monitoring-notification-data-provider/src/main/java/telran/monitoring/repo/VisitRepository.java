package telran.monitoring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.monitoring.entities.VisitEntity;

public interface VisitRepository extends JpaRepository<VisitEntity, Long>{
	
	@Query(value="select doctor_email from visits "
			+ "where patient_id = :patientId "
			+ "order by date desc limit 1", nativeQuery = true)
	String getDoctorEmail(long patientId);
			
}
