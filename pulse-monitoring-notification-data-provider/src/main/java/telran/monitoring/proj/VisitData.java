package telran.monitoring.proj;

import java.time.LocalDate;

public interface VisitData {

	long getVisitId();
	LocalDate getDate();
	String getDoctorName();
	String getEmail();
	long getId();
	String getPatientName();
	
}
