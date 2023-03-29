package telran.monitoring.service;

import java.time.LocalDate;
import java.util.List;

import telran.monitoring.model.*;

public interface VisitsService {

	void addPatient(PatientDto patientDto);
	void addDoctor(DoctorDto doctorDto);
	void addVisit(VisitDto visitDto);
	List<VisitDto> getAllVisits(long patientId);
	List<VisitDto> getVisitsDates(long patientId, LocalDate from, LocalDate to);
	
}
