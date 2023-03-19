package telran.monitoring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.monitoring.entities.VisitEntity;
import telran.monitoring.proj.VisitData;

public interface VisitRepository extends JpaRepository<VisitEntity, Long>{
	
	@Query(value = "select v.visitid, d.name as doctorName, d.email, p.id, p.name as patientName from doctors as d "
			+ "join visits as v on d.email = v.email "
			+ "join patients as p on p.id = v.id "
			+ "where p.id = :id "
			+ "order by date desc "
			+ "limit 1", nativeQuery = true)
	VisitData getLastVisitById(long id);
}
