package telran.monitoring.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.monitoring.entities.PatientEntity;

public interface PatientRepository  extends JpaRepository<PatientEntity, Long>{

}
