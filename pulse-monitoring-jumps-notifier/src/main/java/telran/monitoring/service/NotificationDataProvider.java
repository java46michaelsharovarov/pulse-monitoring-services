package telran.monitoring.service;

import telran.monititoring.model.NotificationData;

public interface NotificationDataProvider {

	NotificationData getData(long patientId);
	
}
