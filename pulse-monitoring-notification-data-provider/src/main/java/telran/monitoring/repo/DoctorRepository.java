package telran.monitoring.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.monitoring.entities.DoctorEntity;

public interface DoctorRepository extends JpaRepository<DoctorEntity, String>{

}
